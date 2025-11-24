package satellaglobe;

import java.util.*;
import java.text.*;
import org.controlsfx.control.*;
import javafx.animation.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.transform.*;
import javafx.stage.*;
import javafx.collections.*;
import javafx.geometry.*;

/**
 * SpaceApp Application creates 3D model for satellite representation 
 * 
 * @author Yingling, Rippey
 * 	
 */
public class SpaceApp extends Application {

	//Scene size variables
	private static final int HEIGHT = (int) Screen.getPrimary().getVisualBounds().getHeight() - 32;
	private static final int WIDTH = (int) Screen.getPrimary().getVisualBounds().getWidth() - 32;	

	private double mouseStartX;
	private double modelStartX;

	// Instance variables
	private ObservableList<String> satelliteNames;
	private Map<String, String> satelliteIdHashMap;
	private Image backgroundStars;
	private Image globeTexture;
	private Map<String, Satellite> activeSatellites;
	private List<String> previousCheckedSatellites;
	private SimpleDateFormat utcFormat;

	private Sphere globe;
	private PhongMaterial globeMaterial;
	private Camera camera;
	private Group satellites;
	private Group model;
	private Group pivot;
	private SubScene view3d;

	private BorderPane ui;
	private CheckComboBox<String> satellitePicker;
	private Scene scene;
	private Slider coordinatesSlider;
	private Label timeLabel;
	private Label distanceDisclaimerLabel;
	private CheckBox realtimeCheckBox;
	private ToolBar toolBar;
	private Rotate rotate;

