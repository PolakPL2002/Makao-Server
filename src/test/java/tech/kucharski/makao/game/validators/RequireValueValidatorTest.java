package tech.kucharski.makao.game.validators;

import org.junit.jupiter.api.Test;
import tech.kucharski.makao.game.deck.Card;
import tech.kucharski.makao.game.deck.CardType;
import tech.kucharski.makao.game.deck.CardValue;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link RequireValueValidator}.
 */
public class RequireValueValidatorTest {
    /**
     * Test for {@link RequireValueValidator#validate(Card)}.
     */
    @Test
    public void validate() {
        for (CardValue value : CardValue.values()) {
            final RequireValueValidator v1 = new RequireValueValidator(value);
            for (CardType type : CardType.values()) {
                final Card card = new Card(UUID.randomUUID(), type);
                assertEquals(card.getType().getValue().equals(value), v1.validate(card));
            }
        }
    }
}