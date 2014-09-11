package us.kbase.kbasegenefamilies;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import us.kbase.common.service.Tuple2;
import us.kbase.kbasegenefamilies.ConstructDomainClustersBuilder.GenomeAnnotationAlignmentProvider;
import us.kbase.workspace.ObjectIdentity;

public class SearchDomainsAndConstructClustersBuilder extends DefaultTaskBuilder<SearchDomainsAndConstructClustersParams> {

	public SearchDomainsAndConstructClustersBuilder() {
	}
	
	public SearchDomainsAndConstructClustersBuilder(File tempDir, ObjectStorage objectStorage) {
		this.tempDir = tempDir;
		this.storage = objectStorage;
	}

	@Override
	public Class<SearchDomainsAndConstructClustersParams> getInputDataType() {
		return SearchDomainsAndConstructClustersParams.class;
	}
	
	@Override
	public String getOutRef(SearchDomainsAndConstructClustersParams inputData) {
		return inputData.getOutWorkspace() + "/" + inputData.getOutResultId();
	}
	
	@Override
	public String getTaskDescription() {
		return "Search domains for set of genomes and construct domain clusters for produced domain annotations";
	}
	
	@Override
	public void run(String token, SearchDomainsAndConstructClustersParams inputData, String jobId,
			String outRef) throws Exception {
		List<Tuple2<DomainAnnotation, DomainAlignments>> annsAlns = 
				new ArrayList<Tuple2<DomainAnnotation, DomainAlignments>>();
		String dmsRef = inputData.getDmsRef();
		if (dmsRef == null) {
			if (inputData.getClustersForExtension() == null)
				throw new IllegalStateException("Domain model set is not defined");
			
			DomainClusterSearchResult parent = storage.getObjects(token, Arrays.asList(new ObjectIdentity().withRef(
					inputData.getClustersForExtension()))).get(0).getData().asClassInstance(DomainClusterSearchResult.class);
			dmsRef = parent.getUsedDmsRef();
		}
		DomainSearchTask dst = new DomainSearchTask(tempDir, storage);
		for (String genomeRef : inputData.getGenomes()) {
			Tuple2<DomainAnnotation, DomainAlignments> annAln = dst.runDomainSearch(token, dmsRef, genomeRef);
			annsAlns.add(annAln);
		}
		GenomeAnnotationAlignmentProvider annAlnProv = new GenomeAnnotationAlignmentProvider(
				storage, token, null, annsAlns);
		ConstructDomainClustersBuilder.constructClusters(storage, token, tempDir, jobId, 
				"search_domains_and_construct_clusters", inputData, 
				annAlnProv, inputData.getDmsRef(), inputData.getClustersForExtension(), 
				inputData.getOutWorkspace(), inputData.getOutResultId(), 
				inputData.getIsDomainClusterDataStoredOutside() != null && 
				inputData.getIsDomainClusterDataStoredOutside() != 0L, 
				inputData.getDomainClusterDataIdPrefix(), 
				inputData.getDomainClusterDataIdSuffix());
	}
}
