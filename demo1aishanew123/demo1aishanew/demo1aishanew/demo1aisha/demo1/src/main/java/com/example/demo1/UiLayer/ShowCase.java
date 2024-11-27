package com.example.demo1.UiLayer;

import com.example.demo1.BusinessLogic.Reports.Case;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;


public class ShowCase
{
    Case m;
    BLConnector b = new BLConnector();
     @FXML
    private VBox contentVBox;

     @FXML
     private Button backButton;
    @FXML
    private TextField department;

    @FXML
    private TextField description;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField loc1232;

    @FXML
    private TextField officer;

    @FXML
    private Label showerror;

    @FXML
    private TextField status;

    @FXML
    private Label type;



        @FXML
        private void onBackButtonClicked(ActionEvent event) throws IOException
        {

            if ("citizen".equalsIgnoreCase(typeeee)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("viewHistory.fxml"));
                AnchorPane root = loader.load();

                historyController a = loader.getController();
                a.receiveMessage(cnic);
                Stage stage = (Stage) backButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("updateStatus.fxml")); // Adjust this to your previous page FXML
                AnchorPane root = loader.load();

                UpdateCaseStatusController a = loader.getController();
                a.receiveMessage(cnic);
                Stage stage = (Stage) backButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }

        }

        String cnic;
        String typeeee;
        int id;
        public void receiveMessage(String cnic, int id ,String type)
        {
            this.cnic = cnic;
            this.id = id;
            this.typeeee = type;
            this.showData(id);
        }

    private void showData(int id) {
        // Get the Case object using the BLConnector
        m = b.showCRDetails(id);

        // Check if the Case object is not null (valid data exists)
        if (m != null) {
            // Populate the UI fields with data from the Case object
            loc1232.setText(m.getLocation());
            department.setText(m.getDepartment());
            description.setText(m.getDescription());
            status.setText(m.getStatus());
            type.setText(m.getType());
            officer.setText(cnic);

            // Display the image if it exists
            if (m.getUserImage() != null) {
                imageView.setImage(m.getUserImage());
            } else {
                imageView.setImage(null);
            }

            // Check the type of the user
            if ("officer".equalsIgnoreCase(typeeee)) {
                // Enable all text fields for editing
                loc1232.setEditable(false);
                department.setEditable(false);
                description.setEditable(true);
                status.setEditable(true);
                officer.setEditable(false);

                // Add a button or functionality to save updates
                Button saveButton = new Button("Save Changes");

// Apply the same styling as the provided button
                saveButton.setStyle("-fx-background-color: linear-gradient(to right, #6fb1fc, #1a73e8); " +
                        "-fx-background-radius: 15; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold;");

// Set the preferred width
                saveButton.setPrefWidth(200.0);
                saveButton.setOnAction(event -> handleOfficerSaveChanges(m));
                contentVBox.getChildren().add(saveButton);
            } else {
                // Disable text fields for non-officer types
                loc1232.setEditable(false);
                department.setEditable(false);
                description.setEditable(false);
                status.setEditable(false);
            }
        } else {
            // If no Case object is found, show an error message
            showerror.setText("No data found for the given ID.");
        }
    }

    private void handleOfficerSaveChanges(Case originalCase) {
        // Collect updated data from the fields
        String updatedDescription = description.getText();
        String updatedStatus = status.getText();

        // Check if the updated status is either "Open" or "Closed"
        if (!updatedStatus.equalsIgnoreCase("Open") && !updatedStatus.equalsIgnoreCase("Closed")) {
            // Use showAlert to show an error message if the status is invalid
            showAlert(Alert.AlertType.ERROR, "Invalid Status", "Status must be 'Open' or 'Closed'.");
            return;  // Exit the method early to prevent further processing
        }

        // Create a new Case object with updated values
        Case updatedCase = new Case(
                originalCase.getLocation(),
                originalCase.getUserImage(), // Keep the original image
                originalCase.getCrimeType(), // Keep the original type
                originalCase.getDepartment(),
                originalCase.getTimeOfIncident(), // Keep the original time of incident
                updatedDescription,
                originalCase.getReporterCnic() // Keep the original reporter's CNIC
        );

        // Update the status separately
        updatedCase.setStatus(updatedStatus);

        // Call a backend method to save or process the updated case
        System.out.println("waiting...");
        boolean success = b.updateCaseDetails(id, updatedCase, m);

        // Show a success or failure message using showAlert
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Case updated successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Error updating case. Please try again.");
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


