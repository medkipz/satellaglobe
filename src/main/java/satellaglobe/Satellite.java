package satellaglobe;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Popup;
import java.util.*;

/**
 * Satellite Class to handle 3D representations of live satellites around earth
 */

public class Satellite extends Sphere {
    private String name;

	private static final double RADIAN_CONVERSION = Math.PI / 180.0;

	private List<Double> latitudes;
	private List<Double> longitudes;
	private List<Double> magnitudes;

	private IntegerProperty listIndex;

	private SatelliteInfoPopup satelliteInfo;

	 /**
     * Satellite Constructor 
     * 
     * @param name for name of satellite
     * @param latitudes for latitudinal position of satellite
     * @param longitudes for longitudinal position of satellite
     * @param magnitudes for (not simulated) distance from earth
	 */
	public Satellite(String name, List<Double> latitudes, List<Double> longitudes, List<Double> magnitudes, int listIndex) {
		super();

		// Popup window to display relevant satellite information on hover
		this.satelliteInfo = new SatelliteInfoPopup();
		this.listIndex = new SimpleIntegerProperty();

		this.setName(name);
		this.setLatitudes(latitudes);
		this.setLongitudes(longitudes);
		this.setMagnitudes(magnitudes);
		this.setListIndex(listIndex);

		// Color value to apply to satellites on hover
        PhongMaterial hoverColor = new PhongMaterial();
        hoverColor.setDiffuseColor(Color.ORANGE);

		 //Changes satellite color to orange and displays the popup when hovered over
        this.addEventHandler(MouseEvent.MOUSE_ENTERED, unused -> {
            this.setMaterial(hoverColor);
            Point2D screenPos = this.localToScreen(0, 0);
            this.satelliteInfo.show(this, screenPos.getX() + 20, screenPos.getY() + 20);
        });

		//Removes coloring and popup once mouse is moved away from the satellite
        this.addEventHandler(MouseEvent.MOUSE_EXITED, unused -> {
            this.setMaterial(null);
            this.satelliteInfo.hide();
        });

		this.translateXProperty().bind(Bindings.createDoubleBinding(() -> {
			return this.getRadius() * 8 * Math.cos(this.getLatitudes().get(this.getListIndex()) * RADIAN_CONVERSION)
					* Math.cos(this.getLongitudes().get(this.getListIndex()) * RADIAN_CONVERSION);
		}, this.radiusProperty(), this.listIndexProperty()));

		this.translateZProperty().bind(Bindings.createDoubleBinding(() -> {
			return this.getRadius() * 8 * Math.cos(this.getLatitudes().get(this.getListIndex()) * RADIAN_CONVERSION)
					* Math.sin(this.getLongitudes().get(this.getListIndex()) * RADIAN_CONVERSION);
		}, this.radiusProperty(), this.listIndexProperty()));

		this.translateYProperty().bind(Bindings.createDoubleBinding(() -> {
			return this.getRadius() * 8 * -Math.sin(this.getLatitudes().get(this.getListIndex()) * RADIAN_CONVERSION);
		}, this.radiusProperty(), this.listIndexProperty()));
	}

	public void setName(String name) {
		this.name = name;
		this.satelliteInfo.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setLatitudes(List<Double> latitudes) {
		this.latitudes = latitudes;
		this.satelliteInfo.setLatitude(this.getLatitudes().get(this.getListIndex()));
	}

	public List<Double> getLatitudes() {
		return this.latitudes;
	}

	public void setLongitudes(List<Double> longitudes) {
		this.longitudes = longitudes;
		this.satelliteInfo.setLongitude(this.getLongitudes().get(this.getListIndex()));
	}

	public List<Double> getLongitudes() {
		return this.longitudes;
	}

	public void setMagnitudes(List<Double> magnitudes) {
		this.magnitudes = magnitudes;
		this.satelliteInfo.setMagnitude(this.getMagnitudes().get(this.getListIndex()));
	}

	public List<Double> getMagnitudes() {
		return this.magnitudes;
	}

	public IntegerProperty listIndexProperty() {
		return this.listIndex;
	}

	public void setListIndex(int listIndex) {
		this.listIndex.set(listIndex);
	}
	public int getListIndex() {
		return this.listIndex.get();
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
			this.latitude.setText("Latitude:\t\t" + Math.round(latitude * 100.) / 100.0 + "°");
		}

		public void setLongitude(double longitude) {
			this.longitude.setText("Longitude:\t" +  Math.round(longitude * 100) / 100.0 + "°");
		}

		public void setMagnitude(double magnitude) {
			this.magnitude.setText("Distance:\t\t" +  Math.round(magnitude) + "km (not simulated)");
		}
	}
}