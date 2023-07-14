package com.example.diskanalyser;

import com.example.diskanalyser.controller.AnalyzerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AnalyzerApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent parent = loader.load();
        AnalyzerController analyzerController = loader.getController();
        analyzerController.initialize(stage);

        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }
}
