package com.github.ricedope;


import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.trees.Tree;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class NLP {

    /**
     * Check if a node has a specific label
     * @param node The node to check
     * @param label The label to check for NP, VP, PP, etc.
     * @return True if the labels match and false if not
     */
    private static boolean hasLabel(Tree node, String label) {
        return node.label().value().equals(label);
    }

    /**
     * Recursively traverse the tree and create a map of different phrases
     * The Map that is supplied to the tree is the one that will be updated with the phrases that are found
     * @param tree The root of the tree to explore
     * @param phraseMap A HashMap created by NLP.makePhraseMap()
     */
    public static void traverseTree(Tree tree, HashMap<String, ArrayList<Tree>> phraseMap) {
        // Print the current node's value
        if (hasLabel(tree, "NP")){
            phraseMap.get("NP").add(tree);
            return;
        } else if (hasLabel(tree, "VP")){
            phraseMap.get("VP").add(tree);
            return;
        } else if (hasLabel(tree, "PP")){
            phraseMap.get("PP").add(tree);
            return;
        } else if (hasLabel(tree, "ADJP")){
            phraseMap.get("ADJP").add(tree);
            return;
        } else if (hasLabel(tree, "ADVP")){
            phraseMap.get("ADVP").add(tree);
            return;
        }

        // Iterate over the children
        for (Tree child : tree.children()) {
            // Recursively call traverseTree for child nodes
            traverseTree(child, phraseMap);
        }
    }

    /**
     * Create a map of phrases that will be used to store the phrases that are found in the tree
     * @return An empty phraseMap
     */
    public static HashMap<String, ArrayList<Tree>> makePhraseMap() {
        HashMap<String, ArrayList<Tree>> phraseMap = new HashMap<>();
        phraseMap.put("NP", new ArrayList<>());
        phraseMap.put("VP", new ArrayList<>());
        phraseMap.put("PP", new ArrayList<>());
        phraseMap.put("ADJP", new ArrayList<>());
        phraseMap.put("ADVP", new ArrayList<>());
        return phraseMap;
    }

    public static ArrayList<HashMap<String, ArrayList<Tree>>> parseParagraph(CoreDocument doc) {

        // Get a list of each sentence in the document
        List<CoreSentence> sentences = doc.sentences();

        // Each index is a sentence made up of dictionaries of phrases
        ArrayList<HashMap<String, ArrayList<Tree>>> sentencePhraseMap = new ArrayList<>();

        for (CoreSentence sentence : sentences) {
            HashMap<String, ArrayList<Tree>> phraseMap = NLP.makePhraseMap();
            Tree tree = sentence.constituencyParse();
            NLP.traverseTree(tree, phraseMap);
            sentencePhraseMap.add(phraseMap);
        }
        return sentencePhraseMap;
    }

    /**
     * Convert a tree into a string representation of the phrase
     * @param tree The tree to convert
     * @return A string representation of the phrase
     */
    public static String phraseToString(Tree tree) {
        List<Word> words = tree.yieldWords();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i).word();

            if (i > 0 && !word.equals(".")) {
                sb.append(" ");
            }

            sb.append(word);
        }

        return sb.toString();
    }

    public static int[] phraseIn(ArrayList<HashMap<String, ArrayList<Tree>>> sentencePhraseMap, Phrase phraseType) {

        String key = phraseType.getLabel();
        ArrayList<Integer> indices = new ArrayList<>();

        for (int i = 0; i < sentencePhraseMap.size(); i++) {
            HashMap<String, ArrayList<Tree>> phraseMap = sentencePhraseMap.get(i);
            ArrayList<Tree> phraseList = phraseMap.get(key);

            if (phraseList != null && !phraseList.isEmpty()) {
                indices.add(i);
            }
        }

        // Convert to int[]
        return indices.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * Swaps out a random phrase of some kind from the first idea with a random phrase of the same kind from the second idea
     * Should nothing change or they not be compatible an empty string will be returned
     * 
     * @param ourIdea The idea to swap a phrase into
     * @param externalIdea The idea to swap a phrase from
     * @param toSwap The type of phrase to swap (NP, VP, PP, etc.)
     * @param currentIdea The current idea that is being worked on
     * @return The new idea with the swapped phrase or an empty string if nothing was swapped
     */
    public static String swapAPhrase(ArrayList<HashMap<String, ArrayList<Tree>>> ourIdea, ArrayList<HashMap<String, ArrayList<Tree>>> externalIdea, Phrase toSwap, String currentIdea) {

        // We are all good to swap as the phrase exists
        if (phraseIn(ourIdea,toSwap).length != 0 && phraseIn(externalIdea, toSwap).length != 0) {

            // Get indexes of all arrays with specific phrase
            int[] sentencesWithGivenPhrase = phraseIn(ourIdea, toSwap);
            int[] sentencesWithGivenPhraseEx = phraseIn(externalIdea, toSwap);

            // Select a random index from where we know the array to exist
            int swappingIndex = sentencesWithGivenPhrase[(int) (Math.random() * sentencesWithGivenPhrase.length)];
            int swappingIndexEx = sentencesWithGivenPhraseEx[(int) (Math.random() * sentencesWithGivenPhraseEx.length)];

            // Get the array of phrases that we can select from
            ArrayList<Tree> ourPhrases = ourIdea.get(swappingIndex).get(toSwap.getLabel());
            ArrayList<Tree> externalPhrases = externalIdea.get(swappingIndexEx).get(toSwap.getLabel());

            // Get a random phrase from each array
            Tree ourRandomPhrase = ourPhrases.get((int) (Math.random() * ourPhrases.size()));
            Tree externalRandomPhrase = externalPhrases.get((int) (Math.random() * externalPhrases.size()));

            // Replace our phrase with the external phrase
            String newIdea = currentIdea.replace(phraseToString(ourRandomPhrase), phraseToString(externalRandomPhrase));

            // Return our changed phrase
            return newIdea;

        }

        return "";

    }

    public static Phrase randomPhrase() {
        // Select a random enum from the Phrase enum
        Phrase[] phrases = Phrase.values();
        int randomIndex = (int) (Math.random() * phrases.length);
        return phrases[randomIndex];
    }

    /**
     * Attempts to find a sentence that is close to our given sentiment score
     * If it cannot then it returns null
     * 
     * Close is defined as one either side for neutral
     * If any kind of negative will look soley for a negative score
     * If any kind of positive will look soley for a positive score
     * @param sentiment The sentiment score 1-5 that we are looking for
     * @param otherDoc The CoreDocument object that we wish to search through
     * @return The closest sentence to the sentiment score or null if none is found
     */
    public static CoreSentence closestToSentiment(int sentiment, CoreDocument otherDoc) {
        // Tally up the sentiment scores as a number in an ArrayList
        // Look for a sentence that is either the same sentiment or close
        // To do this find the number that is 1 either side of the sentiment score
        // If not close then ignore and return null
        // Will prefer a sentiment score that is more like their personality
        // neutral (Either side)
        // Negative: Will prefer Very Negative or Negative scores
        // Positive: Will do the same as above
        ArrayList<Integer> sentimentScores = new ArrayList<>();
        for (CoreSentence sentence : otherDoc.sentences() ){

            sentimentScores.add(sentimentScoreNumbers(sentence.sentiment()));

        }

        // Where is our current sentiment score
        switch(sentiment) {
            case 1:
            case 2:
                // Look for a Negative sentiment score
                for (Integer score : sentimentScores) {
                    if (score == 1 || score == 2) {
                        return otherDoc.sentences().get(sentimentScores.indexOf(score));
                    }
                }
                break;
            case 4:
            case 5:
                // Look for a Positive sentiment score
                for (Integer score : sentimentScores) {
                    if (score == 4 || score == 5) {
                        return otherDoc.sentences().get(sentimentScores.indexOf(score));
                    }
                }
                break;
            default:
                // Look for a Neutral, Positive or Negative sentiment score
                for (Integer score : sentimentScores) {
                    if (score == 2 || score == 3 || score == 4) {
                        return otherDoc.sentences().get(sentimentScores.indexOf(score));
                    }
                }
                break;
        }

        // If we have not found a sentence that is close to the sentiment score then return null
        return null;

    }

    /**
     * Convert the sentiment score of CoreNLP sentiment to a number 1-5
     * 1 = Very Negative
     * 5 = Very positive
     * @param sentiment The sentiment score that comes out of the function
     * @return A number 1 through 5
     */
    public static int sentimentScoreNumbers(String sentiment) {
        // Switch statement to detect the sentiment
        switch (sentiment) {
            case "Very Negative":
                return 1;
            case "Negative":
                return 2;
            case "Neutral":
                return 3;
            case "Positive":
                return 4;
            case "Very Positive":
                return 5;
            default:
                return 0; // Default case if sentiment is not recognized
        }
    }
    
    /**
     * Send a string off to a running llama3 model and get a response back
     * Default timeout: 5 minutes.
     * Prompt:  Please review the following text and return only the corrected version within quotation marks. 
     *          Do not change the order of any non-duplicated phrases. Remove all duplicated phrases. Correct 
     *          grammar and punctuation as needed to ensure the sentence flows naturally. Add connector words 
     *          (e.g., and, but, then) only where necessary for fluidity. Do not include any explanation or 
     *          extra output—only the revised text in quotation marks. [**Your prompt text**]
     * @param finalIdea The final output string from the Jester
     * @return Final transformed output from the llama3 model
     */
    public static String tidyupParagraph(String finalIdea) {
        String NLPPrompt = "Please review the following text and return only the corrected version within quotation marks. Do not change the order of any non-duplicated phrases. Remove all duplicated phrases. Correct grammar and punctuation as needed to ensure the sentence flows naturally. Add connector words (e.g., and, but, then) only where necessary for fluidity. Do not include any explanation or extra output—only the revised text in quotation marks.";
        String response = Llama3Client.requester(NLPPrompt + "[" + finalIdea + "]", 300);
        return response;
    }
}
