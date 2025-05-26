package com.github.ricedope.controllers;

import com.github.ricedope.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
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
        Logger.logprogress("Changing to XML Creator scene");
        FXMLSceneController.loadScene("/FXML/XMLCreator.fxml");
    }

    @FXML
    private void RunXML() {
        // Logic for running the process
        Logger.logprogress("Changing to XML Runner scene");
        FXMLSceneController.loadScene("/FXML/XMLRunner.fxml");
    }
}
