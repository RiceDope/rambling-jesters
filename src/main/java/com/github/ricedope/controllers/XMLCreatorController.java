package com.github.ricedope.controllers;


import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import com.github.ricedope.Logger;
import com.github.ricedope.models.Creator;

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
    private void initialize() {
        promptTextArea.setText("Please review the following text and return only the corrected version within quotation marks. Do not change the order of any non-duplicated phrases. Remove all duplicated phrases. Correct grammar and punctuation as needed to ensure the sentence flows naturally. Add connector words (e.g., and, but, then) only where necessary for fluidity. Do not include any explanation or extra output—only the revised text in quotation marks.");
    }

    @FXML
    private void exit() {
        // Return the the previous screen
        FXMLSceneController.previousScene();
    }

    @FXML
    private void createXML(ActionEvent event) {
        if (seedTextFile == null || jesterNamesFile == null || outputFile == null) {
            // Show an error message or alert to the user
            Logger.logerror("Please select all required files.");
            return;
        }

        Creator.createXML(outputFile.getAbsolutePath(), 
                          seedTextFile.getAbsolutePath(), 
                          jesterNamesFile.getAbsolutePath(), 
                          promptTextArea.getText(),   
                          Integer.parseInt(timeoutField.getText()), 
                          Integer.parseInt(jestersCountField.getText()),   
                          Integer.parseInt(gridSizeField.getText()), Integer.parseInt(minPassageLengthField.getText()),    
                          Integer.parseInt(maxPassageLengthField.getText()), Integer.parseInt(iterationsField.getText()),       
                          xmlFilenameField.getText());

        Logger.logprogress("XML file created successfully at " + outputFile.getAbsolutePath());

    }

    @FXML
    private void directoryChooser(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory");
        outputFile = directoryChooser.showDialog(null);
    }

    /**
     * A File Chooser which will display different options depending on which button called it
     * @param event
     * @return
     */
    @FXML
    private void fileChooser(ActionEvent event) {

        String titleText = "File Chooser";
        String desiredFileExtension = "";
        if (event.getSource() == seedTextButton) {
            titleText = "Select a file that contains some seed text";
            desiredFileExtension = "*.txt";
        } else if (event.getSource() == jesterNamesButton) {
            titleText = "Select a file that contains some jester names";
            desiredFileExtension = "*.csv";
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(titleText);
        fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("Some files", desiredFileExtension)
        );
        File selectedFile = fileChooser.showOpenDialog(null);

        if (event.getSource() == seedTextButton) {
            seedTextFile = selectedFile;
        } else if (event.getSource() == jesterNamesButton) {
            jesterNamesFile = selectedFile;
        }

    }
}