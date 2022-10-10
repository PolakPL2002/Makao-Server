package tech.kucharski.makao.game;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Turn manager
 */
public class TurnManager {
    private final List<UUID> players = Collections.synchronizedList(new ArrayList<>());
    private UUID turnOf = null;

    /**
     * @param uuid ID of the player
     */
    public void addPlayer(UUID uuid) {
        players.add(uuid);
    }

    /**
     * @return ID of the current turn holder
     */
    @NotNull
    public UUID getCurrentPlayer() {
        if (turnOf == null) nextTurn();
        return turnOf;
    }

    /**
     * Updates the player and returns the id
     *
     * @return ID of the player
     */
    @SuppressWarnings("unused")
    @NotNull
    public UUID getNextPlayer() {
        nextTurn();
        return turnOf;
    }

    /**
     * Updates the player
     */
    public void nextTurn() {
        if (turnOf == null) {
            turnOf = players.get((int) (Math.random() * players.size()));
        } else {
            setRandom();
        }
    }

    /**
     * Sets the random player.
     */
    public void setRandom() {
        turnOf = players.get((players.indexOf(turnOf) + 1) % players.size());
    }

    /**
     * @param uuid ID to remove
     */
    @SuppressWarnings("unused")
    public void removePlayer(UUID uuid) {
        if (turnOf != null && turnOf.equals(uuid)) {
            nextTurn();
        }
        players.remove(uuid);
        if (turnOf != null && turnOf.equals(uuid)) {
            turnOf = null;
        }
    }
}
