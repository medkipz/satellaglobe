package satellaglobe;

import jakarta.xml.bind.annotation.*;

/**
 * POJO for observatory from NASA SSCWeb API
 */
@XmlRootElement(name = "Observatory", namespace = "http://sscweb.gsfc.nasa.gov/schema")
public class Observatory {
	private String id;
	private String name;
	private int resolution;
	private String startTime;
	private String endTime;

	/**
	 * @param id to set the observatory id to
	 */
	@XmlElement(name = "Id", namespace = "http://sscweb.gsfc.nasa.gov/schema")
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @return id of the observatory
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @param name to set the observatory name to
	 */
	@XmlElement(name = 	"Name", namespace = "http://sscweb.gsfc.nasa.gov/schema")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return name of the observatory
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param resolution to set the observatory resolution to
	 */
	@XmlElement(name = "Resolution", namespace = "http://sscweb.gsfc.nasa.gov/schema")
	public void setResolution(int resolution) {
		this.resolution = resolution;
	}

	/**
	 * @return the length in seconds between each update of the observatory data
	 */
	public int getResolution() {
		return this.resolution;
	}

	/**
	 * @param startTime to set the observatory start time to
	 */
	@XmlElement(name = "StartTime", namespace = "http://sscweb.gsfc.nasa.gov/schema")
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the time, in ISO 8601 format, which the observatory data starts from
	 */
	public String getStartTime() {
		return this.startTime;
	}

	/**
	 * Sets the observatory end time
	 * @param endTime
	 */
	@XmlElement(name = "EndTime", namespace = "http://sscweb.gsfc.nasa.gov/schema")
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * Gets the observatory end time
	 * @return 
	 */
	public String getEndTime() {
		return this.endTime;
	}
	
	/**
	 * Returns string representation of the observatory
	 * @return string
	 */
	@Override
	public String toString() {
		return "Observatory [id=" + id + ", name=" + name + ", resolution=" + resolution + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}
	
}
