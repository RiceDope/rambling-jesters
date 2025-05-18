package com.github.ricedope.controllers;


import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class XMLCreatorController {

    // Inputs
    @FXML private TextArea promptTextArea;
    @FXML private TextField timeoutField;

    // Grid settings
    @FXML private TextField jestersCountField;
    @FXML private TextField gridSizeField;
    @FXML private TextField iterationsField;

    // Passage length
    @FXML private TextField minPassageLengthField;
    @FXML private TextField maxPassageLengthField;

    // XML output settings
    @FXML private TextField xmlFilenameField;

    // Store selected files
    private File seedTextFile;
    private File jesterNamesFile;
    private File outputFile;

    @FXML
    private void fileChooser(ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet");
    }
}