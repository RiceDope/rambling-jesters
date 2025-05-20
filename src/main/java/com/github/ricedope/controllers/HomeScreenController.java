package com.github.ricedope.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class HomeScreenController {

    @FXML
    private void CreateXML() {
        // Logic for creating a new Runnable XML file
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Create XML");
        alert.setHeaderText(null);
        alert.setContentText("Create XML button clicked!");
        alert.showAndWait();
        FXMLSceneController.loadScene("/FXML/XMLCreator.fxml");
    }

    @FXML
    private void RunXML() {
        // Logic for running the process
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Run XML");
        alert.setHeaderText(null);
        alert.setContentText("Run button clicked!");
        alert.showAndWait();
    }
}
