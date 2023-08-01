module com.example.citybikes {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires com.google.gson;


    opens com.example.citybikes to javafx.fxml;
    exports com.example.citybikes;
}