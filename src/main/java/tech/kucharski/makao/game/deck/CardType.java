package tech.kucharski.makao.game.deck;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.game.validators.ValidatorPreset;

/**
 * Type of the card.
 */
@SuppressWarnings("JavaDoc")
public enum CardType {
    CLUBS_0(CardValue.CARD_0, CardColor.CLUBS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    CLUBS_1(CardValue.CARD_1, CardColor.CLUBS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    CLUBS_2(CardValue.CARD_2, CardColor.CLUBS, new CardSettings(true, false, ValidatorPreset.STANDARD, 2, 0)),
    CLUBS_3(CardValue.CARD_3, CardColor.CLUBS, new CardSettings(true, false, ValidatorPreset.STANDARD, 3, 0)),
    CLUBS_4(CardValue.CARD_4, CardColor.CLUBS, new CardSettings(true, false, ValidatorPreset.STANDARD, 0, 1)),
    CLUBS_5(CardValue.CARD_5, CardColor.CLUBS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    CLUBS_6(CardValue.CARD_6, CardColor.CLUBS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    CLUBS_7(CardValue.CARD_7, CardColor.CLUBS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    CLUBS_8(CardValue.CARD_8, CardColor.CLUBS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    CLUBS_9(CardValue.CARD_9, CardColor.CLUBS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    CLUBS_10(CardValue.CARD_10, CardColor.CLUBS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    CLUBS_11(CardValue.CARD_11, CardColor.CLUBS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    CLUBS_J(CardValue.CARD_JACK, CardColor.CLUBS, new CardSettings(true, false, ValidatorPreset.REQUIRE_VALUE, 0, 0)),
    CLUBS_Q(CardValue.CARD_QUEEN, CardColor.CLUBS, new CardSettings(true, false, ValidatorPreset.ACCEPT_ALL, 0, 0)),
    CLUBS_K(CardValue.CARD_KING, CardColor.CLUBS, new CardSettings(true, false, ValidatorPreset.STANDARD, 5, 0)),
    CLUBS_A(CardValue.CARD_ACE, CardColor.CLUBS, new CardSettings(true, false, ValidatorPreset.REQUIRE_COLOR, 0, 0)),
    DIAMONDS_0(CardValue.CARD_0, CardColor.DIAMONDS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    DIAMONDS_1(CardValue.CARD_1, CardColor.DIAMONDS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    DIAMONDS_2(CardValue.CARD_2, CardColor.DIAMONDS, new CardSettings(true, false, ValidatorPreset.STANDARD, 2, 0)),
    DIAMONDS_3(CardValue.CARD_3, CardColor.DIAMONDS, new CardSettings(true, false, ValidatorPreset.STANDARD, 3, 0)),
    DIAMONDS_4(CardValue.CARD_4, CardColor.DIAMONDS, new CardSettings(true, false, ValidatorPreset.STANDARD, 0, 1)),
    DIAMONDS_5(CardValue.CARD_5, CardColor.DIAMONDS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    DIAMONDS_6(CardValue.CARD_6, CardColor.DIAMONDS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    DIAMONDS_7(CardValue.CARD_7, CardColor.DIAMONDS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    DIAMONDS_8(CardValue.CARD_8, CardColor.DIAMONDS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    DIAMONDS_9(CardValue.CARD_9, CardColor.DIAMONDS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    DIAMONDS_10(CardValue.CARD_10, CardColor.DIAMONDS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    DIAMONDS_11(CardValue.CARD_11, CardColor.DIAMONDS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    DIAMONDS_J(CardValue.CARD_JACK, CardColor.DIAMONDS, new CardSettings(true, false, ValidatorPreset.REQUIRE_VALUE, 0, 0)),
    DIAMONDS_Q(CardValue.CARD_QUEEN, CardColor.DIAMONDS, new CardSettings(true, false, ValidatorPreset.ACCEPT_ALL, 0, 0)),
    DIAMONDS_K(CardValue.CARD_KING, CardColor.DIAMONDS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    DIAMONDS_A(CardValue.CARD_ACE, CardColor.DIAMONDS, new CardSettings(true, false, ValidatorPreset.REQUIRE_COLOR, 0, 0)),
    HEARTS_0(CardValue.CARD_0, CardColor.HEARTS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    HEARTS_1(CardValue.CARD_1, CardColor.HEARTS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    HEARTS_2(CardValue.CARD_2, CardColor.HEARTS, new CardSettings(true, false, ValidatorPreset.STANDARD, 2, 0)),
    HEARTS_3(CardValue.CARD_3, CardColor.HEARTS, new CardSettings(true, false, ValidatorPreset.STANDARD, 3, 0)),
    HEARTS_4(CardValue.CARD_4, CardColor.HEARTS, new CardSettings(true, false, ValidatorPreset.STANDARD, 0, 1)),
    HEARTS_5(CardValue.CARD_5, CardColor.HEARTS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    HEARTS_6(CardValue.CARD_6, CardColor.HEARTS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    HEARTS_7(CardValue.CARD_7, CardColor.HEARTS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    HEARTS_8(CardValue.CARD_8, CardColor.HEARTS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    HEARTS_9(CardValue.CARD_9, CardColor.HEARTS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    HEARTS_10(CardValue.CARD_10, CardColor.HEARTS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    HEARTS_11(CardValue.CARD_11, CardColor.HEARTS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    HEARTS_J(CardValue.CARD_JACK, CardColor.HEARTS, new CardSettings(true, false, ValidatorPreset.REQUIRE_VALUE, 0, 0)),
    HEARTS_Q(CardValue.CARD_QUEEN, CardColor.HEARTS, new CardSettings(true, false, ValidatorPreset.ACCEPT_ALL, 0, 0)),
    HEARTS_K(CardValue.CARD_KING, CardColor.HEARTS, new CardSettings(true, false, ValidatorPreset.STANDARD, 5, 0)),
    HEARTS_A(CardValue.CARD_ACE, CardColor.HEARTS, new CardSettings(true, false, ValidatorPreset.REQUIRE_COLOR, 0, 0)),
    SPADES_0(CardValue.CARD_0, CardColor.SPADES, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    SPADES_1(CardValue.CARD_1, CardColor.SPADES, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    SPADES_2(CardValue.CARD_2, CardColor.SPADES, new CardSettings(true, false, ValidatorPreset.STANDARD, 2, 0)),
    SPADES_3(CardValue.CARD_3, CardColor.SPADES, new CardSettings(true, false, ValidatorPreset.STANDARD, 3, 0)),
    SPADES_4(CardValue.CARD_4, CardColor.SPADES, new CardSettings(true, false, ValidatorPreset.STANDARD, 0, 1)),
    SPADES_5(CardValue.CARD_5, CardColor.SPADES, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    SPADES_6(CardValue.CARD_6, CardColor.SPADES, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    SPADES_7(CardValue.CARD_7, CardColor.SPADES, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    SPADES_8(CardValue.CARD_8, CardColor.SPADES, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    SPADES_9(CardValue.CARD_9, CardColor.SPADES, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    SPADES_10(CardValue.CARD_10, CardColor.SPADES, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    SPADES_11(CardValue.CARD_11, CardColor.SPADES, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    SPADES_J(CardValue.CARD_JACK, CardColor.SPADES, new CardSettings(true, false, ValidatorPreset.REQUIRE_VALUE, 0, 0)),
    SPADES_Q(CardValue.CARD_QUEEN, CardColor.SPADES, new CardSettings(true, false, ValidatorPreset.ACCEPT_ALL, 0, 0)),
    SPADES_K(CardValue.CARD_KING, CardColor.SPADES, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
    SPADES_A(CardValue.CARD_ACE, CardColor.SPADES, new CardSettings(true, false, ValidatorPreset.REQUIRE_COLOR, 0, 0));

    @NotNull
    private final CardColor color;
    @NotNull
    private final CardSettings defaultSettings;
    @NotNull
    private final CardValue value;

    /**
     * @param value           {@link CardValue} of the type.
     * @param color           {@link CardColor} of the type.
     * @param defaultSettings Default {@link CardSettings} of the type.
     */
    CardType(@NotNull CardValue value, @NotNull CardColor color, @NotNull CardSettings defaultSettings) {
        this.value = value;
        this.color = color;
        this.defaultSettings = defaultSettings;
    }

    /**
     * @return Default {@link CardSettings} of the type.
     */
    @NotNull
    public CardSettings getDefaultSettings() {
        return defaultSettings;
    }

    @NotNull
    public JsonObject toJSONObject() {
        JsonObject type = new JsonObject();
        type.addProperty("value", getValue().name());
        type.addProperty("color", getColor().name());
        type.addProperty("code", getCode());
        return type;
    }

    /**
     * @return Double character code of the type.
     */
    @NotNull
    public String getCode() {
        return String.valueOf(value.getCode()) + color.getCode();
    }

    /**
     * @return {@link CardValue} of the type.
     */
    @NotNull
    public CardValue getValue() {
        return value;
    }

    /**
     * @return {@link CardColor} of the type.
     */
    @NotNull
    public CardColor getColor() {
        return color;
    }
}
