package satellaglobe;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventType;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Bounds;
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

	private SatelliteInfoVBox satelliteInfo;

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
		this.satelliteInfo = new SatelliteInfoVBox();
		this.listIndex = new SimpleIntegerProperty();

		this.setName(name);
		this.setLatitudes(latitudes);
		this.setLongitudes(longitudes);
		this.setMagnitudes(magnitudes);
		this.setListIndex(listIndex);

        PhongMaterial randomColor = new PhongMaterial();
        randomColor.setDiffuseColor(Color.hsb(Math.random() * 360, 0.5, 1.0));
		this.setMaterial(randomColor);
		
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

		this.satelliteInfo.setLatitude(this.getLatitudes().get(this.getListIndex()));
		this.satelliteInfo.setLongitude(this.getLongitudes().get(this.getListIndex()));
		this.satelliteInfo.setMagnitude(this.getMagnitudes().get(this.getListIndex()));
	}

	public int getListIndex() {
		return this.listIndex.get();
	}
	
	/**
	 * Private inner class for satellite information hover popup
	 */
	protected class SatelliteInfoVBox extends VBox {
		private Label name;
		private Label latitude;
		private Label longitude;
		private Label magnitude;


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