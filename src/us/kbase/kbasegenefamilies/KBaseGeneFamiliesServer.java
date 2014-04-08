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
     * <p>Original spec-file function name: search_domains</p>
     * <pre>
     * </pre>
     * @param   params   instance of type {@link us.kbase.kbasegenefamilies.SearchDomainsParams SearchDomainsParams}
     * @return   parameter "job_id" of String
     */
    @JsonServerMethod(rpc = "KBaseGeneFamilies.search_domains")
    public String searchDomains(SearchDomainsParams params, AuthToken authPart) throws Exception {
        String returnVal = null;
        //BEGIN search_domains
        //END search_domains
        return returnVal;
    }

    /**
     * <p>Original spec-file function name: construct_domain_clusters</p>
     * <pre>
     * </pre>
     * @param   params   instance of type {@link us.kbase.kbasegenefamilies.ConstructDomainClustersParams ConstructDomainClustersParams}
     * @return   parameter "job_id" of String
     */
    @JsonServerMethod(rpc = "KBaseGeneFamilies.construct_domain_clusters")
    public String constructDomainClusters(ConstructDomainClustersParams params, AuthToken authPart) throws Exception {
        String returnVal = null;
        //BEGIN construct_domain_clusters
        //END construct_domain_clusters
        return returnVal;
    }

    /**
     * <p>Original spec-file function name: search_domains_and_construct_clusters</p>
     * <pre>
     * </pre>
     * @param   params   instance of type {@link us.kbase.kbasegenefamilies.SearchDomainsAndConstructClustersParams SearchDomainsAndConstructClustersParams}
     * @return   parameter "job_id" of String
     */
    @JsonServerMethod(rpc = "KBaseGeneFamilies.search_domains_and_construct_clusters")
    public String searchDomainsAndConstructClusters(SearchDomainsAndConstructClustersParams params, AuthToken authPart) throws Exception {
        String returnVal = null;
        //BEGIN search_domains_and_construct_clusters
        //END search_domains_and_construct_clusters
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
