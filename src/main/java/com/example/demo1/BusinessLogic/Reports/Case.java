package com.example.demo1.BusinessLogic.Reports;

import javafx.scene.image.Image;

public class Case extends Reports {
    private String timeOfIncident;
    private String location;
    private String description;
    private String type;

    // Constructor to initialize Case attributes
    public Case( String incidentLocation, Image userImage, String type, String department,
                String timeOfIncident, String description, String reporterCnic) {
        super(incidentLocation, userImage, type, department, reporterCnic);

        this.timeOfIncident = timeOfIncident;
        this.location = incidentLocation;
        this.description = description;
        this.type = type;
    }

    // Getters and Setters
    public String getTimeOfIncident() {
        return timeOfIncident;
    }

    public void setTimeOfIncident(String timeOfIncident) {
        this.timeOfIncident = timeOfIncident;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCrimeType() {
        return this.type;
    }
}