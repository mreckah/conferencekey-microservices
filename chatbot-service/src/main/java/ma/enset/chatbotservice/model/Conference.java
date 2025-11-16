package ma.enset.chatbotservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conference {
    private Long id;
    private String titre;
    private String type;
    private Date date;
    private Integer duree;
    private Integer nombreInscrits;
    private Double score;
    private Long keynoteId;
}

