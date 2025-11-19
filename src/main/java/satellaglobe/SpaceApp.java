package satellaglobe;

import java.util.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ComboBox;
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
 * @version 1.0
 */
public class SpaceApp extends Application {

    //Scene size variables
    private static final int HEIGHT = 900;
    private static final int WIDTH = 1600;

	private Satellite currentSatellite;

	private double mouseStartX;
	private double modelStartX;

    //Main method override for java applications
    @Override
    public void start(Stage stage) throws Exception {
		final ObservableList<String> satelliteNames = FXCollections.observableList(NasaApiClient.GetAllActiveSatelliteNames());
		final Map<String, String> satelliteIdHashMap = NasaApiClient.GetSatelliteNameIdMap();

		final Image backgroundStars = new Image(getClass().getResource("/satellaglobe/backgroundStars.png").toExternalForm());
		final Image globeTexture = new Image(getClass().getResource("/satellaglobe/globeTexture.jpg").toExternalForm());

        //3D model to represent Earth and center it in the scene
        Sphere globe = new Sphere(50);
        PhongMaterial globeMaterial = new PhongMaterial();
        globeMaterial.setDiffuseMap(globeTexture);
        globe.setMaterial(globeMaterial);
		globe.setRotationAxis(Rotate.Y_AXIS);
		globe.setRotate(-90.0);

		Camera camera = new PerspectiveCamera();

        Group model = new Group(globe);
		model.setRotationAxis(Rotate.Y_AXIS);
		model.setTranslateX(WIDTH / 2);
		model.setTranslateY(HEIGHT / 2);
		model.setTranslateZ(-1000);

        // Use a Pane as the scene root so we can overlay 2D controls
       	SubScene view3d = new SubScene(model, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
		view3d.setFill(Color.BLACK);
		view3d.setFill(new javafx.scene.paint.ImagePattern(backgroundStars));

		view3d.setCamera(camera);

		BorderPane ui = new BorderPane();
		ui.setCenter(view3d);

		ComboBox<String> satellitePicker = new ComboBox<>(satelliteNames);
		satellitePicker.setPromptText("Select the satellite you wish to view.");
		satellitePicker.setOnAction(unused -> {
			String selected = satellitePicker.getValue();
			String selectedId = satelliteIdHashMap.get(selected);
			List<List<Double>> coordinates =  NasaApiClient.getSatelliteLatLonMag(selectedId);

			if (currentSatellite != null) {
				model.getChildren().remove(currentSatellite);
			}

			currentSatellite = new Satellite(
				selected,
				coordinates.get(0).get(0),
				coordinates.get(1).get(0),
				coordinates.get(2).get(0)
			);

			model.getChildren().add(currentSatellite);
		});

		ToolBar toolBar = new ToolBar(satellitePicker);
		toolBar.setOrientation(Orientation.VERTICAL);
		ui.setLeft(toolBar);
		ui.setPrefSize(HEIGHT, 128);

		Scene scene = new Scene(ui);
		
		scene.setOnMousePressed(event -> {
			mouseStartX = event.getSceneX();
			modelStartX = model.getRotate();
		});

		scene.setOnMouseDragged(event -> {
			model.setRotate(modelStartX - (event.getSceneX() - mouseStartX)  * 0.4);
			double bgOffsetX = - (model.getRotate() % 360) / 360.0;
			view3d.setFill(new javafx.scene.paint.ImagePattern(backgroundStars, bgOffsetX, 0, 1, 1, true));
		});

		System.out.println(NasaApiClient.GetAllObservatories());
		
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
