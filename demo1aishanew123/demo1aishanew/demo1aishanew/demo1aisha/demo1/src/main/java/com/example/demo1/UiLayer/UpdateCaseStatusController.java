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
    public Button cp;
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
        newVBox.setPrefWidth(244.0); // Adjusted to account for side margins
        newVBox.setStyle("-fx-background-color: #e8e8e8; -fx-border-radius: 10; -fx-background-radius: 10;");
        newVBox.setPadding(new Insets(5, 5, 5, 5)); // Keep consistent padding
        VBox.setMargin(newVBox, new Insets(10, 8, 5, 8)); // Adjusted side margins to fit 260 total




        // Create the HBox
        HBox hbox = new HBox();
        hbox.setPrefHeight(68.0);
        hbox.setPrefWidth(244.0); // Match VBox width for consistency
        hbox.setStyle("-fx-alignment: center-left;");

        // Add a Label for the name
        Label nameLabel = new Label(name);
        nameLabel.setPrefHeight(26.0);
        nameLabel.setPrefWidth(100.0);
        nameLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #333333;");
        HBox.setMargin(nameLabel, new Insets(0, 10, 0, 0));

        // Add a spacer (Region)
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        // Add a Button for "Show"
        Button showButton = new Button("Show");
        showButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #007BFF; -fx-font-size: 12;");
        showButton.setOnAction(e -> {
            try {
                handleShowButtonClick(id, name);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        HBox.setMargin(showButton, new Insets(0, 0, 40, 10));

        // Add children to HBox
        hbox.getChildren().addAll(nameLabel, spacer, showButton);

        // Add a Label for the description
        Label descriptionLabel = new Label(description);
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(244.0);
        descriptionLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 12;");
        VBox.setMargin(descriptionLabel, new Insets(5, 0, 0, 0));

        // Add children to VBox
        newVBox.getChildren().addAll(hbox, descriptionLabel);

        // Add the new VBox to the main VBox
        VboxMain.getChildren().add(newVBox);
    }




    private void handleShowButtonClick(int id, String type) throws IOException {
        System.out.println("Show Button Clicked!"+ id + type);
        //call next page based on type

        if(!hh)
        {
            try {
                // Load the Login FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfirmPerson.fxml"));
                StackPane root = loader.load(); // Change to AnchorPane

                CPController a = loader.getController();
                a.receiveMessage(cnic,id);
                // Create a new scene with the login page and set it on the stage
                Stage stage = (Stage) backButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Confirm Missing Person form.");
            }

        }
        else{
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
    }

     

    boolean hh;
    String cnic;
    public void receiveMessage(String cnic,boolean h)
    {
        this.cnic = cnic;
        this.hh=h;
        System.out.println(cnic);
        a =  b.getReportsByOfficerCNIC(cnic,h);

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

