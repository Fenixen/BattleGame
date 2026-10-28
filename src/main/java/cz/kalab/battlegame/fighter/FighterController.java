package cz.kalab.battlegame.fighter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/fighters")
@Tag(name = "Fighters", description = "Manage fighters that can enter the arena")
public class FighterController {

    private final FighterService fighterService;

    public FighterController(FighterService fighterService) {
        this.fighterService = fighterService;
    }

    @GetMapping
    @Operation(summary = "List all fighters")
    public List<FighterResponse> findAll() {
        return fighterService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a fighter by id")
    public FighterResponse findById(@PathVariable Long id) {
        return fighterService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new fighter")
    public ResponseEntity<FighterResponse> create(@Valid @RequestBody FighterRequest request) {
        FighterResponse created = fighterService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing fighter")
    public FighterResponse update(@PathVariable Long id, @Valid @RequestBody FighterRequest request) {
        return fighterService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a fighter")
    public void delete(@PathVariable Long id) {
        fighterService.delete(id);
    }
}
