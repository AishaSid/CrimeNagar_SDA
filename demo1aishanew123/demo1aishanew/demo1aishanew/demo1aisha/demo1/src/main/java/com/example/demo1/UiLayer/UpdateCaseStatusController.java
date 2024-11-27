package com.example.demo1.UiLayer;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.io.IOException;
import java.util.Objects;

public class UpdateCaseStatusController {

    public Button backButton;
    public VBox VboxMain;
    BLConnector b = new BLConnector();
    @FXML
    private BarChart<String, Number> barchart;
    String[][] a;

    @FXML
    private void initialize()
    {


    }


    @FXML
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    
    public void addNewVBox(int id, String name, String description) {
        // Create a new VBox
        VBox newVBox = new VBox();
        newVBox.setPrefHeight(68.0);
        newVBox.setPrefWidth(347.0);
        newVBox.setStyle("-fx-background-color: #FFFFFF; -fx-border-radius: 10; -fx-background-radius: 10;");

        // Create the HBox
        HBox hbox = new HBox();
        hbox.setPrefHeight(100.0);
        hbox.setPrefWidth(300.0);
        hbox.setStyle("-fx-alignment: center;");

        // Add a Label for the name (e.g., "Report 1")
        Label nameLabel = new Label(name);
        nameLabel.setPrefHeight(26.0);
        nameLabel.setPrefWidth(67.0);
        nameLabel.setStyle("-fx-font-size: 14;");
        HBox.setMargin(nameLabel, new Insets(0, 0, 0, 7.0)); // Add left margin

        // Add a spacer (Region)
        Region spacer = new Region();
        spacer.setPrefHeight(42.0);
        spacer.setPrefWidth(196.0);
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        // Add a Button for "Show"
        Button showButton = new Button("Show");
        showButton.setStyle("-fx-background-color: transparent;");
        showButton.setOnAction(e -> {
            try {
                handleShowButtonClick(id, name);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }); // Set click event
        HBox.setMargin(showButton, new Insets(0, 7.0, 0, 0)); // Add right margin

        // Add children to HBox
        hbox.getChildren().addAll(nameLabel, spacer, showButton);

        // Add a Label for the description
        Label descriptionLabel = new Label(description);
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(Double.MAX_VALUE);
        descriptionLabel.setStyle("-fx-padding: 3 10 3 10;"); // Padding: top, right, bottom, left
        VBox.setMargin(descriptionLabel, new Insets(3.0, 3.0, 3.0, 10.0)); // Add margins

        // Add children to VBox
        newVBox.getChildren().addAll(hbox, descriptionLabel);

        // Add the new VBox to the main VBox
        VboxMain.getChildren().add(newVBox);
    }



    private void handleShowButtonClick(int id, String type) throws IOException {
        System.out.println("Show Button Clicked!"+ id + type);
        //call next page based on type
        if(Objects.equals(type, "Missing Person"))
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("showMissingPerson.fxml"));
            StackPane root = loader.load();
            ShowMPController a = loader.getController();
            a.receiveMessage(cnic, id, "officer");
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else
        {
            try {
                FXMLLoader loader = new FXMLLoader(this.getClass().getResource("showCase.fxml"));
                Parent root = loader.load();
                ShowCase controller = loader.getController();
                controller.receiveMessage(cnic, id, "officer");
                Stage stage = (Stage) backButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace(); // Prints the full stack trace for debugging
                System.err.println("Error loading FXML: " + e.getMessage());
            }
        }
    }

     

    String cnic;
    public void receiveMessage(String cnic)
    {
        this.cnic = cnic;
        System.out.println(cnic);
        a =  b.getReportsByOfficerCNIC(cnic);

        for (String[] entry : a)
        {
           addNewVBox(Integer.parseInt(entry[0]),entry[1], entry[2]);
        }
    }
    
    

    public void onBackButtonClicked(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("officer_dashboard.fxml")); // Adjust this to your previous page FXML
            StackPane root = loader.load();

            officerDashboardController a = loader.getController();
            a.receiveMessage(cnic);
            Stage stage = (Stage) (backButton.getScene().getWindow());
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the previous page.");
        }
    }

}

