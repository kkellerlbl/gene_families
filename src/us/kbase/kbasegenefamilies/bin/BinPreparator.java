package us.kbase.kbasegenefamilies.bin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import us.kbase.common.utils.CorrectProcess;

public class BinPreparator {
    public static File prepareBin(File dir, String programName) throws Exception {
	File ret = new File(dir, programName);
	if (ret.exists())
	    return ret;
	String suffix = getOsSuffix();
	OutputStream os = new FileOutputStream(ret);
	InputStream is = BinPreparator.class.getResourceAsStream("/" + programName + "." + suffix);
	try {
	    IOUtils.copy(is, os);
	}
	catch (IOException ex) {
	    try { os.close(); } catch (Exception ignore) {}
	    try { if (ret.exists()) ret.delete(); } catch (Exception ignore) {}
	}
	finally {
	    try { os.close(); } catch (Exception ignore) {}
	    try { is.close(); } catch (Exception ignore) {}
	}
	new CorrectProcess(Runtime.getRuntime().exec(CorrectProcess.arr("chmod", "a+x", ret.getAbsolutePath()))).waitFor();
	return ret;
    }
    
    private static String getOsSuffix() {
	String osName = System.getProperty("os.name").toLowerCase();
	String suffix;
	if (osName.contains("linux")) {
	    suffix = "linux";
	} else if (osName.contains("mac os x")) {
	    suffix = "macosx";
	} else {
	    throw new IllegalStateException("Unsupported OS type: " + osName);
	}
	return suffix;
    }
}
