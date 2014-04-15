package us.kbase.kbasegenefamilies;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.core.type.TypeReference;

import us.kbase.auth.AuthService;
import us.kbase.auth.AuthToken;
import us.kbase.common.service.Tuple11;
import us.kbase.common.service.Tuple2;
import us.kbase.common.service.Tuple4;
import us.kbase.common.service.Tuple5;
import us.kbase.common.service.UObject;
import us.kbase.common.utils.AlignUtil;
import us.kbase.common.utils.CorrectProcess;
import us.kbase.common.utils.FastaWriter;
import us.kbase.common.utils.RpsBlastParser;
import us.kbase.kbasegenefamilies.bin.BinPreparator;
import us.kbase.kbasegenefamilies.util.Utils;
import us.kbase.kbasegenomes.Feature;
import us.kbase.kbasegenomes.Genome;
import us.kbase.workspace.ListObjectsParams;
import us.kbase.workspace.ObjectData;
import us.kbase.workspace.ObjectIdentity;
import us.kbase.workspace.SaveObjectsParams;
import us.kbase.workspace.WorkspaceClient;

public class DomainSearchTask {
	
	private static final String tempDirPath = "temp_files";
	private static final String wsUrl = "http://140.221.84.209:7058";  // "https://kbase.us/services/ws/";
	private static final String genomeWsName = "KBasePublicGenomesLoad";
	private static final String domainWsName = "KBasePublicGeneDomains";
	private static final String genomeWsType = "KBaseGenomes.Genome";
	private static final String domainModelWsType = "KBaseGeneFamilies.DomainModel";
	private static final String domainTypeWsType = "KBaseGeneFamilies.DomainModelType";
	private static final String domainSetWsType = "KBaseGeneFamilies.DomainModelSet";
	private static final String defaultDomainSetObjectName = "BacterialProteinDomains.set";
	private static String MAX_EVALUE = "1e-05";
	private static int MIN_COVERAGE = 50;
	private static final int modelBufferMaxSize = 100;
	
	private final File tempDir;
	private final ObjectStorage objectStorage;
	
	public static void main(String[] args) throws Exception {
		String token = AuthService.login("nardevuser1", "*****").getToken().toString();
		WorkspaceClient client = new WorkspaceClient(new URL(wsUrl), new AuthToken(token));
		client.setAuthAllowedForHttp(true);
		ObjectStorage objectStorage = createDefaultObjectStorage(client);
		String domainModelSetRef = domainWsName + "/" + defaultDomainSetObjectName;
		DomainSearchTask task = new DomainSearchTask(new File(tempDirPath), objectStorage);
		for (Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>> info : 
				client.listObjects(new ListObjectsParams().withWorkspaces(Arrays.asList(genomeWsName)).withType(genomeWsType))) {
			String genomeRef = getRefFromObjectInfo(info);
			task.runDomainSearch(token, domainModelSetRef, genomeRef);
			break;
		}
	}
	
	public DomainSearchTask(File tempDir, ObjectStorage objectStorage) {
		this.tempDir = tempDir;
		this.objectStorage = objectStorage;
	}
	
