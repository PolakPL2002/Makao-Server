package tech.kucharski.makao.game;

import org.jetbrains.annotations.NotNull;

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
