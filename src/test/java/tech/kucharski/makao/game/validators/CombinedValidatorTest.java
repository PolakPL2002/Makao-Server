package tech.kucharski.makao.game.validators;

import org.junit.jupiter.api.Test;
import tech.kucharski.makao.game.deck.Card;
import tech.kucharski.makao.game.deck.CardColor;
import tech.kucharski.makao.game.deck.CardType;
import tech.kucharski.makao.game.deck.CardValue;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link CombinedValidator}.
 */
public class CombinedValidatorTest {
    /**
     * Test for {@link CombinedValidator#validate(Card)}.
     */
    @Test
    public void validate() {
        for (CardType type : CardType.values()) {
            assertTrue(new CombinedValidator().validate(new Card(UUID.randomUUID(), type)));
        }
        RequireColorValidator v1 = new RequireColorValidator(CardColor.CLUBS);
        RequireValueValidator v2 = new RequireValueValidator(CardValue.CARD_0);
        for (CardType type : CardType.values()) {
            final Card card = new Card(UUID.randomUUID(), type);
            assertEquals(v1.validate(card) && v2.validate(card), new CombinedValidator(v1, v2).validate(card));
        }
    }
}