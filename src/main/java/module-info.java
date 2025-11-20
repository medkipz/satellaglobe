module satellaglobe {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
	requires java.sql;
	requires org.controlsfx.controls;
    requires transitive javafx.graphics;
    requires jakarta.xml.bind;
    opens satellaglobe to javafx.fxml, jakarta.xml.bind;
    exports satellaglobe;
}
