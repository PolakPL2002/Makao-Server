package tech.kucharski.makao.game.validators;

import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.game.deck.Card;
import tech.kucharski.makao.game.deck.CardValue;

/**
 * Checks for required value.
 */
public class RequireValueValidator implements CardValidator {
    @NotNull
    private final CardValue required;

    /**
     * @param required Required value
     */
    public RequireValueValidator(@NotNull CardValue required) {
        this.required = required;
    }

    @Override
    public boolean validate(@NotNull Card card) {
        return required == card.getType().getValue();
    }
}
