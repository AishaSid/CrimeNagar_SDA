package com.example.demo1.UiLayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MAIN extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            // Ensure the FXML file path is correct
            FXMLLoader fxmlLoader = new FXMLLoader(MAIN.class.getResource("welcome.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("i221154, i221281");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
