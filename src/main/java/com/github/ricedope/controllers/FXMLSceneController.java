package com.github.ricedope.controllers;

import com.github.ricedope.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXMLSceneController {
    
    private static Stage primaryStage;
    private static Scene previousStage;

    public static void setPrimaryStage(Stage primaryStage) {
        FXMLSceneController.primaryStage = primaryStage;
    }

    public static void loadScene(String fxmlFile) {
        try {
            Logger.logprogress("Loading FXML scene: " + fxmlFile);
            Parent root = FXMLLoader.load(FXMLSceneController.class.getResource(fxmlFile));
            previousStage = primaryStage.getScene();
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            Logger.logerror("Failed to load FXML scene: " + fxmlFile);
            e.printStackTrace();
        }
    }

    public static void previousScene() {
        Logger.logprogress("Swapping to previous scene");
        if (previousStage != null) {
            try {
                primaryStage.setScene(previousStage);
                primaryStage.show();
            } catch (Exception e) {
                Logger.logerror("Failed to load previous scene: " + previousStage);
                e.printStackTrace();
            }
        } else {
            Logger.logerror("No previous stage to return to.");
        }
    }

}
