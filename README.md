# Rambling Jesters

This project aims to bring the benefits of co-creativity to a virtual world, modelling jesters (Virtual agents) interacting and sharing ideas until a new Idea is created. This was created as part of my coursework for a Computational Creativity module at the University of Kent.

## adding-gui

This branch is dedicated to the development of a javafx application to run the project. Currently here is the compile steps for progress:

1. Make sure you have javafx SDK TODO: ADD LINK
2. Make sure you have maven and java 24.0.1 installed
3. Run the command below:
```
mvn clean install
```
4. Then run this change the setting --module-path to where your javafx SDK is located
```
java --module-path "C:\javafx-sdk-24.0.1\lib" --add-modules javafx.controls,javafx.fxml -cp target\rambling-jesters-DEMO-0.1-jar-with-dependencies.jar com.github.ricedope.Main
```