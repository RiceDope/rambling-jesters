package com.github.ricedope.controllers.component_controllers;

import com.github.ricedope.Definitions;
import com.github.ricedope.Logger;
import com.github.ricedope.models.NetworkingFunctions;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;

/**
 * This class serves as a base controller for FXML controllers in the application
 */

public class FooterController {
    
    @FXML private Hyperlink githubButton;

    @FXML
    private void initialize() {
        if (githubButton != null) {
            githubButton.setText(Definitions.APP_VERSION + " | " + Definitions.GITHUB_URL);
        }
    }

    @FXML
    private void hyperlink(ActionEvent event) {
        Logger.logprogress("Opening GitHub repository in browser");
        NetworkingFunctions.openBrowserToHyperlink(Definitions.GITHUB_URL);
    }
}
