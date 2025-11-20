package satellaglobe;

import java.util.*;

import org.controlsfx.control.CheckComboBox;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Slider;
import javafx.scene.control.ToolBar;
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
		final ObservableList<String> satelliteNames = FXCollections.observableList(NasaApiClient.GetAllActiveSatelliteNames());
		final Map<String, String> satelliteIdHashMap = NasaApiClient.GetSatelliteNameIdMap();

		final Image backgroundStars = new Image(getClass().getResource("/satellaglobe/backgroundStars.png").toExternalForm());
		final Image globeTexture = new Image(getClass().getResource("/satellaglobe/globeTexture.jpg").toExternalForm());

        // 3D model to represent Earth and center it in the scene
        Sphere globe = new Sphere();
        PhongMaterial globeMaterial = new PhongMaterial();
		Camera camera = new ParallelCamera();
		Group satellites = new Group();
        Group model = new Group(globe, satellites);
		Group pivot = new Group(model);
		SubScene view3d = new SubScene(pivot, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
		BorderPane ui = new BorderPane();
		CheckComboBox<String> satellitePicker = new CheckComboBox<>(satelliteNames);
		Scene scene = new Scene(ui);
		Slider coordinatesSlider = new Slider();
		ToolBar toolBar = new ToolBar();
		Rotate rotate = new Rotate(0, Rotate.Y_AXIS);
		
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
		coordinatesSlider.setValue(0);

		ui.setCenter(view3d);

		toolBar.getItems().addAll(satellitePicker, coordinatesSlider);


		satellitePicker.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) c -> {
			satellites.getChildren().removeIf(n -> n instanceof Satellite);

			for (String name : satellitePicker.getCheckModel().getCheckedItems()) {
				String id = satelliteIdHashMap.get(name);
				List<List<Double>> coordinates = NasaApiClient.getSatelliteLatLonMag(id);

				Satellite satellite = new Satellite(
					name,
					coordinates.get(0),
					coordinates.get(1),
					coordinates.get(2),
					(int) Math.round(coordinatesSlider.getValue() * (coordinates.get(0).size() - 1))
				);

				satellite.radiusProperty().bind(view3d.heightProperty().divide(25));

				satellites.getChildren().add(satellite);
			}
		});

		coordinatesSlider.valueProperty().addListener((objects, oldValue, newValue) -> {
			double indexProportion = newValue.doubleValue();

			for (Node node : satellites.getChildren()) {
				if (node instanceof Satellite satellite) {
					int index = (int) Math.round(indexProportion * (satellite.getLatitudes().size() - 1));
					satellite.setListIndex(index);
				}
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
