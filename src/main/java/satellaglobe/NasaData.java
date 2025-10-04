package satellaglobe;
import java.util.*;
import java.sql.Date;

public class NasaData {
	public static void main(String[] args) {
		final String satelliteUrl = "https://sscweb.gsfc.nasa.gov/WS/sscr/2/observatories"; // Satellite data API endpoint
		final String locationUrl = "https://sscweb.gsfc.nasa.gov/WS/sscr/2/locations"; // Satellite location data API endpoint

		/*
		 * Locations structure:
		 * https://sscweb.gsfc.nasa.gov/WS/sscr/2/locations/[satellite codes separated by commas]/[ISO 8601 start time]/[ISO 8601 end time]/[Coordinate System WE WANT GEO]
		 */

		String satelliteData = HttpRequester.getUrlData(satelliteUrl);

		List<String> satellitesNames = XMLUtils.parseTags(satelliteData, "Name");
		List<String> satelliteIds = XMLUtils.parseTags(satelliteData, "Id");
		
		System.out.println(satellitesNames);
		System.out.println(satelliteIds);

		// Later, this text input can be changed to a picker in the GUI
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter a satellite ID from the list above: ");
		String inputId = scanner.nextLine();

		System.out.print("Enter a start date [YYYY-MM-DD]: ");
		Date inputStartDate = java.sql.Date.valueOf(scanner.nextLine());
		String isoStart = DateUtils.toIsoDateString(inputStartDate);

		System.out.print("Enter an end date [YYYY-MM-DD]: ");
		Date inputEndDate = java.sql.Date.valueOf(scanner.nextLine());
		String isoEnd = DateUtils.toIsoDateString(inputEndDate);

		scanner.close();

		String locationData = HttpRequester.getUrlData(locationUrl + "/" + inputId + "/" + isoStart + "," + isoEnd + "/geo");
		System.out.println(locationData);
	}
}