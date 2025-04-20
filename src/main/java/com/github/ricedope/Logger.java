package com.github.ricedope;

/**
 * Handles the logging of the program that is running
 * 
 * @author Rhys Walker
 * @since 20/04/2025
 */

public class Logger {

    // Adjust this to change the logging level
    public static loggingLevel logLevel = loggingLevel.NONE;
    
    /**
     * The level at which the logger will log messages
     * NONE: No logging (Other than important messages)
     * SOME: Some logging (Jester exchanges, system progress and important messages)
     * ALL: All logging (Jester exchanges, system progress, important messages and misc messages)
     * FEW: Few logging (system progress and important messages)
     */
    enum loggingLevel {
        NONE,
        SOME,
        ALL,
        FEW
    }

    /**
     * Used for logging the progress of the whole system
     * Log things like, Generating Jesters, parsing to llm, etc
     * @param message
     */
    public static void logprogress(String message) {
        if (logLevel == loggingLevel.SOME || logLevel == loggingLevel.ALL || logLevel == loggingLevel.FEW) {
            System.out.println(message);
        }
    }

    /**
     * Used for logging the progress of individual Jesters
     * Log things like communication, passage changes
     * @param message
     */
    public static void logexchanges(String message) {
        if (logLevel == loggingLevel.ALL || logLevel == loggingLevel.SOME) {
            System.out.println("\n[Jester exchange]" + message);
        }
    }

    /**
     * Used for logging important messages that the user must be aware of
     * Use this sparingly and only for critical messages
     * @param message
     */
    public static void logimportant(String message) {
        if (logLevel == loggingLevel.NONE || logLevel == loggingLevel.SOME || logLevel == loggingLevel.ALL || logLevel == loggingLevel.FEW) {
            System.out.println("\n[IMPORTANT] " + message);
        }
    }

    /**
     * Use for logging anything that does not fit into the other categories
     * @param message
     */
    public static void logmisc(String message) {
        if (logLevel == loggingLevel.ALL) {
            System.out.println("\n[MISC] " + message);
        }
    }

}
