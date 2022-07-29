package tech.kucharski.makao.server.messages.game;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.game.Game;
import tech.kucharski.makao.server.Message;

/**
 * When game turn advanced.
 */
public class NextTurnMessage implements Message {
    @NotNull
    private final Game game;

    /**
     * @param game A game that was added.
     */
    public NextTurnMessage(@NotNull Game game) {
        this.game = game;
    }

    /**
     * @return JSON object
     */
    @Override
    public JsonObject toJSONObject() {
        JsonObject response = new JsonObject();
        response.addProperty("req", "GAME.NEXT_TURN");
        response.addProperty("id", game.getGameID().toString());

        response.addProperty("turnOf", game.getTurn().toString());

        return response;
    }
}
