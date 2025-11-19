package satellaglobe;

import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Popup;

/**
 * Satellite Class to handle 3D representations of live satellites around earth
 */

public class Satellite extends Sphere {
    private String name;

	private static final double RADIAN_CONVERSION = Math.PI / 180.0;

	private double latitude;
	private double longitude;
	private double magnitude;

	private SatelliteInfoPopup satelliteInfo;

	 /**
     * Satellite Constructor 
     * 
     * @param name for name of satellite
     * @param latitude for latitudinal position of satellite
     * @param longitude for longitudinal position of satellite
     * @param magnitude for (not simulated) distance from earth
	 */
	public Satellite(String name, double latitude, double longitude, double magnitude) {
		// Instantiates each satellite with a size of 20
		super(20);

		// Popup window to display relevant satellite information on hover
		this.satelliteInfo = new SatelliteInfoPopup();

		//
		this.setName(name);
		this.setLatitude(latitude);
		this.setLongitude(longitude);
		this.setMagnitude(magnitude);

		// Color value to apply to satellites on hover
        PhongMaterial hoverColor = new PhongMaterial();
        hoverColor.setDiffuseColor(Color.ORANGE);

		 //Changes satellite color to orange and displays the popup when hovered over
        this.addEventHandler(MouseEvent.MOUSE_ENTERED, unused -> {
            this.setMaterial(hoverColor);
            Point2D screenPos = this.localToScreen(0, 0);
            this.satelliteInfo.show(this, screenPos.getX() + 10, screenPos.getY() + 10);
        });

		//Removes coloring and popup once mouse is moved away from the satellite
        this.addEventHandler(MouseEvent.MOUSE_EXITED, unused -> {
            this.setMaterial(null);
            this.satelliteInfo.hide();
        });
	}

	public void setName(String name) {
		this.name = name;
		this.satelliteInfo.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
		this.satelliteInfo.setLatitude(latitude);
		this.updateCartesianCoordinates();
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
		this.satelliteInfo.setLongitude(longitude);
		this.updateCartesianCoordinates();
	}

	public double getLongitude() {
		return longitude;
	}

	public void setMagnitude(double magnitude) {
		this.magnitude = magnitude;
		this.satelliteInfo.setMagnitude(magnitude);
		this.updateCartesianCoordinates();
	}

	public double getMagnitude() {
		return magnitude;
	}

	/**
	 * Private method for updating the position of satellite when certain setters are called.
	 * 
	 * Technically not cartesian coordinates because Descartes was STUPID and thought that the Z axis was Yaw
	 */
	private void updateCartesianCoordinates() {
		this.setTranslateX(
			150 * Math.cos(this.latitude * RADIAN_CONVERSION) * Math.cos(this.longitude * RADIAN_CONVERSION)
		);
		this.setTranslateZ(
			150 * Math.cos(this.latitude * RADIAN_CONVERSION) * Math.sin(this.longitude * RADIAN_CONVERSION)
		);
		// This has to be multiplied by negative one  or else the 
		this.setTranslateY(
			-150 * Math.sin(this.latitude * RADIAN_CONVERSION)
		);
	}
	
	/**
	 * Private inner class for satellite information hover popup
	 */
	protected class SatelliteInfoPopup extends Popup {
		private Label name;
		private Label latitude;
		private Label longitude;
		private Label magnitude;

		public SatelliteInfoPopup() {
			super();

			this.name = new Label();
			this.latitude = new Label();
			this.longitude = new Label();
			this.magnitude = new Label();

			this.name.setTextFill(Color.WHITE);
			this.latitude.setTextFill(Color.WHITE);
			this.longitude.setTextFill(Color.WHITE);
			this.magnitude.setTextFill(Color.WHITE);

			VBox popupContent = new VBox(1);
			popupContent.getChildren().addAll(this.name, this.latitude, this.longitude, this.magnitude);

			this.getContent().add(popupContent);
		}

		public void setName(String name) {
			this.name.setText(name);
		}

		public void setLatitude(double latitude) {
			this.latitude.setText("Latitude:\t" + latitude);
		}

		public void setLongitude(double longitude) {
			this.longitude.setText("Longitude:\t" + longitude);
		}

		public void setMagnitude(double magnitude) {
			this.magnitude.setText("Magnitude:\t" + magnitude);
		}
	}
}