package com.github.ricedope;

/**
 * Class that outlines each Jester and its attributes
 */

public class Jester {

    private int id; // Give a unique ID based on when the Jester was created (1 through 50)
    private String name; // Name of the Jester (Randomly assigned at runtime)
    private String seed; // Stored as an array of tokens
    // private Word[] words; // Array of words that is the working memory of the Jester
    /*
     * Word [
     *   String text; // The word itself
     *   Time timeframe; // When the word was added to the idea
     *   Type type; // The type of word (noun, verb, adjective, etc.)
     *   float likeability; // How much the Jester likes the word (0-1)
     * ]
     */
    // String[] favouriteWords; // The words that the Jester likes
    // Characteristic preferedCharacteristics; // The characteristics the Jester likes
    /*
     * Characteristic [
     *   Gender gender; // What gender does the Jester like
     *   Age age; // What age does the Jester like
     * ]
     */

    

    public Jester(int id, String name, String seed) {
        this.id = id;
        this.name = name;
        this.seed = seed;
    }
    
}
