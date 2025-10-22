package satellaglobe;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.*;

public class SpaceApp extends Application {

    private static final int HEIGHT = 1000;
    private static final int WIDTH = 1000;

    @SuppressWarnings("incomplete-switch")
    @Override
    public void start(Stage arg0) throws Exception {

        Sphere globe = new Sphere(50);

        globe.translateXProperty().set(WIDTH/2);
        globe.translateYProperty().set(HEIGHT/2);

        Group spaceUI = new Group();
        spaceUI.getChildren().add(globe);
        spaceUI.getChildren().addAll(new Satellite("s1", WIDTH/3, HEIGHT/3, 200));
        spaceUI.setRotationAxis(Rotate.Y_AXIS);

        Camera telescope = new PerspectiveCamera();

        Scene space = new Scene(spaceUI, WIDTH, HEIGHT);
        space.setCamera(telescope);

        arg0.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case W:
                    spaceUI.translateZProperty().set(spaceUI.getTranslateZ() + 50);
                    break;
                case S:
                    spaceUI.translateZProperty().set(spaceUI.getTranslateZ() - 50);
                    break;
                case A:
                    spaceUI.setRotate(spaceUI.getRotate() + 20);
                    break;
                case D:
                    spaceUI.setRotate(spaceUI.getRotate() - 20);
                    break;
            }
        });

        arg0.setTitle("SatellaGlobe");
        arg0.setScene(space);
        arg0.show();
    }

    public static void main(String[] args) {
        launch();
    }
    
}
