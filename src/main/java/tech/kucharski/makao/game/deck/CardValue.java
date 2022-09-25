package tech.kucharski.makao.game.deck;

/**
 * A value of the card.
 */
@SuppressWarnings("MissingJavadoc")
public enum CardValue {
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

    /**
     * @param code Character code of this value.
     */
    CardValue(char code) {
        this.code = code;
    }

    /**
     * @return Character code of this value.
     */
    public char getCode() {
        return code;
    }
}
