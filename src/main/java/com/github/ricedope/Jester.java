package com.github.ricedope;

/**
 * Class that outlines each Jester and its attributes
 */

public class Jester {

    private int id; // Give a unique ID based on when the Jester was created (1 through 50)
    private String name; // Name of the Jester (Randomly assigned at runtime)
    private String seed; // Stored as an array of tokens
    private Idea idea; // The idea that the Jester is working on

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
     * Take in a new Idea and choose what to swap from the newIdea into the current Idea
     * Can take a few routes:
     *      - Swap noun/verb/prepositional phrases
     *      - Mix subject/verb/object structure across different sentences
     *      - If the other sentance is similar enough can merge to create a new story
     *          - Same named entity for example
     * @param otherIdea
     */
    public void takeNewIdea(Idea otherIdea) {

    }
    
}
