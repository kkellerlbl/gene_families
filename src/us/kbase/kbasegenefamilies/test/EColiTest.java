package us.kbase.kbasegenefamilies.test;

import java.io.*;
import java.util.*;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Test;

import us.kbase.kbasegenomes.Genome;
import us.kbase.workspace.ObjectIdentity;
import us.kbase.workspace.WorkspaceClient;
import us.kbase.kbasegenefamilies.*;

/**
   Annotates E. coli
*/
public class EColiTest {
    private static final String wsUrl = "http://dev04.berkeley.kbase.us:7058";
    private static final String wsName = "KBasePublicGenomesV4";
    private static final String ecoliRef = wsName+"/kb|g.0";

    @Test public void getEColi() throws Exception {
	WorkspaceClient client = new WorkspaceClient(new URL(wsUrl), null);
	client.setAuthAllowedForHttp(true);

	Genome genome = client.getObjects(Arrays.asList(new ObjectIdentity().withRef(ecoliRef))).get(0).getData().asClassInstance(Genome.class);

	System.out.println(genome.getScientificName());
    }
}
