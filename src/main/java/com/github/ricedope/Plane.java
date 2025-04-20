package com.github.ricedope;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A representation of a 2D plane that the Jesters can "wander" around and interect with each other
 * One Jester will be selected as the base Jester which will be the one that generates the Idea. Over time
 * they will interact in a small grid and share ideas with the chosen base Jester.
 */

 @SuppressWarnings("unchecked")
public class Plane {

    private Jester baseJester;
    private ArrayList<Jester> jesters = new ArrayList<Jester>();
    private ArrayList<Jester>[][] grid;
    private ArrayList<Jester> visited = new ArrayList<Jester>();
    private Coordinate bjCoord;
    
    /**
     * Create a new plane object to allow Jesters to interact with each other
     * @param gridSize the n*n grid that the Jesters will interact on
     * @param jesters the number of Jesters that will be created and placed on the grid (Must be less than JesterFactory.possibleJesters()) this includes the Base Jester
     * @param jf The JesterFactory that will be used to create the Jesters
     */
    public Plane(int gridSize, int jesters, JesterFactory jf) { 

        if (jesters > jf.possibleJesters()) {
            throw new IllegalArgumentException("Number of Jesters must be less than the number of possible Jesters.");
        }

        grid = (ArrayList<Jester>[][]) new ArrayList[gridSize][gridSize];
        this.baseJester = jf.newJester(); // Create the base Jester
        // Populate the Array of Jesters
        for (int i = 0; i < jesters-1; i++) {
            this.jesters.add(jf.newJester());
        }

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = new ArrayList<Jester>();
            }
        }

        // Place the base Jester in the middle of the grid
        int mid = gridSize / 2;
        grid[mid][mid].add(baseJester);
        bjCoord = new Coordinate(mid, mid);

        // Place the other Jesters randomly on the grid
        for (Jester j : this.jesters) {
            int x = (int) (Math.random() * gridSize);
            int y = (int) (Math.random() * gridSize);
            grid[x][y].add(j);
        }
    }

    public void regenerateGrid() {
        // Regenerate the grid and place the Jesters randomly on it
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j].clear();
            }
        }

        // Place the base Jester in the middle of the grid
        int mid = grid.length / 2;
        grid[mid][mid].add(baseJester);
        bjCoord = new Coordinate(mid, mid);

        // Place the other Jesters randomly on the grid
        for (Jester j : this.jesters) {
            int x = (int) (Math.random() * grid.length);
            int y = (int) (Math.random() * grid.length);
            grid[x][y].add(j);
        }

        // Visited list not cleared as this is used to move Jesters around
    }

    public void interactionLoop() {
        // Make the Jester look for its closest unvisited neighbor and interact with them
        // If the closest unvisited neighbor is more than gridSize/3 steps away, visit the closest one regardless of if they are visited or not
        // If there are no unvisited neighbors, the Jester will clear its visited list and start over

        // First check if we currently have anyone around us
        if (grid[bjCoord.getX()][bjCoord.getY()].size() > 1) {
            // If we do then interact with them
            ArrayList<Jester> thisLoc = grid[bjCoord.getX()][bjCoord.getY()];
            for (Jester j : thisLoc) {
                if (j != baseJester) {
                    // Interact with the Jester
                    baseJester.growIdea(j);
                }
            }
        } else {
            // If we have nobody in our coordinate
            // Now check if we have any neighbours around that are unvisited and visit one of them

            // Check the grid surrounding the baseJester
            // Outer loop will do y
            // Inner loop will do x
            // First check if we will go out of the grid

            int x = bjCoord.getX();
            int y = bjCoord.getY();

            int maxX;
            int minX;

            int maxY;
            int minY;

            if (x + grid.length/3 > grid.length) {
                maxX = grid.length-1;
                minX = x - grid.length/3;
            } else if ( x - grid.length/3 < 0) {
                maxX = x + grid.length/3;
                minX = 0;
            } else {
                maxX = x + grid.length/3;
                minX = x - grid.length/3;
            }

            if (y + grid.length/3 > grid.length) {
                minY = y - grid.length/3;
                maxY = grid.length-1;
            } else if ( y - grid.length/3 < 0) {
                maxY = y + grid.length/3;
                minY = 0;
            } else {
                maxY = y + grid.length/3;
                minY = y - grid.length/3;
            }

            // Now we have the bounds of the grid we can check
            // The grid will be checked from minX, maxY to maxX, minY
            // Moving sequentially to the right and then down
            ArrayList<Jester> closeJesters = new ArrayList<Jester>();
            for (int cy = maxY; cy > minY; cy--) {
                for (int cx = minX; cx < maxX; cx++) {
                    // Check if the coordinate has a Jester on it
                    if (grid[cx][cy].size() > 0) {
                        if (grid[cx][cy].get(0) == baseJester) {
                            continue; // Skip the base Jester
                        } else {
                            // Add to an ArrayList of close Jesters
                            for (Jester j : grid[cx][cy]) {
                                if (!visited.contains(j)) {
                                    Logger.logexchanges("Found a close Jester: ");
                                    closeJesters.add(j);
                                }
                            }
                        }
                    }
                }
            }

            // Now randomly choose one of the Jesters that are close
            if (closeJesters.size() > 0) {
                // Choose a random Jester from the list of close Jesters
                int randIndex = (int) (Math.random() * closeJesters.size());
                Jester chosenJester = closeJesters.get(randIndex);
                // Interact with the chosen Jester
                baseJester.growIdea(chosenJester);
            } else {
                // If there are no unvisited neighbours, clear the visited list and start over
                visited.clear();
            }
        }
    }

    public String getSeed() {
        return baseJester.getSeed();
    }

    public String getCurrentIdea() {
        return baseJester.shareIdea().currentIdea;
    }
}
