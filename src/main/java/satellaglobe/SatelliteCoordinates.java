package satellaglobe;

import java.util.List;

import jakarta.xml.bind.annotation.*;

/**
 * POJO for coordinates wrapper from NASA SSCWeb API
 */
@XmlRootElement(name = "Coordinates", namespace = "http://sscweb.gsfc.nasa.gov/schema")
public class SatelliteCoordinates {
	private List<Double> latitude;
	private List<Double> longitude;

	@XmlElement(name = "Latitude", namespace = "http://sscweb.gsfc.nasa.gov/schema")
	/**
	 * Set the list of latitudes for the satellite positions.
	 * decimal degrees for each recorded position.
	 *
	 * @param latitude list of latitude values in decimal degrees
	 */
	public void setLatitude(List<Double> latitude) {
		this.latitude = latitude;
	}

	/**
	 * Get the list of latitudes for the satellite positions.
	 *
	 * @return list of latitude values in decimal degrees
	 */
	public List<Double> getLatitude() {
		return this.latitude;
	}

	@XmlElement(name = "Longitude", namespace = "http://sscweb.gsfc.nasa.gov/schema")
	/**
	 * Set the list of longitudes for the satellite positions.
	 * decimal degrees for each recorded position.
	 *
	 * @param longitude list of longitude values in decimal degrees
	 */
	public void setLongitude(List<Double> longitude) {
		this.longitude = longitude;
	}
    
	/**
	 * Get the list of longitudes for the satellite positions.
	 *
	 * @return list of longitude values in decimal degrees
	 */
	public List<Double> getLongitude() {
		return this.longitude;
	}
}
