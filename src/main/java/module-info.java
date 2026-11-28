module com.example.farmatec {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.farmatec to javafx.fxml;
    opens com.example.farmatec.controller to javafx.fxml;
    opens com.example.farmatec.model to javafx.base, javafx.fxml;

    exports com.example.farmatec;
    exports com.example.farmatec.controller;
    exports com.example.farmatec.model;
    exports com.example.farmatec.service;
    exports com.example.farmatec.repository;
}