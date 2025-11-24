package satellaglobe;

import jakarta.xml.bind.annotation.*;

/**
 * POJO for satellite result wrapper from NASA SSCWeb API
 */
@XmlRootElement(name = "Result", namespace = "http://sscweb.gsfc.nasa.gov/schema")
public class SatelliteResult {
	private SatelliteData data;

	@XmlElement(name = "Data", namespace = "http://sscweb.gsfc.nasa.gov/schema")
	/**
	 * Set the data containing coordinates and radial lengths.
	 *
	 * @param data SatelliteData containing coordinates and altitude lists
	 */
	public void setData(SatelliteData data) {
		this.data = data;
	}
    
	/**
	 * Get the data containing coordinates and radial lengths.
	 *
	 * @return SatelliteData instance
	 */
	public SatelliteData getData() {
		return this.data;
	}
}
