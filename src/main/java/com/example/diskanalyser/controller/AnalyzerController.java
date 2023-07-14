package com.example.diskanalyser.controller;

import com.example.diskanalyser.logics.DiskScan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyzerController {
    private Stage stage;
    private Map<String, Long> sizes;
    private final ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    @FXML
    private Button directoryButton;

    @FXML
    private PieChart pieChart;

    @FXML
    private void handleDirectoryButtonAction(ActionEvent event) {
        // Opening the directory selection dialog box
        File file = new DirectoryChooser().showDialog(stage);
        if (file != null) {
            String path = file.getAbsolutePath();
            // Calculate directory sizes
            sizes = new DiskScan().checkSize(Path.of(path));
            createChart(path);
        }
    }

    public void initialize(Stage stage) {
        this.stage = stage;
        stage.setTitle("disk-analyzer");
    }

    private void createChart(String path) {
        pieChartData.clear();
        // Create data for the pie chart
        pieChartData.addAll(
                sizes.entrySet()
                        .parallelStream()
                        .filter(entry -> {
                            Path parent = Path.of(entry.getKey()).getParent();
                            return parent != null && parent.toString().equals(path);
                        })
                        .map(entry -> new PieChart.Data(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList())
        );

        // Setting the data in a pie chart
        pieChart.setData(pieChartData);

        // Assigning event handlers for pie chart segments
        pieChart.getData().forEach(data -> {
            data.getNode().addEventHandler(ActionEvent.ACTION, event -> {
                // Rebuild the pie chart with a new path
                createChart(data.getName());
            });
        });
    }

}