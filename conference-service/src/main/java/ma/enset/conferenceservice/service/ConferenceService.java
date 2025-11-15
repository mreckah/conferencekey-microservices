package ma.enset.conferenceservice.service;

import ma.enset.conferenceservice.dto.ConferenceDTO;
import ma.enset.conferenceservice.dto.ReviewDTO;
import ma.enset.conferenceservice.entities.TypeConference;

import java.util.List;

public interface ConferenceService {
    ConferenceDTO createConference(ConferenceDTO conferenceDTO);
    ConferenceDTO updateConference(Long id, ConferenceDTO conferenceDTO);
    void deleteConference(Long id);
    ConferenceDTO getConferenceById(Long id);
    List<ConferenceDTO> getAllConferences();
    List<ConferenceDTO> getConferencesByType(TypeConference type);
    List<ConferenceDTO> getConferencesByKeynoteId(Long keynoteId);
    
    ReviewDTO addReview(Long conferenceId, ReviewDTO reviewDTO);
    List<ReviewDTO> getReviewsByConferenceId(Long conferenceId);
}

