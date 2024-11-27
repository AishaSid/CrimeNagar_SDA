package com.example.demo1.UiLayer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class officerDashboardController {

    private BLConnector blConnector = new BLConnector();

    public Button updateCaseStatusButton;
    @FXML private Button confirmMissingPersonButton;
    @FXML
    private void onConfirmMissingPersonClicked() {
        try {
            // Load the Login FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("updateStatus.fxml"));
            AnchorPane root = loader.load(); // Change root type if needed
            // Pass data to the next controller
            UpdateCaseStatusController a = loader.getController();
            a.receiveMessage(cnic,false);
            // Create a new scene with the login page and set it on the stage
            Stage stage = (Stage) confirmMissingPersonButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Confirm Missing Person form.");
        }
    }

    @FXML private Button  acceptReportButton;
    @FXML
    private void onAcceptReportClicked() {

        String officertype = blConnector.fetchGenericColumn(cnic, "type");
        System.out.println(officertype);

        if ("Analyst".equals(officertype)) {
            // If the officer type is "Analyst", load the page
            try {
                // Load the approveReport FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("approveReport.fxml"));
                StackPane root = loader.load(); // Change to AnchorPane if needed
                System.out.println(cnic);
                ApproveReportsController a = loader.getController();
                a.receiveMessage(cnic);

                // Create a new scene with the approve report page and set it on the stage
                Stage stage = (Stage) acceptReportButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the page.");
            }
        } else {
            // If the officer type is not "Analyst", show a warning alert
            showAlert(Alert.AlertType.WARNING, "Not Authorized", "You are not authorized to access this page.");
        }
    }






    @FXML private Button  viewReportStatsButton;
    @FXML
    private void onViewReportStatsClicked() {
        try {
            // Load the Login FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("viewStats.fxml"));
            StackPane root = loader.load(); // Change to AnchorPane

            statsController a = loader.getController();
            a.receiveMessage(cnic);
            // Create a new scene with the login page and set it on the stage
            Stage stage = (Stage) viewReportStatsButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the login page.");
        }
    }

    @FXML private Button logoutButton;
    @FXML
    private void onLogoutClicked() {
        // Load the login FXML page
        try {
            // Load the Login FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("welcome.fxml"));
            AnchorPane root = loader.load(); // Change to AnchorPane

            // Create a new scene with the login page and set it on the stage
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the login page.");
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

    public void onUpdateCaseStatusClicked(ActionEvent actionEvent) {

            try {
                // Load the updateStatus FXML file
                System.out.println("Loading updateStatus.fxml...");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("updateStatus.fxml"));
                AnchorPane root = loader.load(); // Change root type if needed


                // Pass data to the next controller
                UpdateCaseStatusController controller = loader.getController();
                if (controller != null) {
                    controller.receiveMessage(cnic,true);
                } else {
                    System.err.println("Controller is null!");
                }

                // Set the new scene
                Stage stage = (Stage) updateCaseStatusButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace(); // Debug the exception
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the page.\n" + e.getMessage());
            }
        }

}
