package com.github.ricedope.controllers;


import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class XMLCreatorController {

    // Different buttons (FileChooser)
    @FXML private Button seedTextButton;
    @FXML private Button jesterNamesButton;
    @FXML private Button outputFileButton;

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
    private File fileChooser(ActionEvent event) {

        String titleText = "";
        String desiredFileExtension = "";
        if (event.getSource() == seedTextButton) {
            titleText = "Select a file that contains some seed text";
            desiredFileExtension = "*.txt";
        } else if (event.getSource() == jesterNamesButton) {
            titleText = "Select a file that contains some jester names";
            desiredFileExtension = "*.txt";
        } else if (event.getSource() == outputFileButton) {
            titleText = "Select a directory to save the output to";
            desiredFileExtension = "*.xml";
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(titleText);
        fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("Some files", desiredFileExtension)
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        return selectedFile;

    }
}