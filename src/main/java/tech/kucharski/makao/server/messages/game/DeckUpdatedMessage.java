package tech.kucharski.makao.server.messages.game;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.game.Game;
import tech.kucharski.makao.server.Message;

/**
 * When a card was played.
 */
public class DeckUpdatedMessage implements Message {
    @NotNull
    private final Game game;

    /**
     * @param game A game that was added.
     */
    public DeckUpdatedMessage(@NotNull Game game) {
        this.game = game;
    }

    /**
     * @return JSON object
     */
    @Override
    public JsonObject toJSONObject() {
        JsonObject response = new JsonObject();
        response.addProperty("req", "GAME.DECK_UPDATED");
        response.addProperty("id", game.getGameID().toString());

        if (game.getDeck() != null) {
            response.add("deck", game.getDeck().toSimpleJSONObject());
        } else {
            response.add("deck", null);
        }

        return response;
    }
}
