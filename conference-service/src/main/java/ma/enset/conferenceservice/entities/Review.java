package ma.enset.conferenceservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    
    @Column(length = 1000)
    private String texte;
    
    private Integer stars; // Note de 1 à 5
    
    @ManyToOne
    @JsonIgnore
    private Conference conference;
}

