module com.vims.vimsapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.vims.ui.controllers to javafx.fxml;
    opens com.vims to javafx.fxml; 

    exports com.vims;
    exports com.vims.model;
    exports com.vims.dao;
    exports com.vims.controller;
    exports com.vims.util;
    //exports com.vims.ui.controllers;
}