module com.example.farmatec {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.farmatec to javafx.fxml;
    exports com.example.farmatec;
}
