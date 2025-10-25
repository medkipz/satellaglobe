package satellaglobe;

import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Popup;

/**
 * Satellite Class to handle 3D representations of live satellites around earth
 */

public class Satellite extends Sphere {
    private String name;
    
    public double xTranslate;
    public double yTranslate;
    public double zTranslate;

    /**
     * Satellite Constructor 
     * 
     * @param name for name of satellite
     * @param xTranslate for javafx x-translation
     * @param yTranslate for javafx y-translation
     * @param zTranslate for javafx z-translation
     */
    public Satellite(String name, double xTranslate, double yTranslate, double zTranslate) {
        
        //Instantiates each satellite with a size of 20
        super(20);
        
        this.name = name;

        this.xTranslate = xTranslate;
        this.yTranslate = yTranslate;
        this.zTranslate = zTranslate;

        super.translateXProperty().set(xTranslate);
        super.translateYProperty().set(yTranslate);
        super.translateZProperty().set(zTranslate);

        //Popup window to display relevant satellite information
        Popup satInfo = new Popup();
        Label satName = new Label("Name: " + this.name);
        Label satLocation = new Label("\nX-Location: " + xTranslate + "\n" + 
            "Y-Location: " + yTranslate + "\n" + "Z-Location: " + zTranslate);
        satInfo.getContent().addAll(satName, satLocation);
		satName.setTextFill(Color.WHITE);
		satLocation.setTextFill(Color.WHITE);

        //Color value to apply to satellites
        PhongMaterial hoverColor = new PhongMaterial();
        hoverColor.setDiffuseColor(Color.ORANGE);

        //Changes satellite color to orange and displays the popup when hovered over
        this.addEventHandler(MouseEvent.MOUSE_ENTERED, unused -> {
            this.setMaterial(hoverColor);
            Point2D screenPos = this.localToScreen(0, 0);
            satInfo.show(this, screenPos.getX() + 10, screenPos.getY() + 10);
        });

        //Removes coloring and popup once mouse is moved away from the satellite
        this.addEventHandler(MouseEvent.MOUSE_EXITED, unused -> {
            this.setMaterial(null);
            satInfo.hide();
        });
    }

}