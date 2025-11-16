package ma.enset.chatbotservice.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import ma.enset.chatbotservice.model.Keynote;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;

@FeignClient(name = "KEYNOTE-SERVICE")
public interface KeynoteRestClient {
    
    @GetMapping("/api/keynotes")
    @CircuitBreaker(name = "keynoteService", fallbackMethod = "getDefaultKeynotes")
    List<Keynote> getAllKeynotes();
    
    @GetMapping("/api/keynotes/{id}")
    @CircuitBreaker(name = "keynoteService", fallbackMethod = "getDefaultKeynote")
    Keynote getKeynoteById(@PathVariable Long id);
    
    default List<Keynote> getDefaultKeynotes(Exception exception) {
        return Collections.emptyList();
    }
    
    default Keynote getDefaultKeynote(Long id, Exception exception) {
        return Keynote.builder()
                .id(id)
                .nom("Not Available")
                .prenom("Not Available")
                .email("N/A")
                .fonction("N/A")
                .build();
    }
}

