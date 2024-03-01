package com.example.demo.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GoogleMapsController {

    @FXML
    private WebView webView;

    @FXML
    private TextField addressTextField;

    public void initialize() {
        WebEngine webEngine = webView.getEngine();

        // Load the HTML file
        String relativePath = getClass().getResource("/com/example/demo/googlemaps.html").toExternalForm();
        webEngine.load(relativePath);
    }

    @FXML
    private void searchAndStoreLocation() {
        WebEngine webEngine = webView.getEngine();
        String address = addressTextField.getText();

        // Execute JavaScript code to search for the address in Google Maps
        String script = "searchLocation('" + address + "');";
        webEngine.executeScript(script);
    }

    public void storeLocation(double lat, double lng) {
        System.out.println("Latitude: " + lat + ", Longitude: " + lng);
        saveLocationToDatabase(lat, lng);
    }

    private void saveLocationToDatabase(double lat, double lng) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/smart", "root", "");

            int userId = 14;
            String sql = "UPDATE utilisateur SET latitude = ?, longitude = ? WHERE id_utilisateur = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setDouble(1, lat);
                preparedStatement.setDouble(2, lng);
                preparedStatement.setInt(3, userId);
                preparedStatement.executeUpdate();
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
