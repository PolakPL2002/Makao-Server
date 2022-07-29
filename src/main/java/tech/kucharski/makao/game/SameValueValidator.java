package tech.kucharski.makao.game;

import org.jetbrains.annotations.NotNull;

/**
 * Checks for the same value rule.
 */
public class SameValueValidator implements CardValidator {
    private final Card topCard;

    /**
     * @param topCard Card on the top.
     */
    public SameValueValidator(@NotNull Card topCard) {
        this.topCard = topCard;
    }

    @Override
    public boolean validate(@NotNull Card card) {
        return topCard.getType().getValue() == card.getType().getValue();
    }
}
