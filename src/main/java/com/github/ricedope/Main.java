package com.github.ricedope;

import com.github.ricedope.Logger;

public class Main {
    
    public static void main(String[] args) {

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
         * logginglevel: The level of logging to be used
         */

        String seedtext = "src/main/resources/a-tale-of-two-cities.txt";
        String jesternames = "src/main/resources/Jester-Names.csv";
        String llmprompt = "Please review the following text and return only the corrected version within quotation marks. Do not change the order of any non-duplicated phrases. Remove all duplicated phrases. Correct grammar and punctuation as needed to ensure the sentence flows naturally. Add connector words (e.g., and, but, then) only where necessary for fluidity. Do not include any explanation or extra output—only the revised text in quotation marks.";
        int llmtimeout = 600;
        int jesters = 15;
        int gridsize = 10;
        int minimumpassagelength = 250;
        int interactions = 100;
        Logger.logLevel = Logger.loggingLevel.FEW;

        // Create JesterFactory and Plane Objects
        Logger.logprogress("Creating Jester factory...");
        JesterFactory jf = new JesterFactory(seedtext, minimumpassagelength, jesternames);
        Logger.logprogress("Created Jester factory");
        Logger.logprogress("Creating plane...");
        Plane plane = new Plane(gridsize, jesters, jf);
        Logger.logprogress("Created plane");

        // Begin the interaction loop
        Logger.logprogress("Initiating interactions...");
        for (int i = 0; i < interactions; i++) {
            plane.interactionLoop();
            plane.regenerateGrid();
        }
        Logger.logprogress("Finished interactions");

        // Retrieve the final idea from the plane and sent it to the LLM for final correction
        String finalIdea = plane.getCurrentIdea();
        Logger.logprogress("Final idea found. Sending to LLM for correction...");
        String response = Llama3Client.requester(llmprompt + "[" +finalIdea + "]", llmtimeout);
        Logger.logimportant("Original Idea:\n" + plane.getSeed() + "\n\n");
        Logger.logimportant("Transformed output:\n" + response);


    }
}
