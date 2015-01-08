package us.kbase.kbasegenefamilies;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import us.kbase.common.service.Tuple11;
import us.kbase.common.service.Tuple2;
import us.kbase.common.service.Tuple4;
import us.kbase.common.service.Tuple5;
import us.kbase.common.utils.AlignUtil;
import us.kbase.common.utils.CorrectProcess;
import us.kbase.common.utils.FastaWriter;
import us.kbase.common.utils.RpsBlastParser;
import us.kbase.kbasegenefamilies.bin.BinPreparator;
import us.kbase.kbasegenefamilies.util.Utils;
import us.kbase.kbasegenomes.Feature;
import us.kbase.kbasegenomes.Genome;
import us.kbase.workspace.ObjectData;
import us.kbase.workspace.ObjectIdentity;

/**
   This class runs a domain search against a single genome, using
   RPS-BLAST and HMMER.  Domain hits are saved in DomainAnnotation
   workspace objects.
*/
public class DomainSearchTask {
    private static String MAX_BLAST_EVALUE = "1e-04";
    private static int MIN_COVERAGE = 50;
    private static final int modelBufferMaxSize = 100;
	
    public static final String domainAnnotationWsType = "KBaseGeneFamilies.DomainAnnotation";
    public static final String domainAlignmentsWsType = "KBaseGeneFamilies.DomainAlignments";
	
    protected File tempDir;
    protected ObjectStorage storage;

    public DomainSearchTask(File tempDir, ObjectStorage objectStorage) {
	this.tempDir = tempDir;
	this.storage = objectStorage;
    }

    /**
       Runs a domain search on a single genome, returning annotations.
       Takes a domainModelSetRef as input, which is searched as
       individual libraries.
    */
    public DomainAnnotation runDomainSearch(String token,
					    String domainModelSetRef,
					    String genomeRef) throws Exception {
	final DomainModelSet dms = storage.getObjects(token, Arrays.asList(new ObjectIdentity().withRef(domainModelSetRef))).get(0).getData().asClassInstance(DomainModelSet.class);
	final Genome genome = storage.getObjects(token, Arrays.asList(new ObjectIdentity().withRef(genomeRef))).get(0).getData().asClassInstance(Genome.class);
	Map<String,String> domainLibMap = dms.getDomainLibs();
	for (String id : domainLibMap.values()) {
	    // should add all these together; instead, just
	    // returns the first one:
	    DomainLibrary dl = storage.getObjects(token, Arrays.asList(new ObjectIdentity().withRef(id))).get(0).getData().asClassInstance(DomainLibrary.class);
	    if (dl.getProgram().equals("rpsblast-2.2.30"))
		return runDomainSearchPSSM(genome, genomeRef, dl);
	    else
		throw new Exception("unsupported program");
	}
	return null;
    }

