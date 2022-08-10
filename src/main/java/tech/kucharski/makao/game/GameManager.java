package tech.kucharski.makao.game;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.kucharski.makao.server.Client;
import tech.kucharski.makao.server.messages.GameAddedMessage;
import tech.kucharski.makao.server.messages.GameRemovedMessage;

import java.util.*;

/**
 * A class used to manage games.
 */
public class GameManager {
    private final Map<UUID, List<UUID>> clientPlayerMap = Collections.synchronizedMap(new HashMap<>());
    /**
     * Map that maps player UUIDs to client UUIDs.
     */
    private final Map<UUID, UUID> playerClientMap = Collections.synchronizedMap(new HashMap<>());
    private final Map<UUID, Game> playerGameMap = Collections.synchronizedMap(new HashMap<>());
    private final Map<UUID, Game> games = Collections.synchronizedMap(new HashMap<>());
    private final Set<UUID> usedPlayerIDs = Collections.synchronizedSet(new HashSet<>());

    /**
     * @param client Client that was removed
     */
    public void onClientRemoved(@NotNull Client client) {
        final List<UUID> list = new ArrayList<>(clientPlayerMap.getOrDefault(client.getUUID(), new ArrayList<>()));
        list.forEach(uuid -> {
            try {
                Objects.requireNonNull(getGameByPlayerID(uuid)).removePlayer(uuid);
            } catch (NullPointerException | IllegalStateException ignored) {
            }
        });
        clientPlayerMap.remove(client.getUUID());
    }

    /**
     * @param playerID Player UUID
     * @return Game of the player or null
     */
    @Nullable
    public Game getGameByPlayerID(@NotNull UUID playerID) {
        return playerGameMap.get(playerID);
    }

    /**
     * @param client Client creating a game
     * @return Newly created game
     * @throws PlayerInGameException A client is already in another game.
     */
    public Game createGame(@NotNull UUID client) throws PlayerInGameException {
        if (games.values().stream().anyMatch(game -> game.getGameState() != GamePhase.FINISHED && game.hasClient(client)))
            throw new PlayerInGameException();
        final Game game;

        synchronized (games) {
            game = new Game(getUniqueGameID());
            games.put(game.getGameID(), game);
        }

        game.addPlayer(client);

        new GameAddedMessage(game).broadcast();

        return game;
    }

    /**
     * @return A unique UUID in the space of game IDs.
     */
    @NotNull
    private UUID getUniqueGameID() {
        synchronized (games) {
            UUID uuid;
            do {
                uuid = UUID.randomUUID();
            } while (games.containsKey(uuid));
            return uuid;
        }
    }

    /**
     * @param clientID Client UUID
     * @return List of games of the client
     */
    public List<Game> getClientGames(UUID clientID) {
        return clientPlayerMap.getOrDefault(clientID, new ArrayList<>()).stream().map(this::getGameByPlayerID).toList();
    }

    /**
     * @param gameID Game UUID
     * @return Game of the player or null
     */
    @Nullable
    public Game getGame(@NotNull UUID gameID) {
        return games.get(gameID);
    }

    /**
     * @return List of joinable games.
     */
    public List<Game> getJoinableGames() {
        return games.values().stream().filter(game -> game.getGameState() == GamePhase.PREPARING).toList();
    }

    /**
     * Creates a unique player ID and associates it with client ID and game.
     *
     * @param clientID Client ID to be associated with the newly created player ID.
     * @param game     Game to be associated with newly created player ID.
     * @return A unique UUID in the space of player IDs.
     */
    public UUID getUniquePlayerID(@NotNull UUID clientID, @NotNull Game game) {
        synchronized (playerClientMap) {
            final UUID id = getUniquePlayerID();
            playerClientMap.put(id, clientID);
            playerGameMap.put(id, game);
            if (!clientPlayerMap.containsKey(clientID))
                clientPlayerMap.put(clientID, new ArrayList<>());
            clientPlayerMap.get(clientID).add(id);
            return id;
        }
    }

    /**
     * @return A unique UUID in the space of player IDs.
     */
    @NotNull
    private UUID getUniquePlayerID() {
        synchronized (usedPlayerIDs) {
            UUID uuid;
            do {
                uuid = UUID.randomUUID();
            } while (usedPlayerIDs.contains(uuid));
            usedPlayerIDs.add(uuid);
            return uuid;
        }
    }

    /**
     * @param game Game to be removed.
     */
    public void removeGame(@NotNull Game game) {
        games.remove(game);
        new GameRemovedMessage(game).broadcast();
    }

    /**
     * @param playerID Player to be removed
     */
    public void removePlayer(UUID playerID) {
        final UUID clientID = getClientID(playerID);
        if (clientID != null) {
            clientPlayerMap.getOrDefault(clientID, new ArrayList<>()).remove(playerID);
        }
        playerClientMap.remove(playerID);
        playerGameMap.remove(playerID);
    }

    /**
     * @param playerID A player to lookup.
     * @return A client ID of the player.
     */
    @Nullable
    public UUID getClientID(@NotNull UUID playerID) {
        return playerClientMap.get(playerID);
    }
}
