package satellaglobe;

import java.util.*;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import org.controlsfx.control.CheckComboBox;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.*;
import javafx.collections.*;
import javafx.geometry.Orientation;

/**
 * SpaceApp Application creates 3D model for satellite representation 
 * 
 * @author Yingling, Rippey
 * 	
 */
public class SpaceApp extends Application {

    //Scene size variables
    private static final int HEIGHT = 900;
    private static final int WIDTH = 1600;	

	private double mouseStartX;
	private double modelStartX;

    //Main method override for java applications
    @Override
    public void start(Stage stage) {
		final List<String> satelliteNames = FXCollections.observableList(NasaApiClient.GetAllActiveSatelliteNames());
		final Map<String, String> satelliteIdHashMap = NasaApiClient.GetSatelliteNameIdMap();

		final Image backgroundStars = new Image(getClass().getResource("/satellaglobe/backgroundStars.png").toExternalForm());
		final Image globeTexture = new Image(getClass().getResource("/satellaglobe/globeTexture.jpg").toExternalForm());

		// Track active satellites by name for fast lookup and safe removal
		final Map<String, Satellite> activeSatellites = new HashMap<>();
		// Mutable snapshot of the checked items; keep the reference final so lambda can mutate contents
		final List<String> previousChecked = new ArrayList<>(/*initial*/ Collections.emptyList());

        // 3D model to represent Earth and center it in the scene
        Sphere globe = new Sphere();
        PhongMaterial globeMaterial = new PhongMaterial();
		Camera camera = new ParallelCamera();
		Group satellites = new Group();
        Group model = new Group(globe, satellites);
		Group pivot = new Group(model);
		SubScene view3d = new SubScene(pivot, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
		BorderPane ui = new BorderPane();
		CheckComboBox<String> satellitePicker = new CheckComboBox<>();
		Scene scene = new Scene(ui, WIDTH, HEIGHT);
		Slider coordinatesSlider = new Slider();
		Label timeLabel = new Label();
		Label distanceDisclaimerLabel = new Label("Note: distance is not simulated.");
		CheckBox realtimeCheckBox = new CheckBox("Realtime");
		ToolBar toolBar = new ToolBar();
		Rotate rotate = new Rotate(0, Rotate.Y_AXIS);

		final SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		satellitePicker.getItems().setAll(satelliteNames);
		
        globeMaterial.setDiffuseMap(globeTexture);
		globe.radiusProperty().bind(view3d.heightProperty().divide(5));
        globe.setMaterial(globeMaterial);
		globe.setRotationAxis(Rotate.Y_AXIS);
		globe.setRotate(-90.0);

		rotate.setPivotX(0);
		rotate.setPivotY(0);
		rotate.setPivotZ(0);

		model.getTransforms().add(rotate);
		model.setRotationAxis(Rotate.Y_AXIS);
		model.setTranslateZ(-1);

		pivot.translateXProperty().bind(view3d.widthProperty().divide(2));
		pivot.translateYProperty().bind(view3d.heightProperty().divide(2));
       	
		view3d.setFill(Color.BLACK);
		view3d.setFill(new javafx.scene.paint.ImagePattern(backgroundStars));
		view3d.setCamera(camera);

		satellitePicker.setPrefWidth(256);
		satellitePicker.setMaxWidth(256);
		satellitePicker.setMinWidth(256);

		coordinatesSlider.setMin(0);
		coordinatesSlider.setMax(1);
		coordinatesSlider.setValue(0.5);

		timeLabel.setText("Time: " + utcFormat.format(NasaApiClient.CURRENT_DATE));

		ui.setCenter(view3d);

		toolBar.getItems().addAll(satellitePicker, coordinatesSlider, realtimeCheckBox, timeLabel, distanceDisclaimerLabel);

		previousChecked.clear();
		previousChecked.addAll(satellitePicker.getCheckModel().getCheckedItems());

		satellitePicker.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) change -> {
			List<String> currentChecked = new ArrayList<>(satellitePicker.getCheckModel().getCheckedItems());

			Set<String> currentSet = new HashSet<>(currentChecked);
			Set<String> previousSet = new HashSet<>(previousChecked);

			for (String removed : previousSet) {
				if (!currentSet.contains(removed)) {
					Satellite toRemove = activeSatellites.remove(removed);
					if (toRemove != null) {
						satellites.getChildren().remove(toRemove);
					}
				}
			}

			for (String added : currentSet) {
				if (!previousSet.contains(added)) {
					if (activeSatellites.containsKey(added)) continue;

					String id = satelliteIdHashMap.get(added);
					if (id == null) continue;

					List<List<Double>> coordinates = NasaApiClient.getSatelliteLatLonMag(id);

					if (coordinates == null || coordinates.size() < 3 || coordinates.get(0).isEmpty()) {
						return;
					}

					Satellite satellite = new Satellite(
							added,
							coordinates.get(0),
							coordinates.get(1),
							coordinates.get(2),
							coordinatesSlider.getValue()
					);

					satellite.radiusProperty().bind(view3d.heightProperty().divide(25));
					activeSatellites.put(added, satellite);
					satellites.getChildren().add(satellite);
				}
			}

			previousChecked.clear();
			previousChecked.addAll(currentChecked);
		});

