package com.example.demo1.BusinessLogic.Reports;

import javafx.scene.image.Image;

public class MissingPerson extends Reports {
    int id;
    private String age;
    private String description;
    private String reporterContact;
    private String reporterRelation;
    private String missingPersonName;

    // Constructor to initialize MissingPerson attributes
    public MissingPerson(String incidentLocation, Image userImage, String age,
                         String missingPersonName, String description, String reporterContact,
                         String reporterRelation, String reporterCnic) {
        // Call the superclass constructor with the reporterCnic field added
        super(incidentLocation, userImage, "Missing Person", null, reporterCnic);

        this.age = age;
        this.missingPersonName = missingPersonName;
        this.description = description;
        this.reporterContact = reporterContact;
        this.reporterRelation = reporterRelation;
    }

    // Getters and Setters

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReporterContact() {
        return reporterContact;
    }

    public void setReporterContact(String reporterContact) {
        this.reporterContact = reporterContact;
    }

    public String getReporterRelation() {
        return reporterRelation;
    }

    public void setReporterRelation(String reporterRelation) {
        this.reporterRelation = reporterRelation;
    }

    public String getMissingPersonName() {
        return missingPersonName;
    }

    public void setMissingPersonName(String missingPersonName) {
        this.missingPersonName = missingPersonName;
    }

    public String getType() {
        return super.getType();
    }

    public void setType(String type) {
        super.setType(type);
    }

    public String getReporterCnic() {
        return super.getReporterCnic();
    }

    public void setReporterCnic(String reporterCnic) {
        super.setReporterCnic(reporterCnic);
    }
}
