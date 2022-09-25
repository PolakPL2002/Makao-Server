package tech.kucharski.makao.game;

import org.jetbrains.annotations.NotNull;

/**
 * Performs 'and' operation on other validators.
 */
public class CombinedValidator implements CardValidator {
    @NotNull
    private final CardValidator[] validators;


    /**
     * @param validators Validators to be tested.
     */
    public CombinedValidator(@NotNull CardValidator... validators) {
        this.validators = validators;
    }

    @Override
    public boolean validate(@NotNull Card card) {
        for (CardValidator validator : validators)
            if (!validator.validate(card))
                return false;
        return true;
    }
}
