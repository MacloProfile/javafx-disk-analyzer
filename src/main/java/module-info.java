module com.example.diskanalyser {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.diskanalyser to javafx.fxml;
    exports com.example.diskanalyser;
    exports com.example.diskanalyser.controller;
    opens com.example.diskanalyser.controller to javafx.fxml;
}