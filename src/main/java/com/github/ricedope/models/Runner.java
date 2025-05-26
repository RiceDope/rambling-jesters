package com.github.ricedope.models;

import java.io.File;

import com.github.ricedope.Logger;
import com.github.ricedope.models.logic.JesterFactory;
import com.github.ricedope.models.logic.Llama3Client;
import com.github.ricedope.models.logic.Output;
import com.github.ricedope.models.logic.Plane;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;

/**
 * Class that handles a single run of the program
 */

public class Runner {

    /**
     * Given a filepath run the main rambling-jesters program
     * @param filepath
     */
    public static Output runMainProgram(String filepath) {
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
            return null;
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
        int maximumpassagelength;
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
            maximumpassagelength = Integer.parseInt(root.getFirstChildElement("maximumpassagelength").getValue());
            Logger.logprogress("Maximum passage length: " + maximumpassagelength);
            interactions = Integer.parseInt(root.getFirstChildElement("interactions").getValue());
            Logger.logprogress("Number of interactions: " + interactions);
        } catch (Exception e) {
            Logger.logerror("Error loading components from XML file: " + e.getMessage());
            System.exit(0);
            return null;
        }
        Logger.logprogress("Loaded individual components successfully.");

        // Create JesterFactory and Plane Objects
        Logger.logprogress("Creating Jester factory...");
        JesterFactory jf = new JesterFactory(seedtext, minimumpassagelength, jesternames, maximumpassagelength);
        Logger.logprogress("Created Jester factory");
        Logger.logprogress("Creating plane (Can take some time) ...");
        Plane plane = new Plane(gridsize, jesters, jf);
        Logger.logprogress("Created plane");

        // Allow cleanup of the JesterFactory object to free up memory
        jf = null;

        // Begin the interaction loop
        Logger.logprogress("Initiating interactions (Can take some time) ...");
        for (int i = 0; i < interactions; i++) {
            Logger.logprogress("Interaction: " + (i+1) + "/" + interactions);
            plane.interactionLoop();
            Logger.logprogress("Current Text Size: " + plane.getCurrentIdea().length() + "/" + maximumpassagelength);
            plane.regenerateGrid();
        }
        Logger.logprogress("Finished interactions");

        // Retrieve the final idea from the plane and sent it to the LLM for final correction
        String finalIdea = plane.getCurrentIdea();
        String seed = plane.getSeed();
        String name = plane.getName();
        plane = null; // memory cleanup

        Logger.logprogress("Final idea found. Sending to LLM for correction...");

        // Handle Ollama server starting etc
        Process p = Llama3Client.startup();
        Llama3Client.wait(5);
        String response = Llama3Client.requester(llmprompt + "[" +finalIdea + "]", llmtimeout);

        Logger.logprogress("LLM response received. Sending to LLM for evaluation...");

        String evaluation = Llama3Client.requester("Please judge the creativity of the following text:\n" + //
                        "\n" + //
                        "Is it original compared to typical writing?\n" + //
                        "\n" + //
                        "Is it coherent (makes some logical sense)?\n" + //
                        "\n" + //
                        "Is it surprising or unexpected?\n" + //
                        "\n" + //
                        "Write a brief evaluation (2 sentences maximum).**\n" + //
                        "\n" + //
                        "Do not include any text other than that of your answer" + //
                        "Text:" + response, llmtimeout);

        Logger.clearConsole();

        Logger.logprogress("Original Idea:\n" + seed + "\n\n");
        Logger.logprogress("Jester-"+name+":\n" + response);
        Logger.logprogress("Evaluation:\n" + evaluation);

        p.destroy(); // Destroy the process to free up memory

        // Create a new Output object and populate it
        Output output = new Output(name, seed, finalIdea, response, llmprompt, interactions, gridsize, minimumpassagelength, maximumpassagelength, jesternames, seedtext, filepath, String.valueOf(llmtimeout), evaluation);
        return output;
    }
}
