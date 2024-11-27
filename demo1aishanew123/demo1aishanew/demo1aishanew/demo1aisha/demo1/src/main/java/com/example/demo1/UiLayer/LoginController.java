package com.example.demo1.UiLayer;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    BLConnector BLController = new BLConnector();
    @FXML
    private VBox contentVBox;

     @FXML
      private TextField type12;
    @FXML
    private Label titleLabel;

    @FXML
    private TextField cnicField;

    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> dept;

    @FXML
    private Button submitButton;

    @FXML
    private Label switchLabel;

    @FXML
    private Hyperlink switchLink;

    private boolean isLoginView = true;

    // Fields for registration form
    private TextField fullNameField;
    private TextField phoneNumberField;
    private TextField emailField;
    private Stage stage;
    private Scene scene;

    @FXML
    private Button backbtn;

    @FXML
    public void handleBackButton() throws IOException {
        navigateToWelcome();
    }

    private void navigateToWelcome() throws IOException {
        // Load the Welcome.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("welcome.fxml"));

        // Set up the stage and scene for the welcome page
        stage = (Stage) backbtn.getScene().getWindow();
        scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private Label parameterLabel;
    String p ="Citizen";
    public void receiveParameter(String parameter) {
        p = parameter;
        parameterLabel.setText(parameter); // Update the UI with the parameter
        parameterLabel.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-weight: bold; -fx-font-size: 33;");

    }
    @FXML
    private void initialize() {
        setupLoginView();
    }

    @FXML
    private void switchToRegister() {
        if (isLoginView) {
            applyFadeTransition(this::setupRegisterView);
        } else {
            applyFadeTransition(this::setupLoginView);
        }
    }
    private void setupLoginView() {
        isLoginView = true;

        contentVBox.getChildren().clear();
        titleLabel.setText("Log in");
        titleLabel.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-weight: bold; -fx-font-size: 25;");

        contentVBox.getChildren().addAll(
                parameterLabel,
                titleLabel,
                cnicField = createTextField("CNIC", "Enter your CNIC"),
                passwordField = createPasswordField("Password", "Enter your password"),
                createSubmitButton("Log in", this::validateLoginForm),
                createSwitchLabel("Don't have an account?", "Register here", this::switchToRegister)
        );
    }
    private void setupRegisterView() {
        isLoginView = false;

        contentVBox.getChildren().clear();
        titleLabel.setText("Register");
        if(Objects.equals(p, "Citizen")) {
            contentVBox.getChildren().addAll(
                    parameterLabel,
                    titleLabel,
                    fullNameField = createTextField("Full Name", "Enter your full name"),
                    cnicField = createTextField("CNIC", "Enter your CNIC"),
                    phoneNumberField = createTextField("Phone Number", "Enter your phone number"),

                    passwordField = createPasswordField("Password", "Enter your password"),
                    createSubmitButton("Register", this::validateRegisterForm),
                    createSwitchLabel("Already have an account?", "Log in here", this::switchToRegister)
            );

        }
        else //officer
        {

            contentVBox.getChildren().addAll(
                    parameterLabel,
                    titleLabel,
                    fullNameField = createTextField("Full Name", "Enter your full name"),
                    createHBox(
                            cnicField = createTextField("CNIC", "Enter your CNIC"),
                                type12 = createTextField("type12", "Analyst/normal")
                    ),
                    dept = createComboBox("Department", "Select your department"),
                    phoneNumberField = createTextField("Phone Number", "Enter your phone number"),
                    passwordField = createPasswordField("Password", "Enter your password"),
                    createSubmitButton("Register", this::validateRegisterForm),
                    createSwitchLabel("Already have an account?", "Log in here", this::switchToRegister)
            );
        }
    }
    private TextField createTextField(String id, String promptText) {
        TextField textField = new TextField();
        textField.setId(id);
        textField.setPromptText(promptText);
        textField.setPrefWidth(300.0);
        textField.setStyle("-fx-background-color: #f9f9f9; -fx-background-radius: 10; -fx-padding: 10; -fx-border-color: #d1d9e6; -fx-border-radius: 10;");
        return textField;
    }
    private HBox createHBox(TextField... fields) {
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(fields);

        return hBox;
    }

    private PasswordField createPasswordField(String id, String promptText) {
        PasswordField passwordField = new PasswordField();
        passwordField.setId(id);
        passwordField.setPromptText(promptText);
        passwordField.setPrefWidth(300.0);
        passwordField.setStyle("-fx-background-color: #f9f9f9; -fx-background-radius: 10; -fx-padding: 10; -fx-border-color: #d1d9e6; -fx-border-radius: 10;");
        return passwordField;
    }
    private Button createSubmitButton(String text, Runnable onSubmit) {
        Button button = new Button(text);
        button.setPrefWidth(200.0);
        button.setStyle("-fx-background-color: linear-gradient(to right, #6fb1fc, #1a73e8); -fx-background-radius: 15; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        button.setOnAction(e -> onSubmit.run());
        return button;
    }
    private VBox createSwitchLabel(String labelText, String linkText, Runnable onAction) {
        Label label = new Label(labelText);
        label.setStyle("-fx-text-fill: #4A4A4A;");

        Hyperlink hyperlink = new Hyperlink(linkText);
        hyperlink.setStyle("-fx-text-fill: #1a73e8;");
        hyperlink.setOnAction(e -> onAction.run());

        VBox container = new VBox(5, label, hyperlink);
        container.setStyle("-fx-alignment: center;");
        return container;
    }
    private void applyFadeTransition(Runnable onFinished) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), contentVBox);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> {
            onFinished.run();

            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), contentVBox);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }
    private void validateLoginForm() {
        String cnic = cnicField.getText();
        String password = passwordField.getText();

        if (cnic.isEmpty() || !cnic.matches("\\d{13}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid CNIC", "Please enter a valid 13-digit CNIC.");
            return;
        }

        if (password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Password", "Password cannot be empty.");
            return;
        }
        //login the person
        int a;
        if (Objects.equals(p, "Citizen"))
        {

            a = BLController.LoginCitizen( password, cnic);
        } else { // For officers
            a = BLController.LoginOfficer(password, cnic);
        }
        if (a == 1)
        {
            // If login is successful, proceed to open the dashboard
            showDashboardPage();
        }
        else
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect Data Provided.");
    }

    private void showDashboardPage() {
        try {
            FXMLLoader loader;
            StackPane dashboardRoot;

            if (Objects.equals(p, "Citizen")) {
                loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
                dashboardRoot = loader.load(); // Load the FXML first
                DashboardController a = loader.getController(); // Now get the controller
                a.receiveMessage(cnicField.getText());
            } else {
                loader = new FXMLLoader(getClass().getResource("officer_dashboard.fxml"));
                dashboardRoot = loader.load(); // Load the FXML first
                officerDashboardController a = loader.getController(); // Now get the controller
                a.receiveMessage(cnicField.getText());
            }

            // Set the scene for the main window (stage)
            Scene scene = new Scene(dashboardRoot, 400, 600);
            Stage stage = (Stage) cnicField.getScene().getWindow(); // Get current window (login window)
            stage.setScene(scene); // Set the scene to the dashboard page
            stage.setTitle("Civilian Dashboard"); // Set the title of the window
            stage.show(); // Show the dashboard

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception properly
        }
    }

    private void validateRegisterForm() {
        String fullName = fullNameField.getText();
        String cnic = cnicField.getText();
        String phoneNumber = phoneNumberField.getText();
        String password = passwordField.getText();
        String area = "IslamAbad";


        if (fullName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Name", "Full name cannot be empty.");
            return;
        }

        if (cnic.isEmpty() || !cnic.matches("\\d{13}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid CNIC", "Please enter a valid 13-digit CNIC.");
            return;
        }

        if (phoneNumber.isEmpty() || !phoneNumber.matches("\\d{11}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Phone Number", "Please enter a valid 11-digit phone number.");
            return;
        }
        if (password.length() < 6) {
            showAlert(Alert.AlertType.ERROR, "Invalid Password", "Password must be at least 6 characters long.");
            return;
        }



        int a;
        if (Objects.equals(p, "Citizen"))
        {
            a =  BLController.RegisterCitizen(area, fullName, password, phoneNumber, cnic);
        }
        else
        {
            // For officers
            // Get the selected department from the ComboBox
            String department = dept.getValue();

            if (department == null || department.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "No Dept", "Dept must be selected.");
            }
            String type1 = type12.getText();
            if (type1 == null || type1.isEmpty())
            {
                showAlert(Alert.AlertType.ERROR, "No Type defined", "Officer type must be selected.");
            }

            a =  BLController.RegisterOfficer(area, fullName, password, phoneNumber, cnic, department,type1);
        }

        if(a == 1)
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "You have registered successfully!");
        else if ( a== 0)
        {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "No such CNIC found.");
        }
        else
        {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "This CNIC has already registered.");

        }
    }
    private ComboBox<String> createComboBox(String id, String promptText) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setId(id);
        comboBox.setPromptText(promptText);
        comboBox.setPrefWidth(300.0);
        comboBox.setStyle("-fx-background-color: #f9f9f9; -fx-background-radius: 10; -fx-padding: 10; -fx-border-color: #d1d9e6; -fx-border-radius: 10;");

        // Adding 6 dummy police department names (stations in Islamabad)
        comboBox.getItems().addAll(
                "F-6 Police Station",
                "F-7 Police Station",
                "G-9 Police Station",
                "I-9 Police Station",
                "Blue Area Police Station",
                "Shahzad Town Police Station"
        );

        return comboBox;
    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
