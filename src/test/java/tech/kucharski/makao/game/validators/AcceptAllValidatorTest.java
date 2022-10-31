package tech.kucharski.makao.game.validators;

import org.junit.jupiter.api.Test;
import tech.kucharski.makao.game.deck.Card;
import tech.kucharski.makao.game.deck.CardType;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link AcceptAllValidator}.
 */
public class AcceptAllValidatorTest {

    /**
     * Test for {@link AcceptAllValidator#validate(Card)}.
     */
    @Test
    public void validate() {
        for (CardType type : CardType.values()) {
            assertTrue(new AcceptAllValidator().validate(new Card(UUID.randomUUID(), type)));
        }
    }
}