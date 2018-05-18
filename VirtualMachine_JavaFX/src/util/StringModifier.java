package util;

public class StringModifier {

	public static String removeSpace(String src) {
		return src.replaceAll(" ", "");
	}
	
	public static String removeByMarker(String src, String marker) {
		if(src.startsWith(marker)) return "";
		return src;
	}
	
	public static String substring(String src, int start, String until) {
		return src.substring(start, src.indexOf(until));
	}
}