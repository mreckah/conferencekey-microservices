package ma.enset.keynoteservice.entities;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "fullKeynote", types = Keynote.class)
public interface KeynoteProjection {
    Long getId();
    String getNom();
    String getPrenom();
    String getEmail();
    String getFonction();
}

