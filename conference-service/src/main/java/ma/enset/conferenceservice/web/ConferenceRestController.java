package ma.enset.conferenceservice.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.enset.conferenceservice.dto.ConferenceDTO;
import ma.enset.conferenceservice.dto.ReviewDTO;
import ma.enset.conferenceservice.entities.TypeConference;
import ma.enset.conferenceservice.service.ConferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conferences")
@RequiredArgsConstructor
@Tag(name = "Conference Platform", description = "APIs for managing conferences and reviews")
public class ConferenceRestController {

    private final ConferenceService conferenceService;

    @GetMapping
    @Operation(summary = "Get all conferences")
    public ResponseEntity<List<ConferenceDTO>> getAllConferences() {
        return ResponseEntity.ok(conferenceService.getAllConferences());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get conference by ID")
    public ResponseEntity<ConferenceDTO> getConferenceById(@PathVariable Long id) {
        return ResponseEntity.ok(conferenceService.getConferenceById(id));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get conferences by type")
    public ResponseEntity<List<ConferenceDTO>> getConferencesByType(@PathVariable TypeConference type) {
        return ResponseEntity.ok(conferenceService.getConferencesByType(type));
    }

    @GetMapping("/keynote/{keynoteId}")
    @Operation(summary = "Get conferences by keynote ID")
    public ResponseEntity<List<ConferenceDTO>> getConferencesByKeynoteId(@PathVariable Long keynoteId) {
        return ResponseEntity.ok(conferenceService.getConferencesByKeynoteId(keynoteId));
    }

    @PostMapping
    @Operation(summary = "Create a new conference")
    public ResponseEntity<ConferenceDTO> createConference(@RequestBody ConferenceDTO conferenceDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(conferenceService.createConference(conferenceDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a conference")
    public ResponseEntity<ConferenceDTO> updateConference(
            @PathVariable Long id,
            @RequestBody ConferenceDTO conferenceDTO) {
        return ResponseEntity.ok(conferenceService.updateConference(id, conferenceDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a conference")
    public ResponseEntity<Void> deleteConference(@PathVariable Long id) {
        conferenceService.deleteConference(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{conferenceId}/reviews")
    @Operation(summary = "Add a review to a conference")
    public ResponseEntity<ReviewDTO> addReview(
            @PathVariable Long conferenceId,
            @RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(conferenceService.addReview(conferenceId, reviewDTO));
    }

    @GetMapping("/{conferenceId}/reviews")
    @Operation(summary = "Get all reviews for a conference")
    public ResponseEntity<List<ReviewDTO>> getReviewsByConferenceId(@PathVariable Long conferenceId) {
        return ResponseEntity.ok(conferenceService.getReviewsByConferenceId(conferenceId));
    }
}
