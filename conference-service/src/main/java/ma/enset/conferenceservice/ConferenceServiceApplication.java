package ma.enset.conferenceservice;

import ma.enset.conferenceservice.entities.Conference;
import ma.enset.conferenceservice.entities.Review;
import ma.enset.conferenceservice.entities.TypeConference;
import ma.enset.conferenceservice.repository.ConferenceRepository;
import ma.enset.conferenceservice.repository.ReviewRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class ConferenceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConferenceServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(ConferenceRepository conferenceRepository,
            ReviewRepository reviewRepository) {
        return args -> {
            // Create sample conferences
            Conference conf1 = Conference.builder()
                    .titre("Spring Boot Microservices Architecture")
                    .type(TypeConference.ACADEMIQUE)
                    .date(new Date())
                    .duree(120)
                    .nombreInscrits(150)
                    .score(4.5)
                    .keynoteId(1L)
                    .build();
            conferenceRepository.save(conf1);

            Conference conf2 = Conference.builder()
                    .titre("Cloud Native Applications")
                    .type(TypeConference.COMMERCIALE)
                    .date(new Date())
                    .duree(90)
                    .nombreInscrits(200)
                    .score(4.8)
                    .keynoteId(2L)
                    .build();
            conferenceRepository.save(conf2);

            Conference conf3 = Conference.builder()
                    .titre("AI and Machine Learning in Production")
                    .type(TypeConference.ACADEMIQUE)
                    .date(new Date())
                    .duree(180)
                    .nombreInscrits(100)
                    .score(4.7)
                    .keynoteId(3L)
                    .build();
            conferenceRepository.save(conf3);

            // Create sample reviews
            Random random = new Random();
            conferenceRepository.findAll().forEach(conference -> {
                for (int i = 0; i < 3; i++) {
                    Review review = Review.builder()
                            .date(new Date())
                            .texte("Great conference! Very informative and well presented.")
                            .stars(3 + random.nextInt(3)) // 3 to 5 stars
                            .conference(conference)
                            .build();
                    reviewRepository.save(review);
                }
            });

            System.out.println("=== Sample Conferences Created ===");
            System.out.println("Total conferences: " + conferenceRepository.count());
            System.out.println("Total reviews: " + reviewRepository.count());
            conferenceRepository.findAll().forEach(c -> {
                System.out.println("Conference: " + c.getTitre());
                System.out.println("Type: " + c.getType());
                System.out.println("Keynote ID: " + c.getKeynoteId());
                System.out.println("=======================");
            });
        };
    }
}