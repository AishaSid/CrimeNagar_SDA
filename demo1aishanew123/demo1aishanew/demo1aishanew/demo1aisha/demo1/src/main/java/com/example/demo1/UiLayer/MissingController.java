package com.example.demo1.UiLayer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class MissingController {
    BLConnector BLController = new BLConnector();
    String cnic;
    public Button backButton;
    @FXML
    private ImageView imageView;
    @FXML
    private ComboBox<String> relationComboBox;
    @FXML
    private TextField age;
    @FXML
    private TextField fullname;
    @FXML
    private TextField lastlocation;
    @FXML
    private TextField phoneField;
    @FXML
    private Button submitButton;
    @FXML
    private Label titleLabel;
    @FXML
    private TextField whenField;
    @FXML
    private Label showerror; // Label to display error messages
    @FXML
    public void onBackButtonClicked() {
        try {
            // Load the Login FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
            StackPane root = loader.load(); // Change to AnchorPane

            DashboardController a = loader.getController();
            a.receiveMessage(cnic);
            // Create a new scene with the login page and set it on the stage
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the page.");
        }
    }

    public void receiveMessage(String cnic)
    {
        this.cnic = cnic;
    }

    @FXML
    private void initialize() {
        // Add items to the ComboBox
        relationComboBox.getItems().addAll("Parent", "Sibling", "Family Relative", "Friend", "Neighbour/Other");
    }

    @FXML
    private void insertImage() {
        // Create a FileChooser instance
        FileChooser fileChooser = new FileChooser();

        // Set the title of the dialog
        fileChooser.setTitle("Select an Image");

        // Set extension filters to restrict file selection to image files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Show the open dialog and capture the selected file
        File selectedFile = fileChooser.showOpenDialog(imageView.getScene().getWindow());

        // Check if a file was selected
        if (selectedFile != null) {
            try {
                // Load the image and display it in the ImageView
                Image image = new Image(selectedFile.toURI().toString());
                imageView.setImage(image);
            } catch (Exception e) {
                // Handle image loading errors
                showAlert();
            }
        }
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Unable to load the selected image.");
        alert.showAndWait();
    }

    @FXML
    private void handleSubmitButtonAction() {
        // Validate all fields
        if (!validateInputs()) {
            return; // If validation fails, stop further execution
        }

        // Create a confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Submit?");
        alert.setContentText("Ensure that all data entered is correct.");

        // Customize the buttons
        ButtonType submitButton = new ButtonType("Submit");
        ButtonType cancelButton = new ButtonType("Cancel");

        alert.getButtonTypes().setAll(submitButton, cancelButton);

        // Show the alert and wait for the user's response
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == submitButton) {
            // SUBMIT REPORT

            try {
                // Extract all data from fields
                String name = fullname.getText();
                String ageText = age.getText();
                String location = lastlocation.getText();
                String reporterContact = phoneField.getText();
                String timeOfMissing = whenField.getText();
                String reporterRelation = relationComboBox.getValue();
                Image userImage = imageView.getImage();

                // Create an object of MissingPerson class
                BLController.MissingPerson(
                        location, userImage, name, ageText,
                        timeOfMissing, reporterContact, reporterRelation, cnic
                );


                // Success message
                showerror.setText("Report submitted successfully!");
                showerror.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            } catch (Exception e) {
                showerror.setText("Failed to submit the report. Please try again.");
                showerror.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                e.printStackTrace(); // Log the exception for debugging
            }
        } else {
            System.out.println("Submission canceled.");
        }
    }

    // Method to validate inputs
    private boolean validateInputs() {
        // Check if the fullname field is empty
        if (fullname.getText() == null || fullname.getText().trim().isEmpty()) {
            showError("Full Name is required.");
            return false;
        }

        // Check if the age field is empty or not a number
        if (age.getText() == null || age.getText().trim().isEmpty() || !age.getText().matches("\\d+")) {
            showError("Valid Age is required (numeric only).");
            return false;
        }

        // Check if the phone field is empty or not a valid phone number
        if (phoneField.getText() == null || phoneField.getText().trim().isEmpty() || !phoneField.getText().matches("\\d{11}")) {
            showError("Valid Phone Number is required (11 digits).");
            return false;
        }

        // Check if the last location field is empty
        if (lastlocation.getText() == null || lastlocation.getText().trim().isEmpty()) {
            showError("Last Location is required.");
            return false;
        }

        // Check if the when field is empty
        if (whenField.getText() == null || whenField.getText().trim().isEmpty()) {
            showError("Time/Date of missing is required.");
            return false;
        }

        // Check if the relationComboBox is empty
        if (relationComboBox.getValue() == null || relationComboBox.getValue().trim().isEmpty()) {
            showError("Relation is required.");
            return false;
        }

        // Check if an image is selected
        if (imageView.getImage() == null) {
            showError("An image is required.");
            return false;
        }

        // If all validations pass
        showerror.setText(""); // Clear any previous error messages
        return true;
    }

    // Method to show error messages in the label
    private void showError(String message) {
        showerror.setText(message); // Display the error message in the label
        showerror.setStyle("-fx-text-fill: red; -fx-font-weight: bold;"); // Optional: Styling the error label
    }

    @FXML
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
