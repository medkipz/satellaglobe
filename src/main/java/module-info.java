module satellaglobe {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
	requires java.sql;

    opens satellaglobe to javafx.fxml;
    exports satellaglobe;
}
