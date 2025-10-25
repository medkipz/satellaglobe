package satellaglobe;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.*;
import javafx.collections.*;

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

	private static final ObservableList<String> satelliteNames = FXCollections.observableList(
		XMLUtils.parseTags(
			HttpRequester.getUrlData("https://sscweb.gsfc.nasa.gov/WS/sscr/2/observatories"),
			"Name"
		)
	);

    //Stops alert related to the switch statment
    @SuppressWarnings("incomplete-switch")

    //Main method override for java applications
    @Override
    public void start(Stage stage) throws Exception {

        //3D model to represent Earth and center it in the scene
        Sphere globe = new Sphere(50);
        Image globeTexture = new Image(getClass().getResource("/satellaglobe/globeTexture.jpg").toExternalForm());
        PhongMaterial globeMaterial = new PhongMaterial();
        globeMaterial.setDiffuseMap(globeTexture);
        globe.setMaterial(globeMaterial);

        //Makes group of 'Earth' and satallites to be instantiated
        Group model = new Group();
		model.setTranslateZ(-1000);
		model.setTranslateX(WIDTH / 2);
		model.setTranslateY(HEIGHT / 2);
        model.getChildren().add(globe);
		
        //test satellites
        model.getChildren().add(new Satellite("s1", 50, 50, 50));
        model.getChildren().add(new Satellite("s2", -50, -50, -50));

        //Makes the center of rotation for the group the y-axis
        model.setRotationAxis(Rotate.Y_AXIS);

        //Sets up a javafx perspective camera to allow for model translation by the user
        Camera camera = new PerspectiveCamera();

        // Use a Pane as the scene root so we can overlay 2D controls
        Pane ui = new Pane();
        ui.getChildren().add(model);

		ComboBox<String> satellitePicker = new ComboBox<>(satelliteNames);
		satellitePicker.setLayoutX(20);
		satellitePicker.setLayoutY(20);

		// Add the picker to the UI
		ui.getChildren().add(satellitePicker);

        // Sets up the scene to be displayed with the ui and camera
        Scene scene = new Scene(ui, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
        scene.setCamera(camera);

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
