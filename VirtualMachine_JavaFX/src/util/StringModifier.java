package util;

public class StringModifier {

	public static String removeSpace(String src) {
		return src.replaceAll(" ", "");
	}
	
	public static String removeByMarker(String src, String marker) {
		if(src.startsWith(marker)) return "";
		return src;
	}
}