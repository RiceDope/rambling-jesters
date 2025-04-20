package com.github.ricedope;

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
         */

        String seedtext = "src/main/resources/a-tale-of-two-cities.txt";
        String jesternames = "src/main/resources/Jester-Names.csv";
        String llmprompt = "Please review the following text and return only the corrected version within quotation marks. Do not change the order of any non-duplicated phrases. Remove all duplicated phrases. Correct grammar and punctuation as needed to ensure the sentence flows naturally. Add connector words (e.g., and, but, then) only where necessary for fluidity. Do not include any explanation or extra output—only the revised text in quotation marks.";
        int llmtimeout = 600;
        int jesters = 15;
        int gridsize = 10;
        int minimumpassagelength = 250;
        int interactions = 100;

        JesterFactory jf = new JesterFactory(seedtext, minimumpassagelength, jesternames);
        Plane plane = new Plane(gridsize, jesters, jf);
        for (int i = 0; i < interactions; i++) {
            plane.interactionLoop();
            plane.regenerateGrid();
        }

        System.out.println("Original Idea:\n" + plane.getSeed() + "\n\n");
        String finalIdea = plane.getCurrentIdea();
        System.out.println("Final Idea:\n" + finalIdea + "\n\n");

        String response = Llama3Client.requester(llmprompt + "[" +finalIdea + "]", llmtimeout);
        System.out.println("Transformed output:\n" + response);


    }
}
