package ma.enset.conferenceservice.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import ma.enset.conferenceservice.model.Keynote;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "KEYNOTE-SERVICE")
public interface KeynoteRestClient {
    
    @GetMapping("/api/keynotes/{id}")
    @CircuitBreaker(name = "keynoteService", fallbackMethod = "getDefaultKeynote")
    Keynote findKeynoteById(@PathVariable Long id);
    
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

