package com.example.demo1.BusinessLogic.Reports;
import javafx.scene.image.Image;

public class ConfirmPerson
{
    private String where;
    private String condition;
    private String reason;
    private Image image;
    private int MissingPersonId;
    private String cnic;

    public ConfirmPerson(String where, String condition,
                         String reason, Image image, int MissingPersonId, String cnic)
    {
        this.where = where;
        this.condition = condition;
        this.reason = reason;
        this.image = image;
        this.MissingPersonId = MissingPersonId;
        this.cnic = cnic;
    }

    // Getters and Setters (for encapsulation)

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getMissingPersonId() {
        return MissingPersonId;
    }

    public void setMissingPersonId(int id) {
        this.MissingPersonId = id;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    // Optional: To print the information as a string, you can override the toString method
    @Override
    public String toString() {
        return "ConfirmPerson{" +
                "where='" + where + '\'' +
                ", condition='" + condition + '\'' +
                ", reason='" + reason + '\'' +
                ", image=" + image + // This may not display well as it's an Image object
                ", id=" + MissingPersonId +
                ", cnic='" + cnic + '\'' +
                '}';
    }
}

