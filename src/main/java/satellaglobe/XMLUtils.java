package satellaglobe;
import java.util.*;
import java.util.regex.*;
import java.io.StringReader;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

/**
 * Utility class for parsing XML data.
 */
public final class XMLUtils {
	/**
	 * Convert XML data to a list of values for a specified tag.
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


	/**
	 * Convert XML data to a nested HashMap structure. The returned map has the
	 * root element name as its single key. Element values are one of:
	 * - String: when the element contains only text
	 * - HashMap<String,Object>: when the element contains attributes or child elements
	 * - List<Object>: when multiple child elements with the same name appear
	 *
	 * Attributes are added using the key "@attrName". Text content is stored
	 * under the key "#text" when the element also has attributes or child elements.
	 *
	 * @param xmlData The XML data as a String
	 * @return A Nested HashMap containing the XML structure
	 */
	 public static final Map<String, Object> convertXmlToMap(String xmlData) {
			 try {
				 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				 dbf.setNamespaceAware(true);
				 dbf.setXIncludeAware(false);
				 dbf.setExpandEntityReferences(false);

				 DocumentBuilder db = dbf.newDocumentBuilder();
				 Document doc = db.parse(new InputSource(new StringReader(xmlData)));
				 Element root = doc.getDocumentElement();

				 Map<String, Object> result = new LinkedHashMap<>();
				 result.put(root.getNodeName(), elementToObject(root));
				 return result;
			 } catch (Exception error) {
				 throw new IllegalArgumentException("Failed to parse XML into map", error);
			 }
		 }

	/**
	 * Helper: convert an Element into either a String, Map, or List representation
	 * @param element The XML Element to convert
	 * @return Either a String, Map, or List representation of the Element
	 */
	 private static Object elementToObject(Element element) {
		 // handle attributes
		 NamedNodeMap attributes = element.getAttributes();
		 HashMap<String, Object> map = new HashMap<>();
		 for (int i = 0; i < attributes.getLength(); i++) {
			 Node a = attributes.item(i);
			 map.put("@" + a.getNodeName(), a.getNodeValue());
		 }

		 // handle child elements and text
		 NodeList children = element.getChildNodes();
		 Map<String, List<Object>> multi = new LinkedHashMap<>();
		 StringBuilder textContent = new StringBuilder();

		 for (int i = 0; i < children.getLength(); i++) {
			 Node node = children.item(i);
			 if (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.CDATA_SECTION_NODE) {
				 String t = node.getTextContent().trim();
				 if (!t.isEmpty()) {
					 if (textContent.length() > 0) textContent.append(' ');
					 textContent.append(t);
				 }
			 } else if (node.getNodeType() == Node.ELEMENT_NODE) {
				 Element childElement = (Element) node;
				 Object childObject = elementToObject(childElement);
				 multi.computeIfAbsent(childElement.getNodeName(), k -> new ArrayList<>()).add(childObject);
			 }
		 }

		 // If no child elements and no attributes, return text directly
		 if (multi.isEmpty() && map.isEmpty()) {
			 return textContent.toString();
		 }

		 // attach remaining text if present
		 if (textContent.length() > 0) {
			 map.put("#text", textContent.toString());
		 }

		 // attach child elements; if only one instance, put single value, else List
		for (Map.Entry<String, List<Object>> entry : multi.entrySet()) {
			List<Object> list = entry.getValue();
			if (list.size() == 1) {
				map.put(entry.getKey(), list.get(0));
			} else {
				map.put(entry.getKey(), list);
			}
		}

		 return map;
	 }
}