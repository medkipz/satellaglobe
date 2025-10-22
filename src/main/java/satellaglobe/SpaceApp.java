package satellaglobe;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.*;

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

    //Stops alert related to the switch statment
    @SuppressWarnings("incomplete-switch")

    //Main method override for java applications
    @Override
    public void start(Stage arg0) throws Exception {

        //3D model to represent Earth and center it in the scene
        Sphere globe = new Sphere(50);
        globe.translateXProperty().set(WIDTH/2);
        globe.translateYProperty().set(HEIGHT/2);

        //Makes group of 'Earth' and satallites to be instantiated
        Group spaceUI = new Group();
        spaceUI.getChildren().add(globe);
        //test satellites
        spaceUI.getChildren().add(new Satellite("s1", WIDTH/3, HEIGHT/3, 200));
        spaceUI.getChildren().add(new Satellite("s2", WIDTH/1.2, HEIGHT/1.5, 50));

        //Makes the center of rotation for the group the y-axis
        spaceUI.setRotationAxis(Rotate.Y_AXIS);

        //Sets up a javafx perspective camera to allow for model translation by the user
        Camera telescope = new PerspectiveCamera();

        //Sets up the scene to be displayed with the group and camera
        Scene space = new Scene(spaceUI, WIDTH, HEIGHT);
        space.setCamera(telescope);

        //Key press event handler to zoom in and out and rotate the group
        arg0.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                //zoom out
                case W:
                    spaceUI.translateZProperty().set(spaceUI.getTranslateZ() + 50);
                    break;
                //zoom in
                case S:
                    spaceUI.translateZProperty().set(spaceUI.getTranslateZ() - 50);
                    break;
                //Rotate left    
                case A:
                    spaceUI.setRotate(spaceUI.getRotate() + 20);
                    break;
                //Rotate right    
                case D:
                    spaceUI.setRotate(spaceUI.getRotate() - 20);
                    break;
            }
        });

        //Instantiates the scene
        arg0.setTitle("SatellaGlobe");
        arg0.setScene(space);
        arg0.show();
    }
    //Runs the application
    public static void main(String[] args) {
        launch();
    }
    
}
