package us.kbase.kbasegenefamilies.test;

import java.io.*;
import java.util.*;
import java.net.URL;

import org.junit.Test;
import static junit.framework.Assert.*;

import org.strbio.IO;
import org.strbio.util.*;
import us.kbase.common.service.*;
import us.kbase.workspace.*;
import us.kbase.kbasegenomes.*;
import us.kbase.kbasegenefamilies.*;
import com.fasterxml.jackson.databind.*;

/**
   Tests for setting up sample db and annotating E. coli
*/
public class EColiTest {
    // private static final String wsUrl = "http://dev04.berkeley.kbase.us:7058";
    private static final String wsUrl = "https://kbase.us/services/ws/";
    private static final String wsName = "KBasePublicGenomesV4";
    private static final String ecoliRef = wsName+"/kb|g.0";

    /**
       check that we can read E coli genome from WS or file;
       if we get from WS, cache in file to avoid delays/loading server
    */
    @Test public void getEColi() throws Exception {
	Genome genome = null;
	
	ObjectMapper mapper = new ObjectMapper();

	File f = new File("/kb/dev_container/modules/gene_families/data/tmp/g.0");
	if (f.canRead()) {
	    System.out.println("Reading genome from file");
	    try {
		genome = mapper.readValue(f, Genome.class);
	    }
	    catch (Exception e) {
		genome = null;
	    }
	}

	if (genome==null) {
	    System.out.println("Reading genome from WS");
	    WorkspaceClient client = new WorkspaceClient(new URL(wsUrl));
	    client.setAuthAllowedForHttp(true);
	    genome = client.getObjects(Arrays.asList(new ObjectIdentity().withRef(ecoliRef))).get(0).getData().asClassInstance(Genome.class);
	    mapper.writeValue(f,genome);
	}
	
	System.out.println(genome.getScientificName());
	assertEquals(genome.getScientificName(), "Escherichia coli K12");
    }

    /**
       Make a DomainModelSet for COGs
    */
    @Test public void getCOGs() throws Exception {
	ObjectMapper mapper = new ObjectMapper();

	DomainModelSet dms = new DomainModelSet().withSetName("COGs-only");

	DomainLibrary dl = new DomainLibrary()
	    .withId("COGs-CDD-3.12")
	    .withSource("CDD")
	    .withSourceUrl("ftp://ftp.ncbi.nih.gov/pub/mmdb/cdd/")
	    .withVersion("3.12")
	    .withReleaseDate("2014-10-03")
	    .withProgram("rpsblast-2.2.30")
	    .withDomainPrefix("COG")
	    .withDbxrefPrefix("http://www.ncbi.nlm.nih.gov/Structure/cdd/cddsrv.cgi?uid=")
	    .withLibraryFiles(null);

	Map<String,DomainModel> domains = new HashMap<String,DomainModel>();
	Map<String,String> accessionToDescription = new HashMap<String,String>();

	BufferedReader infile = IO.openReader("/kb/dev_container/modules/gene_families/data/db/cddid.tbl.gz");
	String buffer;
	while ((buffer=infile.readLine()) != null) {
	    StringTokenizer st = new StringTokenizer(buffer,"\t");
	    DomainModel m = new DomainModel();
	    m.setCddId(st.nextToken());
	    String accession = st.nextToken();
	    m.setAccession(accession);
	    m.setName(st.nextToken());
	    String description = st.nextToken();
	    m.setDescription(description);
	    m.setLength(StringUtil.atol(st.nextToken()));
	    m.setModelType("PSSM");
	    if (accession.startsWith("COG")) {
		domains.put(accession,m);
		accessionToDescription.put(accession,description);
	    }
	}

	dl.setDomains(domains);
	dms.setDomainAccessionToDescription(accessionToDescription);	

	Map<String,String> domainLibs = new HashMap<String,String>();
	domainLibs.put("COG","COGs-CDD-3.12");
	dms.setDomainLibs(domainLibs);
	
	Map<String,String> domainPrefix = new HashMap<String,String>();
	domainPrefix.put("COG","http://www.ncbi.nlm.nih.gov/Structure/cdd/cddsrv.cgi?uid=");
	dms.setDomainPrefixToDbxrefUrl(domainPrefix);

	File f = new File("/kb/dev_container/modules/gene_families/data/tmp/COGs-CDD-3.12.json");
	mapper.writeValue(f,dl);
	f = new File("/kb/dev_container/modules/gene_families/data/tmp/COGs-only.json");
	mapper.writeValue(f,dms);
    }

    @Test public void searchEColi() throws Exception {
	ObjectMapper mapper = new ObjectMapper();
	File f = new File("/kb/dev_container/modules/gene_families/data/tmp/COGs-CDD-3.12.json");
	DomainLibrary dl = mapper.readValue(f,DomainLibrary.class);

	f = new File("/kb/dev_container/modules/gene_families/data/tmp/COGs-only.json");
	DomainModelSet dms = mapper.readValue(f,DomainModelSet.class);
	
	DomainSearchTask dst = new DomainSearchTask(new File("/kb/dev_container/modules/gene_families/data/tmp"), null);
	Map<String,Tuple2<String,String>> modelNameToRefConsensus = new HashMap<String,Tuple2<String,String>>();

	Map<String,DomainModel> domains = dl.getDomains();
	for (String accession : domains.keySet()) {
	    DomainModel m = domains.get(accession);
	    Tuple2<String,String>domainModelRefConsensus =
		new Tuple2<String,String>();
	    domainModelRefConsensus.setE1(accession);
	    domainModelRefConsensus.setE2(new String("TEST"));
	    modelNameToRefConsensus.put(accession, domainModelRefConsensus);
	}

	f = new File("/kb/dev_container/modules/gene_families/data/tmp/g.0");
	Genome genome = mapper.readValue(f, Genome.class);

	Tuple2<DomainAnnotation, DomainAlignments> results = dst.runDomainSearch(genome,ecoliRef,new File("/kb/dev_container/modules/gene_families/data/db/Cog"),modelNameToRefConsensus);

	f = new File("/kb/dev_container/modules/gene_families/data/tmp/DomainAnnotation-g.0.json");
	mapper.writeValue(f,results.getE1());
    }
    

    public static void main(String[] args) {
	try {
	    EColiTest t = new EColiTest();
	    t.getEColi();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
