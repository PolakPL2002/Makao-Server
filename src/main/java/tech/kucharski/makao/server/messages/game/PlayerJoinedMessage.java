package tech.kucharski.makao.server.messages.game;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.game.Game;
import tech.kucharski.makao.game.Player;
import tech.kucharski.makao.server.Message;

/**
 * When a player joined a game.
 */
public class PlayerJoinedMessage implements Message {
    @NotNull
    private final Game game;
    @NotNull
    private final Player player;

    /**
     * @param game   A game that the player joined.
     * @param player A player that was added.
     */
    public PlayerJoinedMessage(@NotNull Game game, @NotNull Player player) {
        this.game = game;
        this.player = player;
    }

    /**
     * @return JSON object
     */
    @Override
    public JsonObject toJSONObject() {
        JsonObject response = new JsonObject();
        response.addProperty("req", "GAME.PLAYER_JOINED");
        response.addProperty("id", game.getGameID().toString());

        response.add("player", player.toJSONObject());

        return response;
    }
}
