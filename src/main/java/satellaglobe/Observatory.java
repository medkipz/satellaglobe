package satellaglobe;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "Observatory", namespace = "http://sscweb.gsfc.nasa.gov/schema")
public class Observatory {
	private String id;
	private String name;
	private int resolution;
	private String startTime;
	private String endTime;

	@XmlElement(name = "Id", namespace = "http://sscweb.gsfc.nasa.gov/schema")
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}

	@XmlElement(name = 	"Name", namespace = "http://sscweb.gsfc.nasa.gov/schema")
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	@XmlElement(name = "Resolution", namespace = "http://sscweb.gsfc.nasa.gov/schema")
	public void setResolution(int resolution) {
		this.resolution = resolution;
	}

	public int getResolution() {
		return this.resolution;
	}

	@XmlElement(name = "StartTime", namespace = "http://sscweb.gsfc.nasa.gov/schema")
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStartTime() {
		return this.startTime;
	}

	@XmlElement(name = "EndTime", namespace = "http://sscweb.gsfc.nasa.gov/schema")
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getEndTime() {
		return this.endTime;
	}
	
	@Override
	public String toString() {
		return "Observatory [id=" + id + ", name=" + name + ", resolution=" + resolution + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}
	
}
