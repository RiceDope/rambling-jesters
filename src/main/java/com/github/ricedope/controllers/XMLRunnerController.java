package com.github.ricedope.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.ricedope.Logger;
import com.github.ricedope.models.Runner;
import com.github.ricedope.models.logic.Output;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class XMLRunnerController {

    @FXML private Label fileStatus;
    @FXML private Label outputLabel;
    @FXML private TextField outputFilename;
    @FXML private Label runLocation;
    @FXML private Label instructionsLabel;

    private File selectedFile;
    private Output mostRecentOutput;

    @FXML
    private void initialize() {
        instructionsLabel.setText("""
Running a file:
Please select the XML Runner file that has been made by clicking "Select a runner file" and then navigating to your target directory.
Assuming all the files can be located and opened, your sample text will be ready in around 5–10 minutes.
Please do not attempt to leave this page as it will interrupt the artifact generation and possibly kill the process.

Options:
- Type in a filename for the output file in the text field provided. If left blank a default name will be applied.

Notes:
- If using the default filename, this can overwrite older outputs with the same default name.
""");
    }

    @FXML 
    private void run(ActionEvent event) {
        
        Logger.logprogress("Attempting to run the main program from selectedXMLFile");

        // Guard to catch file errors
        if (selectedFile == null) {
            Logger.logerror("No file selected to run");
            fileStatus.setText("Unidentified error when running the file");
            return;
        }

        Logger.logprogress("XML file not null, Path found: " + selectedFile.getAbsolutePath());
        Logger.logprogress("Running the main program now");

        Output runOutput = Runner.runMainProgram(selectedFile.getAbsolutePath());
        mostRecentOutput = runOutput;

        if (runOutput == null) {
            Logger.logerror("Output object is null, something went wrong, main output not received");
            outputLabel.setText("Error: Output object is null");
            return;
        }

        // Update the UI with the output
        outputLabel.setText(runOutput.llmCorrectedText);
        
        Logger.logprogress("Output object recieved");
        Logger.logprogress(runOutput.llmCorrectedText);
        Logger.logprogress("Attempt to save output to users home directory");

        Path homePath = Paths.get(System.getProperty("user.home"));

        Logger.logprogress("Directory found: " + homePath.toAbsolutePath());

        String filename = "";
        if (outputFilename.getText().isEmpty() || outputFilename.getText().equals(" ")) {
            Logger.logprogress("No output filename provided, using default name");
            filename = "rambling-jesters-output.txt";
        } else {
            filename = outputFilename.getText();
            Logger.logprogress("Using provided output filename: " + filename);
        }

        try {
            File outputFile = homePath.resolve(filename).toFile();

            Logger.logprogress("Saving output to file: " + outputFile.getAbsolutePath());

            FileOutputStream fos = new FileOutputStream(outputFile);
            fos.write(("Iterations: "+ runOutput.iterations + "\n").getBytes());
            fos.write(("Grid Size: "+ runOutput.gridSize + "\n").getBytes());
            fos.write(("Minimum Passage Length: "+ runOutput.minimumPassageLength + "\n").getBytes());
            fos.write(("Maximum Passage Length: "+ runOutput.maximumPassageLength + "\n").getBytes());
            fos.write(("Jester Names: "+ runOutput.jesterNames + "\n").getBytes());
            fos.write(("LLM Prompt: "+ runOutput.llmPrompt + "\n").getBytes());
            fos.write(("LLM Timeout: "+ runOutput.llmTimeout + "\n").getBytes());
            fos.write(("Seed Text: "+ runOutput.sourceTexts + "\n").getBytes());
            fos.write(("\nOriginal Idea:\n" + runOutput.sourceText + "\n\n").getBytes());
            fos.write(("Jester-"+runOutput.jesterName+":\n" + runOutput.llmCorrectedText + "\n\n").getBytes());
            fos.write(("Llama3 Evaluation:" + runOutput.llmEvaluation).getBytes());
            fos.close();

            Logger.logprogress("File has been written successfully");
            runLocation.setText("Output File: " + outputFile.getAbsolutePath());

        } catch (Exception e) {
            Logger.logerror("Error saving output file: " + e.getMessage());
            outputLabel.setText("Error saving output file");
            return;
        }

        

    }

    @FXML 
    private void selectFile(ActionEvent event) {
        Logger.logprogress("Letting user select an XML file to run");
        // SELECT A XML RUNNER FILE TO RUN
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a XML runner file");
        fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("XML", "*.xml")
        );
        selectedFile = fileChooser.showOpenDialog(null);
        
        if (selectedFile != null) {
            Logger.logprogress("Selected file: " + selectedFile.getAbsolutePath());
            fileStatus.setText("Selected file: " + selectedFile.getName());
        } else {
            Logger.logerror("No file selected");
            fileStatus.setText("File selection failed");
        }
    }

    @FXML
    private void exit(ActionEvent event) {
        FXMLSceneController.previousScene();
    }
    
}
