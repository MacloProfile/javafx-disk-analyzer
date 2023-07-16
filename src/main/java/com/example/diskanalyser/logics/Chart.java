package com.example.diskanalyser.logics;

import com.example.diskanalyser.controller.AnalyzerController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class Chart {

    public void setVisual(ObservableList<PieChart.Data> pieChartData,
                        String path,
                        Map<String, Long> sizes,
                        Long gb,
                        boolean sFullPath){

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

    public void setInfo(PieChart pieChart) {
        pieChart.getData().forEach(data -> {
            data.getNode().addEventHandler(ActionEvent.ACTION, event -> {
                // Folder size
                Tooltip tooltip = new Tooltip(String.format("%.2f GB", data.getPieValue() / 1e9));
                Tooltip.install(data.getNode(), tooltip);
                // Rebuild the pie chart with a new path
                AnalyzerController controller = new AnalyzerController();
                controller.createChart(data.getName());
            });
            data.setName(data.getName() + " " + String.format("%.2f GB", data.getPieValue() / 1e9));
        });
    }
}
