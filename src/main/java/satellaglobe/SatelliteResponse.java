package satellaglobe;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "Response", namespace = "http://sscweb.gsfc.nasa.gov/schema")
@XmlAccessorType(XmlAccessType.FIELD)
public class SatelliteResponse {
	private SatelliteResult result;

	@XmlElement(name = "Result", namespace = "http://sscweb.gsfc.nasa.gov/schema")
	/**
	 * Set the satellite result wrapper parsed from the XML response
	 *
	 * @param result SatelliteResult containing the data
	 */
	public void setSatelliteData(SatelliteResult result) {
		this.result = result;
	}

	/**
	 * Get the satellite result wrapper parsed from the XML response
	 *
	 * @return SatelliteResult instance
	 */
	public SatelliteResult getSatelliteData() {
		return this.result;
	}
}
