package com.github.ricedope.controllers;


import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import com.github.ricedope.Logger;
import com.github.ricedope.models.Creator;
import com.github.ricedope.Definitions;
import com.github.ricedope.models.NetworkingFunctions;

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

    // Status labels
    @FXML private Label seedTextFileName;
    @FXML private Label jesterNamesFileName;
    @FXML private Label outputDirectory;
    @FXML private Label status;
    @FXML private Label instructionsLabel;

    // Accordion options
    @FXML private Accordion runnerAccordion;
    @FXML private TitledPane seedTextTitlePane;

    @FXML private Hyperlink githubButton;

    // Store selected files
    private File seedTextFile;
    private File jesterNamesFile;
    private File outputFile;


    @FXML
    private void initialize() {
        // Set the default accordion pane
        runnerAccordion.setExpandedPane(seedTextTitlePane);
        // Set the default prompt text
        promptTextArea.setText("Please review the following text and return only the corrected version within quotation marks. Do not change the order of any non-duplicated phrases. Remove all duplicated phrases. Correct grammar and punctuation as needed to ensure the sentence flows naturally. Add connector words (e.g., and, but, then) only where necessary for fluidity. Do not include any explanation or extra output—only the revised text in quotation marks.");
        instructionsLabel.setText("""
Creating a file: 
This tool allows for the quick creation of an XML Runner file that can be used to generate an output from the Rambling Jesters project. 
Please fill in the required fields and select the necessary files to create a valid XML Runner file. 
More information about each field can be found in the respective central panes.
                """);
        githubButton.setText(Definitions.APP_VERSION + " | " + Definitions.GITHUB_URL);
    }

    @FXML
    private void hyperlink(ActionEvent event) {
        Logger.logprogress("Opening GitHub repository in browser");
        NetworkingFunctions.openBrowserToHyperlink(Definitions.GITHUB_URL);
    }

    @FXML
    private void exit(ActionEvent event) {
        // Return the the previous screen
        Logger.logprogress("Exiting XML Creator");
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
        status.setText("XML file created successfully at " + outputFile.getAbsolutePath());

    }

    @FXML
    private void directoryChooser(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory");
        outputFile = directoryChooser.showDialog(null);
        outputDirectory.setText("XML Runner will be made in: " + outputFile.getAbsolutePath());
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

        if (event.getSource() == seedTextButton) 
        {
            Logger.logprogress("Selected SeedText file: " + selectedFile.getAbsolutePath());
            seedTextFile = selectedFile;
            seedTextFileName.setText("Seed Text File: " + seedTextFile.getAbsolutePath());
        } else if (event.getSource() == jesterNamesButton) {
            Logger.logprogress("Selected Jester Names file: " + selectedFile.getAbsolutePath());
            jesterNamesFile = selectedFile;
            jesterNamesFileName.setText("Jester Names File: " + jesterNamesFile.getAbsolutePath());
        }

    }
}