package tech.kucharski.makao.server.messages;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.game.Game;
import tech.kucharski.makao.server.Message;

/**
 * When  game was changed.
 */
public class GameStateChangedMessage implements Message {
    @NotNull
    private final Game game;

    /**
     * @param game A game that was added.
     */
    public GameStateChangedMessage(@NotNull Game game) {
        this.game = game;
    }

    /**
     * @return JSON object
     */
    @Override
    public JsonObject toJSONObject() {
        JsonObject response = new JsonObject();
        response.addProperty("req", "GAME_STATE_CHANGED_ADDED");
        response.add("game", game.toJSONObject());

        return response;
    }
}
