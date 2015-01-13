package us.kbase.kbasegenefamilies.prepare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import us.kbase.common.service.JsonClientException;
import us.kbase.common.service.test.Tuple2;
import us.kbase.kbasegenefamilies.DomainModel;
import us.kbase.kbasegenefamilies.DomainModelType;
import us.kbase.kbasegenefamilies.util.Utils;

public class ScorematParser {
    private static final String scorematFilesDir = "/Users/rsutormin/Work/2014-03-17_trees/smp";
	
    public static void main(String[] args) throws IOException {
	Map<String, Integer> typeToSize = new TreeMap<String, Integer>();
	for (File f : new File(scorematFilesDir).listFiles()) {
	    if (f.getName().endsWith(".smp")) {
		List<String> scoremat = readFile(f);
		String title = searchTitle(scoremat);
		if (title.isEmpty())
		    System.out.println(f.getName() + ": " + title);
		String consensus = searchConsensus(scoremat);
		if (consensus.isEmpty())
		    System.out.println(f.getName() + ": " + consensus);
		String type = getDomainTypeForScorematFile(f);
		Integer size = typeToSize.get(type);
		if (size == null) {
		    size = 1;
		} else {
		    size++;
		}
		typeToSize.put(type, size);
		if (size % 3000 == 0)
		    System.out.println("Type " + type + ": size=" + size);
	    }
	}
	System.out.println("---------");
	System.out.println(typeToSize);
    }
	
    public static String getDomainTypeForScorematFile(File f) {
	String ret = f.getName();
	int pos = 0;
	while (Character.isLetter(ret.charAt(pos)))
	    pos++;
	return ret.substring(0, pos);
    }
	
    public static String getDomainNameForScorematFile(File f) {
	String ret = f.getName();
	return ret.substring(0, ret.lastIndexOf('.'));
    }
	
    public static List<String> readFile(File smpFile) throws IOException {
	List<String> ret = new ArrayList<String>();
	BufferedReader br = new BufferedReader(new FileReader(smpFile));
	while (true) {
	    String l = br.readLine();
	    if (l == null)
		break;
	    ret.add(l);
	}
	br.close();
	return ret;
    }
	
    public static String searchTitle(List<String> scoremat) {
	return searchText(scoremat, "title", true);
    }
	
    public static String searchConsensus(List<String> scoremat) {
	return searchText(scoremat, "seq-data ncbieaa", false);
    }
	
    public static String searchText(List<String> scoremat, String prefix, boolean space) {
	StringBuilder ret = null;
	for (String line : scoremat) {
	    line = line.trim();
	    if (ret == null) {
		if (line.startsWith(prefix + " \"")) {
		    ret = new StringBuilder();
		    ret.append(line.substring(prefix.length() + 2));
		}
	    } else {
		if (space)
		    ret.append(' ');
		ret.append(line);
	    }
	    if (ret != null && ret.length() > 0 && ret.charAt(ret.length() - 1) == '\"') {
		ret.deleteCharAt(ret.length() - 1);
		break;
	    }
	}
	if (ret == null)
	    throw new IllegalStateException("Can not find text for: " + prefix);
	return ret.toString().trim();
    }
	
    private static boolean isDomainSupported(String domainTypeName) {
	switch (domainTypeName) {
	case "CHL": return false;
	case "LOAD": return false;
	case "MTH": return false;
	case "PHA": return false;
	case "PLN": return false;
	case "PTZ": return false;
	default: return true;
	}
    }
	
    private static boolean isDomainFullLength(String domainTypeName) {
	switch (domainTypeName) {
	case "COG": return true;
	case "KOG": return true;
	case "PRK": return true;
	case "TIGR": return false;
	case "cd": return false;
	case "pfam": return false;
	case "smart": return false;
	default: throw new IllegalStateException("Unsupported domain type: " + domainTypeName);
	}
    }
	
    private static String getDomainTypeVersion(String domainTypeName) {
	switch (domainTypeName) {
	case "COG": return "1";
	case "KOG": return "-";
	case "PRK": return "-";
	case "TIGR": return "13.0";
	case "cd": return "-";
	case "pfam": return "27";
	case "smart": return "6.0";
	default: throw new IllegalStateException("Unsupported domain type: " + domainTypeName);
	}
    }
	
    private static DomainModelType constructDomainType(String domainTypeName) {
	DomainModelType ret = new DomainModelType();
	ret.setCddRpsBlastVersion("2.2.30");
	ret.setDate("2014-02-20");
	ret.setDescription("Domain type [" + domainTypeName + "] described in CDD database as a set of scoremat profiles");
	ret.setTypeName(domainTypeName);
	ret.setIsCdd(1L);
	ret.setIsFullLength(isDomainFullLength(domainTypeName) ? 1L : 0L);
	ret.setVersion(getDomainTypeVersion(domainTypeName));
	ret.setSourceName("CDD");
	ret.setSourceUrl("ftp://ftp.ncbi.nih.gov/pub/mmdb/cdd");
	ret.setSourceVersion("3.11");
	ret.setIsHmm(0L);
	return ret;
    }
	
    public static String compressScoremat(List<String> lines) {
	StringBuilder buf = new StringBuilder();
	for (String l : lines)
	    buf.append(l).append('\n');
	String text = buf.toString();
	String ret = Utils.gzipBase64(text);
	String unzipped = Utils.unbase64ungzip(ret);
	if (!unzipped.equals(text))
	    throw new IllegalStateException("Unzipped: " + unzipped);
	return ret;
    }
	
    public static DomainModel constructDomainModel(File smpFile, 
						   Map<String, Tuple2<DomainModelType, String>> typeToDescrRef, 
						   DomainModelTypeStorage typeStorage) throws IOException, JsonClientException {
	String typeName = getDomainTypeForScorematFile(smpFile);
	if (!isDomainSupported(typeName))
	    return null;
	List<String> scoremat = readFile(smpFile);
	String title = searchTitle(scoremat);
	String consensus = searchConsensus(scoremat);
	Tuple2<DomainModelType, String> typeDescr = typeToDescrRef.get(typeName);
	if (typeDescr == null) {
	    DomainModelType type = constructDomainType(typeName);
	    String ref = typeStorage.storeDomainModelTypeAndGetRef(type);
	    typeDescr = new Tuple2<DomainModelType, String>().withE1(type).withE2(ref);
	    typeToDescrRef.put(typeName, typeDescr);
	}
	DomainModel ret = new DomainModel();
	// ret.setCddConsensusSeq(consensus);
	ret.setDescription(title);
	ret.setName(getDomainNameForScorematFile(smpFile));
	ret.setModelType(typeDescr.getE2());
	// ret.setCddScorematGzipFile(compressScoremat(scoremat));
	return ret;
    }
	
    public interface DomainModelTypeStorage {
	public String storeDomainModelTypeAndGetRef(DomainModelType type) throws IOException, JsonClientException;
    }
}
