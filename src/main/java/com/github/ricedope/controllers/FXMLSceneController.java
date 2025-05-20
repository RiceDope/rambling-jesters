package com.github.ricedope.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXMLSceneController {
    
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage primaryStage) {
        FXMLSceneController.primaryStage = primaryStage;
    }

    public static void loadScene(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(FXMLSceneController.class.getResource(fxmlFile));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
