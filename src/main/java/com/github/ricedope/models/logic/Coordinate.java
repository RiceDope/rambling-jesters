package com.github.ricedope.models.logic;

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
