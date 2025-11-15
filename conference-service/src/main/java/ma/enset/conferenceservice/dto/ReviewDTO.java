package ma.enset.conferenceservice.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReviewDTO {
    private Long id;
    private Date date;
    private String texte;
    private Integer stars;
    private Long conferenceId;
}

