package com.example.diskanalyser.logics;

import com.example.diskanalyser.controller.AnalyzerController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class Chart {

    private PieChart pieChart;
    private Long gb;
    protected Map<String, Long> sizes;
    protected ObservableList<PieChart.Data> pieChartData;
    protected TextField limit;
    private boolean sFullPath;

    public Chart(PieChart pieChart, Long gb, Map<String, Long> sizes, ObservableList<PieChart.Data> pieChartData,
                 TextField limit, CheckBox sFullPath) {
        this.pieChart = pieChart;
        this.gb = gb;
        this.sizes = sizes;
        this.pieChartData = pieChartData;
        this.limit = limit;
        this.sFullPath = sFullPath.isSelected();
    }

    public void createChart(String path) {
        pieChartData.clear();
        // Create data for the pie chart
        if (!limit.getText().isEmpty()) {
            gb = Math.round(Double.parseDouble(limit.getText()) * 1073741824);
        } else {
            gb = 0L;
        }

        //createChartVisual
        setVisual(path);

        // Setting the data in a pie chart
        pieChart.setData(pieChartData);

        // Assigning event handlers for pie chart segments
        setInfo();
    }

    public void setVisual(String path){

        pieChartData.addAll(
                sizes.entrySet()
                        .parallelStream()
                        .filter(entry -> {
                            Path parent = Path.of(entry.getKey()).getParent();
                            return parent != null && parent.toString().equals(path);
                        })
                        .filter(entry -> entry.getValue() >= gb)
                        .limit(50)
                        .map(entry ->
                                new PieChart.Data(sFullPath ?
                                        entry.getKey() :
                                        Path.of(entry.getKey()).getFileName().toString(), entry.getValue()))
                        .sorted(Comparator.comparing(PieChart.Data::getPieValue).reversed())
                        .collect(Collectors.toList())
        );
    }

    public void setInfo() {
        pieChart.getData().forEach(data -> {
            data.getNode().addEventHandler(ActionEvent.ACTION, event -> {
                // Folder size
                Tooltip tooltip = new Tooltip(String.format("%.2f GB", data.getPieValue() / 1e9));
                Tooltip.install(data.getNode(), tooltip);
                // Rebuild the pie chart with a new path
                AnalyzerController controller = new AnalyzerController();
                createChart(data.getName());
            });
            data.setName(data.getName() + " " + String.format("%.2f GB", data.getPieValue() / 1e9));
        });
    }
}
