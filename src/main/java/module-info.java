module com.example.diskanalyser {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.diskanalyser to javafx.fxml;
    exports com.example.diskanalyser;
}