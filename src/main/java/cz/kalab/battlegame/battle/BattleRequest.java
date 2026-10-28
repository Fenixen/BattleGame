package cz.kalab.battlegame.battle;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public record BattleRequest(@NotNull Long fighterAId, @NotNull Long fighterBId) {

    @Schema(hidden = true)
    @AssertTrue(message = "a fighter cannot fight against itself")
    public boolean isDifferentFighters() {
        return !Objects.equals(fighterAId, fighterBId);
    }
}
