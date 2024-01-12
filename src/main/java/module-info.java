module com.example.cmsc125_lab3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cmsc125_lab3 to javafx.fxml;
    exports com.example.cmsc125_lab3;
}