package com.github.ricedope;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Scanner;
import java.awt.Desktop;

import javax.swing.JFileChooser;

import com.github.ricedope.models.logic.JesterFactory;
import com.github.ricedope.models.logic.Llama3Client;
import com.github.ricedope.models.logic.Plane;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;

public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/XMLCreator.fxml"));
        primaryStage.setTitle("My JavaFX App");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }

}
