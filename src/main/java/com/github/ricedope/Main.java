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
        Jester them = jf.newJester();

        CoreSentence closest = NLP.closestToSentiment(us.sentiment, them.shareIdea().getDoc());
        if (closest == null) {
            System.out.println("No sentences found with the same sentiment as " + us.sentiment);
            return;
        }

        StringBuilder sb = new StringBuilder();
        int chance = (int) (Math.random() * 100);
        if (chance < 50) {
            // Append at the beginning
            sb.append(closest.text());
            sb.append(us.shareIdea().currentIdea);
        } else {
            // Append at the end
            sb.append(us.shareIdea().currentIdea);
            sb.append(closest.text());
        }

        us.shareIdea().takeNewIdea(sb.toString());



    }
}
