package com.github.ricedope;

/**
 * Class that manages the creation of individual Jesters
 * 
 * @author Rhys Walker
 * @since 14/04/2025
 */

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Creates and assigns a new Jester
 * 
 * @author Rhys Walker
 * @since 13/04/2025
 */

public class JesterFactory {
    
    // Most recently assigned ID #
    private int currentId = 0;
    private String textCorpus;
    private ArrayList<String> passages;
    private ArrayList<String> names;

    /**
     * Constructor for the JesterFactory class
     * @param path Path to then gutenburg .txt file to use
     */
    public JesterFactory(String path) {
        // Prepare the text for assignment
        textCorpus = TextPreProcessor.finalCleanupForNLP(TextPreProcessor.removeChapterNamesAndLeadingContents(TextPreProcessor.snipGutenbergStartAndEnd(TextPreProcessor.readFile(path))));
        // Split the text into passages
        String[] rawpassages = textCorpus.split("\\n\\s*\\n");
        passages = new ArrayList<>(Arrays.asList(rawpassages));
        // Read names in from a CSV file
        String[] rawNames = TextPreProcessor.simpleCSVReader("src/main/resources/Jester-Names.csv");
        names = new ArrayList<>(Arrays.asList(rawNames));
    }

    /**
     * Create a new Jester with a unique ID, name and passage of text
     * @return The new Jester Object
     */
    public Jester newJester() {
        // Create a new Jester with a unique ID, name and passage of text
        int id = getNewId();
        String name = getNewName();
        String seed = getNewPassage();
        if (seed.length() < 250) {
            // If the passage is too short, get a new one
            seed = getNewPassage();
        }
        return new Jester(id, name, seed);
    }

    /**
     * Create a new ID with no way of reassigning it
     * @return Integer ID
     */
    private int getNewId() {
        currentId++;
        return currentId;
    }

    /**
     * Get a new passage of text with no way of duplication
     * @return String which is a whole paragraph of text
     */
    private String getNewPassage() {
        // Get a random passage from the text corpus
        int randomIndex = (int) (Math.random() * passages.size());
        String passage = passages.get(randomIndex);
        // Remove the passage from the array to avoid duplicates
        passages.remove(randomIndex);
        return passage;
    }

    /**
     * Get a new name for the Jester with no way of duplication
     * @return String which is the Jesters name
     */
    private String getNewName() {
        // Get a random name from the names array
        int randomIndex = (int) (Math.random() * names.size());
        String name = names.get(randomIndex);
        // Remove the name from the array to avoid duplicates
        names.remove(randomIndex);
        return name;
    }
}
