package cz.kalab.battlegame.fighter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request body for creating / updating a fighter.
 * Validated by Bean Validation before it reaches the service layer.
 */
public record FighterRequest(
        @NotBlank @Size(max = 30) String name,
        @NotNull FighterType type,
        @NotNull @Min(1) @Max(1000) Integer health,
        @NotNull @Min(0) @Max(200) Integer damage,
        @NotNull @Min(0) @Max(200) Integer defense,
        @Min(1) @Max(200) Integer mana,
        @Min(0) @Max(300) Integer magicDamage) {

    @Schema(hidden = true)
    @AssertTrue(message = "mana and magicDamage are required for a MAGE")
    public boolean isMageAttributesValid() {
        return type != FighterType.MAGE || (mana != null && magicDamage != null);
    }
}
