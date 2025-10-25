package satellaglobe;

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

	private double mouseStartX;
	private double modelStartX;

	private static final ObservableList<String> satelliteNames = FXCollections.observableList(NasaApiClient.GetAllSatelliteNames());
	private static final ObservableList<String> satelliteIds = FXCollections.observableList(NasaApiClient.GetAllSatelliteIds());

    //Main method override for java applications
    @Override
    public void start(Stage stage) throws Exception {

        //3D model to represent Earth and center it in the scene
        Sphere globe = new Sphere(50);
        Image globeTexture = new Image(getClass().getResource("/satellaglobe/globeTexture.jpg").toExternalForm());
        PhongMaterial globeMaterial = new PhongMaterial();
        globeMaterial.setDiffuseMap(globeTexture);
        globe.setMaterial(globeMaterial);

		Camera camera = new PerspectiveCamera();

        Group model = new Group(globe);
		model.setRotationAxis(Rotate.Y_AXIS);
		model.setTranslateX(WIDTH / 2);
		model.setTranslateY(HEIGHT / 2);
		model.setTranslateZ(-1000);

        // Use a Pane as the scene root so we can overlay 2D controls
       	SubScene view3d = new SubScene(model, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
		view3d.setFill(Color.BLACK);
		view3d.setCamera(camera);

		BorderPane ui = new BorderPane();
		ui.setCenter(view3d);

		ComboBox<String> satellitePicker = new ComboBox<>(satelliteNames);
		satellitePicker.setPromptText("Select the satellite you wish to view.");
		satellitePicker.setOnAction(event -> {
			String selected = satellitePicker.getValue();
			if (selected != null) {
				System.out.println(selected);
			}
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
