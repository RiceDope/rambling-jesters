package com.github.ricedope;

/**
 * Class that outlines each Jester and its attributes
 */

public class Jester {

    private int id; // Give a unique ID based on when the Jester was created (1 through 50)
    private String name; // Name of the Jester (Randomly assigned at runtime)
    private String seed; // Stored as an array of tokens
    private Idea idea; // The idea that the Jester is working on
    public float personalityIndex = (float) Math.random() % 1; // Number 0-1 depending on the range they are more likely to choose a certain idea

    public Jester(int id, String name, String seed) {
        this.id = id;
        this.name = name;
        this.seed = seed;
        idea = new Idea(seed);
    }

    public Idea shareIdea() {
        return idea;
    }

    /**
     * Take in a new Idea and choose how to modify our current idea based on the new idea given
     * @param otherIdea
     */
    public void takeNewIdea(Idea otherIdea) {

        /*
         * Firstly decide how to operate when listening to the new idea
         * 
         * Simply swapping:
         *      This will be some form of phrase swapping. Whatever type that the Jester would like
         * 
         * Personality:
         *      Where the Jesters personality comes into it.
         *      What does this Jester like:
         *          People? Things? etc
         *      
         * Listen to the other Jester:
         *      Does the other Jester recommend anything?
         *      What does the other Jester like?
         *      Does the other Jester even want to share their Idea?
         * 
         * Expand our Jesters phrase:
         *      Should it be added to the beginning or end of the current idea?
         *      If we are interacting with multiple Jesters at once could we build a full new sentance to be added?
         */

    }
    
}
