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
	String suffix = getOsSuffix();
	File ret = new File(dir, programName + "." + suffix);
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
