package ma.enset.conferenceservice.repository;

import ma.enset.conferenceservice.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByConferenceId(Long conferenceId);
}

