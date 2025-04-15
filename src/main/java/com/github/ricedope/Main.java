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

        // JESTER ONE:
        Jester j = jf.newJester();
        Idea i = j.shareIdea();
        CoreDocument doc = i.getDoc();

        // Each index is a sentence made up of dictionaries of phrases
        ArrayList<HashMap<String, ArrayList<Tree>>> sentencePhraseMap = NLP.parseParagraph(doc);
        
        // System.out.println(Arrays.toString(NLP.getSentencesWithPhrase(sentencePhraseMap, Phrase.NP)));

        // JESTER TWO:
        Jester j2 = jf.newJester();
        Idea i2 = j2.shareIdea();
        CoreDocument doc2 = i2.getDoc();

        // Each index is a sentence made up of dictionaries of phrases
        ArrayList<HashMap<String, ArrayList<Tree>>> sentencePhraseMap2 = NLP.parseParagraph(doc2);

        System.out.println("Jesters original idea: " + i.seed);
        System.out.println("Jesters new Idea: " + NLP.swapAPhrase(sentencePhraseMap, sentencePhraseMap2, Phrase.NP, i.currentIdea));

        // // Swap out whatever type of phrase both sentences have
        // if (sentencePhraseMap.get(0).get("NP").size() > 0 && sentencePhraseMap2.get(0).get("NP").size() > 0) {
        //     // Get the noun phrases from both sentences
        //     ArrayList<Tree> j1NounPhrases = sentencePhraseMap.get((int) Math.random() * sentencePhraseMap.size()).get("NP");
        //     ArrayList<Tree> j2NounPhrases = sentencePhraseMap2.get((int) Math.random() * sentencePhraseMap2.size()).get("NP");

        //     // Get a random noun phrase from each sentence
        //     Tree j1RandomNounPhrase = j1NounPhrases.get((int) (Math.random() * j1NounPhrases.size()));
        //     System.out.println("Jester 1 phrase to swap " + j1RandomNounPhrase.yieldWords().stream().map(Object::toString).collect(Collectors.joining(" ")));
        //     Tree j2RandomNounPhrase = j2NounPhrases.get((int) (Math.random() * j2NounPhrases.size()));
        //     System.out.println("Jester 2 phrase to swap " + j2RandomNounPhrase.yieldWords().stream().map(Object::toString).collect(Collectors.joining(" ")));

        //     // Swap the noun phrases in the sentences
        //     String newSentence1 = i.currentIdea.replace(NLP.phraseToString(j1RandomNounPhrase), NLP.phraseToString(j2RandomNounPhrase));
        //     String newSentence2 = i2.currentIdea.replace(NLP.phraseToString(j2RandomNounPhrase), NLP.phraseToString(j1RandomNounPhrase));

        //     // // Update the ideas with the new sentences
        //     // i.takeNewIdea(newSentence1);
        //     // i2.takeNewIdea(newSentence2);

        //     System.out.println("Jester 1s original idea: " + i.seed);
        //     System.out.println("Jester 1s new idea: " + newSentence1);
        //     System.out.println("Jester 2s original idea: " + i2.seed);
        //     System.out.println("Jester 2s new idea: " + newSentence2);
        // } else {
        //     System.out.println("No noun phrases found in one or both sentences.");
        // }
    }
}
