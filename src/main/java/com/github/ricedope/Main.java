package com.github.ricedope;

import com.github.ricedope.controllers.FXMLSceneController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        FXMLSceneController.setPrimaryStage(primaryStage);
        FXMLSceneController.loadScene("/FXML/HomeScreen.fxml");
        Logger.logprogress("Main application loaded successfully");
        
    }
    
    public static void main(String[] args) {
        Logger.logLevel = Logger.loggingLevel.SOME;
        Logger.logprogress("Launching JavaFX application");
        launch(args);
    }

}
