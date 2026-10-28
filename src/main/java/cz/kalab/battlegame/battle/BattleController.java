package cz.kalab.battlegame.battle;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/battles")
@Tag(name = "Battles", description = "Let two fighters fight and browse the battle history")
public class BattleController {

    private final BattleService battleService;

    public BattleController(BattleService battleService) {
        this.battleService = battleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Fight a battle between two fighters and return the full battle log")
    public BattleResponse fight(@Valid @RequestBody BattleRequest request) {
        return battleService.fight(request);
    }

    @GetMapping
    @Operation(summary = "Battle history, newest first")
    public List<BattleSummary> history() {
        return battleService.history();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a battle summary by id")
    public BattleSummary findById(@PathVariable Long id) {
        return battleService.findById(id);
    }
}
