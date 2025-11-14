package satellaglobe;

import java.util.List;

import jakarta.xml.bind.annotation.*;

/**
 * POJO for observatory response from NASA SSCWeb API
 */
@XmlRootElement(name = "ObservatoryResponse", namespace = "http://sscweb.gsfc.nasa.gov/schema")
@XmlAccessorType(XmlAccessType.FIELD)
public class ObservatoryResponse {
    @XmlElement(name = "Observatory", namespace = "http://sscweb.gsfc.nasa.gov/schema")
    private List<Observatory> observatory;

	/**
	 * @param observatory list of observatories to set
	 */
    public void setObservatory(List<Observatory> observatory) {
        this.observatory = observatory;
    }

	/**
	 * @return list of observatories
	 */
    public List<Observatory> getObservatory() {
        return this.observatory;
    }
}
