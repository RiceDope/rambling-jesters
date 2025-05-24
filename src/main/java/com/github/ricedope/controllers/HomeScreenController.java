package com.github.ricedope.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TitledPane;

public class HomeScreenController {

    @FXML private Accordion helpAccordion;
    @FXML private TitledPane helpTitlePane;

    @FXML void initialize() {
        // Initialize the help accordion
        helpAccordion.setExpandedPane(helpTitlePane);
    }

    @FXML
    private void CreateXML() {
        // Logic for creating a new Runnable XML file
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
