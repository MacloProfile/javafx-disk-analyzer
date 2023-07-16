package com.example.diskanalyser.controller;

import com.example.diskanalyser.logics.Chart;
import com.example.diskanalyser.logics.DiskScan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyzerController {
    private Long gb;
    private Stage stage;
    protected Map<String, Long> sizes;
    protected final ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    private String mainPath;

    @FXML
    private Button directoryButton;

    @FXML
    protected PieChart pieChart;

    @FXML
    protected CheckBox sFullPath;

    @FXML
    protected TextField limit;

    @FXML
    private Button backwardsButton;

    @FXML
    private void handleDirectoryButtonAction(ActionEvent event) {
        // Opening the directory selection dialog box
        File file = new DirectoryChooser().showDialog(stage);
        if (file != null) {
            String path = file.getAbsolutePath();
            mainPath = path;
            // Calculate directory sizes
            sizes = new DiskScan().checkSize(Path.of(path));
            start(path);
        }
    }

    public void initialize(Stage stage) {
        this.stage = stage;
        stage.setTitle("disk-analyzer");
    }

    public void start(String path) {
        Chart chart = new Chart(pieChart, gb, sizes, pieChartData, limit, sFullPath, mainPath);
        chart.createChart(path);

        backwardsButton.setOnAction(actionEvent -> {
            chart.backwards();
        });
    }
}
