package us.kbase.kbasegenefamilies.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import us.kbase.common.service.JacksonTupleModule;
import us.kbase.common.service.JsonClientException;
import us.kbase.common.service.Tuple11;
import us.kbase.common.service.Tuple2;
import us.kbase.workspace.ListObjectsParams;
import us.kbase.workspace.WorkspaceClient;

public class Utils {
	private static ObjectMapper mapper = new ObjectMapper().registerModule(new JacksonTupleModule());

	public static String gzipBase64(String original) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream os = new GZIPOutputStream(baos);
			Writer wr = new OutputStreamWriter(os, Charset.forName("UTF-8"));
			wr.write(original);
			wr.close();
			return Base64.encodeBase64String(baos.toByteArray());
		} catch (IOException ex) {
			throw new IllegalStateException(ex.getMessage(), ex);
		}
	}
	
	public static String unbase64ungzip(String gzippedBase64ed) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decodeBase64(gzippedBase64ed));
			GZIPInputStream is = new GZIPInputStream(bais);
			StringWriter ret = new StringWriter();
			IOUtils.copy(is, ret, Charset.forName("UTF-8"));
			return ret.toString();
		} catch (IOException ex) {
			throw new IllegalStateException(ex.getMessage(), ex);
		}
	}
	
	public static List<Tuple2<String, String>> listAllObjectsRefAndName(WorkspaceClient client, 
			String wsName, String wsType) throws IOException, JsonClientException {
		List<Tuple2<String, String>> ret = new ArrayList<Tuple2<String, String>>();
		for (int partNum = 0; ; partNum++) {
			int sizeOfPart = 0;
			for (Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>> info : 
				client.listObjects(new ListObjectsParams().withWorkspaces(Arrays.asList(wsName))
						.withType(wsType).withLimit(10000L).withSkip(partNum * 10000L))) {
				String ref = getRefFromObjectInfo(info);
				String objectName = info.getE2();
				ret.add(new Tuple2<String, String>().withE1(ref).withE2(objectName));
				sizeOfPart++;
			}
			if (sizeOfPart == 0)
				break;
		}
		return ret;
	}

	public static String getRefFromObjectInfo(Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>> info) {
		return info.getE7() + "/" + info.getE1() + "/" + info.getE5();
	}

	public static ObjectMapper getMapper() {
		return mapper;
	}
}
