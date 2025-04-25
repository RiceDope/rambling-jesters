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
    
    private String textCorpus;
    private ArrayList<String> passages;
    private ArrayList<String> names;
    private int minimumPassageLength;
    private int maximumpassagelength = 0; // Maximum length of the passage to be used

    /**
     * Constructor for the JesterFactory class
     * @param path Path to then gutenburg .txt file to use
     * @param minimumPassageLength Minimum length of the passage to be used
     * @param jesterNames Path to the .csv file containing the names of the Jesters
     */
    public JesterFactory(String path, int minimumPassageLength, String jesterNames, int maxpassagelength) {
        // Prepare the text for assignment
        textCorpus = TextPreProcessor.finalCleanupForNLP(TextPreProcessor.removeChapterNamesAndLeadingContents(TextPreProcessor.snipGutenbergStartAndEnd(TextPreProcessor.readFile(path))));
        // Split the text into passages
        String[] rawpassages = textCorpus.split("\\n\\s*\\n");
        passages = new ArrayList<>(Arrays.asList(rawpassages));
        // Read names in from a CSV file
        String[] rawNames = TextPreProcessor.simpleCSVReader(jesterNames);
        names = new ArrayList<>(Arrays.asList(rawNames));
        // Set minimum passage length and cleanse the text available
        this.minimumPassageLength = minimumPassageLength;
        this.maximumpassagelength = maxpassagelength;
        cleansePassages();
    }

    /**
     * Create a new Jester with a unique ID, name and passage of text
     * @return The new Jester Object
     */
    public Jester newJester() {
        // Create a new Jester with a unique ID, name and passage of text
        String name = getNewName();
        String seed = getNewPassage();
        if (seed.length() < minimumPassageLength) {
            // If the passage is too short, get a new one
            seed = getNewPassage();
        }
        return new Jester(name, seed, maximumpassagelength);
    }

    /**
     * The total number of possible Jesters that can be created based on the names available and the text corpus
     * @return
     */
    public int possibleJesters() {
        if (names.size() > passages.size()) {
            return passages.size();
        } else {
            return names.size();
        }
    }

    /**
     * Remove passages that violate the minimum passage length
     * No maximum passage length is set, but minimum is set at construction
     */
    private void cleansePassages() {
        // Remove passages that are too short
        passages.removeIf(passage -> passage.length() < minimumPassageLength);
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
