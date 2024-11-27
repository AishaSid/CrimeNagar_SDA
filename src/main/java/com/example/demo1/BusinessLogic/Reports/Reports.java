package com.example.demo1.BusinessLogic.Reports;

import javafx.scene.image.Image;

public class Reports {
    private String incidentLocation;
    private String status = "Pending"; // Default value
    private Image userImage;
    private String type;
    private String department;
    private String reporterCnic; // Added reporterCnic field

    // Constructor to initialize common attributes
    public Reports(String incidentLocation, Image userImage, String type, String department, String reporterCnic) {
        this.incidentLocation = incidentLocation;
        this.userImage = userImage;
        this.type = type;
        this.department = department;
        this.reporterCnic = reporterCnic; // Initialize reporterCnic
    }

    // Getters and Setters
    public String getLocation() {
        return incidentLocation;
    }

    public void setLocation(String incidentLocation) {
        this.incidentLocation = incidentLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String s) {
        status = s;
    }

    public Image getUserImage() {
        return userImage;
    }

    public void setUserImage(Image userImage) {
        this.userImage = userImage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getReporterCnic() { // Getter for reporterCnic
        return reporterCnic;
    }

    public void setReporterCnic(String reporterCnic) { // Setter for reporterCnic
        this.reporterCnic = reporterCnic;
    }

}
