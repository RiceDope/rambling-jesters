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

        /*
         
PROMPT:
Please review the following text and return only the corrected version within quotation marks. Do not change the order of any non-duplicated phrases. Remove all duplicated phrases. Correct grammar and punctuation as needed to ensure the sentence flows naturally. Add connector words (e.g., and, but, then) only where necessary for fluidity. Do not include any explanation or extra output—only the revised text in quotation marks.
         */

        // String response = Llama3Client.requester("Generate 100 names for a virtual agent called a Jester. Please only repond in the format 'name, name, name, ...' do not include any other text in the response");
        // System.out.println(response);

        // Idea idea = new Idea("The wind whispered secrets to the swaying trees, carrying tales of distant lands. When Rhys found the trees he said my god the trees tell secrets only the leaves know.");

        JesterFactory jf = new JesterFactory("src/main/resources/a-tale-of-two-cities.txt");
        Jester us = jf.newJester();
        
        ArrayList<Jester> jesters = new ArrayList<Jester>();
        for (int i = 0; i < 15; i++) {
            Jester j = jf.newJester();
            jesters.add(j);
        }

        for (Jester j : jesters) {
            us.growIdea(j);
        }

        System.out.println("Original Idea:\n" + us.getSeed());
        System.out.println("Final Idea:\n" + us.shareIdea().currentIdea);
    }
}
