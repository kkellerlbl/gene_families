package us.kbase.kbasegenefamilies.test;

import java.io.*;
import java.util.*;
import java.net.URL;

import org.junit.Test;
import static junit.framework.Assert.*;

import org.strbio.IO;
import org.strbio.util.*;
import com.fasterxml.jackson.databind.*;

import us.kbase.auth.AuthService;
import us.kbase.auth.AuthToken;
import us.kbase.common.service.*;
import us.kbase.workspace.*;
import us.kbase.kbasegenomes.*;
import us.kbase.kbasegenefamilies.*;

/**
   Tests for setting up sample db and annotating E. coli
*/
public class EColiTest {
    private static final String wsUrl = "https://kbase.us/services/ws/";
    private static final String genomeWsName = "KBasePublicGenomesV4";
    private static final String domainWsName = "KBasePublicGeneDomains";
    private static final String domainLibraryType = "KBaseGeneFamilies.DomainLibrary-1.0";
    private static final String domainModelSetType = "KBaseGeneFamilies.DomainModelSet-1.0";
    private static final String domainAnnotationType = "KBaseGeneFamilies.DomainAnnotation-1.0";
    private static final String ecoliRef = genomeWsName+"/kb|g.0";
    private static final String dlRef = domainWsName+"/COGs-CDD-3.12";
    private static final String dmsRef = domainWsName+"/COGs-only";

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
	    WorkspaceClient wc = createWsClient(null);
	    genome = wc.getObjects(Arrays.asList(new ObjectIdentity().withRef(ecoliRef))).get(0).getData().asClassInstance(Genome.class);
	    mapper.writeValue(f,genome);
	}
	
	System.out.println(genome.getScientificName());
	assertEquals(genome.getScientificName(), "Escherichia coli K12");
    }

    /**
       Make a DomainModelSet for COGs
    */
    // @Test
    public void getCOGs() throws Exception {
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
	Vector<Handle>libraryFiles = new Vector<Handle>();
	dl.setLibraryFiles(libraryFiles);

	Map<String,String> domainPrefix = new HashMap<String,String>();
	domainPrefix.put("COG","http://www.ncbi.nlm.nih.gov/Structure/cdd/cddsrv.cgi?uid=");
	dms.setDomainPrefixToDbxrefUrl(domainPrefix);

	// File f = new File("/kb/dev_container/modules/gene_families/data/tmp/COGs-CDD-3.12.json");
	// mapper.writeValue(f,dl);
	// f = new File("/kb/dev_container/modules/gene_families/data/tmp/COGs-only.json");
	// mapper.writeValue(f,dms);

	// save to ws instead of filesystem
	WorkspaceClient wc = createWsClient(getDevToken());
	String dlRef =
	    getRefFromObjectInfo(wc.saveObjects(new SaveObjectsParams()
			   .withWorkspace(domainWsName)
			   .withObjects(Arrays.asList(new ObjectSaveData()
						      .withType(domainLibraryType)
						      .withName("COGs-CDD-3.12")
						      .withData(new UObject(dl))))).get(0));
	
	Map<String,String> domainLibs = new HashMap<String,String>();
	domainLibs.put("COG",dlRef);
	dms.setDomainLibs(domainLibs);
	
	wc.saveObjects(new SaveObjectsParams()
		       .withWorkspace(domainWsName)
		       .withObjects(Arrays.asList(new ObjectSaveData()
						  .withType(domainModelSetType)
						  .withName("COGs-only")
						  .withData(new UObject(dms)))));
    }

    @Test
	public void searchEColi() throws Exception {

	WorkspaceClient wc = createWsClient(getDevToken());
	DomainLibrary dl =
	    wc.getObjects(Arrays.asList(new ObjectIdentity().withRef(dlRef))).get(0).getData().asClassInstance(DomainLibrary.class);
	    
	DomainModelSet dms = 
	    wc.getObjects(Arrays.asList(new ObjectIdentity().withRef(dmsRef))).get(0).getData().asClassInstance(DomainModelSet.class);
	
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

	Genome genome = wc.getObjects(Arrays.asList(new ObjectIdentity().withRef(ecoliRef))).get(0).getData().asClassInstance(Genome.class);

	Tuple2<DomainAnnotation, DomainAlignments> results = dst.runDomainSearch(genome,ecoliRef,new File("/kb/dev_container/modules/gene_families/data/db/Cog"),modelNameToRefConsensus);

	// f = new File("/kb/dev_container/modules/gene_families/data/tmp/DomainAnnotation-g.0.json");
	// mapper.writeValue(f,results.getE1());

	wc.saveObjects(new SaveObjectsParams()
		       .withWorkspace(domainWsName)
		       .withObjects(Arrays.asList(new ObjectSaveData()
						  .withType(domainAnnotationType)
						  .withName("COGs-g.0")
						  .withData(new UObject(results.getE1())))));
    }

    /**
       creates a workspace client; if token is null, client can
       only read public workspaces
    */
    public static WorkspaceClient createWsClient(AuthToken token) throws Exception {
	WorkspaceClient rv = null;
	if (token==null)
	    rv = new WorkspaceClient(new URL(wsUrl));
	else
	    rv = new WorkspaceClient(new URL(wsUrl),token);
	rv.setAuthAllowedForHttp(true);
	return rv;
    }

    /**
       gets the auth token out of the properties file.  To create
       it on your dev instance, do:
       <pre>
       kbase-login (your user name)
       kbase-whoami -t
       </pre>
       Take the resulting text (starting with "un=") and put it in
       the auth.properties file, as auth.token.  Replace the text
       in the file that says "paste token here" with your token.
    */
    public static AuthToken getDevToken() throws Exception {
	Properties prop = new Properties();
	try {
	    prop.load(EColiTest.class.getClassLoader().getResourceAsStream("auth.properties"));
	}
	catch (IOException e) {
	}
	catch (SecurityException e) {
	}
	String value = prop.getProperty("auth.token", null);
	return new AuthToken(value);
    }

    private static String getRefFromObjectInfo(Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>> info) {
	return info.getE7() + "/" + info.getE1() + "/" + info.getE5();
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
