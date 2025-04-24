# Rambling Jesters

This project aims to bring the benefits of co-creativity to a virtual world, modelling jesters (Virtual agents) interacting and sharing ideas until a new Idea is created. This was created as part of my coursework for a Computational Creativity module at the University of Kent.

## What is an idea?

Fundamentally an idea in this context is taking some form of input text and splitting it up and assigning each Jester with some amount of the source text as a seed. They then interact with eachother based on their individual personalities and at the end the base Jester outputs their seed text alongside a LLM corrected final paragraph.

## Important notes before running
When running the program if your laptop or pc is not particularly powerful then close down as much as possible and limit the number of iterations that the program will be ran. The default is 10 and more lead to a better output.

As a pre-requisite make sure that [llama3](https://ollama.com/library/llama3) is installed alongside [ollama](https://ollama.com/). These are used for the LLM correction phase. (More on that later)

This project was made on and for Windows, it may work on other operating systems but I have not tested.

## Launching
There are a few methods of running the program that will be presented upon launch. Due to GitHub jar filesize limits I cannot supply a jar alongside this project at the moment so you will have to download the maven and run from there:
```
mvn clean compile assembly:single
```
However if you have the jar run this command:
```
java -jar target/rambling-jesters-1.0-SNAPSHOT-jar-with-dependencies.jar
```

You will be given 5 modes of operation from here. When running for the first time I recommend using option 2 to get an idea of how it works.

### The modes of operation
1. Select a custom XML file which will organise the parameters to run. This does require multiple different files to have been created.
2. Run using the default seed-text jester-names etc. Recommended for first time use.
3. Create a new Custom XML file with prompts as to what each value does. They are also explained below.
4. Quit the program.
5. Get a link to this file.

### Creating an XML file
When creating your own XML file to be ran you need two pre-requisite files a txt file for the seedtext. I recommend something from the Gutenburg Project, when using gutenberg it will remove the boilerplate text from that file automatically. Second a csv file which is just one line of different names for the jester (One is provided on the GitHub).

- llmprompt: This will be given to the llama3 model to tidy up the text that is shown to the user. It is recommended to use the default but you can supply your own. The prompt is ran with the whole final idea added to the end. Make sure you understand your prompt before using it.
- llmtimeout: This is how long the program will wait for the model to respond with its tidied text. For slower laptops I recommend a much longer time. My laptop took up to 10 minutes to compute the result.
- jesters: The total number of jesters that will interact on the plane. This does not include the base jester. Generally I would go for gridSize/10. So whatever you set as the gridsize take a tenth of that and that will be your jester number. Never select more Jesters than your text can handle. If you have 100 Jester names but only 50 eligible paragraphs to assign then you can only have 49 jesters with one base jester.
- gridsize: The size of the grid that the jesters will interact on. Each index can hold one jester. So the max of a 10 gridsize plane is 100 jesters.
- minimumpassagelength: The minimum amount of characters per passage given to a jester. Given a long piece of text it will split it into paragraphs. It splits on the \n\n of the text so make sure that your minimum size is not too big.
- interactions: The total number of times that the jesters will attempt to share ideas.

If all of these are ok then you can run the program and generate a unique output based on the input text. They can seem abstract or nonsensical but will be almost entirely novel creations based around authors works or a combination of authors.