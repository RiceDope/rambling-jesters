package com.github.ricedope;

/**
 * LLama3Client.java
 * 
 * Handle a full response and request to the llama3 ollama model running on localhost
 * 
 * @author Rhys Walker
 * @since 13/04/2025
 */

import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Llama3Client {

    // Connection values
    private static final String API_URL = "http://localhost:11434/api/generate";
    private static OkHttpClient client;

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Start the ollama server with no model running
     * @return The process that the ollama server is running on (null if error)
     */
    public static Process startup() {
        try {
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "start", "ollama", "serve");
            pb.inheritIO();
            return pb.start();
        } catch (IOException e) {
            Logger.logerror("Error starting the server: " + e.getMessage());
            return null;
        }
    }

    /**
     * Send a promt to the localhost server running the ollama llama3 model
     * @param prompt The prompt to send to the model
     * @return The response from the model
     */
    @SuppressWarnings("unchecked")
    public static String requester(String prompt, int timeout) {

        client = new OkHttpClient.Builder()
            .connectTimeout(Duration.ofSeconds(30))
            .readTimeout(Duration.ofSeconds(timeout))
            .build();

        // Map for request
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("model", "llama3");
        jsonMap.put("prompt", prompt);
        jsonMap.put("stream", false);
        jsonMap.put("keep_alive", "0m");

        // Map as Json
        String json;
        try {
            json = mapper.writeValueAsString(jsonMap);
        } catch (IOException e) {
            Logger.logimportant("Error: " + e.getMessage());
            return null;
        }

        // HTTP request to web server
        Request request = new Request.Builder()
                .url(API_URL)
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build();

        // Send the request
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                Logger.logimportant("Error: " + response.code());
                return null;
            }

            String responseBody = response.body().string();

            // Parse JSON response into Map
            Map<String, Object> result = mapper.readValue(responseBody, Map.class);
            return (String) result.get("response");
        } catch (IOException e) {
            Logger.logimportant("Error: " + e.getMessage());
            return null;
        }

    }

    public static void wait(int timeframe) {
        try {
            Thread.sleep(timeframe*1000);
        } catch (InterruptedException e) {
            Logger.logimportant("Error waiting " + e.getMessage());
        }
    }
}
