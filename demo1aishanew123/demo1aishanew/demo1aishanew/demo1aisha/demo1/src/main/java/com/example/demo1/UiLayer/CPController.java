package com.example.demo1.UiLayer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class CPController {
    @FXML
    private TextArea PersonDescription;

    @FXML
    private TextField conditionfoundField;

    @FXML
    private Label fullname;

    @FXML
    private ImageView imageView;

    @FXML
    private ImageView imageView1;

    @FXML
    private TextField reasonField;

    @FXML
    private Button submitButton;

    @FXML
    private Label titleLabel;

    @FXML
    private TextField wherefoundField;

    private boolean isImageSelected = false; // Track if an image has been selected

    @FXML private Button backButton;
    @FXML
    private void onBackButtonClicked(ActionEvent event) {
        try {
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
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the page.");
        }
    }

    @FXML
    private void insertImage() {
        if (imageView == null || imageView.getScene() == null) {
            System.out.println("Image view is not initialized or not part of the scene.");
            return;
        }

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
                if (image.isError()) {
                    System.out.println("Unable to load the selected image.");
                    isImageSelected = false;
                } else {
                    imageView.setImage(image);
                    isImageSelected = true; // Mark image as selected
                }
            } catch (Exception e) {
                System.out.println("Unable to load the selected image.");
                isImageSelected = false;
            }
        } else {
            System.out.println("No image file selected.");
            isImageSelected = false;
        }
    }

    @FXML
    private void handleSubmitButtonAction() {
        // Validate inputs before showing the confirmation alert
        if (!isImageSelected) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select an image before submitting.");
            return;
        }
        if (conditionfoundField.getText() == null || conditionfoundField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill all the fields.");
            return;
        }
        if (reasonField.getText() == null || reasonField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill all the fields.");
            return;
        }
        if (wherefoundField.getText() == null || wherefoundField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill all the fields.");
            return;
        }

        // Create a confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Missing Person");
        alert.setHeaderText("Do you want to close the case?");
        alert.setContentText("This action cannot be undone.");

        // Customize the buttons
        ButtonType confirmButton = new ButtonType("Confirm");
        ButtonType cancelButton = new ButtonType("Cancel");

        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        // Show the alert and wait for the user's response
        Optional<ButtonType> result = alert.showAndWait();

        // Handle the user's choice
        if (result.isPresent() && result.get() == confirmButton)
        {
            System.out.println("Case closed successfully!");
            // Perform case closure logic here
            this.sendData();
        } else {
            System.out.println("Confirmation canceled.");
        }
    }

    private void sendData()
    {
      String where = wherefoundField.getText();
      String condition = conditionfoundField.getText();
      String reason = reasonField.getText();
      Image image = imageView.getImage();

      BLConnector B = new BLConnector();
      B.ConfirmPerson(where, condition, reason, image, id, cnic);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    String cnic;
    int id = 1 ;
    public void receiveMessage(String cnic)
    {
        this.cnic = cnic;
        BLConnector c = new BLConnector();
        showData(c.showMP(id));

    }
  public static class data
  {
      public String name; public Image img; public String reportedBy;
  }
    public void showData(data data) {
        fullname.setText(data.name);

        try {
            if (data.img != null && !data.img.isError()) {
                imageView1.setImage(data.img); // Display the image if valid
            } else {
                System.out.println("No valid image found. Clearing ImageView.");
                imageView1.setImage(null); // Clear the ImageView if the image is null or invalid
            }
        } catch (Exception e) {
            System.out.println("Error displaying image: " + e.getMessage());
            imageView1.setImage(null); // Clear the ImageView in case of any exceptions
        }

        PersonDescription.setText(data.reportedBy != null ? data.reportedBy : "No description available."); // Default if null
    }

}
