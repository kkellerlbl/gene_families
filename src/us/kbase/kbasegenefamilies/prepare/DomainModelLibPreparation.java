package us.kbase.kbasegenefamilies.prepare;

import java.io.*;
import java.util.*;
import java.net.URL;

import org.strbio.IO;
import org.strbio.util.*;
import com.fasterxml.jackson.databind.*;

import us.kbase.auth.AuthService;
import us.kbase.auth.AuthToken;
import us.kbase.common.service.*;
import us.kbase.workspace.*;
import us.kbase.kbasegenomes.*;
import us.kbase.kbasegenefamilies.*;

public class DomainModelLibPreparation {
    private static final String wsUrl = "https://kbase.us/services/ws/";
    private static final String domainWsName = "KBasePublicGeneDomains";
    private static final String domainLibraryType = "KBaseGeneFamilies.DomainLibrary-1.0";

    public static void main(String[] args) throws Exception {
	/*
	  parseDomainLibrary("COGs-CDD-3.12",
			     "ftp://ftp.ncbi.nih.gov/pub/mmdb/cdd/",
			     "/kb/dev_container/modules/gene_families/data/db/cddid.tbl.gz",
			     "3.12",
			     "2014-10-03",
			     "COG",
			     "http://www.ncbi.nlm.nih.gov/Structure/cdd/cddsrv.cgi?uid=");
	parseDomainLibrary("CDD-NCBI-curated-3.12",
			   "ftp://ftp.ncbi.nih.gov/pub/mmdb/cdd/",
			   "/kb/dev_container/modules/gene_families/data/db/cddid.tbl.gz",
			   "3.12",
			   "2014-10-03",
			   "cd",
			   "http://www.ncbi.nlm.nih.gov/Structure/cdd/cddsrv.cgi?uid=");
	parseDomainLibrary("SMART-6.0-CDD-3.12",
			   "ftp://ftp.ncbi.nih.gov/pub/mmdb/cdd/",
			   "/kb/dev_container/modules/gene_families/data/db/cddid.tbl.gz",
			   "6.0",
			   "2014-10-03",
			   "smart",
			   "http://smart.embl-heidelberg.de/smart/do_annotation.pl?DOMAIN=");
	parseDomainLibrary("Pfam-27.0",
			   "ftp://ftp.ebi.ac.uk/pub/databases/Pfam/releases/Pfam27.0/Pfam-A.hmm.gz",
			   "/kb/dev_container/modules/gene_families/data/db/Pfam-A.hmm",
			   "27.0",
			   "2013-03-14",
			   "PF",
			   "http://pfam.xfam.org/family/");
	*/
	parseDomainLibrary("TIGRFAMs-15.0",
			   "ftp://ftp.jcvi.org/pub/data/TIGRFAMs/TIGRFAMs_15.0_HMM.LIB.gz",
			   "/kb/dev_container/modules/gene_families/data/db/TIGRFAMs_15.0_HMM.LIB",
			   "15.0",
			   "2014-09-17",
			   "TIGR",
			   "http://www.jcvi.org/cgi-bin/tigrfams/HmmReportPage.cgi?acc=");
    }
    
    /**
       Parses a DomainLibrary out of a set of downloaded CDD files.
       The info for each DomainModel is parsed from cddid.tbl.gz,
       which should have already been downloaded by prepare_3rd_party_dbs.sh
    */
    private static String parseDomainLibrary(String id,
					     String sourceURL,
					     String fileName,
					     String version,
					     String releaseDate,
					     String prefix,
					     String xref) throws Exception {

	System.out.println("Making domain library "+id);

	String source = null;
	String program = null;
	if (sourceURL.indexOf("cdd") > 0) {
	    source = "CDD";
	    program = "rpsblast-2.2.30";
	}
	else if (sourceURL.indexOf("Pfam") > 0) {
	    source = "Pfam";
	    program = "hmmscan-3.1b1";
	}
	else if (sourceURL.indexOf("TIGRFAMs") > 0) {
	    source = "TIGRFAMs";
	    program = "hmmscan-3.1b1";
	}
	else throw new Exception("unknown domain library type");

	DomainLibrary dl = new DomainLibrary()
	    .withId(id)
	    .withSource(source)
	    .withSourceUrl(sourceURL)
	    .withVersion(version)
	    .withReleaseDate(releaseDate)
	    .withProgram(program)
	    .withDomainPrefix(prefix)
	    .withDbxrefPrefix(xref)
	    .withLibraryFiles(null);

	Map<String,DomainModel> domains;
	if (source.equals("CDD"))
	    domains = parseCDDDomains(fileName,
				      prefix);
	else
	    domains = parseHMMDomains(fileName);
	
	dl.setDomains(domains);
	Vector<Handle>libraryFiles = new Vector<Handle>();
	dl.setLibraryFiles(libraryFiles);

	return saveDomainLibrary(dl,id);
    }
    
