module com.example.farmatec {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.farmatec to javafx.fxml;
    exports com.example.farmatec;
    opens com.example to javafx.fxml;
    opens com.example.farmatec.controller to javafx.fxml;
}
