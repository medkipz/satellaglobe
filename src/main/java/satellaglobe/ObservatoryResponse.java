package satellaglobe;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ObservatoryResponse", namespace = "http://sscweb.gsfc.nasa.gov/schema")
@XmlType(name = "ObservatoryResponse")
public class ObservatoryResponse {
    @XmlElement(name = "Observatory", namespace = "http://sscweb.gsfc.nasa.gov/schema")
    private List<Observatory> Observatory;

    public ObservatoryResponse() {}

    public List<Observatory> getObservatory() {
        return Observatory;
    }

    public void setObservatory(List<Observatory> observatory) {
        this.Observatory = observatory;
    }
}
