package tech.kucharski.makao.game;

/**
 * Validator preset
 */
public enum ValidatorPreset {
    /**
     * Standard validator (that is same color or same value)
     */
    STANDARD,
    /**
     * Accept only a given color or another REQUIRE_COLOR card.
     */
    REQUIRE_COLOR,
    /**
     * Accept everything.
     */
    ACCEPT_ALL,
    /**
     * Accept only a given value or another REQUIRE_VALUE card.
     */
    REQUIRE_VALUE
}
