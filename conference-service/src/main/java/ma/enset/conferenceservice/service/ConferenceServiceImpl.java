package ma.enset.conferenceservice.service;

import lombok.RequiredArgsConstructor;
import ma.enset.conferenceservice.dto.ConferenceDTO;
import ma.enset.conferenceservice.dto.ReviewDTO;
import ma.enset.conferenceservice.entities.Conference;
import ma.enset.conferenceservice.entities.Review;
import ma.enset.conferenceservice.entities.TypeConference;
import ma.enset.conferenceservice.feign.KeynoteRestClient;
import ma.enset.conferenceservice.mapper.ConferenceMapper;
import ma.enset.conferenceservice.model.Keynote;
import ma.enset.conferenceservice.repository.ConferenceRepository;
import ma.enset.conferenceservice.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ConferenceServiceImpl implements ConferenceService {
    
    private final ConferenceRepository conferenceRepository;
    private final ReviewRepository reviewRepository;
    private final KeynoteRestClient keynoteRestClient;
    private final ConferenceMapper mapper;
    
    @Override
    public ConferenceDTO createConference(ConferenceDTO conferenceDTO) {
        Conference conference = mapper.toEntity(conferenceDTO);
        Conference savedConference = conferenceRepository.save(conference);
        return enrichWithKeynote(savedConference);
    }
    
    @Override
    public ConferenceDTO updateConference(Long id, ConferenceDTO conferenceDTO) {
        Conference conference = conferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conference not found"));
        
        conference.setTitre(conferenceDTO.getTitre());
        conference.setType(conferenceDTO.getType());
        conference.setDate(conferenceDTO.getDate());
        conference.setDuree(conferenceDTO.getDuree());
        conference.setNombreInscrits(conferenceDTO.getNombreInscrits());
        conference.setScore(conferenceDTO.getScore());
        conference.setKeynoteId(conferenceDTO.getKeynoteId());
        
        Conference updatedConference = conferenceRepository.save(conference);
        return enrichWithKeynote(updatedConference);
    }
    
    @Override
    public void deleteConference(Long id) {
        conferenceRepository.deleteById(id);
    }
    
    @Override
    public ConferenceDTO getConferenceById(Long id) {
        Conference conference = conferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conference not found"));
        return enrichWithKeynote(conference);
    }
    
    @Override
    public List<ConferenceDTO> getAllConferences() {
        return conferenceRepository.findAll().stream()
                .map(this::enrichWithKeynote)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ConferenceDTO> getConferencesByType(TypeConference type) {
        return conferenceRepository.findByType(type).stream()
                .map(this::enrichWithKeynote)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ConferenceDTO> getConferencesByKeynoteId(Long keynoteId) {
        return conferenceRepository.findByKeynoteId(keynoteId).stream()
                .map(this::enrichWithKeynote)
                .collect(Collectors.toList());
    }
    
    @Override
    public ReviewDTO addReview(Long conferenceId, ReviewDTO reviewDTO) {
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conference not found"));
        
        Review review = mapper.reviewToEntity(reviewDTO);
        review.setConference(conference);
        Review savedReview = reviewRepository.save(review);
        
        return mapper.reviewToDTO(savedReview);
    }
    
    @Override
    public List<ReviewDTO> getReviewsByConferenceId(Long conferenceId) {
        return reviewRepository.findByConferenceId(conferenceId).stream()
                .map(mapper::reviewToDTO)
                .collect(Collectors.toList());
    }
    
    private ConferenceDTO enrichWithKeynote(Conference conference) {
        ConferenceDTO dto = mapper.toDTO(conference);
        if (conference.getKeynoteId() != null) {
            try {
                Keynote keynote = keynoteRestClient.findKeynoteById(conference.getKeynoteId());
                dto.setKeynote(keynote);
            } catch (Exception e) {
                // Fallback already handled by circuit breaker
            }
        }
        return dto;
    }
}

