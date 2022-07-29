package tech.kucharski.makao.game;

import org.jetbrains.annotations.NotNull;

/**
 * Checks for the same color rule.
 */
public class SameColorValidator implements CardValidator {
    private final Card topCard;

    /**
     * @param topCard Card on the top.
     */
    public SameColorValidator(@NotNull Card topCard) {
        this.topCard = topCard;
    }

    @Override
    public boolean validate(@NotNull Card card) {
        return topCard.getType().getColor() == card.getType().getColor();
    }
}
