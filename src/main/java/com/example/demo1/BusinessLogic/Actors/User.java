package com.example.demo1.BusinessLogic.Actors;

// Abstract base class: User
public abstract class User {
    private String area;
    private String name;
    private String password;
    private String phone;
    private String cnic;

    // Constructor to initialize User fields
    public User(String area, String name, String password, String phone, String cnic) {
        this.area = area;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.cnic = cnic;
    }

    // Getters and Setters for encapsulation
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }
}

