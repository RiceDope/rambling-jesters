# Rambling Jesters

This project aims to bring the benefits of co-creativity to a virtual world, modelling jesters (Virtual agents) interacting and sharing ideas until a new Idea is created. This was created as part of my coursework for a Computational Creativity module at the University of Kent.

## About this branch

This branch is my "official release", a version of the project that I am happy with even. All current development can be found in adding_gui. Whilst some development may occur in other branches it will all be merged with adding_gui before coming here.

## Important notes before running
**JVM** version: 21
**Maven version** 3.9.6
**JavaFX version** 21

When running the program if your laptop or pc is not particularly powerful then close down as much as possible and limit the number of iterations that the program will be ran. The default is 10 and more lead to a better outputs.

As a pre-requisite make sure that [llama3](https://ollama.com/library/llama3) is installed alongside [ollama](https://ollama.com/). These are used for the LLM correction phase. (More on that later)

Make sure that JavaFX is installed in your C: or other directory you can refer to later.

This project was made on and for Windows, it may work on other operating systems but I have not tested.

**ALL INFORMATION** on how to use the project can be found from within the application. Additional information about how the project works under the hood can be found in the branch called *paper* which contains the paper written at University (This was written before GUI and an exact version of the project used can be found [here](https://github.com/rhys-h-walker/rambling-jesters/tree/main)). 

## Running the project

#### Build the project
```
mvn clean install
```
#### Run the program (Adjust javafx path)
```
java --module-path "C:\javafx-sdk-24.0.1\lib" --add-modules javafx.controls,javafx.fxml -cp target\rambling-jesters-DEMO-0.1-jar-with-dependencies.jar com.github.ricedope.Main
```

Enjoy the project I hope you enjoy and look out for future updates.