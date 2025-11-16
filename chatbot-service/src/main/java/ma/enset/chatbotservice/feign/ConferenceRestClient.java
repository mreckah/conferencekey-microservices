package ma.enset.chatbotservice.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import ma.enset.chatbotservice.model.Conference;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;

@FeignClient(name = "CONFERENCE-SERVICE")
public interface ConferenceRestClient {
    
    @GetMapping("/api/conferences")
    @CircuitBreaker(name = "conferenceService", fallbackMethod = "getDefaultConferences")
    List<Conference> getAllConferences();
    
    @GetMapping("/api/conferences/{id}")
    @CircuitBreaker(name = "conferenceService", fallbackMethod = "getDefaultConference")
    Conference getConferenceById(@PathVariable Long id);
    
    default List<Conference> getDefaultConferences(Exception exception) {
        return Collections.emptyList();
    }
    
    default Conference getDefaultConference(Long id, Exception exception) {
        return Conference.builder()
                .id(id)
                .titre("Not Available")
                .type("N/A")
                .duree(0)
                .nombreInscrits(0)
                .score(0.0)
                .build();
    }
}

