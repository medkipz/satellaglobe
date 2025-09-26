import java.util.*;

public class NasaData {
	public static void main(String[] args) {
		final String satelliteUrl = "https://sscweb.gsfc.nasa.gov/WS/sscr/2/observatories"; // Satellite data API endpoint
		final String locationUrl = "https://sscweb.gsfc.nasa.gov/WS/sscr/2/locations/"; // Satellite location data API endpoint

		/*
		 * Locations structure:
		 * https://sscweb.gsfc.nasa.gov/WS/sscr/2/locations/[satellite code separated by commas]/[ISO 8601 start time]/[ISO 8601 end time]/[Coordinage System WE WANT GEO]
		 */

		String satelliteData = HttpRequester.getUrlData(satelliteUrl);
		String locationData = HttpRequester.getUrlData(locationUrl);

		List<String> satelliteIds = XMLParser.parseTags(satelliteData, "Id");
		List<String> satellitesNames = XMLParser.parseTags(satelliteData, "Name");

		System.out.println(satelliteIds);
		System.out.println(satellitesNames);

		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();

		HttpRequester.getUrlData(locationUrl + "/" + input + "/1");
	}
}