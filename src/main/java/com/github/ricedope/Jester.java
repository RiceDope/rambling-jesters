package com.github.ricedope;

import java.util.ArrayList;
import java.util.HashMap;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.trees.Tree;

/**
 * Class that outlines each Jester and its attributes
 */

public class Jester {

    private int id; // Give a unique ID based on when the Jester was created (1 through 50)
    private String name; // Name of the Jester (Randomly assigned at runtime)
    private String seed; // Stored as an array of tokens
    private Idea idea; // The idea that the Jester is working on
    public int sentiment = ((int) (Math.random()*5)+1); // Sentiment of the Jester 1-5 (1 being very negative and 5 being very positive)
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
     * Return the seed this Jester has been made with
     * @return The seed
     */
    public String getSeed() {
        return seed;
    }

    /**
     * Return the first sentence that matches this Jesters current sentiment
     * @return CoreSentence that matches this Jesters sentiment
     */
    public CoreSentence recommendSentence() {

        return NLP.closestToSentiment(sentiment, idea.getDoc());

    }

    /**
     * Take in a new Idea and choose how to modify our current idea based on the new idea given
     * @param otherJester
     */
    public void growIdea(Jester otherJester) {

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
         *      Cherry picks words that can be changed. Pronouns/Nouns/Places/Things
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

         /*
          * Relative chance of each adjustment happening:
                Swap Phrase: 35% This changes a lot about the current Idea so should be the most common
                Personality: 20% This allows each Jester to cherrypick what they like and dislike
                Listen to the other Jester: 20% The other Jester will take cherry pick their "favourite" parts of their idea
                Expand the Jesters Phrase: 25% This allows the Jester to expand their idea. Tag a sentence to the beginning or end of their current
          */

        int random = (int) (Math.random() * 100); // Random number between 0 and 100

        if (random <= 35) { // Swap a phrase
            System.out.println("Swapping a phrase");

            // Get parse tree of our new Idea
            CoreDocument newDoc = otherJester.shareIdea().getDoc();
            ArrayList<HashMap<String, ArrayList<Tree>>> sentencePhraseMap = NLP.parseParagraph(newDoc);

            // Get the parse tree of the original idea
            CoreDocument originalDoc = idea.getDoc();
            ArrayList<HashMap<String, ArrayList<Tree>>> originalSentencePhraseMap = NLP.parseParagraph(originalDoc);

            // Now allow the swapping of a random style of phrase
            Phrase randomPhrase = NLP.randomPhrase();
            String newIdea = NLP.swapAPhrase(originalSentencePhraseMap, sentencePhraseMap, randomPhrase, idea.currentIdea);

            // Adjust the new Idea based on what was selected
            if (newIdea.equals("")){
                System.out.println("No new idea was created, no similar " + randomPhrase.getLabel() + " found");
            } else {
                idea.takeNewIdea(newIdea);
                System.out.println("New Idea Being taken: " + newIdea);
            }

        } else if (random <= 55) { // Personality
            System.out.println("Using personality to adjust idea with");

            // Jester will select the sentence from the other Jester that most closey matches their personality
            CoreSentence closest = NLP.closestToSentiment(sentiment, otherJester.shareIdea().getDoc());
            if (closest == null) {
                System.out.println("No sentences found with the same sentiment as " + sentiment);
                return;
            }

            StringBuilder sb = new StringBuilder();
            int chance = (int) (Math.random() * 100);
            if (chance < 50) {
                // Append at the beginning
                sb.append(closest.text());
                sb.append(idea.currentIdea);
            } else {
                // Append at the end
                sb.append(idea.currentIdea);
                sb.append(closest.text());
            }

            idea.takeNewIdea(sb.toString());

        } else if (random <= 75) { // Listen to the other Jester
            System.out.println("Listening to the other Jester");

            // Get the other Jesters recomended sentence and then append it to either the end or beginning of the current sentence
            CoreSentence recomended = otherJester.recommendSentence();
            if (recomended == null) {
                System.out.println("No sentences found with the same sentiment as " + otherJester.sentiment);
                return;
            }

            StringBuilder sb = new StringBuilder();
            int chance = (int) (Math.random() * 100);
            if (chance < 50) {
                // Append at the beginning
                sb.append(recomended.text());
                sb.append(idea.currentIdea);
            } else {
                // Append at the end
                sb.append(idea.currentIdea);
                sb.append(recomended.text());
            }

            idea.takeNewIdea(sb.toString());

        } else { // Expand the Jesters Phrase by adding a whole new phrase to the end of the current idea
            System.out.println("Expanding the Jesters Phrase with");
        }

    }
    
}
