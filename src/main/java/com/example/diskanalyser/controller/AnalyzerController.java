package com.example.diskanalyser.controller;

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

    @FXML
    private Button directoryButton;

    @FXML
    protected PieChart pieChart;

    @FXML
    private CheckBox sFullPath;

    @FXML
    private TextField limit;

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

    public void createChart(String path) {
        pieChartData.clear();
        // Create data for the pie chart
        if (!limit.getText().isEmpty()) {
            gb = Math.round(Double.parseDouble(limit.getText()) * 1073741824);
        } else {
            gb = 0L;
        }
        pieChartData.addAll(
                sizes.entrySet()
                        .parallelStream()
                        .filter(entry -> {
                            Path parent = Path.of(entry.getKey()).getParent();
                            return parent != null && parent.toString().equals(path);
                        })
                        .filter(entry -> entry.getValue() >= gb)
                        .map(entry ->
                                new PieChart.Data(sFullPath.isSelected() ?
                                        entry.getKey() :
                                        Path.of(entry.getKey()).getFileName().toString(), entry.getValue()))
                        .sorted(Comparator.comparing(PieChart.Data::getPieValue).reversed())
                        .limit(100)
                        .collect(Collectors.toList())
        );

        // Setting the data in a pie chart
        pieChart.setData(pieChartData);

        // Assigning event handlers for pie chart segments
        pieChart.getData().forEach(data -> {
            data.getNode().addEventHandler(ActionEvent.ACTION, event -> {
                // Folder size
                Tooltip tooltip = new Tooltip(String.format("%.2f GB", data.getPieValue() / 1e9));
                Tooltip.install(data.getNode(), tooltip);
                // Rebuild the pie chart with a new path
                createChart(data.getName());
            });
            data.setName(data.getName() + " " + String.format("%.2f GB", data.getPieValue() / 1e9));
        });
    }
}