    /**
       Runs a domain search on a single genome, returning annotations
       and alignments.  Takes a map of individual models and a model
       db file as input.
    */
    public DomainAnnotation runDomainSearchPSSM(Genome genome,
						String genomeRef,
						DomainLibrary dl) throws Exception {
						
	String genomeName = genome.getScientificName();
	File dbFile = new File(dl.getLibraryFiles().get(0).getFileName());
	File fastaFile = File.createTempFile("proteome", ".fasta", tempDir);
	File tabFile = null;

	final Map<String,Tuple2<String,String>> modelNameToRefConsensus = new HashMap<String,Tuple2<String,String>>();

	Map<String,DomainModel> domains = dl.getDomains();
	for (String accession : domains.keySet()) {
	    DomainModel m = domains.get(accession);
	    Tuple2<String,String>domainModelRefConsensus =
		new Tuple2<String,String>();
	    domainModelRefConsensus.setE1(accession);
	    domainModelRefConsensus.setE2(new String("TEST"));
	    modelNameToRefConsensus.put(accession, domainModelRefConsensus);
	}
	
	try {
	    final Map<String, List<Tuple5<String, Long, Long, Long, Map<String, List<Tuple5<Long, Long, Double, Double, Double>>>>>> contig2prots = 
		new TreeMap<String, List<Tuple5<String, Long, Long, Long, Map<String, List<Tuple5<Long, Long, Double, Double, Double>>>>>>();
	    FastaWriter fw = new FastaWriter(fastaFile);
	    int protCount = 0;
	    final Map<Integer, Tuple2<String, Long>> posToContigFeatIndex = new LinkedHashMap<Integer, Tuple2<String, Long>>();
	    Map<String, Tuple2<String, Long>> featIdToContigFeatIndex = new TreeMap<String, Tuple2<String, Long>>();
	    try {
		for (int pos = 0; pos < genome.getFeatures().size(); pos++) {
		    Feature feat = genome.getFeatures().get(pos);
		    String seq = feat.getProteinTranslation();
		    if (feat.getLocation().size() != 1)
			continue;
		    Tuple4<String, Long, String, Long> loc = feat.getLocation().get(0);
		    String contigId = loc.getE1();
		    if (seq != null && !seq.isEmpty()) {
			fw.write("" + pos, seq);
			Tuple2<String, Long> contigFeatIndex = new Tuple2<String, Long>().withE1(contigId);
			posToContigFeatIndex.put(pos, contigFeatIndex);
			featIdToContigFeatIndex.put(feat.getId(), contigFeatIndex);
			protCount++;
		    }
		    List<Tuple5<String, Long, Long, Long, Map<String, List<Tuple5<Long, Long, Double, Double, Double>>>>> prots = contig2prots.get(contigId);
		    if (prots == null) {
			prots = new ArrayList<Tuple5<String, Long, Long, Long, Map<String, List<Tuple5<Long, Long, Double, Double, Double>>>>>();
			contig2prots.put(contigId, prots);
		    }
		    long start = loc.getE3().equals("-") ? (loc.getE2() - loc.getE4() + 1) : loc.getE2();
		    long stop = loc.getE3().equals("-") ? loc.getE2() : (loc.getE2() + loc.getE4() - 1);
		    long dir = loc.getE3().equals("-") ? -1 : +1;
		    prots.add(new Tuple5<String, Long, Long, Long, Map<String, List<Tuple5<Long, Long, Double, Double, Double>>>>()
			      .withE1(feat.getId()).withE2(start).withE3(stop).withE4(dir)
			      .withE5(new TreeMap<String, List<Tuple5<Long, Long, Double, Double, Double>>>()));
		}
	    }
	    finally {
		try { fw.close(); } catch (Exception ignore) {}
	    }
	    if (protCount == 0)
		throw new IllegalStateException("There are no protein translations in genome " + genomeName + " (" + genomeRef + ")");

	    // make file of all proteins
	    Map<String, Tuple2<Long, Long>> contigSizes = new TreeMap<String, Tuple2<Long, Long>>();
	    for (int contigPos = 0; contigPos < genome.getContigIds().size(); contigPos++) {
		String contigId = genome.getContigIds().get(contigPos);
		if (!contig2prots.containsKey(contigId))
		    continue;
		List<Tuple5<String, Long, Long, Long, Map<String, List<Tuple5<Long, Long, Double, Double, Double>>>>> prots = contig2prots.get(contigId);
		Collections.sort(prots, new Comparator<Tuple5<String, Long, Long, Long, Map<String, List<Tuple5<Long, Long, Double, Double, Double>>>>>() {
			@Override
			    public int compare(Tuple5<String, Long, Long, Long, Map<String, List<Tuple5<Long, Long, Double, Double, Double>>>> o1,
					       Tuple5<String, Long, Long, Long, Map<String, List<Tuple5<Long, Long, Double, Double, Double>>>> o2) {
			    return Long.compare(o1.getE2(), o2.getE2());
			}
		    });
		long contigSize = genome.getContigLengths().get(contigPos);
		contigSizes.put(contigId, new Tuple2<Long, Long>().withE1(contigSize).withE2((long)prots.size()));
		for (int index = 0; index < prots.size(); index++) {
		    String featId = prots.get(index).getE1();
		    Tuple2<String, Long> contigFeatIndex = featIdToContigFeatIndex.get(featId);
		    if (contigFeatIndex != null)
			contigFeatIndex.setE2((long)index);
		}
	    }
	    tabFile = runRpsBlast(dbFile, fastaFile);
	    final Map<String, Map<String, Map<String, String>>> modelToFeatureToStartToAlignment = 
		new TreeMap<String, Map<String, Map<String, String>>>();
	    final long[] stat = {0L};
	    RpsBlastParser.processRpsOutput(tabFile, new RpsBlastParser.RpsBlastCallback() {
		    @Override
			public void next(String query, String subject, int qstart, String qseq,
					 int sstart, String sseq, String evalue, double bitscore,
					 double ident) throws Exception {
			Tuple2<String,String> domainModelRefConsensus = modelNameToRefConsensus.get(subject);
			if (domainModelRefConsensus == null)
			    throw new IllegalStateException("Unexpected subject name in prs blast result: " + subject);
			int featurePos = Integer.parseInt(query);
			/*
			  String consensus = domainModelRefConsensus.getE2();
			  int alnLen = consensus.length();
			  String alignedSeq = AlignUtil.removeGapsFromSubject(alnLen, qseq, sstart - 1, sseq);
			  int coverage = 100 - AlignUtil.getGapPercent(alignedSeq);
			  if (coverage < MIN_COVERAGE)
			  return;
			*/
			int coverage = 100;
			String alignedSeq = "TEST";
			Tuple2<String, Long> contigIdFeatIndex = posToContigFeatIndex.get(featurePos);
			long featureIndex = contigIdFeatIndex.getE2();
			Map<String, List<Tuple5<Long, Long, Double, Double, Double>>> domains = contig2prots.get(
														 contigIdFeatIndex.getE1()).get((int)featureIndex).getE5();
			String domainRef = domainModelRefConsensus.getE1();
			List<Tuple5<Long, Long, Double, Double, Double>> places = domains.get(domainRef);
			if (places == null) {
			    places = new ArrayList<Tuple5<Long, Long, Double, Double, Double>>();
			    domains.put(domainRef, places);
			}
			int qlen = AlignUtil.removeGaps(qseq).length();
			places.add(new Tuple5<Long, Long, Double, Double, Double>().withE1((long)qstart)
				   .withE2((long)qstart + qlen - 1).withE3(Double.parseDouble(evalue))
				   .withE4(bitscore).withE5(coverage / 100.0));
			Map<String, Map<String, String>> featureToStartToAlignment = 
			    modelToFeatureToStartToAlignment.get(domainRef);
			if (featureToStartToAlignment == null) {
			    featureToStartToAlignment = new TreeMap<String, Map<String, String>>();
			    modelToFeatureToStartToAlignment.put(domainRef, featureToStartToAlignment);
			}
			String contigId = contigIdFeatIndex.getE1();
			String featureId = contig2prots.get(contigId).get((int)featureIndex).getE1();
			Map<String, String> startToAlignment = featureToStartToAlignment.get(featureId);
			if (startToAlignment == null) {
			    startToAlignment = new TreeMap<String, String>();
			    featureToStartToAlignment.put(featureId, startToAlignment);
			}
			startToAlignment.put("" + qstart, alignedSeq);
			stat[0] += alignedSeq.length();
		    }
		});
	    DomainAnnotation rv = new DomainAnnotation()
		.withGenomeRef(genomeRef)
		.withData(contig2prots)
		.withContigToSizeAndFeatureCount(contigSizes)
		.withFeatureToContigAndIndex(featIdToContigFeatIndex);
	    return rv;
	}
	finally {
	    try { fastaFile.delete(); } catch (Exception ignore) {}
	    if (tabFile != null)
		try { tabFile.delete(); } catch (Exception ignore) {}
	}
    }

    
    /**
       Formats a library made from a user-defined set of SMP (PSSM)
       files.  Not used by current code, which requires a pre-formatted
       library.
    private void prepareScoremats(String token,
				  String domainModelSetRef,
				  List<File> smpFiles,
				  Map<String,String> modelRefToNameRet,
				  Map<String,Tuple2<String,String>> optionalModelNameToRefConsensus)
	throws Exception {
	DomainModelSet set;
	File domainSetFile = getDomainModelSetJsonFile(domainModelSetRef);
	if (domainSetFile.exists()) {
	    set = Utils.getMapper().readValue(domainSetFile, DomainModelSet.class);
	}
	else {
	    set = storage.getObjects(token, Arrays.asList(new ObjectIdentity().withRef(domainModelSetRef))).get(0).getData().asClassInstance(DomainModelSet.class);
	    Utils.getMapper().writeValue(domainSetFile, set);
	}
	for (String parentRef : set.getParentRefs())
	    prepareScoremats(token, parentRef, smpFiles, modelRefToNameRet, optionalModelNameToRefConsensus);
	List<ObjectIdentity> modelRefCache = new ArrayList<ObjectIdentity>();
	for (String modelRef : set.getDomainModelRefs()) {
	    File modelFile = getDomainModelJsonFile(modelRef);
	    if (modelFile.exists())
		try {
		    DomainModel model = Utils.getMapper().readValue(modelFile, DomainModel.class);
		    prepareModel(modelRef, model, smpFiles, modelRefToNameRet, optionalModelNameToRefConsensus);
		}
		catch (Exception ignore) {
		    modelFile.delete();
		}
	    if (!modelFile.exists())
		modelRefCache.add(new ObjectIdentity().withRef(modelRef));
	    if (modelRefCache.size() >= modelBufferMaxSize)
		cacheDomainModels(token, modelRefCache);
	}
	if (modelRefCache.size() > 0)
	    cacheDomainModels(token, modelRefCache);
	for (String modelRef : set.getDomainModelRefs()) {
	    if (modelRefToNameRet.containsKey(modelRef))
		return;
	    File modelFile = getDomainModelJsonFile(modelRef);
	    DomainModel model = Utils.getMapper().readValue(modelFile, DomainModel.class);
	    prepareModel(modelRef, model, smpFiles, modelRefToNameRet, optionalModelNameToRefConsensus);			
	}
    }
     */

