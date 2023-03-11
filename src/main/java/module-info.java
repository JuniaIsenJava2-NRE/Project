module isen.java {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens isen.java.controllers to javafx.fxml;
    exports isen.java;
}
