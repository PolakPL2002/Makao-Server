package tech.kucharski.makao.game.validators;

import org.junit.jupiter.api.Test;
import tech.kucharski.makao.game.deck.Card;
import tech.kucharski.makao.game.deck.CardColor;
import tech.kucharski.makao.game.deck.CardType;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link RequireColorValidator}.
 */
public class RequireColorValidatorTest {
    /**
     * Test for {@link RequireColorValidator#validate(Card)}.
     */
    @Test
    public void validate() {
        for (CardColor color : CardColor.values()) {
            final RequireColorValidator v1 = new RequireColorValidator(color);
            for (CardType type : CardType.values()) {
                final Card card = new Card(UUID.randomUUID(), type);
                assertEquals(card.getType().getColor().equals(color), v1.validate(card));
            }
        }
    }
}