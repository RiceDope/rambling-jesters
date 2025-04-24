package com.github.ricedope;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Scanner;

import javax.swing.JFileChooser;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;

public class Main {
    
    public static void main(String[] args) {
        // Manually set the logging level for the testing phase
        Logger.logLevel = Logger.loggingLevel.NONE;

        // Ask the user if they would like to supply their own XML file or would like to use the default
        String filepath; // The path to the XML file to use

        // Pre-defined text to show to the user
        String titleMessage = ANSI.RED_BACKGROUND + "==================================" + ANSI.RESET + "\n"  + ANSI.RED_BACKGROUND + "=" + ANSI.RESET + ANSI.GREEN + "        Rambling Jesters        " + ANSI.RESET  + "" + ANSI.RED_BACKGROUND + "=" + ANSI.RESET + "\n" + ANSI.RED_BACKGROUND + "==================================" + ANSI.RESET;
        String choiceMessage = """
        Welcome to the Rambling Jesters project!
        The goal of the program is to harness Co-Creativity in order to create new ideas and stories with existing material
        The program will exit on completion of a set of Jester interactions, or if an error occurs.
        Press ctrl + c to force exit the program at any time.
        Select one of the following five options:
        1. Use a pre-created XML file (You will be prompted to select a file)
        2. Use the default XML file (No extra setup required)
        3. Create a new XML file (You will be taken step by step to make a new XML file)
        4. Exit the program (Closes down)
        5. Help! (Shows this message again with more information)
        """;

        boolean programLoop = true;
        while (programLoop) {
            Scanner scanner = new Scanner(System.in);

            Logger.clearConsole();
            Logger.logLevel = Logger.loggingLevel.NONE; // Set the logging level to NONE to avoid cluttering the console

            System.out.println(titleMessage + "\n" + choiceMessage);
            System.out.println(">>>");
            String choice = scanner.nextLine().toUpperCase();

            switch(choice) {
                case "1":
                    System.out.println("Please select the path to the XML file when the dialogue pops up. (SPACE)");
                    scanner.nextLine();
                    JFileChooser chooser = new JFileChooser();
                    int result = chooser.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        filepath = chooser.getSelectedFile().getAbsolutePath();
                        Logger.logprogress("Using XML file: " + filepath);
                        runMainProgram(filepath);
                    } else {
                        Logger.logerror("No file selected. Using default XML file.");
                        System.exit(0);
                        break;
                    }
                    break;
                case "2":
                    Logger.logprogress("Using default XML file.");
                    filepath = "src/main/resources/default.xml";
                    runMainProgram(filepath);
                    break;
                case "3":
                    Logger.logprogress("Creating new XML file...");
                    filepath ="";
                    createNewXMLFile();
                    System.exit(0);
                    break;
                case "4":
                    Logger.logprogress("Exiting program...");
                    System.exit(0);
                    break;
                case "5":
                    Logger.logLevel = Logger.loggingLevel.ALL; // Set the logging level to ALL to stop console clearning when showing help
                    System.out.println("THIS WILL BE THE HELP MESSAGE");
                    break;
                default:
                    Logger.logerror("Invalid input. Not a recognised option.");
                    break;
            }
        }
    }

    public static void createNewXMLFile() {

        Logger.clearConsole();
        System.out.println("Welcome to the XML file creation tool!");
        System.out.println("We will create a new XML file step by step then allow you to add it to the program.");
        System.out.println("All files are available on the github project at: ");
        System.out.println("https://github.com/RiceDope/rambling-jesters");
        Element root = new Element("runner");
        root.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");

        // Select the seedtext file
        System.out.println("Please select the path to the seedtext file (This is recommended to be a .txt from the gutenberg project)");
        JFileChooser chooser = new JFileChooser();
        String seedtextpath;
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            seedtextpath = chooser.getSelectedFile().getAbsolutePath();
            Element seedtext = new Element("seedtext");
            seedtext.appendChild(seedtextpath);
            root.appendChild(seedtext);
            Logger.logprogress("Seed text selected: " + seedtextpath);
        } else {
            Logger.logerror("No file selected. Exiting program");
            System.exit(0);
        }

        // Select the jesernames file
        System.out.println("Please select the path to the jesternames file (This is recommended to be a .csv file with all names on one line)");
        chooser = new JFileChooser();
        String jesternamespath;
        result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            jesternamespath = chooser.getSelectedFile().getAbsolutePath();
            Element jesternames = new Element("jesternames");
            jesternames.appendChild(jesternamespath);
            root.appendChild(jesternames);
            Logger.logprogress("Jester names selected: " + jesternamespath);
        } else {
            Logger.logerror("No file selected. Exiting program");
            System.exit(0);
        }

        // Type a prompt for the LLM model to use:
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please type a prompt for the LLM model to use (Recommended to use the default): ");
        System.out.println("To use default, type <default>");
        String llmprompt = scanner.nextLine();
        if (llmprompt.equals("<default>")) {
            llmprompt = "Please review the following text and return only the corrected version within quotation marks. Do not change the order of any non-duplicated phrases. Remove all duplicated phrases. Correct grammar and punctuation as needed to ensure the sentence flows naturally. Add connector words (e.g., and, but, then) only where necessary for fluidity. Do not include any explanation or extra output—only the revised text in quotation marks.";
        } else {
            Logger.logprogress("LLM prompt selected: " + llmprompt);
        }
        Element llmpromptElement = new Element("llmprompt");
        llmpromptElement.appendChild(llmprompt);
        root.appendChild(llmpromptElement);

        // Select the LLM timeout
        System.out.println("Please type the timeout for the LLM model to use. For slower machines this should be higher 600 secs");
        System.out.println("This is how long the program will await a response from the local running llama3 model before giving up.");
        String llmtimeoutString = scanner.nextLine();
        Element llmtimeoutElement = new Element("llmtimeout");
        llmtimeoutElement.appendChild(llmtimeoutString);
        root.appendChild(llmtimeoutElement);

        // Select the number of Jesters to create
        System.out.println("Please type the number of Jesters to create. This should be less than the number of possible Jesters.");
        System.out.println("This is the number of Jesters that will be created and used in the program.");
        String jestersString = scanner.nextLine();
        Element jestersElement = new Element("jesters");
        jestersElement.appendChild(jestersString);
        root.appendChild(jestersElement);

        // Select the gridsize
        System.out.println("Please type the size of the grid to use. This is the size of the grid that the Jesters will interact on.");
        System.out.println("The grid will be nxn in size. On each interaction the Base Jester will look in a gridsize/3 radius around them.");
        String gridsizeString = scanner.nextLine();
        Element gridsizeElement = new Element("gridsize");
        gridsizeElement.appendChild(gridsizeString);
        root.appendChild(gridsizeElement);

        // Minimum passage length
        System.out.println("Please type the minimum passage length to use. This is the minimum length of the passage each Jester starts with.");
        System.out.println("This is recommended to be 250 characters for reasonable results.");
        String minimumpassagelengthString = scanner.nextLine();
        Element minimumpassagelengthElement = new Element("minimumpassagelength");
        minimumpassagelengthElement.appendChild(minimumpassagelengthString);
        root.appendChild(minimumpassagelengthElement);

        // Select the number of interactions
        System.out.println("Please type the number of interactions to use. This is the number of interactions the Jester will have with other Jesters.");
        System.out.println("100 generates reasonable results, however if your PC is slower then this may crash or take upwards of 15 minutes.");
        String interactionsString = scanner.nextLine();
        Element interactionsElement = new Element("interactions");
        interactionsElement.appendChild(interactionsString);
        root.appendChild(interactionsElement);

        // Select a new directory to save the XML file to
        chooser = new JFileChooser();
        // Set mode to directories only
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        // Optional: Disable "All Files" filter
        chooser.setAcceptAllFileFilterUsed(false);
        result = chooser.showOpenDialog(null);
        File selectedDirectory;
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedDirectory = chooser.getSelectedFile();
            System.out.println("Selected path: " + selectedDirectory.getAbsolutePath());

            System.out.println("Now please name your running file. (No spaces or special characters)");
            String filename = scanner.nextLine();

            Document doc = new Document(root);
            try {
                OutputStream os = new FileOutputStream(selectedDirectory.getAbsolutePath()+"\\"+filename+".xml");
                Serializer serializer = new Serializer(os, "UTF-8");
                serializer.setIndent(4);
                serializer.write(doc);
                serializer.flush();
                os.close();
            } catch (Exception e) {
                Logger.logerror("Error creating XML file: " + e.getMessage());
                System.exit(0);
            }
        } else {
            Logger.logerror("No selection made. Exiting program");
            System.exit(0);
        }
        
        Logger.logprogress("XML file created successfully");

    }

    /**
     * Given a filepath run the main rambling-jesters program
     * @param filepath
     */
    public static void runMainProgram(String filepath) {
        Logger.logprogress("Using XML file: " + filepath);

        // Load the XML file
        Logger.logprogress("Loading XML file...");
        File runnerData = new File(filepath);
        Builder builder = new Builder();

        Element root;
        try {
            Document doc = builder.build(runnerData);
            root = doc.getRootElement();
        } catch (Exception e) {
            Logger.logerror("Error loading XML file: " + e.getMessage());
            System.exit(0);
            return;
        }

        Logger.logprogress("Loaded XML file successfully.");
        Logger.logprogress("Now loading individual components...");

        // Load the individual components from the XML file

        /*
        * Customisable runtime parameters
        * seedtext: The .txt file to be used a source material for the Jesters to use
        * jesternames: The .csv file to be used as a source for the names of the Jesters (One line csv file)
        * llmprompt: The prompt to be used for the LLM model. (recommended to use the default)
        * llmtimeout: The timeout for the LLM model to respond (In seconds)
        * jesters: The number of Jesters to be created (must be less than the number of possible Jesters)
        * gridsize: The size of the grid that the Jesters will interact on
        * minimumpassagelength: The minimum length of the passage each Jester starts with
        * interactions: The number of interactions the Jester will have with other Jesters
        */

        String seedtext;
        String jesternames;
        String llmprompt;
        int llmtimeout;
        int jesters;
        int gridsize;
        int minimumpassagelength;
        int interactions;

        try {
            seedtext = root.getFirstChildElement("seedtext").getValue();
            Logger.logprogress("Seed path: " + seedtext);
            jesternames = root.getFirstChildElement("jesternames").getValue();
            Logger.logprogress("Jester names path: " + jesternames);
            llmprompt = root.getFirstChildElement("llmprompt").getValue();
            Logger.logprogress("LLM prompt: " + llmprompt);
            llmtimeout = Integer.parseInt(root.getFirstChildElement("llmtimeout").getValue());
            Logger.logprogress("LLM timeout: " + llmtimeout);
            jesters = Integer.parseInt(root.getFirstChildElement("jesters").getValue());
            Logger.logprogress("Number of Jesters: " + jesters);
            gridsize = Integer.parseInt(root.getFirstChildElement("gridsize").getValue());
            Logger.logprogress("Grid size: " + gridsize);
            minimumpassagelength = Integer.parseInt(root.getFirstChildElement("minimumpassagelength").getValue());
            Logger.logprogress("Minimum passage length: " + minimumpassagelength);
            interactions = Integer.parseInt(root.getFirstChildElement("interactions").getValue());
            Logger.logprogress("Number of interactions: " + interactions);
        } catch (Exception e) {
            Logger.logprogress("Error loading components from XML file: " + e.getMessage());
            System.exit(0);
            return;
        }
        Logger.logprogress("Loaded individual components successfully.");

        // Create JesterFactory and Plane Objects
        Logger.logprogress("Creating Jester factory...");
        JesterFactory jf = new JesterFactory(seedtext, minimumpassagelength, jesternames);
        Logger.logprogress("Created Jester factory");
        Logger.logprogress("Creating plane (Can take some time) ...");
        Plane plane = new Plane(gridsize, jesters, jf);
        Logger.logprogress("Created plane");

        // Begin the interaction loop
        Logger.logprogress("Initiating interactions (Can take some time) ...");
        for (int i = 0; i < interactions; i++) {
            Logger.logprogress("Interaction" + (i+1) + "/" + interactions);
            plane.interactionLoop();
            plane.regenerateGrid();
        }
        Logger.logprogress("Finished interactions");

        // Retrieve the final idea from the plane and sent it to the LLM for final correction
        String finalIdea = plane.getCurrentIdea();
        String seed = plane.getSeed();
        plane = null; // memory cleanup

        Logger.logprogress("Final idea found. Sending to LLM for correction...");

        // Handle Ollama server starting etc
        Process p = Llama3Client.startup();
        Llama3Client.wait(5);
        String response = Llama3Client.requester(llmprompt + "[" +finalIdea + "]", llmtimeout);

        Logger.clearConsole();

        Logger.logprogress("Original Idea:\n" + seed + "\n\n");
        Logger.logprogress("Transformed output:\n" + response);

        p.destroy(); // Destroy the process to free up memory

        // Exit the program to allow reading of the output text
        System.exit(0);
    }
}
