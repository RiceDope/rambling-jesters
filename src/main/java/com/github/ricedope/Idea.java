package com.github.ricedope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;

/**
 * Class that contains each "idea" that a Jester has and uses NLP in order to adjust their current thinking or sentance structure
 * The class is initialised with just a Seed and over time will adjust its current idea based on the interactions with other Ideas
 */

public class Idea {

    public String seed;
    public String currentIdea;
    public CoreDocument doc;
    private StanfordCoreNLP pipeline = NLP.pipeline;
    private ArrayList<HashMap<String, ArrayList<Tree>>> sentencePhraseMap = new ArrayList<HashMap<String, ArrayList<Tree>>>(); // Map of the sentences and the phrases that are in them
    private List<CoreSentence> sentences = new ArrayList<CoreSentence>(); // List of the sentences that are in the document


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

        sentencePhraseMap = NLP.parseParagraph(doc);
        sentences = doc.sentences();
        doc = null;
    }

    public List<CoreSentence> getSentences() {
        return sentences;
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
        sentencePhraseMap = NLP.parseParagraph(doc);
        sentences = doc.sentences();
        doc = null;
    }

    public ArrayList<HashMap<String, ArrayList<Tree>>> getDoc() {
        return sentencePhraseMap;
    }

    /**
     * The current working idea that the Jester has.
     */
    public String toString() {
        return currentIdea;
    }
    
}
