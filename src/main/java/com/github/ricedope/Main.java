package com.github.ricedope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.trees.Tree;

public class Main {
    
    public static void main(String[] args) {

        // String response = Llama3Client.requester("Generate 100 names for a virtual agent called a Jester. Please only repond in the format 'name, name, name, ...' do not include any other text in the response");
        // System.out.println(response);

        // Idea idea = new Idea("The wind whispered secrets to the swaying trees, carrying tales of distant lands. When Rhys found the trees he said my god the trees tell secrets only the leaves know.");

        JesterFactory jf = new JesterFactory("src/main/resources/a-tale-of-two-cities.txt");
        Jester us = jf.newJester();

        System.out.println("Original Paragraph:\n" + us.shareIdea().currentIdea + "\n\n");

        System.out.println("-------\nBeginning Jester Creation\n--------");

        // Create our array of Jesters
        ArrayList<Jester> jesterList = new ArrayList<>();
        for (int i = 0; i<15; i++) {
            jesterList.add(jf.newJester());
            System.out.println("Jester " + i + " has been created");
        }

        System.out.println("-------\nJester Creation Complete\n--------");

        System.out.println("-------\nBeginning Jester Interaction\n--------");

        for (Jester jes : jesterList){
            // Get parse tree of our new Idea
            CoreDocument newDoc = jes.shareIdea().getDoc();
            ArrayList<HashMap<String, ArrayList<Tree>>> sentencePhraseMap = NLP.parseParagraph(newDoc);

            // Get the parse tree of the original idea
            CoreDocument originalDoc = us.shareIdea().getDoc();
            ArrayList<HashMap<String, ArrayList<Tree>>> originalSentencePhraseMap = NLP.parseParagraph(originalDoc);

            // Now allow the swapping of a random style of phrase
            Phrase randomPhrase = randomPhrase();
            String newIdea = NLP.swapAPhrase(originalSentencePhraseMap, sentencePhraseMap, randomPhrase, us.shareIdea().currentIdea);

            // Adjust the new Idea based on what was selected
            if (newIdea.equals("")){
                System.out.println("No new idea was created, no similar " + randomPhrase.getLabel() + " found");
            } else {
                us.shareIdea().takeNewIdea(newIdea);
                System.out.println("New Idea Being taken: " + newIdea);
            }
        }

        System.out.println("-------\nJester Interaction Complete\n--------");

        System.out.println("Final Idea: " + us.shareIdea().currentIdea);

        // // JESTER ONE:
        // Jester j = jf.newJester();
        // Idea i = j.shareIdea();
        // CoreDocument doc = i.getDoc();

        // System.out.println(j.personalityIndex);

        // // Each index is a sentence made up of dictionaries of phrases
        // ArrayList<HashMap<String, ArrayList<Tree>>> sentencePhraseMap = NLP.parseParagraph(doc);
        
        // // JESTER TWO:
        // Jester j2 = jf.newJester();
        // Idea i2 = j2.shareIdea();
        // CoreDocument doc2 = i2.getDoc();

        // System.out.println(j2.personalityIndex);

        // // Each index is a sentence made up of dictionaries of phrases
        // ArrayList<HashMap<String, ArrayList<Tree>>> sentencePhraseMap2 = NLP.parseParagraph(doc2);

        // // JESTER THREE:
        // Jester j3 = jf.newJester();
        // Idea i3 = j3.shareIdea();
        // CoreDocument doc3 = i3.getDoc();

        // // Each index is a sentence made up of dictionaries of phrases
        // ArrayList<HashMap<String, ArrayList<Tree>>> sentencePhraseMap3 = NLP.parseParagraph(doc3);

        // // Output original idea
        // System.out.println("Jesters original idea: " + i.seed);

        // // Swap for a random NP
        // String newIdea = NLP.swapAPhrase(sentencePhraseMap, sentencePhraseMap2, Phrase.NP, i.currentIdea);
        // if (newIdea.equals("")){
        //     System.out.println("No new idea was created, no similar NP found");
        // } else {
        //     i.takeNewIdea(newIdea);
        //     System.out.println("New Idea Being taken: " + newIdea);
        // }

        // sentencePhraseMap = NLP.parseParagraph(i.getDoc());
        // newIdea = NLP.swapAPhrase(sentencePhraseMap, sentencePhraseMap3, Phrase.VP, i.currentIdea);
        // if (newIdea.equals("")){
        //     System.out.println("No new idea was created, no similar VP found");
        // } else {
        //     System.out.println("New Idea Being taken: " + newIdea);
        //     i.takeNewIdea(newIdea);
        // }

        // System.out.println("Jesters new Idea: " + i.currentIdea);



    }

    private static Phrase randomPhrase() {
        // Select a random enum from the Phrase enum
        Phrase[] phrases = Phrase.values();
        int randomIndex = (int) (Math.random() * phrases.length);
        return phrases[randomIndex];
    }
}
