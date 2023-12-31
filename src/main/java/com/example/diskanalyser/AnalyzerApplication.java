package com.example.diskanalyser;

import com.example.diskanalyser.controller.AnalyzerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
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

    //error field
    public static void error(String message) throws Exception {
        Stage stage = new Stage();
        Label messageLabel = new Label(message);

        // edition the message
        messageLabel.setWrapText(true);
        messageLabel.setPadding(new Insets(10));
        String customFontStyle = "-fx-font-size: 16px; -fx-font-weight: bold;"; // style
        messageLabel.setStyle(customFontStyle);

        StackPane root = new StackPane();
        root.setPrefSize(220, 130);
        root.getChildren().add(messageLabel);

        Scene scene = new Scene(root);

        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("error");

        stage.show();
    }

}
