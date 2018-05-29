package util;

public class StringModifier {

	public static String removeSpace(String src) {
		return src.replaceAll(" ", "");
	}

	public static String removeByMarker(String src, String marker) {
		if (src.startsWith(marker))
			return "";
		int index = -1;
		if ((index = src.indexOf(marker)) > -1) {
			src = src.substring(0, index);
		}
		return src;
	}

	public static String substring(String src, int start, String until) {
		return src.substring(start, src.indexOf(until));
	}
}