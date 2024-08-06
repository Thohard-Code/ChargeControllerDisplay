module com.example.chargecontrollerdisplayprojekt {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.chargecontrollerdisplayprojekt to javafx.fxml;
    exports com.example.chargecontrollerdisplayprojekt;
}