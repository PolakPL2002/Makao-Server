package tech.kucharski.makao.game;

import org.jetbrains.annotations.NotNull;

/**
 * Checks for the required color.
 */
public class RequireColorValidator implements CardValidator {
    @NotNull
    private final Card.CardType.Color required;

    /**
     * @param required Required color.
     */
    public RequireColorValidator(@NotNull Card.CardType.Color required) {
        this.required = required;
    }

    @Override
    public boolean validate(@NotNull Card card) {
        return required == card.getType().getColor();
    }
}
