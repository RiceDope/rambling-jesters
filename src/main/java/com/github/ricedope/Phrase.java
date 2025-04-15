package com.github.ricedope;

public enum Phrase {
    NP("NP"), // Noun Phrase
    VP("VP"), // Verb Phrase
    PP("PP"), // Prepositional Phrase
    ADJP("ADJP"), // Adjective Phrase
    ADVP("ADVP"); // Adverb Phrase

    private final String label;

    Phrase(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}