package tech.kucharski.makao.game.validators;

import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.game.deck.Card;
import tech.kucharski.makao.game.deck.CardColor;

/**
 * Checks for the required color.
 */
public class RequireColorValidator implements CardValidator {
    @NotNull
    private final CardColor required;

    /**
     * @param required Required color.
     */
    public RequireColorValidator(@NotNull CardColor required) {
        this.required = required;
    }

    @Override
    public boolean validate(@NotNull Card card) {
        return required == card.getType().getColor();
    }
}