    /**
       Creates a set of DomainModels for CDD domains.  The info for
       each DomainModel is parsed from a file (should generally be
       cddid.tbl.gz, which is downloaded by prepare_3rd_party_dbs.sh)
    */
    private static Map<String,DomainModel> parseCDDDomains(String fileName,
							   String prefix) throws Exception {
	Map<String,DomainModel> domains = new HashMap<String,DomainModel>();

	BufferedReader infile = IO.openReader(fileName);
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
	    if (accession.startsWith(prefix))
		domains.put(accession,m);
	}
	infile.close();

	return domains;
    }

    /**
       Creates a set of DomainModels from a HMM library.  The info for
       each DomainModel is parsed from two files: the HMM library itself,
       and if suffix is set, the "full" file that says what type of
       domain each is.
    */
    private static Map<String,DomainModel> parseHMMDomains(String fileName) throws Exception {
	Map<String,DomainModel> domains = new HashMap<String,DomainModel>();

	BufferedReader infile = IO.openReader(fileName);
	String name= null, acc=null, desc=null;
	double tc=0.0;
	long l=0;

	while (infile.ready()) {
	    String buffer = infile.readLine();

	    if (buffer.startsWith("NAME "))
		name = buffer.substring(6).trim();
	    else if (buffer.startsWith("DESC "))
		desc = buffer.substring(6).trim();
	    else if (buffer.startsWith("TC "))
		tc = StringUtil.atod(buffer.substring(6));
	    else if (buffer.startsWith("ACC "))
		acc = buffer.substring(6).trim();
	    else if (buffer.startsWith("LENG "))
		l = StringUtil.atol(buffer.substring(6));
	    else if (buffer.startsWith("HMM ")) {
		DomainModel m = new DomainModel()
		    .withAccession(acc)
		    .withName(name)
		    .withDescription(desc)
		    .withLength(l)
		    .withModelType("HMM-Family");
		domains.put(acc,m);
	    }
	}
	infile.close();

	// hack to get Pfam domain types
	if (fileName.indexOf("Pfam") > -1) {
	    int pos = fileName.lastIndexOf(".");
	    fileName = fileName.substring(0,pos)+".full.gz";

	    infile = IO.openReader(fileName);
	    acc = null;
	    while (infile.ready()) {
		String buffer = infile.readLine();
		if (buffer.startsWith("# STOCK"))
		    acc = null;
		else if (buffer.startsWith("#=GF AC "))
		    acc = buffer.substring(10);
		else if (buffer.startsWith("#=GF TP ")) {
		    String domainType = buffer.substring(10);
		    DomainModel m = domains.get(acc);
		    if (m != null)
			m.setModelType("HMM-"+domainType);
		}
	    }
	    infile.close();
	}

	return domains;
    }
    
    /**
       saves a DomainLibrary in the public domain workspace, under
       a given ID.  Returns ref to the object.
    */
    private static String saveDomainLibrary(DomainLibrary dl,
					    String id) throws Exception {
	WorkspaceClient wc = createWsClient(getDevToken());
	String dlRef =
	    getRefFromObjectInfo(wc.saveObjects(new SaveObjectsParams()
			   .withWorkspace(domainWsName)
			   .withObjects(Arrays.asList(new ObjectSaveData()
						      .withType(domainLibraryType)
						      .withName(id)
						      .withData(new UObject(dl))))).get(0));

	return dlRef;
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
	    prop.load(DomainModelLibPreparation.class.getClassLoader().getResourceAsStream("auth.properties"));
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
}
