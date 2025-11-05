package satellaglobe;

import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Popup;

/**
 * Satellite Class to handle 3D representations of live satellites around earth
 */

public class Satellite extends Sphere {
    private String name;

	private double latitude;
	private double longitude;
	private double magnitude;

	private SatelliteInfoPopup satelliteInfo;

    /*
     * Satellite Constructor 
     * 
     * @param name for name of satellite
     * @param xTranslate for javafx x-translation
     * @param yTranslate for javafx y-translation
     * @param zTranslate for javafx z-translation
    public Satellite(String name, double xTranslate, double yTranslate, double zTranslate, double asjfkla) {
        
        //Instantiates each satellite with a size of 20
        super(20);
        
        this.name = name;

        this.xTranslate = xTranslate;
        this.yTranslate = yTranslate;
        this.zTranslate = zTranslate;

        super.translateXProperty().set(xTranslate);
        super.translateYProperty().set(yTranslate);
        super.translateZProperty().set(zTranslate);

        //Popup window to display relevant satellite information
        Popup satInfo = new Popup();
        Label satName = new Label("Name: " + this.name);
        Label satLocation = new Label("\nX-Location: " + xTranslate + "\n" +  "Y-Location: " + yTranslate + "\n" + "Z-Location: " + zTranslate);
        satInfo.getContent().addAll(satName, satLocation);
		satName.setTextFill(Color.WHITE);
		satLocation.setTextFill(Color.WHITE);

        //Color value to apply to satellites
        PhongMaterial hoverColor = new PhongMaterial();
        hoverColor.setDiffuseColor(Color.ORANGE);

        //Changes satellite color to orange and displays the popup when hovered over
        this.addEventHandler(MouseEvent.MOUSE_ENTERED, unused -> {
            this.setMaterial(hoverColor);
            Point2D screenPos = this.localToScreen(0, 0);
            satInfo.show(this, screenPos.getX() + 10, screenPos.getY() + 10);
        });

        //Removes coloring and popup once mouse is moved away from the satellite
        this.addEventHandler(MouseEvent.MOUSE_EXITED, unused -> {
            this.setMaterial(null);
            satInfo.hide();
        });
    }
	*/

	public Satellite(String name, double latitude, double longitude, double magnitude) {
		// Instantiates each satellite with a size of 20
		super(20);

		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.magnitude = magnitude;

		// Popup window to display relevant satellite information on hover
		this.satelliteInfo = new SatelliteInfoPopup();

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
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
		this.satelliteInfo.setLongitude(longitude);
	}

	public double getLongitude() {
		return longitude;
	}

	public void setMagnitude(double magnitude) {
		this.magnitude = magnitude;
		this.satelliteInfo.setMagnitude(magnitude);
	}

	public double getMagnitude() {
		return magnitude;
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

			this.getContent().addAll(this.name, this.latitude, this.longitude, this.magnitude);
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