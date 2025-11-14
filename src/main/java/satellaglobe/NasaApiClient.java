package satellaglobe;

import java.text.*;
import java.util.*;

import jakarta.xml.bind.*;

/**
 * Utility class for interacting with the NASA API.
 */
public class NasaApiClient {
	/**
	 * Get a list of all satellite names from the NASA API.
	 * @return A list of satellite names
	 */
	public static final List<String> GetAllSatelliteNames() {
		return XMLUtils.parseTags(
			HttpRequester.getUrlData("https://sscweb.gsfc.nasa.gov/WS/sscr/2/observatories"),
			"Name"
		);
	}

	/**
	 * Get a list of all satellite names from the NASA API.
	 * @return A list of satellite names that are currently active
	 */
	public static final List<String> GetAllActiveSatelliteNames() throws ParseException {
		Date currentDate = new Date();

		List<String> allNames = XMLUtils.parseTags(
			HttpRequester.getUrlData("https://sscweb.gsfc.nasa.gov/WS/sscr/2/observatories"),
			"Name"
		);

		List<String> endTimes = XMLUtils.parseTags(
			HttpRequester.getUrlData("https://sscweb.gsfc.nasa.gov/WS/sscr/2/observatories"),
			"EndTime"
		);

		for (int i = endTimes.size() - 1; i > 0 ; i--) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			Date endDate = df.parse(endTimes.get(i));
			if (endDate.before(currentDate)) {
				allNames.remove(i);
			}
		}

		return allNames;
	}

	public static final List<Observatory> GetAllObservatories() throws JAXBException {
		String xmlData = HttpRequester.getUrlData("https://sscweb.gsfc.nasa.gov/WS/sscr/2/observatories");
		JAXBContext jaxbContext = JAXBContext.newInstance(ObservatoryResponse.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		java.io.StringReader reader = new java.io.StringReader(xmlData);
		ObservatoryResponse response = (ObservatoryResponse)jaxbUnmarshaller.unmarshal(reader);
		if (response != null && response.getObservatory() != null) {
			return response.getObservatory();
		} else {
			return new ArrayList<>();
		}
	}

	public static final SatelliteResponse getSatelliteResponse(String satelliteId) throws JAXBException {
		Date startDate = new Date();
		Date endDate  = new Date(startDate.getTime() + 3600 * 1000);

		String startTime = (new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")).format(startDate);
		String endTime = (new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")).format(endDate);

		String xmlData = HttpRequester.getUrlData("https://sscweb.gsfc.nasa.gov/WS/sscr/2/locations/" + satelliteId + "/" + startTime + "," + endTime + "/geo");
		JAXBContext jaxbContext = JAXBContext.newInstance(SatelliteResponse.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		java.io.StringReader reader = new java.io.StringReader(xmlData);
		SatelliteResponse result = (SatelliteResponse)jaxbUnmarshaller.unmarshal(reader);
		return result;
	}

	/**
	 * Get a list of all satellite IDs from the NASA API.
	 * @return A list of satellite IDs
	 */
	public static final List<String> GetAllSatelliteIds() {
		return XMLUtils.parseTags(
			HttpRequester.getUrlData("https://sscweb.gsfc.nasa.gov/WS/sscr/2/observatories"),
			"Id"
		);
	}

	/**
	 * Get a hashmap of Ids for names from the NASA API.
	 * @return A hashmap of satellite names to their IDs
	 */
	public static final java.util.Map<String, String> GetSatelliteNameIdMap() {
		List<String> names = GetAllSatelliteNames();
		List<String> ids = GetAllSatelliteIds();

		java.util.Map<String, String> nameIdMap = new java.util.HashMap<>();
		for (int i = 0; i < names.size() && i < ids.size(); i++) {
			nameIdMap.put(names.get(i), ids.get(i));
		}

		return nameIdMap;
	}

	/**
	 * Get satellite coordinates by its ID from the NASA API using an input time.
	 * @param satelliteId The ID of the satellite
	 * @param time The time for which to get the satellite coordinates
	 * @return an array of latitude and longitude
	 */
	public static final double[] GetSatelliteCoordinates(String satelliteId, String time) {
		return new double[] {0.0, 0.0}; // Placeholder implementation
	}

	 /**
	 * Get satellite details by its ID from the NASA API using current time.
	 * @param satelliteId The ID of the satellite
	 * @return A map of satellite details
	 */
	public static final double[] GetSatelliteCoordinates(String satelliteId) {
		Date startDate = new Date();
		Date endDate  = new Date(startDate.getTime() + 3600 * 1000);

		String startTime = (new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")).format(startDate);
		String endTime = (new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")).format(endDate);

		String xmlResponse = HttpRequester.getUrlData("https://sscweb.gsfc.nasa.gov/WS/sscr/2/locations/" + satelliteId + "/" + startTime + "," + endTime + "/geo");
		
		return new double[] {
			Double.parseDouble(XMLUtils.parseTags(xmlResponse, "X").get(0)),
			Double.parseDouble(XMLUtils.parseTags(xmlResponse, "Y").get(0)),
			Double.parseDouble(XMLUtils.parseTags(xmlResponse, "Z").get(0))
		};
	}
}