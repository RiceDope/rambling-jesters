
package com.github.ricedope.models.logic;

/**
 * An object that represents an output from the system
 */

public class Output {
    
    public String jesterName;
    public String sourceText;
    public String outputText;
    public String evaluation;
    public String llmCorrectedText;
    public String llmPrompt;
    public int iterations;
    public int gridSize;
    public int minimumPassageLength;
    public int maximumPassageLength;
    public String jesterNames;
    public String sourceTexts;
    public String runnerFile;

    /**
     * Constructor for the Output class
     * @param jesterName
     * @param sourceText
     * @param outputText
     * @param llmCorrectedText
     * @param llmPrompt
     * @param iterations
     * @param gridSize
     * @param minimumPassageLength
     * @param maximumPassageLength
     */
    public Output(String jesterName, String sourceText, String outputText, String llmCorrectedText, String llmPrompt, int iterations, int gridSize, int minimumPassageLength, int maximumPassageLength, String jesterNames, String sourceTexts, String runnerFile) {
        this.jesterName = jesterName;
        this.sourceText = sourceText;
        this.outputText = outputText;
        this.llmCorrectedText = llmCorrectedText;
        this.llmPrompt = llmPrompt;
        this.iterations = iterations;
        this.gridSize = gridSize;
        this.minimumPassageLength = minimumPassageLength;
        this.maximumPassageLength = maximumPassageLength;
        this.jesterNames = jesterNames;
        this.sourceTexts = sourceTexts;
        this.runnerFile = runnerFile;
    }

}


    