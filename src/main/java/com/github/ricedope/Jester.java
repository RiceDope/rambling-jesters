package com.github.ricedope;

/**
 * Class that outlines each Jester and its attributes
 */

public class Jester {

    private int id; // Give a unique ID based on when the Jester was created (1 through 50)
    private String name; // Name of the Jester (Randomly assigned at runtime)
    private String[] text; // Stored as an array of tokens
    

    public Jester(int id, String name, String[] text) {
        this.id = id;
        this.name = name;
        this.text = text;
    }

    public String[] shareText() {
        return text;
    }

    public void listen(String[] othersText) {
        
        // Listen to the other Jesters text

    }
    
}
