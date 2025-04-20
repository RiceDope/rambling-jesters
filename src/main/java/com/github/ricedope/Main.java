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
        JesterFactory jf = new JesterFactory("src/main/resources/a-tale-of-two-cities.txt", 250);
        Plane plane = new Plane(10, 15, jf);
        for (int i = 0; i < 100; i++) {
            plane.interactionLoop();
            plane.regenerateGrid();
        }

        System.out.println("Original Idea:\n" + plane.getSeed() + "\n\n");
        String finalIdea = plane.getCurrentIdea();
        System.out.println("Final Idea:\n" + finalIdea + "\n\n");

        String NLPPrompt = "Please review the following text and return only the corrected version within quotation marks. Do not change the order of any non-duplicated phrases. Remove all duplicated phrases. Correct grammar and punctuation as needed to ensure the sentence flows naturally. Add connector words (e.g., and, but, then) only where necessary for fluidity. Do not include any explanation or extra output—only the revised text in quotation marks.";
        String response = Llama3Client.requester(NLPPrompt + "[" +finalIdea + "]");
        System.out.println("Transformed output:\n" + response);


    }
}
