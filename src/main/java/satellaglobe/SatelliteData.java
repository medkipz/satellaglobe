package satellaglobe;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "Data", namespace = "http://sscweb.gsfc.nasa.gov/schema")
public class SatelliteData {
	// Lol why do they store coordinates and ""RadialLength"" separately
	// ObservatoryResponse -> Observatory ->
	// SatelliteResponse -> SatelliteResult -> SatelliteData -> SatelliteCoordinates
	// ?????? ok
	private SatelliteCoordinates coordinates;
	private List<Double> altitude;

	@XmlElement(name = "Coordinates", namespace = "http://sscweb.gsfc.nasa.gov/schema")
	/**
	 * Set the coordinates object which contains latitude and longitude lists
	 *
	 * @param coordinates SatelliteCoordinates instance representing positions
	 */
	public void setCoordinates(SatelliteCoordinates coordinates) {
		this.coordinates = coordinates;
	}

	/**
	 * Get the coordinates object which contains latitude and longitude lists.
	 *
	 * @return SatelliteCoordinates instance
	 */
	public SatelliteCoordinates getCoordinates() {
		return this.coordinates;
	}

	@XmlElement(name = "RadialLength", namespace = "http://sscweb.gsfc.nasa.gov/schema")
	/**
	 * Set the radial length values (altitude) for each satellite position.
	 *
	 * @param altitude list of altitude values (same index order as coordinates)
	 */
	public void setAltitude(List<Double> altitude) {
		this.altitude = altitude;
	}

	/**
	 * Get the radial length values (altitude) for each satellite position.
	 *
	 * @return list of altitude values
	 */
	public List<Double> getAltitude() {
		return this.altitude;
	}
}
