package com.example.demo1.UiLayer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent; // Import the correct ActionEvent class
import java.io.IOException;

public class WelcomeController {
    private Stage stage;
    private Scene scene;

    @FXML
    private Button Citizen;
    @FXML
    private Button PoliceOfficer;
    @FXML
    private ImageView image;

    @FXML
    public void handleCitizenButton(ActionEvent event) throws IOException {
        navigateToRegister(event, "Citizen");
    }

    @FXML
    public void handlePoliceOfficerButton(ActionEvent event) throws IOException {
        navigateToRegister(event, "Officer");
    }

    private void navigateToRegister(ActionEvent event, String userType) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));

        // Load the new FXML file
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene newScene = new Scene(loader.load());

        // Pass the userType parameter to the next controller
        LoginController loginController = loader.getController();
        loginController.receiveParameter(userType);

        // Set the new scene and show it
        currentStage.setScene(newScene);
        currentStage.show();
    }
}
