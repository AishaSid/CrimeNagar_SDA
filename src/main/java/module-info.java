module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;

    exports com.example.demo1.UiLayer;
    opens com.example.demo1.UiLayer to javafx.fxml;
}