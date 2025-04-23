package com.github.ricedope;

import java.io.File;
import java.util.Scanner;

import javax.swing.JFileChooser;

import com.github.ricedope.Logger;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;

public class Main {
    
    public static void main(String[] args) {
        // Manually set the logging level for the testing phase
        Logger.logLevel = Logger.loggingLevel.ALL;

        // Ask the user if they would like to supply their own XML file or would like to use the default
        Scanner scanner = new Scanner(System.in);
        String filepath; // The path to the XML file to use

        System.out.println("Would you like to specify your own XML file? (y/n)?");
        String choice = scanner.nextLine().toUpperCase();

        switch(choice) {
            case "Y":
                System.out.println("Please select the path to the XML file when the dialogue pops up. (SPACE)");
                scanner.nextLine();
                JFileChooser chooser = new JFileChooser();
                int result = chooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    filepath = chooser.getSelectedFile().getAbsolutePath();
                    Logger.logprogress("Using XML file: " + filepath);
                } else {
                    Logger.logerror("No file selected. Using default XML file.");
                    filepath = "src/main/resources/default.xml";
                }
                break;
            case "N":
                Logger.logprogress("Using default XML file.");
                filepath = "src/main/resources/default.xml";
                break;
            default:
                Logger.logerror("Invalid input. Using default XML file.");
                filepath = "src/main/resources/default.xml";
                break;
        }

        scanner.close();
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
            Logger.logprogress("Error loading XML file: " + e.getMessage());
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

        Logger.logprogress("Original Idea:\n" + seed + "\n\n");
        Logger.logprogress("Transformed output:\n" + response);

        p.destroy(); // Destroy the process to free up memory
    }
}
