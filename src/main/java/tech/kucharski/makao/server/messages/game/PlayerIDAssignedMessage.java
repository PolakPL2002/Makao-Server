package tech.kucharski.makao.server.messages.game;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.game.Game;
import tech.kucharski.makao.game.Player;
import tech.kucharski.makao.server.Message;

/**
 * When player got assigned an ID.
 */
public class PlayerIDAssignedMessage implements Message {
    @NotNull
    private final Game game;
    @NotNull
    private final Player player;

    /**
     * @param game   A game
     * @param player A player
     */
    public PlayerIDAssignedMessage(@NotNull Game game, @NotNull Player player) {
        this.game = game;
        this.player = player;
    }

    /**
     * @return JSON object
     */
    @Override
    public JsonObject toJSONObject() {
        JsonObject response = new JsonObject();
        response.addProperty("req", "GAME.PLAYER_ID_ASSIGNED");
        response.addProperty("id", game.getGameID().toString());

        response.add("player", player.toJSONObject());

        return response;
    }
}
