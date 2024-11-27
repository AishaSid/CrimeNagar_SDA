package com.example.demo1.UiLayer;

import com.example.demo1.BusinessLogic.Reports.Case;
import com.example.demo1.BusinessLogic.Reports.MissingPerson;
import com.example.demo1.BusinessLogic.UiConnector;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BLConnector {

  public UiConnector connector;

    private static BLConnector instance;

    // Private constructor to prevent instantiation from outside the class
    public BLConnector() {
        connector = UiConnector.getInstance();  // This is correct
    }

    // Public static method to get the instance of the class
    public static BLConnector getInstance() {
        // If the instance is null, create a new instance
        if (instance == null) {
            instance = new BLConnector();
        }
        // Return the existing instance
        return instance;
    }





  // 1. REGISTER
  public int RegisterCitizen(String area, String name, String password, String phone, String cnic) {
    return connector.registerCitizen(area, name, password, phone, cnic);
  }

  public int RegisterOfficer(
          String area, String name, String password, String phone, String cnic, String department, String type1) {
    return connector.registerOfficer(area, name, password, phone, cnic, department, type1);
  }

  // 2. LOGIN
  public int LoginCitizen(String password, String cnic) {

    int a = connector.loginCitizen(password, cnic);
    return a;
  }

  public int LoginOfficer(String password, String cnic) {
    return connector.loginOfficer(password, cnic);
  }

  // 3. MISSING PERSON
  public void MissingPerson(
      String location,
      Image userImage,
      String name,
      String age,
      String timeOfMissing,
      String reporterContact,
      String reporterRelation,
      String cnic) {
    connector.MissingPerson(
        location, userImage, name, age, timeOfMissing, reporterContact, reporterRelation, cnic);
  }

  private List<Case> caseList = new ArrayList<>();

  public void reportCrime(
      String cnic,
      String crimeType,
      String description,
      String location,
      LocalDate time,
      Image evidenceFile,
      String policeStation) {

    // Convert LocalDate to String for timeOfIncident
    String timeOfIncident = time.toString();

    // Create a new Case object
    Case newCase =
        new Case(
            location, evidenceFile, crimeType, policeStation, timeOfIncident, description, cnic);

    // Add the Case to the case list
    caseList.add(newCase);

    // Optionally, print the case details for debugging
    System.out.println(
        "Case reported: " + newCase); // Pass the data to UiConnector to create a Case report
    connector.reportCrime(
        cnic, crimeType, description, location, time, evidenceFile, policeStation);
  }

  // Method to get all cases
  public List<Case> getAllCases() {
    return caseList;
  }

  public boolean updateReportStatus(String caseId, String newStatus) {
    return connector.updateCaseStatus(caseId, newStatus);
  }

  public boolean updateMissingPersonStatus(String caseId, String newStatus) {
    return connector.updateCaseStatus(caseId, newStatus, "missingperson");
  }

  public void submitFeedback(String cnic, String feedback) {
    // Forward the feedback data to UIConnector for database handling
    connector.insertFeedback(cnic, feedback);
  }

  public void ConfirmPerson(
      String where, String condition, String reason, Image image, int id, String cnic) {
    connector.ConfirmPerson(where, condition, reason, image, id, cnic);
  }

  public CPController.data showMP(int id) {
    return connector.showMP(id);
  }

  public XYChart.Series<String, Number> fetchBarChartData(String area) {
    // Fetch data from UIConnector
    return connector.getCrimeDataByArea(area);
  }

  public String fetchGenericColumn(String cnic, String columnName) {
    // Create a UIConnector instance

    // Use UIConnector to fetch the data
    String result = connector.getGenericColumnValue(cnic, columnName);

    // Return the result to the calling method
    return result;
  }

  public List<String> getPendingReports(String department, String crimeType) {

    return connector.fetchPendingReports(department, crimeType);
  }

  public List<String> getOfficersByDepartment(String department) {
    return connector.fetchOfficersByDepartment(department);
  }

  public boolean assignMissingPersonCase(String officerCnic, String caseId) {
    return connector.assignCaseToOfficer(officerCnic, caseId, "missingperson_assigned_cases");
  }

  public boolean assignCaseReport(String officerCnic, String caseId) {
    return connector.assignCaseToOfficer(officerCnic, caseId, "casereport_assigned_cases");
  }

  public XYChart.Series<String, Number> viewreportstats() {
    return connector.reportstats();
  }

  public String[][] getReportsByCNIC(String cnic) {
    return connector.getReportsByCNIC(cnic);
  }

  public String[][] getReportsByOfficerCNIC(String cnic,boolean h) {
    return connector.getReportsByOfficerCNIC(cnic,h);
  }

  public MissingPerson showMPDetails(int id) {
    return connector.showMPDetails(id);
  }

  public Case showCRDetails(int id) {
    return connector.showCRDetails(id);
  }
    public boolean updateCaseDetails(int id, Case updatedCase, Case ogCase) {
        Map<String, String> columnValues = new HashMap<>();

        // Compare the original case and updated case for status and description
        if (!updatedCase.getStatus().equals(ogCase.getStatus())) {
            columnValues.put("status", updatedCase.getStatus());
        }

        if (!updatedCase.getDescription().equals(ogCase.getDescription())) {
            columnValues.put("description", updatedCase.getDescription());
        }

        // If no changes found, return true (no need to update)
        if (columnValues.isEmpty()) {
            return true;
        }

        // Call the UIConnector function to update the database
        return connector.updateCaseColumns(id, columnValues, "casereport");
    }

    public boolean updateMissingPersonDetails(int id, MissingPerson updatedCase, MissingPerson ogcase) {
        // Create a map to hold the columns and their new values
        Map<String, String> columnValues = new HashMap<>();

        // Compare the original case and updated case for location and status
        if (!updatedCase.getStatus().equals(ogcase.getStatus())) {
            columnValues.put("status", updatedCase.getStatus());
        }

        if (!updatedCase.getLocation().equals(ogcase.getLocation())) {
            columnValues.put("location", updatedCase.getLocation());
        }

        // If no changes are found, return true (no need to update)
        if (columnValues.isEmpty()) {
            return true;
        }

        // Call the UIConnector function to update the database
        return connector.updateCaseColumns(id, columnValues, "missingperson");
    }

}

