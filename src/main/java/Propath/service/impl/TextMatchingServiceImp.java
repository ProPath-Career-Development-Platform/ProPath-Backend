package Propath.service.impl;

import Propath.service.TextMatchingService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TextMatchingServiceImp implements TextMatchingService {

    Dotenv dotenv = Dotenv.load();
    String APIKEY = dotenv.get("GEMINI_API_KEY");

    private final WebClient webClient;

    public TextMatchingServiceImp(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://generativelanguage.googleapis.com/v1beta").build();
    }

    /**
     * Compares the job description and CV text using the Gemini API.
     *
     * @param jobDescription the job description text
     * @param cvText         the CV text


     * @return the similarity percentage as a double
     */
    public double calculateMatchPercentage(String jobDescription, String cvText) {

        if (jobDescription == null || jobDescription.isEmpty()) {
            throw new IllegalArgumentException("Job description cannot be null or empty");
        }

        if (cvText == null || cvText.isEmpty()) {
            throw new IllegalArgumentException("CV text cannot be null or empty");
        }
        // Construct the full API URL
        String model = "models/text-bison-001";
        String endpoint = String.format("/%s:generateMessage", model);

        // Combine the input into the prompt as per API requirements
        String combinedPrompt = String.format(
                "Analyze the similarity between the following texts by identifying matching keywords, phrases, and context. Consider the relevance, frequency, and semantic similarity of the terms in both texts. Provide a similarity percentage: %nJob Description: %s%nCV Text: %s",
                jobDescription, cvText
        );

        System.out.println("Combined Prompt: " + combinedPrompt);


        // Create the request body
        GenerateMessageRequest requestBody = new GenerateMessageRequest(
                new MessagePrompt(combinedPrompt), 0.0, 1, null, null
        );

        // Send the API request and parse the response
        GenerateMessageResponse response = webClient.post()
                .uri(endpoint)
                .header("Authorization","Bearer " + APIKEY) // Replace with your API key
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(GenerateMessageResponse.class)
                .block();

        if (response != null && !response.getCandidates().isEmpty()) {
            // Extract the similarity percentage from the first candidate message
            String resultText = response.getCandidates().get(0).getContent();
            return extractPercentage(resultText);
        }

        throw new RuntimeException("Failed to retrieve similarity percentage from the API.");
    }

    /**
     * Extracts the similarity percentage from the response text.
     *
     * @param text the response text
     * @return the extracted percentage
     */
    private double extractPercentage(String text) {
        // Assuming the percentage is mentioned as a number in the response (e.g., "Similarity: 85%")
        String percentageString = text.replaceAll("[^\\d]", "");
        return Double.parseDouble(percentageString) / 100.0;
    }

    // Request and Response Classes for API Communication

    private static class GenerateMessageRequest {
        private final MessagePrompt prompt;
        private final Double temperature;
        private final Integer candidateCount;
        private final Double topP;
        private final Integer topK;

        public GenerateMessageRequest(MessagePrompt prompt, Double temperature, Integer candidateCount, Double topP, Integer topK) {
            this.prompt = prompt;
            this.temperature = temperature;
            this.candidateCount = candidateCount;
            this.topP = topP;
            this.topK = topK;
        }

        public MessagePrompt getPrompt() {
            return prompt;
        }

        public Double getTemperature() {
            return temperature;
        }

        public Integer getCandidateCount() {
            return candidateCount;
        }

        public Double getTopP() {
            return topP;
        }

        public Integer getTopK() {
            return topK;
        }
    }

    private static class MessagePrompt {
        private final String text;

        public MessagePrompt(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    private static class GenerateMessageResponse {
        private final java.util.List<Message> candidates;

        public GenerateMessageResponse(java.util.List<Message> candidates) {
            this.candidates = candidates;
        }

        public java.util.List<Message> getCandidates() {
            return candidates;
        }
    }

    private static class Message {
        private final String content;

        public Message(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }

}
