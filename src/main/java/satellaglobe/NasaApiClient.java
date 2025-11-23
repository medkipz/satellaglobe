package satellaglobe;

import java.text.*;
import java.util.*;

import jakarta.xml.bind.*;

/**
 * Utility class for interacting with the NASA API.
 */
public class NasaApiClient {
	public static final Date CURRENT_DATE = new Date();
	public static final Date START_DATE = new Date(CURRENT_DATE.getTime() - 3600 * 10000);
	public static final Date END_DATE = new Date(CURRENT_DATE.getTime() + 3600 * 10000);

	/**
	 * Get a list of all satellite names from the NASA API.
	 * @return A list of satellite names
	 */
	public static final List<String> GetAllSatelliteNames() {
		ArrayList<String> names = new ArrayList<>();

		for (Observatory observatory : GetAllObservatories()) {
			names.add(observatory.getName());
		}
		
		return names;
	}

	/**
	 * Get a list of all satellite names from the NASA API.
	 * @return A list of satellite names that are currently active
	 */
	public static final List<String> GetAllActiveSatelliteNames() {
		List<Observatory> observatories = GetAllObservatories();
		List<String> names = new ArrayList<>();

		// Index in reverse so removals don't mess up order
		for (int index = observatories.size() - 1; index > 0 ; index--) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			try {
				Date satelliteEndDate = df.parse(observatories.get(index).getEndTime());

				if (satelliteEndDate.before(CURRENT_DATE)) {
					observatories.remove(index);
				}
			} catch (ParseException e) {
				observatories.remove(index);
			}
		}

		for (Observatory observatory : observatories) {
			names.add(observatory.getName());
		}

		return names;
	}

	public static final List<Observatory> GetAllObservatories() {
		try {
			String xmlData = HttpRequester.getUrlData("https://sscweb.gsfc.nasa.gov/WS/sscr/2/observatories");
			JAXBContext jaxbContext = JAXBContext.newInstance(ObservatoryResponse.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			java.io.StringReader reader = new java.io.StringReader(xmlData);
			ObservatoryResponse response = (ObservatoryResponse) jaxbUnmarshaller.unmarshal(reader);
			if (response != null && response.getObservatory() != null) {
				return response.getObservatory();
			} else {
				return new ArrayList<>();
			}
		} catch (JAXBException e) {
			System.out.println("Encountered JAXBException: " + e);
			return new ArrayList<>();
		}
	}

	public static final SatelliteResponse getSatelliteResponse(String satelliteId) {
		String startTime = (new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")).format(START_DATE);
		String endTime = (new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")).format(END_DATE);

		try {
			String xmlData = HttpRequester.getUrlData("https://sscweb.gsfc.nasa.gov/WS/sscr/2/locations/" + satelliteId + "/" + startTime + "," + endTime + "/geo");
			JAXBContext jaxbContext = JAXBContext.newInstance(SatelliteResponse.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			java.io.StringReader reader = new java.io.StringReader(xmlData);
			SatelliteResponse result = (SatelliteResponse)jaxbUnmarshaller.unmarshal(reader);

			return result;
		} catch (JAXBException e) {
			System.out.println("Encountered JAXBException " + e);
			return new SatelliteResponse();
		}
	}

	/**
	 * Get a list of all satellite IDs from the NASA API.
	 * @return A list of satellite IDs
	 */
	public static final List<String> GetAllSatelliteIds() {
		ArrayList<String> ids = new ArrayList<>();

		for (Observatory observatory : GetAllObservatories()) {
			ids.add(observatory.getId());
		}
		
		return ids;
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

	public static final List<List<Double>> getSatelliteLatLonMag(String satelliteId) {
		SatelliteResponse response = NasaApiClient.getSatelliteResponse(satelliteId);
		SatelliteResult satelliteData = response.getSatelliteData();
		SatelliteData data = satelliteData.getData();
		SatelliteCoordinates coordinates = data.getCoordinates();

		List<List<Double>> result = new ArrayList<List<Double>>();
		result.add(coordinates.getLatitude());
		result.add(coordinates.getLongitude());
		result.add(data.getAltitude());

		return result;
	}
}