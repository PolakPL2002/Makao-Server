package tech.kucharski.makao.server.messages;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.game.Game;
import tech.kucharski.makao.server.Message;

/**
 * When new game was added to the list.
 */
public class GameAddedMessage implements Message {
    @NotNull
    private final Game game;

    /**
     * @param game A game that was added.
     */
    public GameAddedMessage(@NotNull Game game) {
        this.game = game;
    }

    /**
     * @return JSON object
     */
    @Override
    public JsonObject toJSONObject() {
        JsonObject response = new JsonObject();
        response.addProperty("req", "GAME_LIST_ADDED");
        response.add("game", game.toJSONObject());

        return response;
    }
}
