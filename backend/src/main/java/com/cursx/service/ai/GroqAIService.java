package com.cursx.service.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GroqAIService implements AIService {

    @Value("${groq.api.key:}")
    private String apiKey;

    @Value("${groq.api.url:https://api.groq.com/openai/v1/chat/completions}")
    private String apiUrl;

    @Value("${groq.model:llama-3.1-8b-instant}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String generateContent(String prompt) {
        if (apiKey == null || apiKey.isEmpty()) {
            log.warn("Groq API key not configured, returning mock response");
            return generateMockResponse();
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(Map.of(
                    "role", "user",
                    "content", prompt
                )),
                "max_tokens", 1000,
                "temperature", 0.7
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                (Class<Map<String, Object>>) (Class<?>) Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }

            log.error("Unexpected response from Groq API: {}", response);
            return generateMockResponse();

        } catch (Exception e) {
            log.error("Error calling Groq API", e);
            return generateMockResponse();
        }
    }

    private String generateMockResponse() {
        return """
            1. "AI won't replace jobs. Someone who knows how to use AI will."
            2. "People scared of AI are just scared of learning again. The wheel didn't break the running industry either."
            3. "AI is like fire: amazing tool, terrible master. But hey, at least it won't burn your house down... probably."
            """;
    }
}
