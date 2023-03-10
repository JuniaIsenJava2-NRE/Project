module isen.java {
    requires javafx.controls;
    requires javafx.fxml;

    opens isen.java to javafx.fxml;
    exports isen.java;
}
