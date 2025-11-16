package ma.enset.chatbotservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.chatbotservice.dto.ChatRequest;
import ma.enset.chatbotservice.dto.ChatResponse;
import ma.enset.chatbotservice.feign.ConferenceRestClient;
import ma.enset.chatbotservice.feign.KeynoteRestClient;
import ma.enset.chatbotservice.model.Conference;
import ma.enset.chatbotservice.model.Keynote;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatbotService {

    private final KeynoteRestClient keynoteRestClient;
    private final ConferenceRestClient conferenceRestClient;
    private final LLMService llmService;
    private final ObjectMapper objectMapper;

    public ChatResponse processMessage(ChatRequest request) {
        log.info("Processing chat message: {}", request.getMessage());

        String conversationId = request.getConversationId() != null
                ? request.getConversationId()
                : UUID.randomUUID().toString();

        String context = gatherContext(request.getMessage());

        String llmResponse = llmService.generateResponse(request.getMessage(), context);

        return ChatResponse.builder()
                .message(llmResponse)
                .conversationId(conversationId)
                .timestamp(LocalDateTime.now())
                .context(context)
                .build();
    }

    private String gatherContext(String userMessage) {
        StringBuilder context = new StringBuilder();

        try {
            boolean needsKeynotes = containsKeywords(userMessage,
                    "keynote", "speaker", "presenter", "who", "email", "contact");
            boolean needsConferences = containsKeywords(userMessage,
                    "conference", "event", "talk", "presentation", "when", "date", "schedule");

            if (!needsKeynotes && !needsConferences) {
                needsKeynotes = true;
                needsConferences = true;
            }

            if (needsKeynotes) {
                List<Keynote> keynotes = keynoteRestClient.getAllKeynotes();
                if (!keynotes.isEmpty()) {
                    context.append("KEYNOTE SPEAKERS:\n");
                    for (Keynote keynote : keynotes) {
                        context.append(String.format(
                                "- %s %s (ID: %d)\n  Function: %s\n  Email: %s\n",
                                keynote.getPrenom(), keynote.getNom(), keynote.getId(),
                                keynote.getFonction(), keynote.getEmail()));
                    }
                    context.append("\n");
                }
            }

            if (needsConferences) {
                List<Conference> conferences = conferenceRestClient.getAllConferences();
                if (!conferences.isEmpty()) {
                    context.append("CONFERENCES:\n");
                    for (Conference conf : conferences) {
                        context.append(String.format(
                                "- %s (ID: %d)\n  Type: %s\n  Duration: %d minutes\n  Attendees: %d\n  Score: %.1f\n  Keynote ID: %d\n",
                                conf.getTitre(), conf.getId(), conf.getType(),
                                conf.getDuree(), conf.getNombreInscrits(), conf.getScore(), conf.getKeynoteId()));
                    }
                    context.append("\n");
                }
            }

            if (context.length() == 0) {
                context.append("No data available at the moment.");
            }

        } catch (Exception e) {
            log.error("Error gathering context: {}", e.getMessage(), e);
            context.append("Error retrieving data from services.");
        }

        log.debug("Gathered context: {}", context.toString());
        return context.toString();
    }

    private boolean containsKeywords(String message, String... keywords) {
        String lowerMessage = message.toLowerCase();
        for (String keyword : keywords) {
            if (lowerMessage.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public String getHealthStatus() {
        try {
            List<Keynote> keynotes = keynoteRestClient.getAllKeynotes();
            List<Conference> conferences = conferenceRestClient.getAllConferences();

            return String.format(
                    "Chatbot Service is healthy. Connected to: Keynote Service (%d speakers), Conference Service (%d conferences)",
                    keynotes.size(), conferences.size());
        } catch (Exception e) {
            return "Chatbot Service is running but some dependencies are unavailable: " + e.getMessage();
        }
    }
}
