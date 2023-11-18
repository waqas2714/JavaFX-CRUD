module com.example.javafx_crud {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.javafx_crud to javafx.fxml;
    exports com.example.javafx_crud;
}