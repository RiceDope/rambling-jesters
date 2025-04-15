package com.github.ricedope;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class TextPreProcessor {

    public static String[] simpleCSVReader(String filepath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)), StandardCharsets.UTF_8);
            return content.split(",");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String readFile(String filepath) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(filepath));
            String content = new String(bytes, StandardCharsets.UTF_8);
            content = content.toLowerCase();
            return content;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String snipGutenbergStartAndEnd(String book) {
        String[] lines = book.split("\n");
        int start = 0;
        int end = lines.length;

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("*** start of")) {
                start = i + 1;
            }
            if (lines[i].contains("*** end of")) {
                end = i;
                break;
            }
        }

        return String.join("\n", Arrays.copyOfRange(lines, start, end));

    }

    public static String removeChapterNamesAndLeadingContents(String book) {
        // Remove all lines that start with chapter and the line that follows which is the chapter name
        String[] lines = book.split("\n");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i<lines.length; i++) {
            if (lines[i].trim().matches("^(chapter|book).*")) {
                i++;
            } else {
                sb.append(lines[i]).append("\n");
            }
        }

        return sb.toString();
    }

    public static String finalCleanupForNLP(String book) {
        book = book.replace("“", "\"").replace("”", "\"");
        book = book.replace("‘", "'").replace("’", "'");
        book = book.replace("—", "-");
        book = book.replaceAll("\\n{2,}", "\n");
        book = book.replaceAll("[\\p{Punct}&&[^.]]", "");
        return book;
    }
}
