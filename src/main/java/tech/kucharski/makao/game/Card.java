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
        CLUBS_0(Value.CARD_0, Color.CLUBS),
        CLUBS_1(Value.CARD_1, Color.CLUBS),
        CLUBS_2(Value.CARD_2, Color.CLUBS),
        CLUBS_3(Value.CARD_3, Color.CLUBS),
        CLUBS_4(Value.CARD_4, Color.CLUBS),
        CLUBS_5(Value.CARD_5, Color.CLUBS),
        CLUBS_6(Value.CARD_6, Color.CLUBS),
        CLUBS_7(Value.CARD_7, Color.CLUBS),
        CLUBS_8(Value.CARD_8, Color.CLUBS),
        CLUBS_9(Value.CARD_9, Color.CLUBS),
        CLUBS_10(Value.CARD_10, Color.CLUBS),
        CLUBS_11(Value.CARD_11, Color.CLUBS),
        CLUBS_J(Value.CARD_JACK, Color.CLUBS),
        CLUBS_Q(Value.CARD_QUEEN, Color.CLUBS),
        CLUBS_K(Value.CARD_KING, Color.CLUBS),
        CLUBS_A(Value.CARD_ACE, Color.CLUBS),
        DIAMONDS_0(Value.CARD_0, Color.DIAMONDS),
        DIAMONDS_1(Value.CARD_1, Color.DIAMONDS),
        DIAMONDS_2(Value.CARD_2, Color.DIAMONDS),
        DIAMONDS_3(Value.CARD_3, Color.DIAMONDS),
        DIAMONDS_4(Value.CARD_4, Color.DIAMONDS),
        DIAMONDS_5(Value.CARD_5, Color.DIAMONDS),
        DIAMONDS_6(Value.CARD_6, Color.DIAMONDS),
        DIAMONDS_7(Value.CARD_7, Color.DIAMONDS),
        DIAMONDS_8(Value.CARD_8, Color.DIAMONDS),
        DIAMONDS_9(Value.CARD_9, Color.DIAMONDS),
        DIAMONDS_10(Value.CARD_10, Color.DIAMONDS),
        DIAMONDS_11(Value.CARD_11, Color.DIAMONDS),
        DIAMONDS_J(Value.CARD_JACK, Color.DIAMONDS),
        DIAMONDS_Q(Value.CARD_QUEEN, Color.DIAMONDS),
        DIAMONDS_K(Value.CARD_KING, Color.DIAMONDS),
        DIAMONDS_A(Value.CARD_ACE, Color.DIAMONDS),
        HEARTS_0(Value.CARD_0, Color.HEARTS),
        HEARTS_1(Value.CARD_1, Color.HEARTS),
        HEARTS_2(Value.CARD_2, Color.HEARTS),
        HEARTS_3(Value.CARD_3, Color.HEARTS),
        HEARTS_4(Value.CARD_4, Color.HEARTS),
        HEARTS_5(Value.CARD_5, Color.HEARTS),
        HEARTS_6(Value.CARD_6, Color.HEARTS),
        HEARTS_7(Value.CARD_7, Color.HEARTS),
        HEARTS_8(Value.CARD_8, Color.HEARTS),
        HEARTS_9(Value.CARD_9, Color.HEARTS),
        HEARTS_10(Value.CARD_10, Color.HEARTS),
        HEARTS_11(Value.CARD_11, Color.HEARTS),
        HEARTS_J(Value.CARD_JACK, Color.HEARTS),
        HEARTS_Q(Value.CARD_QUEEN, Color.HEARTS),
        HEARTS_K(Value.CARD_KING, Color.HEARTS),
        HEARTS_A(Value.CARD_ACE, Color.HEARTS),
        SPADES_0(Value.CARD_0, Color.SPADES),
        SPADES_1(Value.CARD_1, Color.SPADES),
        SPADES_2(Value.CARD_2, Color.SPADES),
        SPADES_3(Value.CARD_3, Color.SPADES),
        SPADES_4(Value.CARD_4, Color.SPADES),
        SPADES_5(Value.CARD_5, Color.SPADES),
        SPADES_6(Value.CARD_6, Color.SPADES),
        SPADES_7(Value.CARD_7, Color.SPADES),
        SPADES_8(Value.CARD_8, Color.SPADES),
        SPADES_9(Value.CARD_9, Color.SPADES),
        SPADES_10(Value.CARD_10, Color.SPADES),
        SPADES_11(Value.CARD_11, Color.SPADES),
        SPADES_J(Value.CARD_JACK, Color.SPADES),
        SPADES_Q(Value.CARD_QUEEN, Color.SPADES),
        SPADES_K(Value.CARD_KING, Color.SPADES),
        SPADES_A(Value.CARD_ACE, Color.SPADES);

        @NotNull
        private final Color color;
        @NotNull
        private final Value value;

        CardType(@NotNull Value value, @NotNull Color color) {
            this.value = value;
            this.color = color;
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
