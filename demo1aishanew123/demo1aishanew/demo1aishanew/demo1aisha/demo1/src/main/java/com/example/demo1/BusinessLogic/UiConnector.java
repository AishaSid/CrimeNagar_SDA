package com.example.demo1.BusinessLogic;

import com.example.demo1.BusinessLogic.Actors.Citizen;
import com.example.demo1.BusinessLogic.Actors.Officer;
import com.example.demo1.BusinessLogic.Actors.User;
import com.example.demo1.BusinessLogic.Reports.Case;
import com.example.demo1.BusinessLogic.Reports.ConfirmPerson;
import com.example.demo1.BusinessLogic.Reports.MissingPerson;
import com.example.demo1.BusinessLogic.Reports.Reports;
import com.example.demo1.UiLayer.CPController;
import com.example.demo1.DataBase.db;
import com.example.demo1.UiLayer.BLConnector;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class UiConnector
{
    // Private static variable to hold the single instance of the class
    private static UiConnector instance;

    // Private constructor to prevent instantiation from outside the class
    private UiConnector() {
        // Constructor code (if needed)
    }

    // Public static method to get the instance of the class
    public static UiConnector getInstance() {
        // If the instance is null, create a new instance
        if (instance == null) {
            instance = new UiConnector();
        }
        // Return the existing instance
        return instance;
    }

    BLConnector blConnector = null;
    User registerUser;
    Reports newReport;
    db db = new db();
    ConfirmPerson cp;
    //1. USE CASE 1
    //checks DB -> compare username ->returns bool
    // create -> store in DB
    public int check(String cnic)
    {
        String p = db.LoginC(cnic);
        if(p== null)
        {return 0;}
    return 1;
    }
    public int registerCitizen(String area, String name, String password, String phone, String cnic)  {

        if(!db.Register(cnic))
            return 0;
        String p = db.LoginC(cnic);
        if(p != null)
        {
            return 6;
        }
        registerUser = new Citizen(area, name, password, phone, cnic);
        //store in db
        db.storeCitizen(registerUser);
        return 1;
    }
    public int registerOfficer(String area, String name, String password, String phone, String cnic, String department, String type1)  {
        if(!db.Register(cnic))
            return 0;
        String p = db.LoginP(cnic);
        if(p != null)
        {
            return 6;
        }
        registerUser = new Officer(area, name, password, phone, cnic, department, type1);
        //store in db
        db.storePolice(registerUser);
        return 1;
    }


    public String[][] getReportsByCNIC(String cnic)
    {
        return db.getReportsByCNIC(cnic);
    }

    public String[][] getReportsByOfficerCNIC(String cnic)
    {
        return db.getReportsByOfficerCNIC(cnic);
    }


    //REPORT STATS
    public XYChart.Series<String, Number> reportstats()
    {
        //total crimes
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Reported Crimes", db.totalCrimes()));
        //resolved crimes
        series.getData().add(new XYChart.Data<>("Resolved Crimes", db.resolvedCrimes()));
        //MP -> open cases
        series.getData().add(new XYChart.Data<>("Missing Person",db.missingPersonRate()));
        //most cri
        // series.getData().add(new XYChart.Data<>("Most Crimes", ));
        return series;
    }
    //2. LOGIN
    // checks DB, compare username/password ->returns bool
    public int loginCitizen(String password, String cnic) {
        String p = db.LoginC(cnic); // Get password from DB using cnic
        if (p == null || !p.equals(password)) {
            return 0; // Return 0 if no password is found or mismatch
        }
        return 1; // Return 1 for successful login
    }

    public int loginOfficer(String password, String cnic) {
        String p = db.LoginP(cnic); // Get password from DB using cnic
        if (p == null || !p.equals(password)) {
            return 0; // Return 0 if no password is found or mismatch
        }
        return 1; // Return 1 for successful login
    }


    public void MissingPerson(String location, Image userImage,
                              String name, String age,
                              String timeOfMissing, String reporterContact,
                              String reporterRelation, String Cnic)
    {
        newReport = new MissingPerson(location, userImage, age, name, timeOfMissing, reporterContact, reporterRelation, Cnic);
       // store in db
        db.storeMissingP(newReport);

        //  for testing
        System.out.println("Missing Person Report:");
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Location: " + location);
        System.out.println("Time of Missing: " + timeOfMissing);
        System.out.println("Reporter Relation: " + reporterRelation);
        System.out.println("Reporter Contact: " + reporterContact);
        System.out.println("CNIC: " + Cnic);
        System.out.println("User Image: " + (userImage != null ? "Image Provided" : "No Image Provided"));

    }

    public void reportCrime(String cnic, String crimeType, String description, String location, LocalDate time, Image evidenceFile, String Dep) {
        // Create the Case report object
        newReport = new Case(location, evidenceFile, crimeType, Dep, time.toString(), description, cnic);
        // Here you can store the report in the database or in memory as needed
        db.storeCase(newReport);

        // Display the report details for testing
        System.out.println("Crime Report Created: ");
        System.out.println("Description: " + ((Case) newReport).getDescription());
        System.out.println("Type: " + newReport.getType());
        System.out.println("Location: " + ((Case) newReport).getLocation());
        System.out.println("Time of Incident: " + ((Case) newReport).getTimeOfIncident());
        System.out.println("Department: " + newReport.getDepartment());
        System.out.println("Reporter CNIC: " + newReport.getReporterCnic());
        System.out.println("Status: " + newReport.getStatus());
    }
    public void ConfirmPerson(String where,
                              String condition, String reason,
                              Image image, int id, String cnic)
    {
     // store in DB
        cp = new ConfirmPerson(where, condition, reason, image, id, cnic);
        db.storeConfirmP(cp);
        // update status in db
        db.updateMPStatus(id);

    }

    public void insertFeedback(String cnic, String feedback) {
        // Logic to store feedback in the database (e.g., using JDBC or any database connection method)
        System.out.println("Inserting feedback into the database...");
        System.out.println("CNIC: " + cnic);
        System.out.println("Feedback: " + feedback);
        db.storeFeedback(cnic,feedback);
        // Example: Assuming you have a database class to interact with
        // DatabaseHandler.insertFeedbackToDB(cnic, feedback);
    }
    public CPController.data showMP(int id)
    {
        return db.retrieveMissingP(id);
    }


    public XYChart.Series<String, Number> getCrimeDataByArea(String area) {
        // Fetch crime data by area from DB
        Map<String, Integer> crimeData = db.fetchCrimeDataByArea(area); // Assuming the fetch method works correctly

        if (crimeData == null || crimeData.isEmpty()) {
            System.out.println("No crime data found for the selected area.");
            return new XYChart.Series<>(); // Return an empty series
        }

        // Create a new series for the bar chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(area); // Set the series name to the area

        // List of colors to assign to each crime type
        List<String> colors = List.of("#FF5733", "#33FF57", "#3357FF", "#FFD700", "#8A2BE2");

        // Initialize color index to cycle through the color list
        int colorIndex = 0;

        // Loop through the crime data and add it to the series
        for (Map.Entry<String, Integer> entry : crimeData.entrySet()) {
            String crimeType = entry.getKey(); // Crime type (e.g., Theft, Burglary)
            int count = entry.getValue(); // Crime count

            // Create a data point for the chart
            XYChart.Data<String, Number> data = new XYChart.Data<>(crimeType, count);

            // Add the data to the series (without applying style immediately)
            series.getData().add(data);

            // Increment color index and cycle through the color list
            colorIndex++;
        }

        // Set the colors for each bar after the chart layout is done
        Platform.runLater(() -> {
            for (int i = 0; i < series.getData().size(); i++) {
                XYChart.Data<String, Number> data = series.getData().get(i);
                String color = colors.get(i % colors.size()); // Get the color for this crime type
                // Set the style after the node is created
                data.getNode().setStyle("-fx-bar-fill: " + color);
            }
        });

        return series;
    }

    public String getGenericColumnValue(String cnic, String columnName) {
        // Create a DB instance

        // Fetch the generic column value using the CNIC and column name
        String value = db.getGenericColumnValue(cnic, columnName);

        // Return the value to BLConnector
        return value;
    }


    public List<String> fetchPendingReports(String department, String crimeType) {

        return db.getPendingReports(department, crimeType);
    }


    public boolean updateCaseStatus(String caseId, String newStatus) {
        return db.updateStatus(caseId, newStatus);
    }

    public boolean updateCaseStatus(String caseId, String newStatus, String tableName) {
        return db.updateStatus(caseId, newStatus, tableName);
    }

    public List<String> fetchOfficersByDepartment(String department) {
        return db.getOfficersByDepartment(department);
    }


    public boolean assignCaseToOfficer(String officerCnic, String caseId, String tableName) {
        return db.insertCaseAssignment(officerCnic, caseId, tableName);
    }

public MissingPerson showMPDetails(int id)
{
    return db.showMPDetails(id);
}


    public Case showCRDetails(int id)
    {
        return db.showCRDetails(id);
    }
    public boolean updateCaseColumns(int caseId, Map<String, String> columnValues, String tableName) {
        // Call the database layer to execute the update query
        return db.updateCaseInDB(caseId, columnValues, tableName);
    }

}

