package com.example.demo1.DataBase;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.sql.*;

import com.example.demo1.BusinessLogic.Actors.Feedback;
import com.example.demo1.BusinessLogic.Actors.Officer;
import com.example.demo1.BusinessLogic.Actors.User;
import com.example.demo1.BusinessLogic.Reports.Case;
import com.example.demo1.BusinessLogic.Reports.ConfirmPerson;
import com.example.demo1.BusinessLogic.Reports.MissingPerson;
import com.example.demo1.BusinessLogic.Reports.Reports;
import com.example.demo1.UiLayer.CPController;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class db
{
    Connection connection;
    public db()
    {
        connection = connectToDatabase();
//        // Optional: Close the connection after usage
//        if (connection != null) {
//            try {
//                connection.close();
//                System.out.println("Connection closed.");
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public byte[] imageToByteArray(Image image) {
        if (image == null)
        {
            return null;
        }

        try {
            // Create a WritableImage (only required if you need to read pixels)
            WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
            PixelReader pixelReader = image.getPixelReader();
            writableImage.getPixelWriter().setPixels(0, 0, (int) image.getWidth(), (int) image.getHeight(), pixelReader, 0, 0);

            // Convert the WritableImage to BufferedImage
            BufferedImage bufferedImage = new BufferedImage((int) writableImage.getWidth(), (int) writableImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

            for (int y = 0; y < writableImage.getHeight(); y++) {
                for (int x = 0; x < writableImage.getWidth(); x++) {
                    bufferedImage.setRGB(x, y, writableImage.getPixelReader().getArgb(x, y));
                }
            }

            // Convert the BufferedImage to byte array using ImageIO
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            return imageBytes;

        } catch (IOException e) {
            e.printStackTrace();
            return null; // Return null if there's an error
        }
    }

    public static Connection connectToDatabase() {
        Connection c = null;
        String url = "jdbc:mysql://localhost:3306/db";  // Replace with your DB URL
        String username = "root";
        String password = "51ainah41";//"51ainah41";  // Replace with your DB password

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            c = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database successfully!");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }
//    public static Blob convertImageToBlob(Image userImage, Connection connection) throws Exception {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        ImageIO.write(SwingFXUtils.fromFXImage(userImage, null), "png", outputStream);
//        byte[] imageBytes = outputStream.toByteArray();
//
//        Blob imageBlob = connection.createBlob();
//        imageBlob.setBytes(1, imageBytes);
//
//        return imageBlob;
//    }
//for login
    public String LoginC(String cnic) {
    String query = "SELECT password FROM Citizen WHERE cnic = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setString(1, cnic);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString("password"); // Return password if CNIC exists
        }
    } catch (SQLException e) {
        System.out.println("Error in fetching Citizen password."); // Minimal error handling
    }
    return null; // Return null if CNIC doesn't exist or an error occurs
}
    public String LoginP(String cnic) {
        String query = "SELECT password FROM Officer WHERE cnic = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, cnic);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("password"); // Return password if CNIC exists
            }
        } catch (SQLException e) {
            System.out.println("Error in fetching Officer password."); // Minimal error handling
        }
        return null; // Return null if CNIC doesn't exist or an error occurs
    }

    //for register
    public Boolean Register(String cnic)
    {
        String query = "SELECT COUNT(*) FROM NADRA WHERE cnic = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query))
        {

            preparedStatement.setString(1, cnic);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                // Check the count result
                int count = resultSet.getInt(1);
                return count > 0;
            }}
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Error storing citizen: " + e.getMessage());
        }
        return false;
    }

    public void storeCitizen(User user) {
        String query = "INSERT INTO Citizen (name, password, phone, cnic) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set parameters in the prepared statement
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getPhone());
            preparedStatement.setString(4, user.getCnic());

            // Execute the insert query
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Citizen stored successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error storing citizen: " + e.getMessage());
        }
    }
    public void storePolice(User user) {
        Officer officer = (Officer) user;
        String query = "INSERT INTO Officer (name, password, phone, cnic, department, type) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set parameters in the prepared statement
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getPhone());
            preparedStatement.setString(4, user.getCnic());
            preparedStatement.setString(5, officer.getDepartment());
            preparedStatement.setString(6, officer.getType1());


            // Execute the insert query
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Officer stored successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error storing officer: " + e.getMessage());
        }
    }

    public void storeMissingP (Reports reports)
    {
        MissingPerson report= (MissingPerson) reports;
        String query = "INSERT INTO MissingPerson (location, user_image, age, name, time_of_missing, reporter_contact, reporter_relation, cnic, type) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the values from the report object to the PreparedStatement
            preparedStatement.setString(1, report.getLocation());

            // Handle image as BLOB (assuming the report has a method to get the image as byte array)
            if (report.getUserImage() != null) {
                preparedStatement.setBytes(2, imageToByteArray(report.getUserImage()));
            } else {
                preparedStatement.setNull(2, java.sql.Types.BLOB);  // If no image, set it to null
            }

            preparedStatement.setInt(3, Integer.parseInt(report.getAge()));
            preparedStatement.setString(4, report.getMissingPersonName());
            preparedStatement.setString(5, report.getDescription());
            preparedStatement.setString(6, report.getReporterContact());
            preparedStatement.setString(7, report.getReporterRelation());
            preparedStatement.setString(8, report.getReporterCnic());
            preparedStatement.setString(9, "Missing Person");  // Type is fixed as 'Missing Person'

            // Execute the insert statement
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Missing Person report successfully stored in the database.");
            }
        } catch (SQLException e) {
            System.out.println("Error while storing Missing Person report: " + e.getMessage());
        }
    }

    public void storeConfirmP(ConfirmPerson cp) {
        // SQL query to insert data into the ConfirmPerson table
        String sql = "INSERT INTO ConfirmPerson (where_location, condition_found, reason, image, officer_cnic, missing_person_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Set the values from the ConfirmPerson object into the PreparedStatement
            stmt.setString(1, cp.getWhere());  // where_location
            stmt.setString(2, cp.getCondition());      // condition_found
            stmt.setString(3, cp.getReason());         // reason
            if (cp.getImage() != null) {
                stmt.setBytes(4, imageToByteArray(cp.getImage())); // image (converted to byte array)
            } else {
                stmt.setNull(4, java.sql.Types.BLOB);  // Handle case where image is null
            }
            stmt.setString(5, cp.getCnic());    // officer_cnic
            stmt.setInt(6, cp.getMissingPersonId());   // missing_person_id

            // Execute the update (insert data into the database)
            stmt.executeUpdate();
            System.out.println("Confirmation report stored successfully.");
        } catch (SQLException e) {
            System.err.println("Error while storing confirmation report: " + e.getMessage());
        }
    }


    // for history
    public void retrieveAllReports(){}

    // for Cp only -> name, img, description

