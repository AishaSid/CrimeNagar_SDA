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

    @FXML
    private Label actionMessageLabel;

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
        filterComboBox.setPromptText("Crime Type");
        // Fetch officer's department
        String department = blConnector.fetchGenericColumn(cnic, "department");
        // Fetch selected crime type from ComboBox
        String selectedCrimeType = filterComboBox.getValue();

        if (selectedCrimeType == null || selectedCrimeType.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error", "Please select a crime type.");
            return;
        }

        // Get pending reports
        ObservableList<String> pendingReports = FXCollections.observableArrayList(
                blConnector.getPendingReports(department, selectedCrimeType)
        );

        if (pendingReports.isEmpty()) {
            // If no reports, show "No reports found" in the ListView
            pendingReports.add("No reports found for the selected crime type.");
        }

        // Update the ListView with the pending reports or placeholder message
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
        String selectedOfficerCnic = officerComboBox.getSelectionModel().getSelectedItem();
        String selectedCrimeType = filterComboBox.getValue();

        if (selectedReport != null && selectedOfficerCnic != null && selectedCrimeType != null) {
            String caseId = extractCaseId(selectedReport);

            if (caseId != null) {
                boolean success;
                if ("Missing Person".equalsIgnoreCase(selectedCrimeType)) {
                    success = blConnector.updateMissingPersonStatus(caseId, newStatus) &&
                            blConnector.assignMissingPersonCase(selectedOfficerCnic, caseId);
                } else {
                    success = blConnector.updateReportStatus(caseId, newStatus) &&
                            blConnector.assignCaseReport(selectedOfficerCnic, caseId);
                }

                if (success) {
                    allReports.remove(selectedReport);
                    reportListView.setItems(allReports);
                    // Update the label instead of showing an alert
                    actionMessageLabel.setText("Report " + action.toLowerCase() + ": " + selectedReport);
                }  else {
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