		coordinatesSlider.valueProperty().addListener((objects, oldValue, newValue) -> {
			double listProportion = newValue.doubleValue();

			for (Node node : satellites.getChildren()) {
				if (node instanceof Satellite satellite) {
					satellite.setListProportion(listProportion);
				}
			}

			long millis = NasaApiClient.START_DATE.getTime() + (long) Math.round(listProportion * (NasaApiClient.END_DATE.getTime() - NasaApiClient.START_DATE.getTime()));
			timeLabel.setText("Time: " + utcFormat.format(new Date(millis)));
		});

		realtimeCheckBox.selectedProperty().addListener((obs, wasChecked, isChecked) -> {
			if (isChecked) {
				coordinatesSlider.setDisable(true);
				final javafx.animation.Timeline[] realtimeTimelineHolder = new javafx.animation.Timeline[1];
				realtimeTimelineHolder[0] = new javafx.animation.Timeline(
					new javafx.animation.KeyFrame(javafx.util.Duration.millis(100), evt -> {
						if (!realtimeCheckBox.isSelected()) {
							realtimeTimelineHolder[0].stop();
							return;
						}

						long now = System.currentTimeMillis();
						long start = NasaApiClient.START_DATE.getTime();
						long end = NasaApiClient.END_DATE.getTime();

						double proportion;
						if (end > start) {
							proportion = Math.clamp((now - start) / (double) (end - start), 0.0, 1.0);
						} else {
							proportion = 0.0;
						}

						coordinatesSlider.setValue(proportion);
					})
				);

				realtimeTimelineHolder[0].setCycleCount(javafx.animation.Animation.INDEFINITE);
				realtimeTimelineHolder[0].play();
			} else {
				coordinatesSlider.setDisable(false);
			}
		});

		toolBar.setOrientation(Orientation.VERTICAL);
		ui.setLeft(toolBar);
		ui.setPrefSize(HEIGHT, 128);

		view3d.widthProperty().bind(ui.widthProperty());
		view3d.heightProperty().bind(ui.heightProperty());
		
		scene.setOnMousePressed(event -> {
			mouseStartX = event.getSceneX();
			modelStartX = rotate.getAngle();
		});

		scene.setOnMouseDragged(event -> {
			rotate.setAngle(modelStartX - (event.getSceneX() - mouseStartX) * 0.4);

			double bgOffsetX = -(rotate.getAngle() % 360) / 360.0;
			view3d.setFill(new javafx.scene.paint.ImagePattern(backgroundStars, bgOffsetX, 0, 1, 1, true));
		});
		
        //Instantiates the scene
        stage.setTitle("SatellaGlobe");
        stage.setScene(scene);
        stage.show();
    }
    //Runs the application
    public static void main(String[] args) {
        launch();
    }
}
