package satellaglobe;

import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Sphere;
import javafx.geometry.*;
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

	private DoubleProperty listProportion;

	private SatelliteInfoVBox satelliteInfo;

	 /**
	 * Satellite Constructor 
	 * 
	 * @param name for name of satellite
	 * @param latitudes for latitudinal position of satellite
	 * @param longitudes for longitudinal position of satellite in GEO coordinate system
	 * @param magnitudes for (not simulated) distance from earth
	 */
	public Satellite(String name, List<Double> latitudes, List<Double> longitudes, List<Double> magnitudes, double listProportion) {
		super();

		// Popup window to display relevant satellite information on hover
		this.satelliteInfo = new SatelliteInfoVBox();
		this.listProportion = new SimpleDoubleProperty();

		this.setName(name);
		this.setLatitudes(latitudes);
		this.setLongitudes(longitudes);
		this.setMagnitudes(magnitudes);
		this.setListProportion(listProportion);

		PhongMaterial randomColor = new PhongMaterial();
		randomColor.setDiffuseColor(Color.hsb(Math.random() * 360, 0.5, 1.0));
		this.setMaterial(randomColor);
		
		this.translateXProperty().bind(Bindings.createDoubleBinding(() -> {
			return this.getRadius() * 8 * Math.cos(this.getLatitudeAtCurrentProportion() * RADIAN_CONVERSION)
					* Math.cos(this.getLongitudeAtCurrentProportion() * RADIAN_CONVERSION);
		}, this.radiusProperty(), this.listProportionProperty()));

		this.translateZProperty().bind(Bindings.createDoubleBinding(() -> {
			return this.getRadius() * 8 * Math.cos(this.getLatitudeAtCurrentProportion() * RADIAN_CONVERSION)
					* Math.sin(this.getLongitudeAtCurrentProportion() * RADIAN_CONVERSION);
		}, this.radiusProperty(), this.listProportionProperty()));

		this.translateYProperty().bind(Bindings.createDoubleBinding(() -> {
			return this.getRadius() * 8 * -Math.sin(this.getLatitudeAtCurrentProportion() * RADIAN_CONVERSION);
		}, this.radiusProperty(), this.listProportionProperty()));

		this.localToSceneTransformProperty().addListener((obs, oldVal, newVal) -> {
			updateSatelliteInfoPosition();
		});

		this.sceneProperty().addListener((obs, oldScene, newScene) -> {
			if (newScene != null) {
				// Satellite was added to a scene, so add the satelliteInfo too
				if (newScene.getRoot() instanceof Pane root) {
					if (!root.getChildren().contains(satelliteInfo)) {
						root.getChildren().add(satelliteInfo);
					}
					this.updateSatelliteInfoPosition();
				}
			} else {
				// Satellite was removed from the scene, so delete the satelliteInfo too
				if (oldScene != null && oldScene.getRoot() instanceof Pane oldRoot) {
					oldRoot.getChildren().remove(satelliteInfo);
				}
			}
		});

		this.parentProperty().addListener((obs, oldParent, newParent) -> {
			if (newParent == null) {
				if (oldParent instanceof Pane oldPane) {
					oldPane.getChildren().remove(satelliteInfo);
				} else {
					// Satellite was removed from the parent, so delete the satelliteInfo too
					if (this.getScene() != null && this.getScene().getRoot() instanceof Pane root) {
						root.getChildren().remove(satelliteInfo);
					}
				}
			}
		});
	}

	/**
	 * Set naem of satellite 
	 * @param name String name of satellite
	 */
	public void setName(String name) {
		this.name = name;
		this.satelliteInfo.setName(name);
	}

	/**
	 * Get name of satellite
	 * @return String name of satellite
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set list of latitudes 
	 * @param latitudes list of double latitudes
	 */
	public void setLatitudes(List<Double> latitudes) {
		this.latitudes = latitudes;
		this.satelliteInfo.setLatitude(this.getLatitudeAtCurrentProportion());
	}

	/**
	 * Get list of latitudes
	 * @return list of double latitudess
	 */
	public List<Double> getLatitudes() {
		return this.latitudes;
	}

	/**
	 * Get latitude at the current percentage of the listProportion property
	 * @return double latitude interpolated between two nearest latitudes in their list
	 */
	public double getLatitudeAtCurrentProportion() {
		return this.getListValueAtCurrentProportion(latitudes);
	}

	/**
	 * Set list of longitudes 
	 * @param longitudes list of double longitudes
	 */
	public void setLongitudes(List<Double> longitudes) {
		this.longitudes = longitudes;
		this.satelliteInfo.setLongitude(this.getLongitudeAtCurrentProportion());
	}

	/**
	 * Get list of longitudes
	 * @return list of double longitudes
	 */
	public List<Double> getLongitudes() {
		return this.longitudes;
	}

	/**
	 * Get longitude at the current percentage of the listProportion property
	 * @return double longitude interpolated between two nearest longitudes in their list
	 */
	public double getLongitudeAtCurrentProportion() {
		// Requires special handling because of wraparound
		if (longitudes == null || longitudes.isEmpty()) return 0.0;

		double proportion = this.getListProportion();
		double scaledIndex = proportion * (longitudes.size() - 1);

		int indexLow = (int) Math.floor(scaledIndex);
		int indexHigh = (int) Math.ceil(scaledIndex);

		if (indexLow < 0) indexLow = 0;
		if (indexHigh >= longitudes.size()) indexHigh = longitudes.size() - 1;

		double low = longitudes.get(indexLow);
		double high = longitudes.get(indexHigh);

		if (indexLow == indexHigh) {
			return (low % 360 + 360) % 360;
		} else {
			// Account for wraparound
			double alpha = scaledIndex - indexLow;
			double delta = ((high - low + 540.0) % 360.0) - 180.0;
			double interpolated = low + alpha * delta;

			return (interpolated % 360.0 + 360.0) % 360.0;
		}
	}

	/**
	 * Set list of magnitudes (distance from earth)
	 * @param magnitudes list of double magnitudes
	 */
	public void setMagnitudes(List<Double> magnitudes) {
		this.magnitudes = magnitudes;
		this.satelliteInfo.setMagnitude(this.getMagnitudeAtCurrentProportion());
	}

	/**
	 * Get list of magnitudes (distance from earth)
	 * @return list of double magnitudes
	 */
	public List<Double> getMagnitudes() {
		return this.magnitudes;
	}

	/**
	 * Get magnitude at the current percentage of the listProportion property
	 * @return double magnitude interpolated between two nearest magnitudes in their list
	 */
	public double getMagnitudeAtCurrentProportion() {
		return this.getListValueAtCurrentProportion(magnitudes);
	}

	/**
	 * Get the listProportion property
	 * @return DoubleProperty listProportion
	 */
	public DoubleProperty listProportionProperty() {
		return this.listProportion;
	}

	/**
	 * Set the value of the listProportion property
	 * @param listProportion double value of listProportion
	 */
	public void setListProportion(double listProportion) {
		this.listProportion.set(Math.clamp(listProportion, 0.0, 1.0));

		this.satelliteInfo.setLatitude(this.getLatitudeAtCurrentProportion());
		this.satelliteInfo.setLongitude(this.getLongitudeAtCurrentProportion());
		this.satelliteInfo.setMagnitude(this.getMagnitudeAtCurrentProportion());
	}

	/**
	 * Get current listProportion value
	 * @return listproportion double value
	 */
	public double getListProportion() {
		return this.listProportion.get();
	}

	/**
	 * Get value from given list at current listProportion with linear interpolation
	 * @param list list of double values to interpolate from
	 * @return interpolated double value from list at current listProportion
	 */
	private double getListValueAtCurrentProportion(List<Double> list) {
		double proportion = this.getListProportion();
		double scaledIndex = proportion * (list.size() - 1);
		
		int indexLow = (int) Math.floor(scaledIndex);
		int indexHigh = (int) Math.ceil(scaledIndex);

		if (indexLow < 0) indexLow = 0;
		if (indexHigh >= list.size()) indexHigh = list.size() - 1;

		if (indexLow == indexHigh) {
			return list.get(indexLow);
		} else {
			double alpha = scaledIndex - indexLow;
			return lerp(list.get(indexLow), list.get(indexHigh), alpha);
		}
	}

	/**
	 * Helper method for binding satelliteInfo position to Satellite's 2D space
	 */
	private void updateSatelliteInfoPosition() {
		if (this.getScene() == null || this.getScene().getRoot() == null) return;

		Point2D screenPos = this.localToScreen(0, 0);
		if (screenPos == null) return;

		Point2D panePosition = this.getScene().getRoot().screenToLocal(screenPos);
		if (panePosition == null) return;

		Bounds infoBounds = satelliteInfo.getLayoutBounds();
		double offsetX = -infoBounds.getWidth() / 2.0;
		double offsetY = -infoBounds.getHeight() - 8.0;

		satelliteInfo.setTranslateX(panePosition.getX() + offsetX);
		satelliteInfo.setTranslateY(panePosition.getY() + offsetY);
	}

	/**
	 * Linear interpolation helper method
	 * @param start double value at start
	 * @param goal double value at end
	 * @param alpha proportion between start and goal (0.0 - 1.0)
	 * @return interpolated double value
	 */
	private static double lerp(double start, double goal, double alpha) {
		alpha = Math.clamp(alpha, 0.0, 1.0);
		return start + alpha * (goal - start);
	}
	
	/**
	 * Private inner class for satellite information hover popup
	 */
	protected class SatelliteInfoVBox extends VBox {
		private Label name;
		private Label latitude;
		private Label longitude;
		private Label magnitude;

		/**
		 * SatelliteInfoVBox Constructor
		 */
		public SatelliteInfoVBox() {
			super(1);
			this.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
			this.setWidth(256);

			this.name = new Label();
			this.latitude = new Label();
			this.longitude = new Label();
			this.magnitude = new Label();

			this.name.setTextFill(Color.WHITE);
			this.latitude.setTextFill(Color.WHITE);
			this.longitude.setTextFill(Color.WHITE);
			this.magnitude.setTextFill(Color.WHITE);

			this.getChildren().addAll(this.name, this.latitude, this.longitude, this.magnitude);
		}

		/**
		 * Set the name of the satellite in the info box
		 * @param name
		 */
		public void setName(String name) {
			this.name.setText(name);
		}

		/**
		 * Set the latitude of the satellite in the info box
		 * @param latitude
		 */
		public void setLatitude(double latitude) {
			this.latitude.setText("Latitude:\t\t" + Math.round(latitude * 100.) / 100.0 + "°");
		}

		/**
		 * Set the longitude of the satellite in the info box
		 * @param longitude
		 */
		public void setLongitude(double longitude) {
			// Convert longitude to GIS standard for display
			longitude = ((longitude + 180.0) % 360.0 + 360.0) % 360.0 - 180.0;
			this.longitude.setText("Longitude:\t" +  Math.round(longitude * 100) / 100.0 + "°");
		}

		/**
		 * Set the magnitude (distance) of the satellite in the info box
		 * @param magnitude
		 */
		public void setMagnitude(double magnitude) {
			this.magnitude.setText("Distance:\t\t" +  Math.round(magnitude) + "km");
		}
	}
}