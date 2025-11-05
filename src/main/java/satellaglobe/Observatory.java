package satellaglobe;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Observatory", namespace = "http://sscweb.gsfc.nasa.gov/schema")
public class Observatory {
	private String id;
	private String name;
	private int resolution;
	private String startTime;
	private String endTime;

	@XmlElement
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public void setResolution(int resolution) {
		this.resolution = resolution;
	}

	public int getResolution() {
		return resolution;
	}

	@XmlElement
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStartTime() {
		return startTime;
	}

	@XmlElement
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getEndTime() {
		return endTime;
	}
}
