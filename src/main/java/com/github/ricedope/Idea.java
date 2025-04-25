package com.github.ricedope;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * Class that contains each "idea" that a Jester has and uses NLP in order to adjust their current thinking or sentance structure
 * The class is initialised with just a Seed and over time will adjust its current idea based on the interactions with other Ideas
 */

public class Idea {

    public String seed;
    public String currentIdea;
    public CoreDocument doc;
    private StanfordCoreNLP pipeline = NLP.pipeline;


    /**
     * Constructor for the Idea class
     * @param seed The seed is the initial idea that the Jester has. It is a string that will be used to generate new ideas.
     */
    public Idea(String seed) {

        // Set the seed and the current idea (Which is just the initial)
        this.seed = seed;
        this.currentIdea = seed;

        // Annotate the current idea using the pipeline
        doc = new CoreDocument(currentIdea);
        pipeline.annotate(doc);
    }

    /**
     * Take in a new Idea and update the current CoreNLP document with the new idea
     * @param newIdea
     */
    public void takeNewIdea(String newIdea) {
        currentIdea = newIdea;
        // Annotate the new idea using the pipeline
        doc = new CoreDocument(currentIdea);
        pipeline.annotate(doc);
    }

    public CoreDocument getDoc() {
        return doc;
    }

    /**
     * The current working idea that the Jester has.
     */
    public String toString() {
        return currentIdea;
    }
    
}
