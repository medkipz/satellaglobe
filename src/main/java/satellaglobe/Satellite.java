package satellaglobe;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class Satellite extends Sphere {
    private String name;
    
    public double xTranslate;
    public double yTranslate;
    public double zTranslate;

    public Satellite(String name, double xTranslate, double yTranslate, double zTranslate) {
        super(20);
        this.name = name;
        super.translateXProperty().set(xTranslate);
        super.translateYProperty().set(yTranslate);
        super.translateZProperty().set(zTranslate);

        PhongMaterial hoverColor = new PhongMaterial();
        hoverColor.setDiffuseColor(Color.ORANGE);
        
        this.addEventHandler(MouseEvent.MOUSE_ENTERED, _ -> {
            this.setMaterial(hoverColor);
        });
        this.addEventHandler(MouseEvent.MOUSE_EXITED, _ -> {
            this.setMaterial(null);
        });
    }


}