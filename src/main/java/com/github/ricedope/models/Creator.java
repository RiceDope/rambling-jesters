package com.github.ricedope.models;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Scanner;

import javax.swing.JFileChooser;

import com.github.ricedope.ANSI;
import com.github.ricedope.Logger;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;

/**
 * Creates a runnable XML file that can be used in the program
 */

public class Creator {
    
    public static void createXML(String filepath, String seedText, String jesterNames, String llmPrompt, int llmTimeout, int jesters, int gridSize, int minimumPassageLength, int maximumPassageLength, int interactions, String fileName) {

        // Create the root element of the XML
        Element root = new Element("runner");
        root.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        

        // Select the seedtext file
        Element seedtext = new Element("seedtext");
        seedtext.appendChild(seedText);
        root.appendChild(seedtext);

        // Select the jesernames file
        Element jesternames = new Element("jesternames");
        jesternames.appendChild(jesterNames);
        root.appendChild(jesternames);

        // Select the LLM prompt
        Element llmpromptElement = new Element("llmprompt");
        llmpromptElement.appendChild(llmPrompt);
        root.appendChild(llmpromptElement);

        // Select the LLM timeout
        Element llmtimeoutElement = new Element("llmtimeout");
        llmtimeoutElement.appendChild(String.valueOf(llmTimeout));
        root.appendChild(llmtimeoutElement);

        // Select the number of Jesters to create
        Element jestersElement = new Element("jesters");
        jestersElement.appendChild(String.valueOf(jesters));
        root.appendChild(jestersElement);

        // Select the gridsize
        Element gridsizeElement = new Element("gridsize");
        gridsizeElement.appendChild(String.valueOf(gridSize));
        root.appendChild(gridsizeElement);

        // Minimum passage length
        Element minimumpassagelengthElement = new Element("minimumpassagelength");
        minimumpassagelengthElement.appendChild(String.valueOf(minimumPassageLength));
        root.appendChild(minimumpassagelengthElement);

        // Maximum passage length
        Element maximumpassagelengthElement = new Element("maximumpassagelength");
        maximumpassagelengthElement.appendChild(String.valueOf(maximumPassageLength));
        root.appendChild(maximumpassagelengthElement);

        // Select the number of interactions
        Element interactionsElement = new Element("interactions");
        interactionsElement.appendChild(String.valueOf(interactions));
        root.appendChild(interactionsElement);

        File selectedDirectory = new File(filepath);

        Document doc = new Document(root);
        try {
            OutputStream os = new FileOutputStream(selectedDirectory.getAbsolutePath()+"\\"+fileName+".xml");
            Serializer serializer = new Serializer(os, "UTF-8");
            serializer.setIndent(4);
            serializer.write(doc);
            serializer.flush();
            os.close();
        } catch (Exception e) {
            Logger.logerror("Error writing XML file: " + e.getMessage());
            System.exit(0);
        }
    }
}
