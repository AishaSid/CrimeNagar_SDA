package com.example.demo1.UiLayer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class DashboardController {

    public Button viewReportHistory;
    public Button ReportMissingPerson;
    public Button reportCrimeButton;
    @FXML
    private VBox mainContentArea;  // Main content area for dynamic page content

    // Buttons for different actions
    @FXML private Button viewCrimeRates;
    @FXML private Button feedbackButton;
    @FXML private Button logoutButton;

    String cnic;
    private BLConnector blConnector = new BLConnector();

    public void receiveMessage(String cnic) {
        this.cnic = cnic;
    }

    @FXML
    private void onViewReportHistoryClicked() {
        try {
            // Load the Login FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("viewHistory.fxml"));
            AnchorPane root = loader.load(); // Change to AnchorPane

            historyController a = loader.getController();
            a.receiveMessage(cnic);
            // Create a new scene with the login page and set it on the stage
            Stage stage = (Stage) viewReportHistory.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the pageRH.");
        }
    }

    @FXML
    private void onviewCrimRatesClicked() {
        try {
            // Load the Login FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("viewCrimeRates.fxml"));
            StackPane root = loader.load(); // Change to AnchorPane

            CRController a = loader.getController();
            a.receiveMessage(cnic);
            // Create a new scene with the login page and set it on the stage
            Stage stage = (Stage) viewCrimeRates.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the pageCR.");
        }
    }

    @FXML
    private void onReportMissingPersonClicked() {
        try {
            // Load the Missing Person FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MissingPerson.fxml"));
            StackPane root = loader.load();

            MissingController a = loader.getController();
            a.receiveMessage(cnic);
            // Get the stage from the event source
            Stage stage = (Stage) ReportMissingPerson.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the pageMP.");
        }
    }

    @FXML
    private void onReportCrimeClicked() {
        try {
            // Load the Login FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("reportCrime.fxml"));
            StackPane root = loader.load();

            ReportCrimeController a = loader.getController();
            a.receiveMessage(cnic);
            // Create a new scene with the login page and set it on the stage
            Stage stage = (Stage) reportCrimeButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the pageRC.");
        }
    }

    @FXML
    private void openFeedbackPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("feedback.fxml"));
            StackPane feedbackRoot = loader.load();

            // Pass the CNIC to the feedback page, handled in the same controller
            DashboardController feedbackPageController = loader.getController();
            feedbackPageController.receiveMessage(cnic); // Pass CNIC here if FeedbackPageController exists

            // Set the scene for the Feedback page
            Scene scene = new Scene(feedbackRoot, 400, 600);  // Set the desired size for the page
            Stage stage = (Stage) feedbackButton.getScene().getWindow(); // Get current window (dashboard window)
            stage.setScene(scene); // Set the scene to the Feedback page
            stage.setTitle("Feedback"); // Set the title of the window
            stage.show(); // Show the feedback page
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception properly
        }
    }

    @FXML
    private Button submitFeedbackButton;

    @FXML
    private TextArea feedbackTextArea; // Example input area for feedback

    // Submit feedback to BLController
    @FXML
    private void submitFeedback(ActionEvent event) {
        String feedback = feedbackTextArea.getText();

        if (feedback == null || feedback.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Feedback cannot be empty.");
        } else {
            // Call the BLController to handle the feedback submission
            if (blConnector != null) {
                // Submit feedback along with CNIC
                System.out.println(this.cnic);
                blConnector.submitFeedback(this.cnic, feedback);  // Send feedback and CNIC to BLController
                showAlert(Alert.AlertType.INFORMATION, "Feedback", "Thank you for your feedback.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "BLController not initialized.");
            }
        }
    }


    @FXML
    private void goBackToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
            StackPane dashboardRoot = loader.load();

            DashboardController a = loader.getController();
            a.receiveMessage(cnic);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(dashboardRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the Dashboard page.");
        }
    }

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
}