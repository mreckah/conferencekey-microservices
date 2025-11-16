package ma.enset.chatbotservice.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.chatbotservice.dto.ChatRequest;
import ma.enset.chatbotservice.dto.ChatResponse;
import ma.enset.chatbotservice.service.ChatbotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Chatbot", description = "AI Chatbot API for Conference Platform")
public class ChatbotRestController {

    private final ChatbotService chatbotService;

    @PostMapping("/chat")
    @Operation(summary = "Send a message to the chatbot", description = "Send a message and receive an AI-generated response with context from keynotes and conferences")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        log.info("Received chat request: {}", request.getMessage());
        
        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        ChatResponse response = chatbotService.processMessage(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    @Operation(summary = "Check chatbot health", description = "Get health status and connectivity to other services")
    public ResponseEntity<String> health() {
        String status = chatbotService.getHealthStatus();
        return ResponseEntity.ok(status);
    }

    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Simple test endpoint to verify the service is running")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Chatbot service is running!");
    }
}