	private void runDomainSearch(String token, String domainModelSetRef, String genomeRef) throws Exception {
		domainModelSetRef = correctRef(token, domainModelSetRef);
		System.out.println("DomainSearchTask: setref=" + domainModelSetRef);
		final Genome genome = objectStorage.getObjects(token, Arrays.asList(
				new ObjectIdentity().withRef(genomeRef))).get(0).getData().asClassInstance(Genome.class);
		UObject.getMapper().writeValue(new File(tempDir, "temp.json"), genome);
		System.out.println("Genome: " + genome.getScientificName() + " (" + genome.getId() + ")");
		String genomeName = genome.getScientificName();
		File fastaFile = File.createTempFile("proteome", ".fasta", tempDir);
		File tabFile = null;
		try {
			final Map<String, List<Tuple5<String, Long, Long, Long, Map<String, List<Tuple4<Long, Long, Double, Double>>>>>> contig2prots = 
					new HashMap<String, List<Tuple5<String, Long, Long, Long, Map<String, List<Tuple4<Long, Long, Double, Double>>>>>>();
			FastaWriter fw = new FastaWriter(fastaFile);
			int protCount = 0;
			final Map<Integer, Tuple2<String, Integer>> posToContigFeatIndex = new LinkedHashMap<Integer, Tuple2<String, Integer>>();
			final Map<String, Map<String, Tuple2<String, Integer>>> contigIdToFeatIdToContigFeatIndex = 
					new LinkedHashMap<String, Map<String, Tuple2<String, Integer>>>();
			try {
				for (int pos = 0; pos < genome.getFeatures().size(); pos++) {
					Feature feat = genome.getFeatures().get(pos);
					String seq = feat.getProteinTranslation();
					Tuple4<String, Long, String, Long> loc = feat.getLocation().get(0);
					String contigId = loc.getE1();
					if (seq != null && !seq.isEmpty()) {
						fw.write("" + pos, seq);
						Tuple2<String, Integer> contigFeatIndex = new Tuple2<String, Integer>().withE1(contigId);
						posToContigFeatIndex.put(pos, contigFeatIndex);
						Map<String, Tuple2<String, Integer>> featIdToContigFeatIndex = contigIdToFeatIdToContigFeatIndex.get(contigId);
						if (featIdToContigFeatIndex == null) {
							featIdToContigFeatIndex = new LinkedHashMap<String, Tuple2<String, Integer>>();
							contigIdToFeatIdToContigFeatIndex.put(contigId, featIdToContigFeatIndex);
						}
						featIdToContigFeatIndex.put(feat.getId(), contigFeatIndex);
						protCount++;
					}
					List<Tuple5<String, Long, Long, Long, Map<String, List<Tuple4<Long, Long, Double, Double>>>>> prots = contig2prots.get(contigId);
					if (prots == null) {
						prots = new ArrayList<Tuple5<String, Long, Long, Long, Map<String, List<Tuple4<Long, Long, Double, Double>>>>>();
						contig2prots.put(contigId, prots);
					}
					long start = loc.getE3().equals("-") ? (loc.getE2() - loc.getE4() + 1) : loc.getE2();
					long stop = loc.getE3().equals("-") ? (loc.getE2() - loc.getE4() + 1) : loc.getE2();
					long dir = loc.getE3().equals("-") ? -1 : +1;
					prots.add(new Tuple5<String, Long, Long, Long, Map<String, List<Tuple4<Long, Long, Double, Double>>>>()
							.withE1(feat.getId()).withE2(start).withE3(stop).withE4(dir)
							.withE5(new TreeMap<String, List<Tuple4<Long, Long, Double, Double>>>()));
				}
			} finally {
				try { fw.close(); } catch (Exception ignore) {}
			}
			if (protCount == 0)
				throw new IllegalStateException("There are no protein translations in genome " + genomeName + " (" + genomeRef + ")");
			Map<String, Long> contigSizes = new LinkedHashMap<>();
			for (String contigId : contig2prots.keySet()) {
				List<Tuple5<String, Long, Long, Long, Map<String, List<Tuple4<Long, Long, Double, Double>>>>> prots = contig2prots.get(contigId);
				Collections.sort(prots, new Comparator<Tuple5<String, Long, Long, Long, Map<String, List<Tuple4<Long, Long, Double, Double>>>>>() {
					@Override
					public int compare(
							Tuple5<String, Long, Long, Long, Map<String, List<Tuple4<Long, Long, Double, Double>>>> o1,
							Tuple5<String, Long, Long, Long, Map<String, List<Tuple4<Long, Long, Double, Double>>>> o2) {
						return Long.compare(o1.getE2(), o2.getE2());
					}
				});
				contigSizes.put(contigId, (long)prots.size());
				Map<String, Tuple2<String, Integer>> featIdToContigFeatIndex = contigIdToFeatIdToContigFeatIndex.get(contigId);
				if (featIdToContigFeatIndex != null) {
					for (int index = 0; index < prots.size(); index++) {
						String featId = prots.get(index).getE1();
						Tuple2<String, Integer> contigFeatIndex = featIdToContigFeatIndex.get(featId);
						if (contigFeatIndex != null)
							contigFeatIndex.setE2(index);
					}
				}
			}
			File dbFile = getDomainModelSetDbFile(domainModelSetRef);
			File mapFile = getDomainModelSetMapFile(domainModelSetRef);
			final Map<String,Tuple2<String,String>> modelNameToRefConsensus;
			if (mapFile.exists() && dbFile.exists()) {
				modelNameToRefConsensus = UObject.getMapper().readValue(mapFile, new TypeReference<Map<String,Tuple2<String,String>>>() {});
			} else {
				List<File> smpFiles = new ArrayList<File>();
				modelNameToRefConsensus = new TreeMap<String, Tuple2<String,String>>();
				Map<String,String> modelRefToNameRet = new HashMap<String, String>(); 
				prepareScoremats(token, domainModelSetRef, smpFiles, modelRefToNameRet, modelNameToRefConsensus);
				formatRpsDb(smpFiles, dbFile);
				UObject.getMapper().writeValue(mapFile, modelNameToRefConsensus);
			}
			tabFile = runRpsBlast(dbFile, fastaFile);
			RpsBlastParser.processRpsOutput(tabFile, new RpsBlastParser.RpsBlastCallback() {
				@Override
				public void next(String query, String subject, int qstart, String qseq,
						int sstart, String sseq, String evalue, double bitscore,
						double ident) throws Exception {
					Tuple2<String,String> domainModelRefConsensus = modelNameToRefConsensus.get(subject);
					if (domainModelRefConsensus == null)
						throw new IllegalStateException("Unexpected subject name in prs blast result: " + subject);
					int featurePos = Integer.parseInt(query);
					int alnLen = domainModelRefConsensus.getE2().length();
					String alignedSeq = AlignUtil.removeGapsFromSubject(alnLen, qseq, sstart - 1, sseq);
					int coverage = 100 - AlignUtil.getGapPercent(alignedSeq);
					if (coverage < MIN_COVERAGE)
						return;
					Tuple2<String, Integer> contigIdFeatIndex = posToContigFeatIndex.get(featurePos);
					Map<String, List<Tuple4<Long, Long, Double, Double>>> domains = contig2prots.get(
							contigIdFeatIndex.getE1()).get(contigIdFeatIndex.getE2()).getE5();
					List<Tuple4<Long, Long, Double, Double>> places = domains.get(domainModelRefConsensus.getE1());
					if (places == null) {
						places = new ArrayList<Tuple4<Long, Long, Double, Double>>();
						domains.put(domainModelRefConsensus.getE1(), places);
					}
					int qlen = AlignUtil.removeGaps(qseq).length();
					places.add(new Tuple4<Long, Long, Double, Double>().withE1((long)qstart)
							.withE2((long)qstart + qlen - 1).withE3(Double.parseDouble(evalue)).withE4(bitscore));
					System.out.println(domainModelRefConsensus.getE2());
					System.out.println(alignedSeq);
					System.out.println();
				}
			});
		} finally {
			try { fastaFile.delete(); } catch (Exception ignore) {}
			//if (tabFile != null)
			//	try { tabFile.delete(); } catch (Exception ignore) {}
		}
	}
	
