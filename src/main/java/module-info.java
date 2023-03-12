module project {
    requires java.sql;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires junit;
    requires org.assertj.core;
    requires org.xerial.sqlitejdbc;

    opens isen.java to javafx.fxml;
    opens isen.java.controllers to javafx.fxml;

    exports isen.java;
    exports isen.java.db.daos;
    exports isen.java.controllers;
    exports isen.java.db.entities;
    exports isen.java.db;
}
