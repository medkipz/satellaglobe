package satellaglobe;

public class SatelliteResponseTester {
	public static void main(String[] args) throws Exception {
		SatelliteResponse response = NasaApiClient.getSatelliteResponse("ace");
		SatelliteResult result = response.getSatelliteData();
		SatelliteData data = result.getData();
		SatelliteCoordinates coords = data.getCoordinates();
		System.out.println("Satellite Coordinates:");
		System.out.println("Latitudes: " + coords.getLatitude());
		System.out.println("Longitudes: " + coords.getLongitude());
	}
}
