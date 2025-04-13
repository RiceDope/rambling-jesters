package com.github.ricedope;

/**
 * Creates and assigns a new Jester
 * 
 * @author Rhys Walker
 * @since 13/04/2025
 */

public class JesterFactory {
    
    private int currentId = 0;

    /**
     * Create a new ID
     * @return Integer ID
     */
    private int getNewId() {
        currentId++;
        return currentId;
    }

    // TODO: Read the CSV file into an array
    // TODO: Method to assign a new name, no duplication
    // TODO: Generate passages of text from the shakespeare corpus
    // TODO: Generate each Jester

}
