package com.example.demo1.BusinessLogic.Actors;

// Subclass: Officer
public class Officer extends User {
    private String department;

    // Constructor for Officer
    public Officer(String area, String name, String password, String phone, String cnic, String department) {
        super(area, name, password, phone, cnic); // Call the constructor of the parent class
        this.department = department;
    }

    // Getters and Setters for the department field
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


}
