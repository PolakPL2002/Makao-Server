package tech.kucharski.makao.game;

import java.util.UUID;

/**
 * A player of the game
 */
public class Player {
    private final UUID uuid;

    /**
     * @param uuid ID of the player
     */
    public Player(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * @return ID of the player
     */
    public UUID getUUID() {
        return uuid;
    }
}
