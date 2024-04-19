package com.somnang.telegrambot;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service
public class GeminiService {

    //private String geminiApiKey = "AIzaSyDNNd1nb36Sm5cRHypwS6iASs79I9T9cWE";

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=AIzaSyBt6GK0o-DfQ4ulZxe8FzG_dYU7oaYY3wE";

    public String getGeminiResponse(String message) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            /*HttpHeaders headers = new HttpHeaders();
            headers.set("Bearer", geminiApiKey);*/

            Request req = new Request();
            Content content = new Content();
            Part part = new Part();
            part.setText(message);
            List<Part> partsList = new ArrayList<>();
            partsList.add(part);
            content.setParts(partsList);
            List<Content> contentsList = new ArrayList<>();
            contentsList.add(content);
            req.setContents(contentsList);

            HttpEntity<Request> request = new HttpEntity<>(req);

            ResponseEntity<String> response = restTemplate.exchange(
                    GEMINI_API_URL,
                    HttpMethod.POST,
                    request,
                    String.class// Assuming a GeminiResponse class to map the response
            );
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                return "Sorry, I had trouble getting a response from Gemini.";
            }

        } catch (Exception e) {
            // Log the error for analysis
            System.err.println("Error communicating with Gemini API: " + e.getMessage());
            return "Sorry, I'm experiencing a technical issue. Please try again later.";
        }
    }
}

