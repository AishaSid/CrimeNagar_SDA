package com.example.demo1.UiLayer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class statsController {

    public Button backButton;
    @FXML
    private Text descriptionText;

    @FXML
    private BarChart<String, Number> barchart;

    private final BLConnector blConnector = new BLConnector();

    @FXML
    private void onBackButtonClicked(ActionEvent event) throws IOException {
        // Load the Login FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("officer_dashboard.fxml"));
        StackPane root = loader.load(); // Change to AnchorPane
        officerDashboardController a = loader.getController();
        a.receiveMessage(cnic);
        // Create a new scene with the login page and set it on the stage
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void initialize() {
        // Set description text
        System.out.println("running");

        descriptionText.setText("This bar chart represents the weekly crime statistics based on collected data.");

        // Create a data series for the BarChart
        XYChart.Series<String, Number> crimeRateSeries = blConnector.viewreportstats();
        crimeRateSeries.setName("Crime Rate");
        updateBarChart(crimeRateSeries);
        showDescription(crimeRateSeries);
    }

    private void showDescription(XYChart.Series<String, Number> crimeRateSeries)
    {
        StringBuilder description = new StringBuilder();
        for (XYChart.Data<String, Number> data : crimeRateSeries.getData())
        {
            description.append(data.getXValue())
                    .append(":   ")
                    .append(data.getYValue())
                    .append("\n\n");
        }
        descriptionText.setText(description.toString());
    }

    private String getRandomColor() {
        // Array of predefined color codes
        String[] colors = {
                "#FF5733", // Red (Theft)
                "#33FF57", // Green (Burglary)
                "#3357FF", // Blue (Assault)
                "#FFD700", // Yellow (Robbery)
                "#8A2BE2"  // Purple (Murder)
        };

        // Generate a random index to select a color
        Random random = new Random();
        int randomIndex = random.nextInt(colors.length);  // Random index between 0 and colors.length-1

        return colors[randomIndex];  // Return the randomly selected color
    }
    private void updateBarChart(XYChart.Series<String, Number> crimeRateSeries)
    {
        barchart.getData().clear();
        barchart.getData().add(crimeRateSeries);
        // just coloring
        for (XYChart.Data<String, Number> data : crimeRateSeries.getData())
        {
            String color = getRandomColor();
            data.getNode().setStyle("-fx-bar-fill: " + color);
        }
        adjustYAxis(crimeRateSeries);
    }

    private void adjustYAxis(XYChart.Series<String, Number> crimeRateSeries)
    {
        double maxValue = 0;
        for (XYChart.Data<String, Number> data : crimeRateSeries.getData()) {
            maxValue = Math.max(maxValue, data.getYValue().doubleValue());
        }
        // If the data count is 1 or the values are very small, set a reasonable range
        if (maxValue <= 1) {
            if (barchart.getYAxis() instanceof NumberAxis) {
                NumberAxis yAxis = (NumberAxis) barchart.getYAxis();
                yAxis.setAutoRanging(true);
                yAxis.setLowerBound(0);
                yAxis.setUpperBound(1);
            }
        } else {
            if (barchart.getYAxis() instanceof NumberAxis) {
                NumberAxis yAxis = (NumberAxis) barchart.getYAxis();
                yAxis.setAutoRanging(false);
                yAxis.setUpperBound(maxValue * 1.1);
            }
        }
    }

    @FXML
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    String cnic;
    public void receiveMessage(String cnic)
    {
        this.cnic = cnic;
    }
}

