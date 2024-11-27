package com.example.demo1.BusinessLogic.Actors;

public class Feedback {
    private String usercnic;
    private String Description;

    public Feedback(String usercnic, String Description) {

        this.usercnic = usercnic;
        this.Description = Description;
    }
    public String getUsercnic() {return usercnic;}
    public String getDescription() {return Description;}
     public void setUsercnic(String usercnic) {this.usercnic = usercnic;}
    public void setDescription(String Description) {this.Description = Description;}

    public void DisplayFeedback() {
        System.out.println(usercnic + " " + Description);
    }
}
