module satellaglobe {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
	requires java.sql;
    requires transitive javafx.graphics;

    opens satellaglobe to javafx.fxml;
    exports satellaglobe;
}
