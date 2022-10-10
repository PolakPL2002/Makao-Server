package tech.kucharski.makao.game.validators;

import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.game.deck.Card;

/**
 * Accepts everything.
 */
public class AcceptAllValidator implements CardValidator {
    /**
     * Constructor
     */
    public AcceptAllValidator() {
    }

    @Override
    public boolean validate(@NotNull Card card) {
        return true;
    }
}
