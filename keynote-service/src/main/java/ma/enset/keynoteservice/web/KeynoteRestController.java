package ma.enset.keynoteservice.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.enset.keynoteservice.entities.Keynote;
import ma.enset.keynoteservice.repository.KeynoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keynotes")
@RequiredArgsConstructor
@Tag(name = "Keynote Management", description = "APIs for managing keynote speakers")
public class KeynoteRestController {

    private final KeynoteRepository keynoteRepository;

    @GetMapping
    @Operation(summary = "Get all keynotes")
    public ResponseEntity<List<Keynote>> getAllKeynotes() {
        return ResponseEntity.ok(keynoteRepository.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get keynote by ID")
    public ResponseEntity<Keynote> getKeynoteById(@PathVariable Long id) {
        return keynoteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new keynote")
    public ResponseEntity<Keynote> createKeynote(@RequestBody Keynote keynote) {
        Keynote savedKeynote = keynoteRepository.save(keynote);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedKeynote);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a keynote")
    public ResponseEntity<Keynote> updateKeynote(
            @PathVariable Long id,
            @RequestBody Keynote keynote) {
        return keynoteRepository.findById(id)
                .map(existingKeynote -> {
                    existingKeynote.setNom(keynote.getNom());
                    existingKeynote.setPrenom(keynote.getPrenom());
                    existingKeynote.setEmail(keynote.getEmail());
                    existingKeynote.setFonction(keynote.getFonction());
                    Keynote updatedKeynote = keynoteRepository.save(existingKeynote);
                    return ResponseEntity.ok(updatedKeynote);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a keynote")
    public ResponseEntity<Void> deleteKeynote(@PathVariable Long id) {
        if (keynoteRepository.existsById(id)) {
            keynoteRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
