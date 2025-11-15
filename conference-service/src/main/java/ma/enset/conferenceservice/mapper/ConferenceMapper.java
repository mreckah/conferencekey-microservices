package ma.enset.conferenceservice.mapper;

import ma.enset.conferenceservice.dto.ConferenceDTO;
import ma.enset.conferenceservice.dto.ReviewDTO;
import ma.enset.conferenceservice.entities.Conference;
import ma.enset.conferenceservice.entities.Review;
import ma.enset.conferenceservice.model.Keynote;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ConferenceMapper {

    public ConferenceDTO toDTO(Conference conference) {
        if (conference == null)
            return null;

        return ConferenceDTO.builder()
                .id(conference.getId())
                .titre(conference.getTitre())
                .type(conference.getType())
                .date(conference.getDate())
                .duree(conference.getDuree())
                .nombreInscrits(conference.getNombreInscrits())
                .score(conference.getScore())
                .keynoteId(conference.getKeynoteId())
                .keynote(conference.getKeynote() != null ? (Keynote) conference.getKeynote() : null)
                .reviews(conference.getReviews() != null ? conference.getReviews().stream()
                        .map(this::reviewToDTO)
                        .collect(Collectors.toList()) : null)
                .build();
    }

    public Conference toEntity(ConferenceDTO dto) {
        if (dto == null)
            return null;

        return Conference.builder()
                .id(dto.getId())
                .titre(dto.getTitre())
                .type(dto.getType())
                .date(dto.getDate())
                .duree(dto.getDuree())
                .nombreInscrits(dto.getNombreInscrits())
                .score(dto.getScore())
                .keynoteId(dto.getKeynoteId())
                .build();
    }

    public ReviewDTO reviewToDTO(Review review) {
        if (review == null)
            return null;

        return ReviewDTO.builder()
                .id(review.getId())
                .date(review.getDate())
                .texte(review.getTexte())
                .stars(review.getStars())
                .conferenceId(review.getConference() != null ? review.getConference().getId() : null)
                .build();
    }

    public Review reviewToEntity(ReviewDTO dto) {
        if (dto == null)
            return null;

        return Review.builder()
                .id(dto.getId())
                .date(dto.getDate())
                .texte(dto.getTexte())
                .stars(dto.getStars())
                .build();
    }
}
