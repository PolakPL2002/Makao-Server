package tech.kucharski.makao.game;

import org.jetbrains.annotations.NotNull;

/**
 * Checks for required value.
 */
public class RequireValueValidator implements CardValidator {
    @NotNull
    private final Card.CardType.Value required;

    /**
     * @param required Required value
     */
    public RequireValueValidator(@NotNull Card.CardType.Value required) {
        this.required = required;
    }

    @Override
    public boolean validate(@NotNull Card card) {
        return required == card.getType().getValue();
    }
}
