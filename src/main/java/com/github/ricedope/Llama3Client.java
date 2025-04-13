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
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(Duration.ofSeconds(30))
            .readTimeout(Duration.ofSeconds(240))
            .build();

    private static final ObjectMapper mapper = new ObjectMapper();

    public static boolean startup() {
        try {
            ProcessBuilder pb = new ProcessBuilder("ollama", "serve");
            pb.inheritIO(); // optional: pipe Ollama logs to your console
            pb.start();
            Thread.sleep(5000); // give it a few seconds to boot up
            System.out.println("Ollama started returning to main process");
            return true;
        } catch (IOException | InterruptedException e) {
            System.err.println("Error starting Ollama: " + e.getMessage());
            return false;
        }
    }

    /**
     * Send a promt to the localhost server running the ollama llama3 model
     * @param prompt The prompt to send to the model
     * @return The response from the model
     */
    @SuppressWarnings("unchecked")
    public static String requester(String prompt) {

        // Map for request
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("model", "llama3");
        jsonMap.put("prompt", prompt);
        jsonMap.put("stream", false);

        // Map as Json
        String json;
        try {
            json = mapper.writeValueAsString(jsonMap);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
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
                System.err.println("Error: " + response.code());
                return null;
            }

            String responseBody = response.body().string();

            // Parse JSON response into Map
            Map<String, Object> result = mapper.readValue(responseBody, Map.class);
            return (String) result.get("response");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }

    }
}