    /**
       prepares a single RPS-BLAST model.  Not used by current code,
       which requires a pre-formatted library.
    private void prepareModel(String modelRef, DomainModel model, List<File> smpFiles,
			      Map<String,String> modelRefToNameRet, Map<String,Tuple2<String,String>> modelNameToRefConsensus) {
	if (modelRefToNameRet.containsKey(modelRef))
	    return;
	smpFiles.add(getDomainModelSmpFile(modelRef));
	modelRefToNameRet.put(modelRef, model.getDomainName());
	if (modelNameToRefConsensus != null)
	    modelNameToRefConsensus.put(model.getDomainName(), 
					new Tuple2<String,String>().withE1(modelRef).withE2(model.getCddConsensusSeq()));
    }
    */

    
    /**
       ???
    */
    public static String getRefFromObjectInfo(Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>> info) {
	return info.getE7() + "/" + info.getE1() + "/" + info.getE5();
    }

    /**
       Has to do with formatting a domain model library.  Not used
       in current code:
       
    private void cacheDomainModels(String token, List<ObjectIdentity> refs) throws Exception {
	for (ObjectData data : storage.getObjects(token, refs)) {
	    String ref = getRefFromObjectInfo(data.getInfo());
	    DomainModel model = data.getData().asClassInstance(DomainModel.class);
	    File smpOutputFile = getDomainModelSmpFile(ref);
	    saveModelSmpIntoFile(model, smpOutputFile);
	    model.setCddScorematGzipFile("");
	    Utils.getMapper().writeValue(getDomainModelJsonFile(ref), model);
	}
	refs.clear();
    }
    */

