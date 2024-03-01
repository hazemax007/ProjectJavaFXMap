package com.example.demo.Controllers;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LeafletController {

    @FXML
    private WebView mapWebView;

    public void initialize() {
        WebEngine webEngine = mapWebView.getEngine();
        webEngine.load(getClass().getResource("/com/example/demo/leafletmap.html").toExternalForm());
        // Enable Java-to-JavaScript communication
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == javafx.concurrent.Worker.State.SUCCEEDED) {
                JSObject jsObject = (JSObject) webEngine.executeScript("window");
                jsObject.setMember("javafxConnector", new JavaFXConnector(this));
            }
        });
    }

    public void saveCoordinates(double latitude, double longitude) {
        // Implement database saving logic here
        System.out.println("Saving coordinates: Latitude=" + latitude + ", Longitude=" + longitude);

        // JDBC Connection parameters
        String jdbcUrl = "jdbc:mysql://localhost:3306/your_database";
        String user = "your_username";
        String password = "your_password";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, user, password)) {
            // Prepare statement to insert coordinates into the 'map' table
            String sql = "INSERT INTO map (latitude, longitude) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setDouble(1, latitude);
                preparedStatement.setDouble(2, longitude);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public class JavaFXConnector {
        private final LeafletController controller;

        public JavaFXConnector(LeafletController controller) {
            this.controller = controller;
        }

        public void saveCoordinates(double latitude, double longitude) {
            controller.saveCoordinates(latitude, longitude);
        }
    }
}
