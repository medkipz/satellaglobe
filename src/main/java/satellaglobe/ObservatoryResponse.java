package satellaglobe;

import java.util.List;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "ObservatoryResponse", namespace = "http://sscweb.gsfc.nasa.gov/schema")
@XmlAccessorType(XmlAccessType.FIELD)
public class ObservatoryResponse {
    @XmlElement(name = "Observatory", namespace = "http://sscweb.gsfc.nasa.gov/schema")
    private List<Observatory> observatory;

    public void setObservatory(List<Observatory> observatory) {
        this.observatory = observatory;
    }

    public List<Observatory> getObservatory() {
        return this.observatory;
    }
}
