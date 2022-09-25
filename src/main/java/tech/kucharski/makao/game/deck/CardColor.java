package tech.kucharski.makao.game.deck;


/**
 * A color of the card.
 */
@SuppressWarnings("MissingJavadoc")
public enum CardColor {
    CLUBS('C'),
    DIAMONDS('D'),
    HEARTS('H'),
    SPADES('S');

    private final char code;

    /**
     * @param code Character code of this color.
     */
    CardColor(char code) {
        this.code = code;
    }

    /**
     * @return Character code of this color.
     */
    public char getCode() {
        return code;
    }
}
