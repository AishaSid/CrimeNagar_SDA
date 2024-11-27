package com.example.demo1.UiLayer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class ReportCrimeController {

    public ComboBox<String>crimeLocationComboBox;
    @FXML private ComboBox<String> crimeTypeComboBox;
    @FXML private ComboBox<String> policeStationComboBox;
    @FXML private TextField crimeDescription;
    @FXML private TextField crimeLocation;
    @FXML private DatePicker crimeTime;
    @FXML private Button submitReportButton;
    @FXML private Button backButton;
    @FXML private ImageView evidenceImageView;

    private Image evidenceImage; // Changed to Image
    private String cnic; // User's CNIC for association.
    private BLConnector blConnector = new BLConnector(); // Create instance of BLConnector

    // Handle back button click (navigate to Dashboard).
    @FXML
    private void goBackToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
            StackPane dashboardRoot = loader.load();

            DashboardController dashboardController = loader.getController();
            dashboardController.receiveMessage(cnic);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(dashboardRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load the Dashboard page.");
        }
    }

    // Insert crime evidence image.
    @FXML
    private void insertCrimeEvidence(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            evidenceImage = new Image(selectedFile.toURI().toString());
            evidenceImageView.setImage(evidenceImage); // Set the image in the ImageView
        } else {
            showAlert(Alert.AlertType.WARNING, "No File Selected", "Please select an image to upload.");
        }
    }

    // Submit crime report.
    @FXML
    private void handleSubmitReport(ActionEvent event) {
        if (validateInputs()) {
            // Fetch the data from the form
            String crimeType = crimeTypeComboBox.getValue();
            String description = crimeDescription.getText();
            String location = crimeLocationComboBox.getValue();
            LocalDate time = crimeTime.getValue();
            String policeStation = policeStationComboBox.getValue(); // Capture the selected police station
            String reporterCnic = cnic; // Assuming 'cnic' is the variable holding the user's CNIC
            String timeOfIncident = time.toString(); // Convert LocalDate to String (you can format it if needed)
            Image evidenceImage = this.evidenceImage; // Assuming the evidence image is already set

            // Use the BLConnector to report the crime (this will also store the case)
            blConnector.reportCrime(reporterCnic, crimeType, description, location, time, evidenceImage, policeStation);

            // Display success message
            showAlert(Alert.AlertType.INFORMATION, "Report Submitted", "Your crime report has been submitted successfully.");
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all the required fields.");
        }
    }


    // Validate form inputs.
    private boolean validateInputs() {
        return crimeTypeComboBox.getValue() != null &&
                !crimeDescription.getText().isEmpty() &&
                crimeLocationComboBox.getValue() != null &&
                crimeTime.getValue() != null  &&
                policeStationComboBox.getValue() != null && // Validate police station
                evidenceImage != null; // Ensure an image is selected
    }

    // Clear form fields.
    private void clearFields() {
        crimeTypeComboBox.setValue(null);
        crimeDescription.clear();
        crimeLocationComboBox.setValue(null);
        crimeTime.setValue(null);
        policeStationComboBox.setValue(null); // Clear the police station combo box
        evidenceImageView.setImage(null);
        evidenceImage = null;
    }

    // Show alerts.
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void receiveMessage(String cnic) {
        this.cnic = cnic;
    }
}
