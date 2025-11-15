package ma.enset.keynoteservice;

import ma.enset.keynoteservice.entities.Keynote;
import ma.enset.keynoteservice.repository.KeynoteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KeynoteServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeynoteServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(KeynoteRepository keynoteRepository) {
		return args -> {
			keynoteRepository.save(Keynote.builder()
					.nom("NABBAR")
					.prenom("Oussama")
					.email("oussama.nabbar@conference.com")
					.fonction("Professor")
					.build());
			keynoteRepository.save(Keynote.builder()
					.nom("ELYOUSSFI")
					.prenom("Mohamed")
					.email("mohamed.elyoussfi@tech.com")
					.fonction("CTO")
					.build());
			keynoteRepository.save(Keynote.builder()
					.nom("BERRADA")
					.prenom("Youness")
					.email("youness.berrada@research.com")
					.fonction("Research Director")
					.build());
			keynoteRepository.findAll().forEach(k -> {
				System.out.println("ID: " + k.getId());
				System.out.println("Nom: " + k.getNom());
				System.out.println("Prenom: " + k.getPrenom());
				System.out.println("Email: " + k.getEmail());
				System.out.println("Fonction: " + k.getFonction());
				System.out.println("=======================");
			});
		};
	}
}
