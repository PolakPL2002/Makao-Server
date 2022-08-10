package tech.kucharski.makao.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.kucharski.makao.Makao;
import tech.kucharski.makao.server.Client;
import tech.kucharski.makao.server.ClientState;
import tech.kucharski.makao.server.Message;
import tech.kucharski.makao.server.messages.GameRemovedMessage;
import tech.kucharski.makao.server.messages.GameUpdatedMessage;
import tech.kucharski.makao.server.messages.game.*;
import tech.kucharski.makao.util.JSONConvertible;

import java.util.*;

/**
 * A game of Makao.
 */
public class Game implements JSONConvertible {
    private final List<CardValidator> cardValidators = Collections.synchronizedList(new ArrayList<>());
    private final UUID gameID;
    private final List<Player> players = Collections.synchronizedList(new ArrayList<>());
    private final TurnManager turnManager = new TurnManager();
    private Deck deck = null;
    private boolean drawnCard = false;
    private final Map<UUID, UUID> clientPlayerMap = Collections.synchronizedMap(new HashMap<>());
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

        final Player player = new Player(Makao.getInstance().getGameManager().getUniquePlayerID(clientID, this));
        players.add(player);
        clientPlayerMap.put(clientID, player.getUUID());
        turnManager.addPlayer(player.getUUID());
        send(player, new PlayerIDAssignedMessage(this, player));
        sendUpdate(player);
        sendAll(new PlayerJoinedMessage(this, player));
        new GameUpdatedMessage(this).broadcast();
        return player;
    }

    /**
     * @return State of the game
     */
    public GamePhase getGameState() {
        return gamePhase;
    }

    /**
     * @param clientID Client ID
     * @return Player ID
     */
    @Nullable
    public UUID getPlayerID(UUID clientID) {
        return clientPlayerMap.get(clientID);
    }

    /**
     * @param uuid Player to be removed
     * @throws IllegalStateException When game is in incorrect phase.
     */
    public void removePlayer(@NotNull UUID uuid) throws IllegalStateException {
        if (getGameState() != GamePhase.PREPARING && getGameState() != GamePhase.IN_GAME)
            throw new IllegalStateException(String.format("A player cannot be removed in %s phase of the game.", getGameState()));

        final Player player = getPlayer(uuid);
        if (player == null) return;

        players.remove(player);
        if (deck != null)
            deck.removePlayer(player.getUUID());
        Makao.getInstance().getGameManager().removePlayer(uuid);
        sendAll(new PlayerLeftMessage(this, player));
        if (getGameState() == GamePhase.PREPARING)
            new GameUpdatedMessage(this).broadcast();
        if (players.size() == 0) {
            Makao.getInstance().getGameManager().removeGame(this);
        }
    }

    /**
     * Sends a full update to a player.
     *
     * @param player Player that update should be sent to
     */
    private void sendUpdate(@NotNull Player player) {
        final GameStateChangedMessage message = new GameStateChangedMessage(this);
        send(player, message);
    }

    /**
     * Sends a message to a player.
     *
     * @param player  Player the message shall be sent to.
     * @param message A message.
     */
    private void send(@NotNull Player player, Message message) {
        final Client client = Makao.getInstance().getServer()
                .getClient(Makao.getInstance().getGameManager().getClientID(player.getUUID()));
        if (client != null && client.getClientState() == ClientState.CONNECTED)
            message.send(client.getSocket());
    }

    /**
     * Sends a message to all players.
     *
     * @param message Message to be sent.
     */
    private void sendAll(@NotNull final Message message) {
        players.forEach(player -> send(player, message));
    }

    /**
     * @param playerID ID to check
     * @return If player is an admin.
     */
    public boolean checkAdmin(@NotNull UUID playerID) {
        if (players.size() == 0) return false;
        return players.get(0).getUUID().equals(playerID);
    }

    /**
     * @param playerID Player drawing a card.
     * @throws PlayerNotFoundException Player wasn't found.
     * @throws WrongTurnException      It's not player's turn.
     * @throws IllegalStateException   Player has already drawn a card in this turn.
     */
    public void drawCard(UUID playerID) throws PlayerNotFoundException, WrongTurnException, IllegalStateException {
        final Player player = getPlayer(playerID);
        if (player == null)
            throw new PlayerNotFoundException();
        if (!turnManager.getCurrentPlayer().equals(playerID))
            throw new WrongTurnException();
        if (drawnCard)
            throw new IllegalStateException();
        drawnCard = true;
        deck.givePlayerCards(playerID, 1);
        sendUpdatedCardsToPlayer(player);
    }

    /**
     * @param playerID Player ID
     * @return A player if found
     */
    @Nullable
    private Player getPlayer(UUID playerID) {
        return players.stream().filter(player -> player.getUUID().equals(playerID)).findFirst().orElse(null);
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
        sendUpdate();
        players.forEach(player -> {
            deck.givePlayerCards(player.getUUID(), 20);
            sendUpdatedCardsToPlayer(player);
        });
        cardValidators.add(new SameColorValidator(deck.getCurrentTopCard()));
        cardValidators.add(new SameValueValidator(deck.getCurrentTopCard()));
        nextTurn();
    }

    /**
     * @return A deck
     */
    @Nullable
    public Deck getDeck() {
        return deck;
    }

    /**
     * @return ID of the game.
     */
    public UUID getGameID() {
        return gameID;
    }

    /**
     * @return Current player ID.
     */
    public UUID getTurn() {
        return turnManager.getCurrentPlayer();
    }

    /**
     * @param clientID Client we look for
     * @return Whether game contains this client
     */
    public boolean hasClient(@NotNull UUID clientID) {
        return players.stream().anyMatch(player -> clientID.equals(Makao.getInstance().getGameManager().getClientID(player.getUUID())));
    }

    /**
     * @param playerID Player playing a card.
     * @param cardID   A card being played.
     * @param request  Requested value (for supported cards)
     * @throws PlayerNotFoundException Player wasn't found.
     * @throws CardNotFoundException   Card wasn't found.
     * @throws InvalidCardException    Card cannot be played right now.
     * @throws WrongTurnException      It's not player's turn.
     */
    public void playCard(@NotNull UUID playerID, @NotNull UUID cardID, @Nullable String request) throws PlayerNotFoundException,
            CardNotFoundException, InvalidCardException, WrongTurnException {
        final Player player = getPlayer(playerID);
        if (player == null)
            throw new PlayerNotFoundException();
        final Card card = deck.getCardByUUID(cardID);
        if (card == null)
            throw new CardNotFoundException();
        if (!validateCard(card))
            throw new InvalidCardException();
        if (!turnManager.getCurrentPlayer().equals(playerID))
            throw new WrongTurnException();
        deck.playCard(card);
        sendAll(new DeckUpdatedMessage(this));
        sendUpdatedCardsToPlayer(player);
        nextTurn();
    }

    /**
     * Processes a turn.
     */
    public void nextTurn() {
        drawnCard = false;
        turnManager.nextTurn();
        sendAll(new NextTurnMessage(this));
        //TODO Start timer

    }

    /**
     * @param card Card to be validated.
     * @return Whether card can be played.
     */
    private boolean validateCard(@NotNull Card card) {
        for (CardValidator cardValidator : cardValidators) {
            if (cardValidator.validate(card)) return true;
        }
        return false;
    }

    /**
     * @param player Player the cards should be sent to.
     */
    private void sendUpdatedCardsToPlayer(@NotNull Player player) {
        if (deck == null) return;
        send(player, new SelfCardsUpdatedMessage(this, deck.getPlayerCards(player.getUUID())));
    }

    /**
     * @param playerID Player to be updated
     */
    public void updatePlayer(UUID playerID) {
        final Player player = getPlayer(playerID);
        if (player == null) return;

        sendUpdate(player);
        sendUpdatedCardsToPlayer(player);
    }

    /**
     * @return A JSON object with all data.
     */
    public JsonObject toFullJSONObject() {
        JsonObject obj = new JsonObject();

        obj.addProperty("uuid", gameID.toString());

        obj.addProperty("phase", gamePhase.name());

        JsonArray players = new JsonArray();
        this.players.forEach(player -> players.add(player.toJSONObject()));

        obj.add("deck", deck != null ? deck.toJSONObject() : null);

        obj.add("players", players);

        obj.addProperty("turnOf", turnManager.getCurrentPlayer().toString());
        return obj;
    }

    @Override
    public JsonObject toJSONObject() {
        JsonObject obj = new JsonObject();

        obj.addProperty("uuid", gameID.toString());

        obj.addProperty("phase", gamePhase.name());

        JsonArray players = new JsonArray();
        this.players.forEach(player -> players.add(player.toJSONObject()));

        obj.add("players", players);
        return obj;
    }

    /**
     * Sends full update to all players.
     */
    private void sendUpdate() {
        sendAll(new GameStateChangedMessage(this));
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
}
