package satellaglobe;
import java.util.*;
import java.util.regex.*;

/**
 * Utility class for parsing XML data.
 */
public final class XMLUtils {
	/**
	 * Parse XML data to extract values of a specific tag.
	 * @param xmlData The XML data as a String
	 * @param tagName The tag name to extract values for
	 * @return A list of values found within the specified tags
	 */
	public static final List<String> parseTags(String xmlData, String tagName) {
		List<String> result = new ArrayList<>();

		// TODO parsing xml with regex is a HACK! MAKE THIS BETTER!!!
		Pattern pattern = Pattern.compile("<" + tagName + ">(.*?)</" + tagName + ">");
		Matcher matcher = pattern.matcher(xmlData);

		while (matcher.find()) {
			result.add(matcher.group(1));
		}

		return result;
	}
}