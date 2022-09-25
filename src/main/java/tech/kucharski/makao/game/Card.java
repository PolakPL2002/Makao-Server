package tech.kucharski.makao.game;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.util.JSONConvertible;

import java.util.UUID;

/**
 * Card
 */
public class Card implements JSONConvertible {
    private final CardType type;
    private final UUID uuid;

    /**
     * @param uuid Card UUID
     * @param type Card type
     */
    public Card(UUID uuid, CardType type) {
        this.uuid = uuid;
        this.type = type;
    }

    /**
     * @return Card type
     */
    public CardType getType() {
        return type;
    }

    /**
     * @return JSON object
     */
    public JsonObject toJSONObject() {
        JsonObject obj = new JsonObject();

        obj.addProperty("uuid", getUUID().toString());

        obj.add("type", type.toJSONObject());
        return obj;
    }

    /**
     * @return Card UUID
     */
    public UUID getUUID() {
        return uuid;
    }

    @SuppressWarnings("JavaDoc")
    public
    enum CardType {
        CLUBS_0(Value.CARD_0, Color.CLUBS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        CLUBS_1(Value.CARD_1, Color.CLUBS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        CLUBS_2(Value.CARD_2, Color.CLUBS, new CardSettings(true, false, ValidatorPreset.STANDARD, 2, 0)),
        CLUBS_3(Value.CARD_3, Color.CLUBS, new CardSettings(true, false, ValidatorPreset.STANDARD, 3, 0)),
        CLUBS_4(Value.CARD_4, Color.CLUBS, new CardSettings(true, false, ValidatorPreset.STANDARD, 0, 1)),
        CLUBS_5(Value.CARD_5, Color.CLUBS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        CLUBS_6(Value.CARD_6, Color.CLUBS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        CLUBS_7(Value.CARD_7, Color.CLUBS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        CLUBS_8(Value.CARD_8, Color.CLUBS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        CLUBS_9(Value.CARD_9, Color.CLUBS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        CLUBS_10(Value.CARD_10, Color.CLUBS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        CLUBS_11(Value.CARD_11, Color.CLUBS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        CLUBS_J(Value.CARD_JACK, Color.CLUBS, new CardSettings(true, false, ValidatorPreset.REQUIRE_VALUE, 0, 0)),
        CLUBS_Q(Value.CARD_QUEEN, Color.CLUBS, new CardSettings(true, false, ValidatorPreset.ACCEPT_ALL, 0, 0)),
        CLUBS_K(Value.CARD_KING, Color.CLUBS, new CardSettings(true, false, ValidatorPreset.STANDARD, 5, 0)),
        CLUBS_A(Value.CARD_ACE, Color.CLUBS, new CardSettings(true, false, ValidatorPreset.REQUIRE_COLOR, 0, 0)),
        DIAMONDS_0(Value.CARD_0, Color.DIAMONDS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        DIAMONDS_1(Value.CARD_1, Color.DIAMONDS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        DIAMONDS_2(Value.CARD_2, Color.DIAMONDS, new CardSettings(true, false, ValidatorPreset.STANDARD, 2, 0)),
        DIAMONDS_3(Value.CARD_3, Color.DIAMONDS, new CardSettings(true, false, ValidatorPreset.STANDARD, 3, 0)),
        DIAMONDS_4(Value.CARD_4, Color.DIAMONDS, new CardSettings(true, false, ValidatorPreset.STANDARD, 0, 1)),
        DIAMONDS_5(Value.CARD_5, Color.DIAMONDS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        DIAMONDS_6(Value.CARD_6, Color.DIAMONDS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        DIAMONDS_7(Value.CARD_7, Color.DIAMONDS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        DIAMONDS_8(Value.CARD_8, Color.DIAMONDS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        DIAMONDS_9(Value.CARD_9, Color.DIAMONDS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        DIAMONDS_10(Value.CARD_10, Color.DIAMONDS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        DIAMONDS_11(Value.CARD_11, Color.DIAMONDS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        DIAMONDS_J(Value.CARD_JACK, Color.DIAMONDS, new CardSettings(true, false, ValidatorPreset.REQUIRE_VALUE, 0, 0)),
        DIAMONDS_Q(Value.CARD_QUEEN, Color.DIAMONDS, new CardSettings(true, false, ValidatorPreset.ACCEPT_ALL, 0, 0)),
        DIAMONDS_K(Value.CARD_KING, Color.DIAMONDS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        DIAMONDS_A(Value.CARD_ACE, Color.DIAMONDS, new CardSettings(true, false, ValidatorPreset.REQUIRE_COLOR, 0, 0)),
        HEARTS_0(Value.CARD_0, Color.HEARTS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        HEARTS_1(Value.CARD_1, Color.HEARTS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        HEARTS_2(Value.CARD_2, Color.HEARTS, new CardSettings(true, false, ValidatorPreset.STANDARD, 2, 0)),
        HEARTS_3(Value.CARD_3, Color.HEARTS, new CardSettings(true, false, ValidatorPreset.STANDARD, 3, 0)),
        HEARTS_4(Value.CARD_4, Color.HEARTS, new CardSettings(true, false, ValidatorPreset.STANDARD, 0, 1)),
        HEARTS_5(Value.CARD_5, Color.HEARTS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        HEARTS_6(Value.CARD_6, Color.HEARTS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        HEARTS_7(Value.CARD_7, Color.HEARTS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        HEARTS_8(Value.CARD_8, Color.HEARTS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        HEARTS_9(Value.CARD_9, Color.HEARTS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        HEARTS_10(Value.CARD_10, Color.HEARTS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        HEARTS_11(Value.CARD_11, Color.HEARTS, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        HEARTS_J(Value.CARD_JACK, Color.HEARTS, new CardSettings(true, false, ValidatorPreset.REQUIRE_VALUE, 0, 0)),
        HEARTS_Q(Value.CARD_QUEEN, Color.HEARTS, new CardSettings(true, false, ValidatorPreset.ACCEPT_ALL, 0, 0)),
        HEARTS_K(Value.CARD_KING, Color.HEARTS, new CardSettings(true, false, ValidatorPreset.STANDARD, 5, 0)),
        HEARTS_A(Value.CARD_ACE, Color.HEARTS, new CardSettings(true, false, ValidatorPreset.REQUIRE_COLOR, 0, 0)),
        SPADES_0(Value.CARD_0, Color.SPADES, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        SPADES_1(Value.CARD_1, Color.SPADES, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        SPADES_2(Value.CARD_2, Color.SPADES, new CardSettings(true, false, ValidatorPreset.STANDARD, 2, 0)),
        SPADES_3(Value.CARD_3, Color.SPADES, new CardSettings(true, false, ValidatorPreset.STANDARD, 3, 0)),
        SPADES_4(Value.CARD_4, Color.SPADES, new CardSettings(true, false, ValidatorPreset.STANDARD, 0, 1)),
        SPADES_5(Value.CARD_5, Color.SPADES, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        SPADES_6(Value.CARD_6, Color.SPADES, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        SPADES_7(Value.CARD_7, Color.SPADES, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        SPADES_8(Value.CARD_8, Color.SPADES, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        SPADES_9(Value.CARD_9, Color.SPADES, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        SPADES_10(Value.CARD_10, Color.SPADES, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        SPADES_11(Value.CARD_11, Color.SPADES, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        SPADES_J(Value.CARD_JACK, Color.SPADES, new CardSettings(true, false, ValidatorPreset.REQUIRE_VALUE, 0, 0)),
        SPADES_Q(Value.CARD_QUEEN, Color.SPADES, new CardSettings(true, false, ValidatorPreset.ACCEPT_ALL, 0, 0)),
        SPADES_K(Value.CARD_KING, Color.SPADES, new CardSettings(true, true, ValidatorPreset.STANDARD, 0, 0)),
        SPADES_A(Value.CARD_ACE, Color.SPADES, new CardSettings(true, false, ValidatorPreset.REQUIRE_COLOR, 0, 0));

        @NotNull
        private final Color color;
        @NotNull
        private final Value value;
        @NotNull
        private final CardSettings defaultSettings;

        CardType(@NotNull Value value, @NotNull Color color, @NotNull CardSettings defaultSettings) {
            this.value = value;
            this.color = color;
            this.defaultSettings = defaultSettings;
        }

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

        @NotNull
        public String getCode() {
            return String.valueOf(value.getCode()) + color.getCode();
        }

        @NotNull
        public Value getValue() {
            return value;
        }

        @NotNull
        public Color getColor() {
            return color;
        }

        public enum Color {
            CLUBS('C'),
            DIAMONDS('D'),
            HEARTS('H'),
            SPADES('S');

            private final char code;

            Color(char code) {
                this.code = code;
            }

            public char getCode() {
                return code;
            }
        }

        public enum Value {
            CARD_0('0'),
            CARD_1('1'),
            CARD_2('2'),
            CARD_3('3'),
            CARD_4('4'),
            CARD_5('5'),
            CARD_6('6'),
            CARD_7('7'),
            CARD_8('8'),
            CARD_9('9'),
            CARD_10('T'),
            CARD_11('E'),
            CARD_JACK('J'),
            CARD_QUEEN('Q'),
            CARD_KING('K'),
            CARD_ACE('A');
            private final char code;

            Value(char code) {
                this.code = code;
            }

            public char getCode() {
                return code;
            }
        }
    }
}
