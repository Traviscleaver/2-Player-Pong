module com.ponggame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ponggame to javafx.fxml;
    exports com.ponggame;
}