    /*
      Doesn't work for now; model libraries must be pre-formatted:
      
    public static void saveModelSmpIntoFile(DomainModel model, File smpOutputFile)
	throws IOException {
	String smpText = Utils.unbase64ungzip(model.getCddScorematGzipFile());
	Writer smpW = new FileWriter(smpOutputFile);
	smpW.write(smpText);
	smpW.close();
    }
    */
	
    private File getDomainModelJsonFile(String modelRef) {
	return new File(getDomainsDir(), "model_" + modelRef.replace('/', '_') + ".json");
    }

    private File getDomainModelSmpFile(String modelRef) {
	return new File(getDomainsDir(), "model_" + modelRef.replace('/', '_') + ".smp");
    }

    private File getDomainModelSetJsonFile(String modelSetRef) {
	return new File(getDomainsDir(), "modelset_" + modelSetRef.replace('/', '_') + ".json");
    }

    public File getBinDir() {
	File ret = new File(tempDir, "bin");
	if (!ret.exists())
	    ret.mkdir();
	return ret;
    }

    private File getDomainsDir() {
	File ret = new File(tempDir, "domains");
	if (!ret.exists())
	    ret.mkdir();
	return ret;
    }

    private File getFormatRpsDbBin() throws Exception {
	return BinPreparator.prepareBin(getBinDir(), "makeprofiledb");
    }

