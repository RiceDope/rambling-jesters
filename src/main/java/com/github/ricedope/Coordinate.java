package com.github.ricedope;

/**
 * Class that handles the coordinates of the a Jester on a plane 
 * Handles movement of jesters
 */

public class Coordinate {
    
    private int x;
    private int y;
    
    /**
     * Constructor for the Coordinate class
     * @param x The x coordinate of the Jester on the grid
     * @param y The y coordinate of the Jester on the grid
     * @param gridSize The size of the grid that the Jester is on
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Default constructor for Coordinate
     * Generate a random coordinate on the grid
     * @param gridSize The size of the grid that the Jester is on
     */
    public Coordinate(int gridSize) {
        this.x = (int) (Math.random() * gridSize);
        this.y = (int) (Math.random() * gridSize);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Given a new coordinate, choose a point that is on the line between the two points
     * The point should be moveSize away from the current point and in the direction of the new point
     * @param newCoord the new coordinate
     * @return the new coordinate that is moveSize on the current path
     */
    public Coordinate moveTowards(Coordinate newCoord, int moveSize) {
        // Get the distance between the two points
        double distance = Math.sqrt(Math.pow(newCoord.getX() - x, 2) + Math.pow(newCoord.getY() - y, 2));
        // Get the direction of the new point
        double directionX = (newCoord.getX() - x) / distance;
        double directionY = (newCoord.getY() - y) / distance;
        // Move the current point in the direction of the new point
        return new Coordinate((int) (x + directionX * moveSize), (int) (y + directionY * moveSize));
    }

    public boolean withinGrid(int gridSize) {
        return (x >= 0 && x < gridSize && y >= 0 && y < gridSize);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
