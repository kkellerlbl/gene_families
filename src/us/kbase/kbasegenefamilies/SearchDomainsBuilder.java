package us.kbase.kbasegenefamilies;

import java.io.File;
import java.util.Arrays;

import us.kbase.common.service.Tuple2;
import us.kbase.common.service.UObject;
import us.kbase.workspace.ObjectSaveData;
import us.kbase.workspace.ProvenanceAction;
import us.kbase.workspace.SaveObjectsParams;

public class SearchDomainsBuilder extends DefaultTaskBuilder<SearchDomainsParams> {

	public SearchDomainsBuilder() {
	}
	
	public SearchDomainsBuilder(File tempDir, ObjectStorage objectStorage) {
		this.tempDir = tempDir;
		this.storage = objectStorage;
	}

	@Override
	public Class<SearchDomainsParams> getInputDataType() {
		return SearchDomainsParams.class;
	}
	
	@Override
	public String getOutRef(SearchDomainsParams inputData) {
		return inputData.getOutWorkspace() + "/" + inputData.getOutResultId();
	}
	
	@Override
	public String getTaskDescription() {
		return "Search domains for one genome";
	}
	
	@Override
	public void run(String token, SearchDomainsParams inputData, String jobId,
			String outRef) throws Exception {
		DomainSearchTask dst = new DomainSearchTask(tempDir, storage);
		Tuple2<DomainAnnotation, DomainAlignments> res = dst.runDomainSearch(
				token, inputData.getDmsRef(), inputData.getGenome());
		saveResult(inputData.getOutWorkspace(), inputData.getOutResultId(), token, res.getE1(), res.getE2(), inputData);
	}
	
	private void saveResult(String ws, String id, String token, DomainAnnotation annRes, 
			DomainAlignments alnRes, SearchDomainsParams inputData) throws Exception {
		ProvenanceAction alnProv = new ProvenanceAction().withDescription(
				"Domain alignments for genome " + inputData.getGenome())
				.withService(KBaseGeneFamiliesServer.SERVICE_REGISTERED_NAME)
				.withServiceVer(KBaseGeneFamiliesServer.SERVICE_VERSION)
				.withMethod("construct_multiple_alignment")
				.withMethodParams(Arrays.asList(new UObject(inputData)));
		String alignmentsRef = DomainSearchTask.getRefFromObjectInfo(storage.saveObjects(token,
				new SaveObjectsParams().withWorkspace(ws).withObjects(
				Arrays.asList(new ObjectSaveData().withType(DomainSearchTask.domainAlignmentsWsType)
						.withName(id + ".aln").withData(new UObject(alnRes))
						.withProvenance(Arrays.asList(alnProv))))).get(0));
		annRes.setAlignmentsRef(alignmentsRef);
		ObjectSaveData data = new ObjectSaveData().withData(new UObject(annRes))
				.withType(DomainSearchTask.domainAnnotationWsType)
				.withProvenance(Arrays.asList(new ProvenanceAction()
				.withDescription("Domain annotation was constructed using rps-blast program")
				.withService("KBaseGeneFamilies").withServiceVer(KBaseGeneFamiliesServer.SERVICE_VERSION)
				.withMethod("construct_multiple_alignment")
				.withMethodParams(Arrays.asList(new UObject(inputData)))));
		try {
			long objid = Long.parseLong(id);
			data.withObjid(objid);
		} catch (NumberFormatException ex) {
			data.withName(id);
		}
		storage.saveObjects(token, new SaveObjectsParams().withWorkspace(ws).withObjects(
				Arrays.asList(data)));
	}

}
