package com.example.javafx_crud;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TableColumn<User, String> CourseColumn;

    @FXML
    private TableColumn<User, Integer> IDColumn;

    @FXML
    private TableColumn<User, String> MobileColumn;

    @FXML
    private TableColumn<User, String> NameColumn;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableView<User> table;

    @FXML
    private TextField txtCourse;

    @FXML
    private TextField txtMobile;

    @FXML
    private TextField txtName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTable();

        //Add listener to the table that if a row gets clicked,
        // it calls displaySelectedUserData()
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                displaySelectedUserData(newSelection);
            }
        });
    }

    // Method to display selected user data in text fields
    private void displaySelectedUserData(User selectedUser) {
        txtMobile.setText(selectedUser.getMobile());
        txtName.setText(selectedUser.getName());
        txtCourse.setText(selectedUser.getCourse());
    }

    @FXML
    void Add(ActionEvent event) {
        String course, mobile, name;

        course = txtCourse.getText();
        mobile = txtMobile.getText();
        name = txtName.getText();

        if (course.equals("") || name.equals("") || mobile.equals("")) {
            System.out.println("Please input all the fields.");
        } else {
            Connection connection = HelloApplication.getConnection();
            if (connection != null) {
                try {
                    String query = "INSERT INTO user (name, Mobile, Course) VALUES (?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, mobile);
                    preparedStatement.setString(3, course);

                    // Execute the update query
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Added Successfully");
                        table.getItems().clear();
                        populateTable();
                    } else {
                        System.out.println("Failed to add the record.");
                    }

                    // Close resources
                    preparedStatement.close();


                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @FXML
    void Delete(ActionEvent event) {
        Connection connection = HelloApplication.getConnection();
        String name = txtName.getText();
        String mobile = txtMobile.getText();
        PreparedStatement preparedStatement = null; // Initialize outside try-catch to ensure its availability in finally

        if (name.isEmpty() && mobile.isEmpty()) {
            System.out.println("Please select a user or enter the correct name/mobile to proceed.");
            return; // Exit the method early if no name or mobile provided
        }

        try {
            if (connection != null) {
                // Use a single prepared statement and modify the query based on conditions
                String sql = "DELETE FROM User WHERE ";

                if (!name.isEmpty()) {
                    sql += "name = ?";
                } else if (!mobile.isEmpty()) {
                    sql += "mobile = ?";
                }

                preparedStatement = connection.prepareStatement(sql);

                // Set the parameter based on the non-empty field
                preparedStatement.setString(1, name.isEmpty() ? mobile : name);

                // Execute the delete query
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("User deleted successfully!");
                    table.getItems().clear();
                    populateTable();
                } else {
                    System.out.println("User with the provided name/mobile not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources in finally block to ensure they're always closed
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void Update(ActionEvent event) {
        Connection connection = HelloApplication.getConnection();
        String name = txtName.getText();
        String newMobile = txtMobile.getText();
        String newCourse = txtCourse.getText();

        if (name.isEmpty()) {
            System.out.println("Please give the name of the user.");
            return;
        }

        try {
            if (connection != null) {
                if (!newMobile.isEmpty() && !newCourse.isEmpty()) {
                    // Update both mobile and course
                    String query = "UPDATE User SET Mobile = ?, Course = ? WHERE name = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, newMobile);
                    preparedStatement.setString(2, newCourse);
                    preparedStatement.setString(3, name);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("User details updated successfully!");
                        table.getItems().clear();
                        populateTable();
                    } else {
                        System.out.println("User with the provided name not found.");
                    }

                    preparedStatement.close();
                } else if (!newMobile.isEmpty()) {
                    // Update only mobile
                    String query = "UPDATE User SET Mobile = ? WHERE name = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, newMobile);
                    preparedStatement.setString(2, name);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("User mobile updated successfully!");
                        table.getItems().clear();
                        populateTable();
                    } else {
                        System.out.println("User with the provided name not found.");
                    }

                    preparedStatement.close();
                } else if (!newCourse.isEmpty()) {
                    // Update only course
                    String query = "UPDATE User SET Course = ? WHERE name = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, newCourse);
                    preparedStatement.setString(2, name);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("User course updated successfully!");
                        table.getItems().clear();
                        populateTable();
                    } else {
                        System.out.println("User with the provided name not found.");
                    }

                    preparedStatement.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    void populateTable() {
        List<User> userList = new ArrayList<>();

        try {
            // Fetch data from the database
            Connection connection = HelloApplication.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM User");

            // Iterate through the result set and create User objects
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String mobile = resultSet.getString("Mobile");
                String course = resultSet.getString("Course");
                int id = resultSet.getInt("id");
                User user = new User(id, name, mobile, course);
                userList.add(user);
            }

            // Close connections
            resultSet.close();
            statement.close();
//            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set up CellValueFactory for each column
        NameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        MobileColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMobile()));
        CourseColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCourse()));

        // Use PropertyValueFactory for the IDColumn
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));


        // Set the items in the TableView
        table.getItems().addAll(userList);
    }
}