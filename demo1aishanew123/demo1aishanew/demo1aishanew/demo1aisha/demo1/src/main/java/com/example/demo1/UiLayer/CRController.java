package com.example.demo1.UiLayer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class CRController {

    @FXML
    private ComboBox<String> areaComboBox;
    @FXML
    private BarChart<String, Number> barchart;

    private final BLConnector blConnector = new BLConnector();

    @FXML
    private void onBackButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
        StackPane root = loader.load();

        DashboardController a = loader.getController();
        a.receiveMessage(cnic);
        Stage stage = (Stage) areaComboBox.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    private String cnic;
    @FXML
    public void initialize() {
        // Populate ComboBox with all areas of Islamabad
        areaComboBox.getItems().addAll(
                "F-5", "F-6", "F-7", "F-8", "F-9", "F-10", "F-11", "F-12", "F-13",
                "G-5", "G-6", "G-7", "G-8", "G-9", "G-10", "G-11", "G-12", "G-13", "G-14",
                "H-8", "H-9", "H-10", "H-11", "H-12",
                "I-8", "I-9", "I-10", "I-11", "I-12", "I-13", "I-14", "I-15", "I-16",
                "Blue Area", "Diplomatic Enclave", "Bahria Town", "DHA", "E-11",
                "Fazaia Colony", "Margalla Town", "Shakarparian", "Rawal Town",
                "Golra", "Tarnol", "Bara Kahu", "Bhara Kahu"
        );

        // Set listener for ComboBox selection
        areaComboBox.setOnAction(event -> onAreaSelected());
    }

    private void onAreaSelected() {
        String selectedArea = areaComboBox.getValue();
        if (selectedArea != null && !selectedArea.isEmpty()) {
            // Fetch data from BLConnector
            XYChart.Series<String, Number> dataSeries = blConnector.fetchBarChartData(selectedArea);

            // Update BarChart
            updateBarChart(dataSeries);
        }
    }

    private void updateBarChart(XYChart.Series<String, Number> dataSeries) {
        // Clear existing data and labels
        barchart.getData().clear(); // Clear existing data from the chart
        barchart.getData().add(dataSeries); // Add new data series

        // Set fixed size for the BarChart (adjust these as needed)
        double fixedWidth = 200;  // Fixed width for the chart
        double fixedHeight = 200; // Fixed height for the chart
        barchart.setPrefWidth(fixedWidth);
        barchart.setPrefHeight(fixedHeight);
        barchart.setMaxWidth(fixedWidth);
        barchart.setMaxHeight(fixedHeight);
        barchart.setMinWidth(fixedWidth);
        barchart.setMinHeight(fixedHeight);

        // Get the container for the labels (we assume the parent is an AnchorPane)
        AnchorPane parentPane = (AnchorPane) barchart.getParent(); // Parent of the chart

        // Remove any existing label HBox before adding new labels
        parentPane.getChildren().removeIf(child -> child instanceof HBox); // Remove old HBox if present

        // Create a container for the labels (HBox or VBox can be used)
        HBox crimeTypeLabelsBox = new HBox(10); // 10px spacing between labels
        crimeTypeLabelsBox.setLayoutX(50); // Adjust this based on your layout

        // Position the labels below the chart, ensuring they don't overlap
        crimeTypeLabelsBox.setLayoutY(barchart.getLayoutY() + fixedHeight + 10); // Position labels below the chart

        // Add color and crime type labels for only the crimes shown in the graph
        for (XYChart.Data<String, Number> data : dataSeries.getData()) {
            String crimeType = data.getXValue();
            String color = getColorForCrimeType(crimeType); // Get color for the crime type

            // Create a label with color box and crime type
            Label crimeLabel = new Label(crimeType);
            crimeLabel.setStyle("-fx-background-color: " + color + "; -fx-padding: 5px;");

            // Add the label to the container
            crimeTypeLabelsBox.getChildren().add(crimeLabel);
        }

        // Add the container of crime labels below the chart
        parentPane.getChildren().add(crimeTypeLabelsBox);

        // Adjust the Y-axis range to avoid excessive scaling
        adjustYAxis(dataSeries);

        // Set colors for bars after chart rendering
        Platform.runLater(() -> {
            for (XYChart.Data<String, Number> data : dataSeries.getData()) {
                String color = getColorForCrimeType(data.getXValue());
                data.getNode().setStyle("-fx-bar-fill: " + color); // Set the color for the bar
            }
        });
    }

    private void adjustYAxis(XYChart.Series<String, Number> dataSeries) {
        // Get the maximum value in the data series
        double maxValue = 0;
        for (XYChart.Data<String, Number> data : dataSeries.getData()) {
            maxValue = Math.max(maxValue, data.getYValue().doubleValue());
        }

        // If the data count is 1 or the values are very small, set a reasonable range
        if (maxValue == 1) {
            // Ensure that the Y-Axis is a NumberAxis before setting lower/upper bounds
            if (barchart.getYAxis() instanceof NumberAxis) {
                NumberAxis yAxis = (NumberAxis) barchart.getYAxis();  // Cast to NumberAxis
                yAxis.setAutoRanging(true);  // Disable auto-ranging
                yAxis.setLowerBound(0);      // Set lower bound
                yAxis.setUpperBound(1);      // Set upper bound
            }
        } else {
            // Set a dynamic upper bound based on the max value (with some padding)
            if (barchart.getYAxis() instanceof NumberAxis) {
                NumberAxis yAxis = (NumberAxis) barchart.getYAxis();  // Cast to NumberAxis
                yAxis.setAutoRanging(false);  // Disable auto-ranging
                // Set an upper bound with padding (e.g., 10% more than the max value)
                yAxis.setUpperBound(maxValue * 1.1);
            }
        }
    }




    private String getColorForCrimeType(String crimeType) {
        // Return a unique color for each crime type
        switch (crimeType) {
            case "Theft":
                return "#FF5733"; // Example: Red
            case "Burglary":
                return "#33FF57"; // Example: Green
            case "Assault":
                return "#3357FF"; // Example: Blue
            case "Robbery":
                return "#FFD700"; // Example: Yellow
            case "Murder":
                return "#8A2BE2"; // Example: Purple
            default:
                // Generate a random color for the default case
                return generateRandomColor();
        }
    }

    private String generateRandomColor() {
        Random rand = new Random();

        // Generate random values for RGB (each between 0 and 255)
        int r = rand.nextInt(256);
        int g = rand.nextInt(256);
        int b = rand.nextInt(256);

        // Convert RGB values to hex format (e.g., #RRGGBB)
        return String.format("#%02X%02X%02X", r, g, b);
    }


    public void receiveMessage(String cnic) {
        this.cnic = cnic;
    }
}
