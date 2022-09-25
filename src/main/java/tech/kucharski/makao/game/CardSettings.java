package tech.kucharski.makao.game;

import com.google.gson.JsonObject;
import tech.kucharski.makao.util.JSONConvertible;

/**
 * @param includeInDeck   Whether a card should be present in deck
 * @param canBeStartCard  Whether a card can be a starting card
 * @param validatorPreset Card validators to be used after this card
 * @param cardsToDraw     How many cards should be drawn after this is played
 */
public record CardSettings(boolean includeInDeck, boolean canBeStartCard, ValidatorPreset validatorPreset,
                           int cardsToDraw, int turnsToWait) implements JSONConvertible {

    /**
     * @return JSON representation of the object.
     */
    @Override
    public JsonObject toJSONObject() {
        return null;
    }
}
