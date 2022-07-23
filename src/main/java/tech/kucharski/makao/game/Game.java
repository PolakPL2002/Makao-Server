package tech.kucharski.makao.game;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.Makao;
import tech.kucharski.makao.server.Client;
import tech.kucharski.makao.server.messages.GameRemovedMessage;
import tech.kucharski.makao.server.messages.GameStateChangedMessage;
import tech.kucharski.makao.util.JSONConvertible;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * A game of Makao.
 */
public class Game implements JSONConvertible {
    private final UUID gameID;
    private final List<Player> players = Collections.synchronizedList(new ArrayList<>());
    private final TurnManager turnManager = new TurnManager();
    private Deck deck = null;
    /**
     * State of the game.
     */
    private GamePhase gamePhase = GamePhase.PREPARING;

    /**
     * @param gameID ID of the game.
     */
    public Game(UUID gameID) {
        this.gameID = gameID;
    }

    /**
     * Adds a player to the game. This should only be called in {@link GamePhase#PREPARING} phase of the game.
     *
     * @param clientID Client ID of the player to be added.
     * @return A newly added player.
     * @throws IllegalStateException When the method is called in the wrong game phase.
     */
    public Player addPlayer(@NotNull UUID clientID) throws IllegalStateException {
        if (getGameState() != GamePhase.PREPARING)
            throw new IllegalStateException("A player can only be added in preparing phase of the game.");

        final Player player = new Player(Makao.getInstance().getGameManager().getUniquePlayerID(clientID));
        players.add(player);
        turnManager.addPlayer(player.getUUID());
        sendUpdate(player);
        //TODO Send update
        return player;
    }

    /**
     * @return State of the game
     */
    public GamePhase getGameState() {
        return gamePhase;
    }

    /**
     * Sends a full update to a player.
     *
     * @param player Player that update should be sent to
     */
    private void sendUpdate(@NotNull Player player) {
        final GameStateChangedMessage message = new GameStateChangedMessage(this);
        final Client client = Makao.getInstance().getServer()
                .getClient(Makao.getInstance().getGameManager().getClientID(player.getUUID()));
        if (client != null)
            message.send(client.getSocket());
    }

    /**
     * @return ID of the game.
     */
    public UUID getGameID() {
        return gameID;
    }

    /**
     * @param clientID Client we look for
     * @return Whether game contains this client
     */
    public boolean hasClient(@NotNull UUID clientID) {
        return players.stream().anyMatch(player -> clientID.equals(Makao.getInstance().getGameManager().getClientID(player.getUUID())));
    }

    /**
     * Processes a turn.
     */
    public void nextTurn() {
        turnManager.nextTurn();
        sendUpdate();
    }

    /**
     * Sends full update to all players.
     */
    private void sendUpdate() {
        final GameStateChangedMessage message = new GameStateChangedMessage(this);
        players.forEach(player -> {
            final Client client = Makao.getInstance().getServer()
                    .getClient(Makao.getInstance().getGameManager().getClientID(player.getUUID()));
            if (client != null)
                message.send(client.getSocket());
        });
    }

    /**
     * @param player Player to be removed
     * @throws IllegalStateException When game is in incorrect phase.
     */
    public void removePlayer(@NotNull Player player) throws IllegalStateException {
        if (getGameState() != GamePhase.PREPARING && getGameState() != GamePhase.IN_GAME)
            throw new IllegalStateException(String.format("A player cannot be removed in %s phase of the game.", getGameState()));

        players.remove(player);
        if (deck != null)
            deck.removePlayer(player.getUUID());
        sendUpdate();
        //TODO Send update
    }

    /**
     * @throws IllegalStateException When game is in incorrect state.
     */
    public void startGame() throws IllegalStateException {
        if (getGameState() != GamePhase.PREPARING)
            throw new IllegalStateException("A game can only be started in preparing phase of the game.");

        setGamePhase(GamePhase.IN_GAME);

        new GameRemovedMessage(this).broadcast();

        deck = new Deck((players.size() - 1) / 4 + 1);
        players.forEach(player -> deck.givePlayerCards(player.getUUID(), 5));
    }

    /**
     * Sets a game phase.
     *
     * @param gamePhase New game phase.
     */
    private void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
        sendUpdate();
    }

    @Override
    public JsonObject toJSONObject() {
        JsonObject obj = new JsonObject();

        obj.addProperty("uuid", gameID.toString());

        obj.addProperty("phase", gamePhase.name());
        obj.addProperty("players", players.size());
        return obj;
    }
}
