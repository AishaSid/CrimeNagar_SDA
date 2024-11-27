package com.example.demo1.UiLayer;

import com.example.demo1.BusinessLogic.Reports.MissingPerson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.scene.layout.VBox;
import java.io.IOException;

public class ShowMPController
{
    BLConnector b = new BLConnector();

    @FXML
    private TextField age;

    @FXML
    private Button backButton;

    @FXML
    private VBox contentVBox;

    @FXML
    private TextField fullname;

    @FXML
    private ImageView imageView;
    @FXML
    private ComboBox<String> lastlocation;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField relation;

    @FXML
    private Label showerror;

    @FXML
    private TextField status;

    @FXML
    private Label titleLabel;

    @FXML
    private TextField whenField;

    // Method for back button click
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

    // Method to receive data from the previous page
    public void receiveMessage(String cnic, int id ,String type)
    {
        this.cnic = cnic;
        this.id = id;
        this.typeeee = type;
        this.showData(id);
    }

    // Method to display data for the Missing Person report
    private void showData(int id) {
        // Retrieve MissingPerson data using BLConnector
        MissingPerson m = b.showMPDetails(id);

        if (m != null) {
            // Populate UI fields with data from the MissingPerson object
            fullname.setText(m.getMissingPersonName());
            age.setText(m.getAge()); // Assuming age is stored as a String
            lastlocation.setValue(m.getLocation());
            phoneField.setText(m.getReporterContact());
            relation.setText(m.getReporterRelation());
            whenField.setText(m.getDescription()); // Assuming this field is used for 'time_of_missing'
            status.setText(m.getStatus()); // Example: If status is not in MissingPerson class, set default

            // Load image into ImageView if available
            if (m.getUserImage() != null) {
                imageView.setImage(m.getUserImage());
            } else {
                imageView.setImage(null); // Set a placeholder or clear image if null
            }

            // Check user type and adapt UI accordingly
            if ("officer".equalsIgnoreCase(typeeee)) {
                // Enable editing for officers
                status.setEditable(true);  // Allow officers to edit status
                lastlocation.setEditable(true);
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
                // Disable editing for citizens
                status.setEditable(false);
            }
        } else {
            // Display error message if no data is found
            showerror.setText("No data found for the given ID.");
            showerror.setVisible(true);
        }
    }
    private void handleOfficerSaveChanges(MissingPerson originalPerson) {
        // Get the updated status and location from the fields
        String updatedStatus = status.getText();
        String updatedLocation = lastlocation.getValue(); // Get the selected value from the ComboBox

        // Check if the updated status is either "open" or "closed"
        if (!updatedStatus.equalsIgnoreCase("open") && !updatedStatus.equalsIgnoreCase("closed")) {
            // Use showAlert to show an error message if the status is invalid
            showAlert(Alert.AlertType.ERROR, "Invalid Status", "Status must be 'open' or 'closed'.");
            return;  // Exit the method early to prevent further processing
        }

        // Create a new MissingPerson object with the updated status and location
        MissingPerson updatedPerson = new MissingPerson(
                updatedLocation, // Use the updated location
                originalPerson.getUserImage(), // Keep the original image
                originalPerson.getAge(), // Keep the original age
                originalPerson.getMissingPersonName(), // Keep the original missing person name
                originalPerson.getDescription(), // Keep the original description
                originalPerson.getReporterContact(), // Keep the original contact
                originalPerson.getReporterRelation(), // Keep the original relation
                originalPerson.getReporterCnic() // Keep the original CNIC
        );

        // Set the updated status
        updatedPerson.setStatus(updatedStatus);

        // Call the backend method to save or process the updated case
        boolean success = b.updateMissingPersonDetails(id, updatedPerson, originalPerson);

        // Show success or failure message using showAlert
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
