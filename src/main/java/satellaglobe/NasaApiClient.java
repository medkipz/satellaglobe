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
}