package tech.kucharski.makao.game;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.kucharski.makao.server.messages.GameAddedMessage;
import tech.kucharski.makao.server.messages.GameRemovedMessage;

import java.util.*;

/**
 * A class used to manage games.
 */
public class GameManager {
    private final List<Game> games = Collections.synchronizedList(new ArrayList<>());
    /**
     * Map that maps player UUIDs to client UUIDs.
     */
    private final Map<UUID, UUID> playerClientMap = Collections.synchronizedMap(new HashMap<>());
    private final Map<UUID, Game> playerGameMap = Collections.synchronizedMap(new HashMap<>());
    private final Set<UUID> usedGameIDs = Collections.synchronizedSet(new HashSet<>());

    /**
     * @param client Client creating a game
     * @return Newly created game
     * @throws PlayerInGameException A client is already in another game.
     */
    public Game createGame(@NotNull UUID client) throws PlayerInGameException {
        if (games.stream().anyMatch(game -> game.getGameState() != GamePhase.FINISHED && game.hasClient(client)))
            throw new PlayerInGameException();
        final Game game;

        synchronized (usedGameIDs) {
            game = new Game(getUniqueGameID());
            usedGameIDs.add(game.getGameID());
        }

        games.add(game);
        game.addPlayer(client);

        new GameAddedMessage(game).broadcast();

        return game;
    }

    /**
     * @return A unique UUID in the space of game IDs.
     */
    @NotNull
    private UUID getUniqueGameID() {
        synchronized (usedGameIDs) {
            UUID uuid;
            do {
                uuid = UUID.randomUUID();
            } while (usedGameIDs.contains(uuid));
            return uuid;
        }
    }

    /**
     * @param playerID A player to lookup.
     * @return A client ID of the player.
     */
    @Nullable
    public UUID getClientID(@NotNull UUID playerID) {
        return playerClientMap.get(playerID);
    }

    /**
     * @param playerID Player UUID
     * @return Game of the player or null
     */
    @Nullable
    public Game getGame(@NotNull UUID playerID) {
        return playerGameMap.get(playerID);
    }

    /**
     * @return List of joinable games.
     */
    public List<Game> getJoinableGames() {
        return games.stream().filter(game -> game.getGameState() == GamePhase.PREPARING).toList();
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
            return id;
        }
    }

    /**
     * @return A unique UUID in the space of player IDs.
     */
    @NotNull
    private UUID getUniquePlayerID() {
        synchronized (playerClientMap) {
            UUID uuid;
            do {
                uuid = UUID.randomUUID();
            } while (playerClientMap.containsKey(uuid));
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
}
