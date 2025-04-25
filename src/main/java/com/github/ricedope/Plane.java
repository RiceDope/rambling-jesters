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
    private Jester[][] grid;
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

        grid = new Jester[gridSize][gridSize];
        this.baseJester = jf.newJester(); // Create the base Jester
        // Populate the Array of Jesters
        for (int i = 0; i < jesters-1; i++) {
            this.jesters.add(jf.newJester());
        }

        // Place the base Jester in the middle of the grid
        int mid = gridSize / 2;
        grid[mid][mid] = baseJester;
        bjCoord = new Coordinate(mid, mid);

        // Place the other Jesters randomly on the grid
        for (Jester j : this.jesters) {
            int x = (int) (Math.random() * gridSize);
            int y = (int) (Math.random() * gridSize);

            placeJester(j, x, y);
            
        }
    }

    /**
     * Attempts to place a Jester on the grid at the specified location. If the location is already occupied look at the spaces around
     * @param j
     * @param targetX
     * @param targetY
     */
    public void placeJester(Jester j, int targetX, int targetY) {

        // Check if we can place on target location
        if (grid[targetX][targetY] != null) {
            // First get the bounds we are allowed to check within +1 on x and +1 on y of our target
            int minx;
            int maxx;
            int miny;
            int maxy;
            if (targetX - 1 < 0) {
                minx = 0;
            } else {
                minx = targetX - 1;
            }
            if (targetX + 1 > grid.length - 1) {
                maxx = grid.length - 1;
            } else {
                maxx = targetX + 1;
            }
            if (targetY - 1 < 0) {
                miny = 0;
            } else {
                miny = targetY - 1;
            }
            if (targetY + 1 > grid.length - 1) {
                maxy = grid.length - 1;
            } else {
                maxy = targetY + 1;
            }

            // Check in a spiral pattern around the target location based on max and min values to look for a null location
            for (int y = maxy; y >= miny; y--) {
                for (int x = minx; x <= maxx; x++) {
                    if (grid[x][y] == null) {
                        grid[x][y] = j;
                        return; // Place the Jester and exit the method
                    }
                }
            }
        } else {
            grid[targetX][targetY] = j;
        }

        
    }

    public void regenerateGrid() {
        // Regenerate the grid and place the Jesters randomly on it
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = null;
            }
        }

        // Place the base Jester in the middle of the grid
        int mid = grid.length / 2;
        grid[mid][mid] = baseJester;
        bjCoord = new Coordinate(mid, mid);

        // Place the other Jesters randomly on the grid
        for (Jester j : this.jesters) {
            int x = (int) (Math.random() * grid.length);
            int y = (int) (Math.random() * grid.length);
            
            placeJester(j, x, y);

        }

        // Visited list not cleared as this is used to move Jesters around
    }

    public void interactionLoop() {
        // Make the Jester look for its closest unvisited neighbor and interact with them
        // If the closest unvisited neighbor is more than gridSize/3 steps away, visit the closest one regardless of if they are visited or not
        // If there are no unvisited neighbors, the Jester will clear its visited list and start over

        
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
                if (grid[cx][cy] != null) {
                    if (grid[cx][cy] == baseJester) {
                        continue; // Skip the base Jester
                    } else {
                        if (!visited.contains(grid[cx][cy])) {
                            Logger.logexchanges("Found a close Jester: ");
                            closeJesters.add(grid[cx][cy]);
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

    public String getSeed() {
        return baseJester.getSeed();
    }

    public String getCurrentIdea() {
        return baseJester.shareIdea().currentIdea;
    }

    public String getName() {
        return baseJester.getName();
    }
}
