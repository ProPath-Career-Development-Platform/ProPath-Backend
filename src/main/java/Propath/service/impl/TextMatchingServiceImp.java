package Propath.service.impl;

import Propath.service.TextMatchingService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import io.github.cdimascio.dotenv.Dotenv;

@Service
public class TextMatchingServiceImp implements TextMatchingService {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String API_URL = "https://api.app.labelf.ai/v2/similarity";
    private static final String BEARER_TOKEN = dotenv.get("Labelf_Bear_Token"); // Load from environment

    public double calculateMatchPercentage(String baseText, String compareText) {
        try {
            // Create request headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(BEARER_TOKEN);

            // Create the JSON payload
            JSONObject jsonData = new JSONObject();
            jsonData.put("top_n", 1);

            JSONObject baseTexts = new JSONObject();
            baseTexts.put("base1", baseText); // Use "base1" as key for base text
            jsonData.put("base_texts", baseTexts);

            JSONObject compareToTexts = new JSONObject();
            compareToTexts.put("compare1", compareText); // Use "compare1" as key for compare text
            jsonData.put("compare_to_texts", compareToTexts);

            // Create HTTP request
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonData.toString(), headers);
            RestTemplate restTemplate = new RestTemplate();

            // Send POST request
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, requestEntity, String.class);

            // Parse the response
            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject responseJson = new JSONObject(response.getBody());
                // Extract similarity score for compare1
                if (responseJson.has("compare1")) {
                    double similarity = responseJson.getJSONArray("compare1").getJSONObject(0).getDouble("similarity");
                    return similarity * 100;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0; // Return 0 if an error occurs
    }

}
