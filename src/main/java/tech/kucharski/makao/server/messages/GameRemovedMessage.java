package tech.kucharski.makao.server.messages;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.game.Game;
import tech.kucharski.makao.server.Message;

/**
 * When a game was removed from the list.
 */
public class GameRemovedMessage implements Message {
    @NotNull
    private final Game game;

    /**
     * @param game A game that was removed.
     */
    public GameRemovedMessage(@NotNull Game game) {
        this.game = game;
    }

    /**
     * @return JSON object
     */
    @Override
    public JsonObject toJSONObject() {
        JsonObject response = new JsonObject();
        response.addProperty("req", "GAME_LIST_REMOVED");
        response.addProperty("gameID", game.getGameID().toString());

        return response;
    }
}
