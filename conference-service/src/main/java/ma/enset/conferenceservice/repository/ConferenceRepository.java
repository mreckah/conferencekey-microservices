package ma.enset.conferenceservice.repository;

import ma.enset.conferenceservice.entities.Conference;
import ma.enset.conferenceservice.entities.TypeConference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ConferenceRepository extends JpaRepository<Conference, Long> {
    List<Conference> findByType(TypeConference type);
    List<Conference> findByKeynoteId(Long keynoteId);
}

