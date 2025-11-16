package ma.enset.chatbotservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class LLMService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${llm.api.key}")
    private String apiKey;

    @Value("${llm.api.url}")
    private String apiUrl;

    @Value("${llm.model}")
    private String model;

    @Value("${llm.temperature:0.7}")
    private double temperature;

    @Value("${llm.max-tokens:500}")
    private int maxTokens;

    public LLMService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper,
            @Value("${llm.api.url}") String apiUrl) {
        String baseUrl = apiUrl.replace("/chat/completions", "");
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.objectMapper = objectMapper;
    }

    public String generateResponse(String userMessage, String context) {
        try {
            String systemPrompt = buildSystemPrompt(context);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("messages", List.of(
                    Map.of("role", "system", "content", systemPrompt),
                    Map.of("role", "user", "content", userMessage)));
            requestBody.put("temperature", temperature);
            requestBody.put("max_tokens", maxTokens);

            log.info("Sending request to LLM API with model: {}", model);
            log.debug("User message: {}", userMessage);
            log.debug("Context: {}", context);

            String response = webClient.post()
                    .uri("/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .header("HTTP-Referer", "http://localhost:4200")
                    .header("X-Title", "Conference Platform Chatbot")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode jsonResponse = objectMapper.readTree(response);
            String assistantMessage = jsonResponse
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            log.info("Received response from LLM API");
            return assistantMessage;

        } catch (Exception e) {
            log.error("Error calling LLM API: {}", e.getMessage(), e);
            if (e.getCause() != null) {
                log.error("Cause: {}", e.getCause().getMessage());
            }
            return "I apologize, but I'm having trouble processing your request right now. Please try again later.";
        }
    }

    private String buildSystemPrompt(String context) {
        return """
                You are a helpful AI assistant for the Conference Platform application.
                You help users find information about keynote speakers and conferences.

                Your role is to:
                - Answer questions about keynote speakers (their names, emails, functions)
                - Provide information about conferences (titles, types, dates, duration, attendees, scores)
                - Help users understand the conference platform features
                - Be friendly, professional, and concise

                Current data context:
                %s

                Instructions:
                - Use the provided context to answer questions accurately
                - If the information is not in the context, politely say you don't have that information
                - Keep responses concise and helpful
                - Format lists clearly when showing multiple items
                - Use professional language appropriate for a conference platform
                """.formatted(context);
    }
}
