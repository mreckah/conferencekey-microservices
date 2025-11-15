package ma.enset.conferenceservice.dto;

import lombok.*;
import ma.enset.conferenceservice.entities.TypeConference;
import ma.enset.conferenceservice.model.Keynote;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ConferenceDTO {
    private Long id;
    private String titre;
    private TypeConference type;
    private Date date;
    private Integer duree;
    private Integer nombreInscrits;
    private Double score;
    private Long keynoteId;
    private Keynote keynote;
    private List<ReviewDTO> reviews;
}

