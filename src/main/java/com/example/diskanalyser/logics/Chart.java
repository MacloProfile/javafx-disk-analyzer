package com.example.diskanalyser.logics;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.nio.file.Path;
import java.nio.file.Paths;
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

    private String mainPath;

    public Chart(PieChart pieChart, Long gb, Map<String, Long> sizes, ObservableList<PieChart.Data> pieChartData,
                 TextField limit, CheckBox sFullPath, String mainPath) {
        this.pieChart = pieChart;
        this.gb = gb;
        this.sizes = sizes;
        this.pieChartData = pieChartData;
        this.limit = limit;
        this.sFullPath = sFullPath.isSelected();
        this.mainPath = mainPath;
    }

    public Chart() {
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
        setInfo(path);
    }

    private void setVisual(String path){

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

    private void setInfo(String path) {
        pieChart.getData().forEach(data -> {
            //info about the folder selected by the user
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                // get folder name
                String selectedFolderName = data.getName();

                // If sFullPath is selected, selectedFolderName already contains the full path,
                // otherwise add it to the root path to get the full path
                if (sFullPath)
                    mainPath = selectedFolderName;
                else
                    mainPath = Paths.get(mainPath, selectedFolderName).toString();
                //delete GB info
                mainPath = mainPath.substring(0, mainPath.length() - 8).trim();
                System.out.println(mainPath);
                repeat();
            });
            data.setName(data.getName() + " " + String.format("%.2f GB", data.getPieValue() / 1e9));
        });
    }

    // create a new diagram on a new path
    private void repeat(){
        createChart(mainPath);
    }

    // going back to the folder by pressing the corresponding button
    public void backwards() {
        System.out.println(mainPath);

        // only if the user has selected a folder other than the root of the disk
        if (mainPath.length() > 3) {
            Path parentFolder = Paths.get(mainPath).getParent();
            mainPath = parentFolder + "";
            repeat();
        // root folder
        } else if (mainPath.length() > 1) {
            repeat();
        }

    }
}
