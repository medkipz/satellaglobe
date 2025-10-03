module medkipz {
    requires javafx.controls;
    requires javafx.fxml;

    opens medkipz to javafx.fxml;
    exports medkipz;
}