	private String correctRef(String token, String ref) throws Exception {
		return getRefFromObjectInfo(objectStorage.getObjects(token, Arrays.asList(new ObjectIdentity().withRef(ref))).get(0).getInfo());
	}
	
	private void prepareScoremats(String token, String domainModelSetRef, List<File> smpFiles,
			Map<String,String> modelRefToNameRet, Map<String,Tuple2<String,String>> optionalModelNameToRefConsensus) throws Exception {
		DomainModelSet set;
		File domainSetFile = getDomainModelSetJsonFile(domainModelSetRef);
		if (domainSetFile.exists()) {
			set = UObject.getMapper().readValue(domainSetFile, DomainModelSet.class);
		} else {
			set = objectStorage.getObjects(token, Arrays.asList(
					new ObjectIdentity().withRef(domainModelSetRef))).get(0).getData().asClassInstance(DomainModelSet.class);
			UObject.getMapper().writeValue(domainSetFile, set);
		}
		for (String parentRef : set.getParentRefs())
			prepareScoremats(token, parentRef, smpFiles, modelRefToNameRet, optionalModelNameToRefConsensus);
		List<ObjectIdentity> modelRefCache = new ArrayList<ObjectIdentity>();
		for (String modelRef : set.getDomainModelRefs()) {
			File modelFile = getDomainModelJsonFile(modelRef);
			if (modelFile.exists())
				try {
					DomainModel model = UObject.getMapper().readValue(modelFile, DomainModel.class);
					prepareModel(modelRef, model, smpFiles, modelRefToNameRet, optionalModelNameToRefConsensus);
				} catch (Exception ignore) {
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
			DomainModel model = UObject.getMapper().readValue(modelFile, DomainModel.class);
			prepareModel(modelRef, model, smpFiles, modelRefToNameRet, optionalModelNameToRefConsensus);			
		}
	}

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

	private static String getRefFromObjectInfo(Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>> info) {
		return info.getE7() + "/" + info.getE1() + "/" + info.getE5();
	}

	private void cacheDomainModels(String token, List<ObjectIdentity> refs) throws Exception {
		for (ObjectData data : objectStorage.getObjects(token, refs)) {
			String ref = getRefFromObjectInfo(data.getInfo());
			DomainModel model = data.getData().asClassInstance(DomainModel.class);
			String smpText = Utils.unbase64ungzip(model.getCddScorematGzipFile());
			Writer smpW = new FileWriter(getDomainModelSmpFile(ref));
			smpW.write(smpText);
			smpW.close();
			model.setCddScorematGzipFile("");
			UObject.getMapper().writeValue(getDomainModelJsonFile(ref), model);
		}
		refs.clear();
	}
	
	private File getDomainModelJsonFile(String modelRef) {
		return new File(getDomainsDir(), "model_" + modelRef.replace('/', '_') + ".json");
	}

	private File getDomainModelSmpFile(String modelRef) {
		return new File(getDomainsDir(), "model_" + modelRef.replace('/', '_') + ".smp");
	}

	private File getDomainModelSetJsonFile(String modelSetRef) {
		return new File(getDomainsDir(), "modelset_" + modelSetRef.replace('/', '_') + ".json");
	}

	private File getDomainModelSetDbFile(String modelSetRef) {
		return new File(getDomainsDir(), "modelset_" + modelSetRef.replace('/', '_') + ".db");
	}

	private File getDomainModelSetMapFile(String modelSetRef) {
		return new File(getDomainsDir(), "modelset_" + modelSetRef.replace('/', '_') + ".map");
	}

	private File getBinDir() {
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
		} catch(Exception ex) {
			try{ 
				errBaos.close(); 
			} catch (Exception ignore) {}
			try{ 
				if(cp!=null) 
					cp.destroy(); 
			} catch (Exception ignore) {}
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
					"-evalue", MAX_EVALUE));
			errBaos = new ByteArrayOutputStream();
			cp = new CorrectProcess(p, fos, "", errBaos, "");
			p.waitFor();
			errBaos.close();
			procExitValue = p.exitValue();
		} catch(Exception ex) {
			try{ 
				errBaos.close(); 
			} catch (Exception ignore) {}
			try{ 
				if(cp!=null) 
					cp.destroy(); 
			} catch (Exception ignore) {}
			err = ex;
		} finally {
			try { fos.close(); } catch (Exception ignore) {}
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
		return tempOutputFile;
	}
	
	public void processRpsOutput(File results, RpsBlastParser.RpsBlastCallback callback) throws Exception {
		RpsBlastParser.processRpsOutput(results, callback);
	}
	
	public static ObjectStorage createDefaultObjectStorage(final WorkspaceClient client) {
		return new ObjectStorage() {
			
			@Override
			public List<Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String, String>>> saveObjects(
					String authToken, SaveObjectsParams params) throws Exception {
				return client.saveObjects(params);
			}
			
			@Override
			public List<ObjectData> getObjects(String authToken,
					List<ObjectIdentity> objectIds) throws Exception {
				return client.getObjects(objectIds);
			}
		};
	}
}