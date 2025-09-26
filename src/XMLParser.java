package src;
import java.util.*;
import java.util.regex.*;

public final class XMLParser {
	/**
	 * Parse XML data to extract values of a specific tag.
	 * @param xmlData The XML data as a String
	 * @param tagName The tag name to extract values for
	 * @return A list of values found within the specified tags
	 */
	public static final List<String> parseTags(String xmlData, String tagName) {
		List<String> result = new ArrayList<>();

		Pattern pattern = Pattern.compile("<" + tagName + ">(.*?)</" + tagName + ">");
		Matcher matcher = pattern.matcher(xmlData);

		while (matcher.find()) {
			result.add(matcher.group(1));
		}

		return result;
	}
}