package com.github.ricedope;

import com.github.ricedope.controllers.FXMLSceneController;
import com.github.rhys_h_walker.Logger;
import com.github.rhys_h_walker.core_enums.LoggingLevel;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        primaryStage.setTitle("Rambling Jesters by Rhys Walker");

        FXMLSceneController.setPrimaryStage(primaryStage);
        FXMLSceneController.loadScene("/FXML/HomeScreen.fxml");
        Logger.logprogress("Main application loaded successfully");
        
    }
    
    public static void main(String[] args) {
        Logger.initializeLogger("rambling-jesters", LoggingLevel.ALL);
        Logger.logprogress("Launching JavaFX application");
        launch(args);
    }

}