public CPController.data retrieveMissingP(int id)
{
    String query = "SELECT name, user_image, time_of_missing, CONCAT(time_of_missing, ' reported by ', reporter_relation) AS reported_by "
            + "FROM MissingPerson where id = " + id;

    try (PreparedStatement stmt = connection.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        if (rs.next())
        {
            String name = rs.getString("name");
            byte[] imageBytes = rs.getBytes("user_image");
            Image img = convertByteArrayToImage(imageBytes);
            String reportedBy = rs.getString("reported_by");
            System.out.println(name + " reported by " + reportedBy);
            CPController.data d = new CPController.data();
            d.name = name;
            d.img = img;
            d.reportedBy = reportedBy;
            return d;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

public void saveImageToFile(byte[] imageBytes, String fileName)
{
    try (FileOutputStream fileOut = new FileOutputStream("images/" + fileName + ".png")) {
        fileOut.write(imageBytes);
    } catch (IOException e)
    {
        e.printStackTrace();
    }
}
    // for approval
    public void retrieveCase(){}
    public void retrieveFeedback(){}

    public void updateMPStatus(int id)
    {
        String sql = "UPDATE MissingPerson SET status = 'closed' WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            // Execute the update
            int rowsAffected = stmt.executeUpdate();

            // Check if any row was updated
            if (rowsAffected > 0) {
                System.out.println("Status updated to 'closed' for report with ID: " + id);
            } else {
                System.out.println("No report found with ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error while updating status: " + e.getMessage());
        }
    }

    public Image convertByteArrayToImage(byte[] imageBytes) {
        if (imageBytes != null) {
            // Convert byte[] to Image using ByteArrayInputStream
            return new Image(new ByteArrayInputStream(imageBytes));
        } else {
            return null;
        }
    }

    public void storeFeedback(Feedback fb) {
        // SQL query to insert feedback into the Feedback table
        String query = "INSERT INTO Feedback (citizen_id, feedback_text) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set the citizen_id (cnic) and feedback_text parameters
            preparedStatement.setString(1, fb.getUsercnic()); // Assuming cnic is the citizen_id
            preparedStatement.setString(2, fb.getDescription()); // Set the feedback text

            // Execute the insert statement
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Feedback successfully stored in the database.");
            }
        } catch (SQLException e) {
            System.out.println("Error while storing feedback: " + e.getMessage());
        }
    }


    public void storeCase(Reports reports) {

        Case report = (Case) reports; // Cast the Reports object to Case
        String query = "INSERT INTO CaseReport (incident_location, status, user_image, type, department, reporter_cnic, time_of_incident, description) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set the values from the Case object to the PreparedStatement
            preparedStatement.setString(1, report.getLocation()); // Incident Location
            preparedStatement.setString(2, report.getStatus());   // Status (default: "Open")
            // Handle image as BLOB
            if (report.getUserImage() != null) {
                preparedStatement.setBytes(3, imageToByteArray(report.getUserImage())); // Convert Image to byte array
            } else {
                preparedStatement.setNull(3, java.sql.Types.BLOB); // If no image, set to null
            }
            preparedStatement.setString(4, report.getType());          // Type of the case
            preparedStatement.setString(5, report.getDepartment());    // Department handling the case
            preparedStatement.setString(6, report.getReporterCnic());  // Reporter CNIC
            preparedStatement.setString(7, report.getTimeOfIncident()); // Time of incident (ensure it's in proper format)
            preparedStatement.setString(8, report.getDescription());   // Description of the incident

            // Execute the insert statement
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Case report successfully stored in the database.");
            }
        } catch (SQLException e) {
            System.out.println("Error while storing Case report: " + e.getMessage());
        }
    }

    public Map<String, Integer> fetchCrimeDataByArea(String area) {
        Map<String, Integer> crimeData = new HashMap<>();
        String query = "SELECT type, COUNT(*) AS crime_count " +
                "FROM CaseReport " +
                "WHERE incident_location = ? AND status != 'Pending' " +
                "GROUP BY type";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, area);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String crimeType = rs.getString("type");
                int count = rs.getInt("crime_count");
                crimeData.put(crimeType, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return crimeData;
    }


    public String getGenericColumnValue(String cnic, String columnName) {
        String query = "SELECT " + columnName + " FROM officer WHERE cnic = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the CNIC in the prepared statement
            preparedStatement.setString(1, cnic);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Fetch the column value
                return resultSet.getString(columnName);
            } else {
                return "No Officer Found";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching data: " + e.getMessage();
        }
    }

    public List<String> getPendingReports(String department, String crimeType) {
        List<String> reports = new ArrayList<>();
        String query;

        // Define the query to fetch only 3 columns
        System.out.println(crimeType);
        if ("Missing Person".equalsIgnoreCase(crimeType)) {
            // Retrieve only ID, Name, and Location for Missing Person reports
            System.out.println("meow");
            query = "SELECT id, name, location FROM missingperson WHERE status = 'pending'";
        } else {
            // Retrieve only ID, Description, and Incident Location for Casereports
            query = "SELECT id, description, incident_location FROM casereport " +
                    "WHERE status = 'Pending' AND department = ? AND type = ?";
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Log department and crimeType for debugging
            System.out.println(department);
            System.out.println(crimeType);

            if (!"Missing Person".equalsIgnoreCase(crimeType)) {
                preparedStatement.setString(1, department);
                preparedStatement.setString(2, crimeType);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String report = "";

                    // Handle Missing Person case
                    if ("Missing Person".equalsIgnoreCase(crimeType)) {
                        report = "ID: " + resultSet.getInt("id") +
                                ", Name: " + resultSet.getString("name") +
                                ", Location: " + resultSet.getString("location");
                    } else {
                        // Handle Casereport case
                        report = "ID: " + resultSet.getInt("id") +
                                ", Description: " + resultSet.getString("description") +
                                ", Incident Location: " + resultSet.getString("incident_location");
                    }

                    // Add the report to the list
                    reports.add(report);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Log error
        }

        return reports;
    }

    public boolean updateStatus(String caseId, String newStatus) {
        String query = "UPDATE casereport SET status = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newStatus);
            preparedStatement.setString(2, caseId);

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0; // Return true if at least one row was updated
        } catch (SQLException e) {
            e.printStackTrace(); // Log error
        }
        return false; // Return false if the update fails
    }


    public boolean updateStatus(String caseId, String newStatus, String tableName) {
        String query = "UPDATE " + tableName + " SET status = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newStatus);
            preparedStatement.setString(2, caseId);

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0; // Return true if at least one row was updated
        } catch (SQLException e) {
            e.printStackTrace(); // Log error
        }
        return false; // Return false if the update fails
    }



    public List<String> getOfficersByDepartment(String department) {
        List<String> officerNames = new ArrayList<>();
        String query = "SELECT cnic FROM officer WHERE department = ? AND type != 'Analyst'";


        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, department);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                officerNames.add(rs.getString("cnic"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return officerNames;
    }

    public boolean insertCaseAssignment(String officerCnic, String caseId, String tableName) {
        String query = "INSERT INTO " + tableName + " (officer_cnic, " +
                (tableName.equals("missingperson_assigned_cases") ? "missingperson_id" : "casereport_id") +
                ") VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, officerCnic);
            preparedStatement.setString(2, caseId);

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0; // Return true if a row was inserted
        } catch (SQLException e) {
            e.printStackTrace(); // Log error
        }
        return false; // Return false if insertion fails
    }


    public Number totalCrimes()
    {
        Statement stmt = null;
        ResultSet rs = null;
        int totalCount = 0;
        try {
            stmt = connection.createStatement();
            String query = "SELECT (SELECT COUNT(*) FROM casereport) + (SELECT COUNT(*) FROM missingperson) AS total_crimes";
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                totalCount = rs.getInt("total_crimes");
            }
        } catch (Exception e) {
            System.err.println("Error occurred while fetching total crimes: " + e.getMessage());
            totalCount = 0;  // In case of error, return 0
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (Exception e) {
                System.err.println("Error occurred while closing resources: " + e.getMessage());
            }
        }
        return totalCount;
    }


    public Number resolvedCrimes()
    {
        Statement stmt = null;
        ResultSet rs = null;
        int totalCount = 0;
        try {
            stmt = connection.createStatement();
            String query = "SELECT (SELECT COUNT(*) FROM casereport where status = 'closed') + (SELECT COUNT(*) FROM missingperson where status = 'closed') AS total_crimes ";
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                totalCount = rs.getInt("total_crimes");
            }
        } catch (Exception e) {
            System.err.println("Error occurred while fetching total crimes: " + e.getMessage());
            totalCount = 0;  // In case of error, return 0
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (Exception e) {
                System.err.println("Error occurred while closing resources: " + e.getMessage());
            }
        }
        return totalCount;
    }


    public Number missingPersonRate()
    {
        Statement stmt = null;
        ResultSet rs = null;
        int totalCount = 0;
        try {
            stmt = connection.createStatement();
            String query = "SELECT  (SELECT COUNT(*) FROM missingperson where status = 'open') AS total_crimes ";
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                totalCount = rs.getInt("total_crimes");
            }
        } catch (Exception e) {
            System.err.println("Error occurred while fetching total crimes: " + e.getMessage());
            totalCount = 0;  // In case of error, return 0
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (Exception e) {
                System.err.println("Error occurred while closing resources: " + e.getMessage());
            }
        }
        return totalCount;
    }

    public String[][] getReportsByCNIC(String cnic)
    {
        String[][] reports = new String[100][3];
        int index = 0; // To track the next available row in the array

        String missingPersonQuery = "SELECT id, type, name, location FROM missingperson WHERE cnic = ?";
        String caseReportQuery = "SELECT id, type, description, incident_location FROM casereport WHERE reporter_cnic = ?";

        try {
            // Query missingperson table
            try (PreparedStatement missingPersonStatement = connection.prepareStatement(missingPersonQuery)) {
                missingPersonStatement.setString(1, cnic);
                try (ResultSet resultSet = missingPersonStatement.executeQuery()) {
                    while (resultSet.next() && index < reports.length) {
                        reports[index][0] = String.valueOf(resultSet.getInt("id")); // ID
                        reports[index][1] = resultSet.getString("type"); // Type
                        reports[index][2] = "Name: " + resultSet.getString("name") +
                                ", Location: " + resultSet.getString("location"); // Details
                        index++;
                    }
                }
            }

            // Query casereport table
            try (PreparedStatement caseReportStatement = connection.prepareStatement(caseReportQuery)) {
                caseReportStatement.setString(1, cnic);
                try (ResultSet resultSet = caseReportStatement.executeQuery()) {
                    while (resultSet.next() && index < reports.length) {
                        reports[index][0] = String.valueOf(resultSet.getInt("id")); // ID
                        reports[index][1] = resultSet.getString("type"); // Type
                        reports[index][2] = "Description: " + resultSet.getString("description") +
                                ", Incident Location: " + resultSet.getString("incident_location"); // Details
                        index++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception
        }

        String[][] result = new String[index][3];
        for (int i = 0; i < index; i++) {
            result[i] = reports[i];
        }

        return result;
    }


    public String[][] getReportsByOfficerCNIC(String officerCnic, boolean h) {
        String[][] reports = new String[100][3];
        int index = 0; // To track the next available row in the array

        // Query for missing person cases assigned to the officer
        String missingPersonQuery =
                """
       SELECT 
           mp.id, 
           mp.type, 
           mp.name, 
           mp.location 
       FROM 
           missingperson_assigned_cases mpac 
       JOIN 
           missingperson mp 
       ON 
           mpac.missingperson_id = mp.id 
       WHERE 
           mpac.officer_cnic = ? AND status IN ('open', 'closed');
       """;

        // Query for case reports assigned to the officer
        String caseReportQuery =
                "SELECT cr.id, cr.type, cr.description, cr.incident_location " +
                        "FROM casereport_assigned_cases crac " +
                        "JOIN casereport cr ON crac.casereport_id = cr.id " +
                        "WHERE crac.officer_cnic = ? AND status IN ('Open', 'Closed')";

        try {
            // Query the missing person cases assigned to the officer
            try (PreparedStatement missingPersonStatement = connection.prepareStatement(missingPersonQuery)) {
                missingPersonStatement.setString(1, officerCnic);
                try (ResultSet resultSet = missingPersonStatement.executeQuery()) {
                    while (resultSet.next() && index < reports.length) {
                        reports[index][0] = String.valueOf(resultSet.getInt("id")); // ID
                        reports[index][1] = resultSet.getString("type"); // Type
                        reports[index][2] = "Name: " + resultSet.getString("name") +
                                ", Location: " + resultSet.getString("location"); // Details
                        index++;
                    }
                }
            }
            if (h) {
                // Query the case reports assigned to the officer
                try (PreparedStatement caseReportStatement = connection.prepareStatement(caseReportQuery)) {
                    caseReportStatement.setString(1, officerCnic);
                    try (ResultSet resultSet = caseReportStatement.executeQuery()) {
                        while (resultSet.next() && index < reports.length) {
                            reports[index][0] = String.valueOf(resultSet.getInt("id")); // ID
                            reports[index][1] = resultSet.getString("type"); // Type
                            reports[index][2] =
                                    "Description: "
                                            + resultSet.getString("description")
                                            + ", Incident Location: "
                                            + resultSet.getString("incident_location"); // Details
                            index++;
                        }
                    }
                }


            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception
        }

        // Return only the populated rows
        String[][] result = new String[index][3];
        for (int i = 0; i < index; i++) {
            result[i] = reports[i];
        }

        return result;
    }


    public MissingPerson showMPDetails(int id)
{
    MissingPerson missingPerson = null;
    String query = "SELECT location, user_image, age, name, time_of_missing, " +
            "reporter_contact, reporter_relation, cnic, status, type, created_at " +
            "FROM missingperson WHERE id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query))
    {
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next())
        {
            String location = resultSet.getString("location");
            Image userImage = null;
            Blob imageBlob = resultSet.getBlob("user_image");
            if (imageBlob != null)
            {
                userImage = new Image(imageBlob.getBinaryStream());
            }
            int age = resultSet.getInt("age");
            String name = resultSet.getString("name");
            String timeOfMissing = resultSet.getString("time_of_missing");
            String reporterContact = resultSet.getString("reporter_contact");
            String reporterRelation = resultSet.getString("reporter_relation");
            String cnic = resultSet.getString("cnic");
            String status = resultSet.getString("status");
            String type = resultSet.getString("type");
            String createdAt = resultSet.getTimestamp("created_at").toString();
            missingPerson = new MissingPerson(location, userImage, Integer.toString(age), name,
                    timeOfMissing, reporterContact, reporterRelation, cnic);
            missingPerson.setStatus(status);
        }
    } catch (Exception e) {
        e.printStackTrace();
        // Handle exceptions such as logging or displaying an error message
    }
    return missingPerson;
}


    public Case showCRDetails(int id) {
        Case caseDetails = null;
        String query = "SELECT incident_location, user_image, type, department, reporter_cnic, " +
                "time_of_incident, description, status, created_at " +
                "FROM casereport WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Retrieve data from the result set
                String incidentLocation = resultSet.getString("incident_location");
                Image userImage = null;
                Blob imageBlob = resultSet.getBlob("user_image");
                if (imageBlob != null) {
                    userImage = new Image(imageBlob.getBinaryStream());
                }
                String type = resultSet.getString("type");
                String department = resultSet.getString("department");
                String reporterCnic = resultSet.getString("reporter_cnic");
                String timeOfIncident = resultSet.getTimestamp("time_of_incident").toString();
                String description = resultSet.getString("description");
                String status = resultSet.getString("status");
                String createdAt = resultSet.getTimestamp("created_at").toString();

                // Create a new Case object with the retrieved data
                caseDetails = new Case(incidentLocation, userImage, type, department,
                        timeOfIncident, description, reporterCnic);
                caseDetails.setStatus(status);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions such as logging or displaying an error message
        }
        return caseDetails;
    }
    public boolean updateCaseInDB(int caseId, Map<String, String> columnValues, String tableName) {
        // Construct the dynamic SQL query
        StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");

        // Add the columns to the query dynamically
        for (String column : columnValues.keySet()) {
            query.append(column).append(" = ?, ");
        }

        // Remove the trailing comma and space
        query.delete(query.length() - 2, query.length());

        // Add the WHERE clause for the case ID
        query.append(" WHERE id = ?");

        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
            // Set the values for the columns dynamically
            int index = 1;
            for (String value : columnValues.values()) {
                stmt.setString(index++, value);
            }

            // Set the case ID for the WHERE clause
            stmt.setInt(index, caseId);

            // Execute the update query
            int rowsAffected = stmt.executeUpdate();

            // Return true if one or more rows were affected (meaning the update was successful)
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Handle any SQL exceptions by printing the error and returning false
            e.printStackTrace();
            return false;
        }
    }




}
