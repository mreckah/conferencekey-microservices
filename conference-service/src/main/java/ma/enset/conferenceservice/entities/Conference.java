package ma.enset.conferenceservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Conference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    
    @Enumerated(EnumType.STRING)
    private TypeConference type;
    
    @Temporal(TemporalType.DATE)
    private Date date;
    
    private Integer duree; // durée en minutes
    private Integer nombreInscrits;
    private Double score;
    
    private Long keynoteId; // Reference to Keynote from keynote-service
    
    @OneToMany(mappedBy = "conference", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;
    
    @Transient
    private Object keynote; // Will be populated by Feign client
}

