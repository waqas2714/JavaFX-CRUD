package com.example.javafx_crud;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HelloApplication extends Application {

    private static Connection connection;

    @Override
    public void start(Stage stage) throws IOException {
        // Connect to the database when the application starts
        connectToDatabase();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private static void connectToDatabase() {
        try {
            String URL = "jdbc:mysql://localhost:3306/user_registration";
            String USERNAME = "root";
            String PASSWORD = "MySqlPassword";
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // You can add a getter method to retrieve the connection
    public static Connection getConnection() {
        return connection;
    }

    @Override
    public void stop() throws Exception {
        // Close the database connection when the application stops
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
