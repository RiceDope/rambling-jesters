package com.github.ricedope.controllers;

import com.github.ricedope.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXMLSceneController {
    
    private static Stage primaryStage;
    private static Stage previousStage;

    public static void setPrimaryStage(Stage primaryStage) {
        FXMLSceneController.primaryStage = primaryStage;
    }

    public static void loadScene(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(FXMLSceneController.class.getResource(fxmlFile));
            previousStage = primaryStage;
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void previousScene() {
        if (previousStage != null) {
            primaryStage.setScene(previousStage.getScene());
            primaryStage.show();
        } else {
            Logger.logerror("No previous stage to return to.");
        }
    }

}
