package com.example.demo1.BusinessLogic.Actors;

// Subclass: Citizen
public class Citizen extends User {
    // Constructor for Citizen
    public Citizen(String area, String name, String password, String phone, String cnic) {
        super(area, name, password, phone, cnic); // Call the constructor of the parent class
    }

}
