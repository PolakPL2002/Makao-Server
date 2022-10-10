package tech.kucharski.makao.game.validators;

import tech.kucharski.makao.game.deck.Card;

/**
 * Validates if the card can be placed on top of the current card.
 */
public interface CardValidator {
    /**
     * @param card Card to be validated.
     * @return Whether the card can be placed on top of the current card.
     */
    boolean validate(Card card);
}
