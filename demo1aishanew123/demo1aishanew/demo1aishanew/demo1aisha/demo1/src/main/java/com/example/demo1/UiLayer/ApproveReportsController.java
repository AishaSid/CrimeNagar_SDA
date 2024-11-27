package com.example.demo1.UiLayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ApproveReportsController {

    public Button backButton;
    public Button reportCrimeButton;
    private String cnic;
    private BLConnector blConnector = new BLConnector();

    @FXML private ListView<String> reportListView;
    @FXML private ComboBox<String> filterComboBox;
    @FXML private VBox detailsVBox;
    @FXML private Label reportDetails;
    @FXML private Button approveButton;
    @FXML private Button rejectButton;
    @FXML private Button seeDetailsButton;  // Added button for See Details

    private ObservableList<String> allReports = FXCollections.observableArrayList();

    @FXML private ComboBox<String> officerComboBox; // ComboBox for officer names

    @FXML
    public void initialize() {
        // Populate crime types dropdown
        filterComboBox.setItems(FXCollections.observableArrayList(
                "Missing Person", "Theft", "Assault", "Vandalism", "Burglary", "Fraud", "Robbery", "Kidnapping",
                "Murder", "Cybercrime", "Drug Possession", "Arson", "Domestic Violence", "Homicide", "Smuggling",
                "Vehicle Theft", "Public Disturbance", "Pickpocketing", "Road Accident", "Trespassing", "Stalking",
                "Harassment", "Carjacking", "Extortion", "Forgery", "Terrorism", "Illegal Weapons", "Hate Speech", "Other"
        ));

        filterComboBox.getSelectionModel().select(0); // Select first item by default

        // Event listener for report selection
        reportListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showReportDetails(newValue);
            }
        });

        // Event listener for ComboBox selection change
        filterComboBox.setOnAction(event -> initializeReportViewList());

        // Populate officer ComboBox

    }

    private void populateOfficerDropdown() {
        // Fetch department of the logged-in officer
        String department = blConnector.fetchGenericColumn(cnic, "department");

        System.out.println(cnic+"meowwwwwwwwwww");
        System.out.println(department);
        // Fetch officer names from the same department
        ObservableList<String> officers = FXCollections.observableArrayList(

                blConnector.getOfficersByDepartment(department)
        );

        // Populate the officer ComboBox
        officerComboBox.setItems(officers);
        if (!officers.isEmpty()) {
            officerComboBox.getSelectionModel().select(0); // Select first officer by default
        }
    }


    @FXML
    public void initializeReportViewList() {
        String department = blConnector.fetchGenericColumn(cnic, "department"); // Fetch officer's department
        String selectedCrimeType = filterComboBox.getValue();

        if (selectedCrimeType == null || selectedCrimeType.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error", "Please select a crime type.");
            return;
        }

        ObservableList<String> pendingReports = FXCollections.observableArrayList(
                blConnector.getPendingReports(department, selectedCrimeType)
        );

        if (pendingReports.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Reports", "No reports found for the selected crime type.");
        }

        allReports.setAll(pendingReports);
        reportListView.setItems(pendingReports);
    }

    private void showReportDetails(String report) {
        detailsVBox.setVisible(true);
        reportDetails.setText("Details of Report: " + report);
    }

    @FXML
    private void onApproveReport(ActionEvent event) {
        handleReportAction("Approved", "Open");
    }

    @FXML
    private void onRejectReport(ActionEvent event) {
        handleReportAction("Rejected", "Rejected");
    }

    private void handleReportAction(String action, String newStatus) {
        String selectedReport = reportListView.getSelectionModel().getSelectedItem();
        String selectedOfficerCnic = officerComboBox.getSelectionModel().getSelectedItem(); // Directly fetch CNIC
        String selectedCrimeType = filterComboBox.getValue(); // Crime type selected by user

        if (selectedReport != null && selectedOfficerCnic != null && selectedCrimeType != null) {
            // Extract the report ID
            String caseId = extractCaseId(selectedReport);

            if (caseId != null && selectedOfficerCnic != null) {
                boolean success;
                if ("Missing Person".equalsIgnoreCase(selectedCrimeType)) {
                    // Update Missing Person status and assign the case
                    success = blConnector.updateMissingPersonStatus(caseId, newStatus) &&
                            blConnector.assignMissingPersonCase(selectedOfficerCnic, caseId);
                } else {
                    // Update Case Report status and assign the case
                    success = blConnector.updateReportStatus(caseId, newStatus) &&
                            blConnector.assignCaseReport(selectedOfficerCnic, caseId);
                }

                if (success) {
                    allReports.remove(selectedReport);
                    reportListView.setItems(allReports); // Update ListView after removal
                    showAlert(Alert.AlertType.INFORMATION, action, "Report " + action.toLowerCase() + ": " + selectedReport);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update the report or assign the case.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid report or officer selection.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a report and an officer to " + action.toLowerCase() + ".");
        }
    }



    private String extractCaseId(String report) {
        // Assumes report format is "ID: <id>, Description: ... "
        String[] parts = report.split(",");
        if (parts.length > 0) {
            String idPart = parts[0]; // "ID: <id>"
            String[] idSplit = idPart.split(": ");
            if (idSplit.length > 1) {
                return idSplit[1].trim(); // Return the case ID
            }
        }
        return null; // Return null if case ID cannot be extracted
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onBackButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("officer_dashboard.fxml"));
            StackPane root = loader.load();

            officerDashboardController a = loader.getController();
            a.receiveMessage(cnic);

            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the page.");
        }
    }

    public void receiveMessage(String cnic) {
        this.cnic = cnic;

        populateOfficerDropdown();

    }
}
