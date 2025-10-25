package satellaglobe;

import java.util.List;

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
}