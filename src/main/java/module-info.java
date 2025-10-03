module medkipz {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;

    opens medkipz to javafx.fxml;
    exports medkipz;
}
