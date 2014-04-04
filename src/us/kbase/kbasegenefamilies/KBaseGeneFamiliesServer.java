package us.kbase.kbasegenefamilies;

import us.kbase.auth.AuthToken;
import us.kbase.common.service.JsonServerMethod;
import us.kbase.common.service.JsonServerServlet;

//BEGIN_HEADER
//END_HEADER

/**
 * <p>Original spec-file module name: KBaseGeneFamilies</p>
 * <pre>
 * </pre>
 */
public class KBaseGeneFamiliesServer extends JsonServerServlet {
    private static final long serialVersionUID = 1L;

    //BEGIN_CLASS_HEADER
    //END_CLASS_HEADER

    public KBaseGeneFamiliesServer() throws Exception {
        super("KBaseGeneFamilies");
        //BEGIN_CONSTRUCTOR
        //END_CONSTRUCTOR
    }

    /**
     * <p>Original spec-file function name: search_protein_families</p>
     * <pre>
     * </pre>
     * @param   input   instance of type {@link us.kbase.kbasegenefamilies.SearchProteinFamiliesParams SearchProteinFamiliesParams} (original type "search_protein_families_params")
     * @return   parameter "job_id" of String
     */
    @JsonServerMethod(rpc = "KBaseGeneFamilies.search_protein_families")
    public String searchProteinFamilies(SearchProteinFamiliesParams input, AuthToken authPart) throws Exception {
        String returnVal = null;
        //BEGIN search_protein_families
        //END search_protein_families
        return returnVal;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: <program> <server_port>");
            return;
        }
        new KBaseGeneFamiliesServer().startupServer(Integer.parseInt(args[0]));
    }
}