    private File getRpsBlastBin() throws Exception {
	return BinPreparator.prepareBin(getBinDir(), "rpsblast");
    }

    /**
       Formats a RPS-BLAST database, using the same parameters
       used by CDD, according to:
       ftp://ftp.ncbi.nih.gov/pub/mmdb/cdd/README
    */
    public void formatRpsDb(List<File> scorematFiles, File dbFile) throws Exception {
	//File tempInputFile = File.createTempFile("rps", ".db", tempDir);
	PrintWriter pw = new PrintWriter(dbFile);
	for (File f : scorematFiles) {
	    pw.println(f.getAbsolutePath());
	}
	pw.close();
	CorrectProcess cp = null;
	ByteArrayOutputStream errBaos = null;
	Exception err = null;
	String binPath = getFormatRpsDbBin().getAbsolutePath();
	int procExitValue = -1;
	try {
	    Process p = Runtime.getRuntime().exec(CorrectProcess.arr(binPath,
								     "-in", dbFile.getAbsolutePath(), "-threshold", "9.82", 
								     "-scale", "100.0", "-dbtype", "rps", "-index", "true"));
	    errBaos = new ByteArrayOutputStream();
	    cp = new CorrectProcess(p, null, "formatrpsdb", errBaos, "");
	    p.waitFor();
	    errBaos.close();
	    procExitValue = p.exitValue();
	}
	catch(Exception ex) {
	    try{ 
		errBaos.close(); 
	    }
	    catch (Exception ignore) {}
	    try{ 
		if(cp!=null) 
		    cp.destroy(); 
	    }
	    catch (Exception ignore) {}
	    err = ex;
	}
	if (errBaos != null) {
	    String err_text = new String(errBaos.toByteArray());
	    if (err_text.length() > 0)
		err = new Exception("FastTree: " + err_text, err);
	}
	if (procExitValue != 0) {
	    if (err == null)
		err = new IllegalStateException("FastTree exit code: " + procExitValue);
	    throw err;
	}
    }

    /**
       Runs RPS-BLAST on a file
    */
    public File runRpsBlast(File dbFile, File fastaQuery) throws Exception {
	File tempOutputFile = File.createTempFile("rps", ".tab", tempDir);
	CorrectProcess cp = null;
	ByteArrayOutputStream errBaos = null;
	Exception err = null;
	String binPath = getRpsBlastBin().getAbsolutePath();
	int procExitValue = -1;
	FileOutputStream fos = new FileOutputStream(tempOutputFile);
	try {
	    Process p = Runtime.getRuntime().exec(CorrectProcess.arr(binPath,
								     "-db", dbFile.getAbsolutePath(), "-query", fastaQuery.getAbsolutePath(), 
								     "-outfmt", RpsBlastParser.OUTPUT_FORMAT_STRING, 
								     "-evalue", MAX_BLAST_EVALUE));
	    errBaos = new ByteArrayOutputStream();
	    cp = new CorrectProcess(p, fos, "", errBaos, "");
	    p.waitFor();
	    errBaos.close();
	    procExitValue = p.exitValue();
	}
	catch(Exception ex) {
	    try{ 
		errBaos.close(); 
	    }
	    catch (Exception ignore) {}
	    try{ 
		if(cp!=null) 
		    cp.destroy(); 
	    }
	    catch (Exception ignore) {}
	    err = ex;
	}
	finally {
	    try { fos.close(); } catch (Exception ignore) {}
	}
	if (errBaos != null) {
	    String err_text = new String(errBaos.toByteArray());
	    if (err_text.length() > 0)
		err = new Exception("RPS-BLAST: " + err_text, err);
	}
	if (procExitValue != 0) {
	    if (err == null)
		err = new IllegalStateException("RPS-BLAST exit code: " + procExitValue);
	    throw err;
	}
	return tempOutputFile;
    }
	
    public void processRpsOutput(File results, RpsBlastParser.RpsBlastCallback callback) throws Exception {
	RpsBlastParser.processRpsOutput(results, callback);
    }
}
