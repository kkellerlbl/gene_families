package us.kbase.kbasegenefamilies.test;

import java.io.*;
import java.util.*;
import java.net.URL;

import static junit.framework.Assert.*;

import org.junit.Test;

import us.kbase.workspace.*;
import us.kbase.kbasegenomes.*;
import us.kbase.kbasegenefamilies.*;
import com.fasterxml.jackson.databind.*;

/**
   Annotates E. coli
*/
public class EColiTest {
    // private static final String wsUrl = "http://dev04.berkeley.kbase.us:7058";
    private static final String wsUrl = "https://kbase.us/services/ws/";
    private static final String wsName = "KBasePublicGenomesV4";
    private static final String ecoliRef = wsName+"/kb|g.0";

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
