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
        System.out.println(ANSI.GREEN_BACKGROUND + "[Progress]"  + "" + ANSI.RESET +" "+ message);
    }

    /**
     * Used for logging the progress of individual Jesters
     * Log things like communication, passage changes
     * @param message
     */
    public static void logexchanges(String message) {
        if (logLevel == loggingLevel.ALL || logLevel == loggingLevel.SOME) {
            System.out.println("[Jester exchange] " + message);
        }
    }

    /**
     * Used for logging important messages that the user must be aware of
     * Use this sparingly and only for critical messages
     * @param message
     */
    public static void logimportant(String message) {
        System.out.println(ANSI.YELLOW_BACKGROUND + "[IMPORTANT]" + "" + ANSI.RESET +" "+ message );
    }

    /**
     * Use for logging anything that does not fit into the other categories
     * @param message
     */
    public static void logmisc(String message) {
        if (logLevel == loggingLevel.ALL) {
            System.out.println("[MISC] " + message);
        }
    }

    /**
     * For logging errors that occur in the system
     * @param message
     */
    public static void logerror(String message) {
        System.out.println(ANSI.RED_BACKGROUND + "[ERROR]" + "" + ANSI.RESET +" "+ message);
    }

    /**
     * Clears the console screen based on the debugging level
     * Only clear when we are not really debugging just seeing the system progress
     */
    public static void clearConsole() {
        if (logLevel == loggingLevel.NONE || logLevel == loggingLevel.FEW) {
            try {
                if (System.getProperty("os.name").contains("Windows")) {
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                } else {
                    new ProcessBuilder("clear").inheritIO().start().waitFor();
                }
            } catch (Exception e) {
                System.out.println("Error clearing console: " + e.getMessage());
            }
        }
    }
}