	/**
	 * Main method override for java applications
	 */
	@Override
	public void start(Stage stage) {
		initializeData();

		initialize3d();
		initializeUi();

		prepareMouseEventListeners();
		prepareSatellitePickerEventListeners();
		prepareSliderEventListeners();
		prepareRealtimeCheckboxEventListeners();

		//Instantiates the scene
		stage.setTitle("SatellaGlobe");
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Helper method for initializing variable data
	 */
	private void initializeData() {
		this.satelliteNames = FXCollections.observableList(NasaApiClient.GetAllActiveSatelliteNames());
		this.satelliteIdHashMap = NasaApiClient.GetSatelliteNameIdMap();
		this.backgroundStars = new Image(getClass().getResource("/satellaglobe/backgroundStars.png").toExternalForm());
		this.globeTexture = new Image(getClass().getResource("/satellaglobe/globeTexture.jpg").toExternalForm());
		this.activeSatellites = new HashMap<>();
		this.previousCheckedSatellites = new ArrayList<>(Collections.emptyList());
		this.utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		this.utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	/**
	 * Helper method for initializing 3d components
	 */
	private void initialize3d() {
		this.globe = new Sphere();
		this.globeMaterial = new PhongMaterial();
		this.camera = new ParallelCamera();
		this.satellites = new Group();
		this.model = new Group(globe, satellites);
		this.pivot = new Group(model);
		this.view3d = new SubScene(pivot, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
		this.rotate = new Rotate(0, Rotate.Y_AXIS);

		this.globeMaterial.setDiffuseMap(globeTexture);

		this.globe.radiusProperty().bind(view3d.heightProperty().divide(5));
		this.globe.setMaterial(globeMaterial);
		this.globe.setRotationAxis(Rotate.Y_AXIS);
		this.globe.setRotate(-90.0);

		this.rotate.setPivotX(0);
		this.rotate.setPivotY(0);
		this.rotate.setPivotZ(0);

		this.model.getTransforms().add(rotate);
		this.model.setRotationAxis(Rotate.Y_AXIS);
		this.model.setTranslateZ(-1);

		this.pivot.translateXProperty().bind(view3d.widthProperty().divide(2));
		this.pivot.translateYProperty().bind(view3d.heightProperty().divide(2));
	   	
		this.view3d.setFill(Color.BLACK);
		this.view3d.setFill(new javafx.scene.paint.ImagePattern(backgroundStars));
		this.view3d.setCamera(camera);
	}

	/**
	 * Helper method for initializing UI components
	 */
	private void initializeUi() {
		this.ui = new BorderPane();
		this.satellitePicker = new CheckComboBox<>(satelliteNames);
		this.scene = new Scene(ui, WIDTH, HEIGHT);
		this.coordinatesSlider = new Slider();
		this.timeLabel = new Label("Time: " + utcFormat.format(new Date()));
		this.distanceDisclaimerLabel = new Label("\nNote: distance is not simulated.");
		this.realtimeCheckBox = new CheckBox("Realtime");
		this.toolBar = new ToolBar(this.satellitePicker, this.coordinatesSlider, this.timeLabel, this.realtimeCheckBox, this.distanceDisclaimerLabel);

		this.satellitePicker.setPrefWidth(256);
		this.satellitePicker.setMaxWidth(256);
		this.satellitePicker.setMinWidth(256);

		this.coordinatesSlider.setMin(0);
		this.coordinatesSlider.setMax(1);
		this.coordinatesSlider.setValue(0.5);
		
		this.previousCheckedSatellites.clear();
		this.previousCheckedSatellites.addAll(satellitePicker.getCheckModel().getCheckedItems());

		this.toolBar.setOrientation(Orientation.VERTICAL);

		this.ui.setCenter(view3d);
		this.ui.setLeft(toolBar);
		this.ui.setPrefSize(HEIGHT, 128);

		this.view3d.widthProperty().bind(ui.widthProperty());
		this.view3d.heightProperty().bind(ui.heightProperty());
	}

	/**
	 * Helper method for preparing mouse event listeners
	 */
	private void prepareMouseEventListeners() {
		scene.setOnMousePressed(event -> {
			this.mouseStartX = event.getSceneX();
			this.modelStartX = this.rotate.getAngle();
		});

		scene.setOnMouseDragged(event -> {
			double bgOffsetX = (rotate.getAngle() % 360) / 360.0;

			this.rotate.setAngle(this.modelStartX - (event.getSceneX() - this.mouseStartX) * 0.4);
			this.view3d.setFill(new ImagePattern(this.backgroundStars, bgOffsetX, 0, 1, 1, true));
		});
	}

	/**
	 * Helper method for preparing satellitePicker event listeners
	 */
	private void prepareSatellitePickerEventListeners() {
		satellitePicker.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) change -> {
			List<String> currentCheckedList = new ArrayList<>(satellitePicker.getCheckModel().getCheckedItems());
			Set<String> currentCheckedSet = new HashSet<>(currentCheckedList);
			Set<String> previousCheckedSet = new HashSet<>(previousCheckedSatellites);

			for (String previouslyCheckedSatellite : previousCheckedSet) {
				if (!currentCheckedSet.contains(previouslyCheckedSatellite)) {
					activeSatellites.remove(previouslyCheckedSatellite);
				}
			}

			for (String currentlyCheckedSatellite : currentCheckedSet) {
				if (!previousCheckedSet.contains(currentlyCheckedSatellite)) {
					if (activeSatellites.containsKey(currentlyCheckedSatellite)) continue;

					String id = satelliteIdHashMap.get(currentlyCheckedSatellite);
					if (id == null) continue;

					List<List<Double>> coordinates = NasaApiClient.getSatelliteLatLonMag(id);

					if (coordinates == null || coordinates.size() < 3 || coordinates.get(0).isEmpty()) {
						return;
					}

					Satellite satellite = new Satellite(
							currentlyCheckedSatellite,
							coordinates.get(0),
							coordinates.get(1),
							coordinates.get(2),
							coordinatesSlider.getValue()
					);

					satellite.radiusProperty().bind(view3d.heightProperty().divide(25));
					activeSatellites.put(currentlyCheckedSatellite, satellite);
					satellites.getChildren().add(satellite);
				}
			}

			previousCheckedSatellites.clear();
			previousCheckedSatellites.addAll(currentCheckedList);
		});
	}

	/**
	 * Helper method for preparing slider event listeners
	 */
	private void prepareSliderEventListeners() {
		coordinatesSlider.valueProperty().addListener((objects, oldValue, newValue) -> {
			double listProportion = newValue.doubleValue();

			for (Node node : satellites.getChildren()) {
				if (node instanceof Satellite satellite) {
					satellite.setListProportion(listProportion);
				}
			}

			long millis = NasaApiClient.START_DATE.getTime() + (long) Math.round(listProportion * (NasaApiClient.END_DATE.getTime() - NasaApiClient.START_DATE.getTime()));
			this.timeLabel.setText("Time: " + utcFormat.format(new Date(millis)));
		});

	}

	/**
	 * Helper method for realtimeCheckBox slider event listeners
	 */
	private void prepareRealtimeCheckboxEventListeners() {
		realtimeCheckBox.selectedProperty().addListener((obs, wasChecked, isChecked) -> {
			if (isChecked) {
				this.coordinatesSlider.setDisable(true);
				final Timeline[] realtimeTimelineHolder = new Timeline[1];
				realtimeTimelineHolder[0] = new Timeline(
					new KeyFrame(javafx.util.Duration.millis(100), evt -> {
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
	}

	//Runs the application
	public static void main(String[] args) {
		launch();
	}
}
