package us.kbase.kbasegenefamilies.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

public class Utils {
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
}
