package tech.kucharski.makao.server.messages.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.game.Game;
import tech.kucharski.makao.server.Message;

import java.util.List;
import java.util.UUID;

/**
 * When player's cards were changed.
 */
public class SelfCardsUpdatedMessage implements Message {
    @NotNull
    private final List<UUID> cards;
    @NotNull
    private final Game game;

    /**
     * @param game  A game
     * @param cards Cards of the player.
     */
    public SelfCardsUpdatedMessage(@NotNull Game game, @NotNull List<UUID> cards) {
        this.game = game;
        this.cards = cards;
    }

    /**
     * @return JSON object
     */
    @Override
    public JsonObject toJSONObject() {
        JsonObject response = new JsonObject();
        response.addProperty("req", "GAME.SELF_CARDS_UPDATED");
        response.addProperty("id", game.getGameID().toString());

        JsonArray cards = new JsonArray();
        this.cards.forEach(uuid -> cards.add(uuid.toString()));

        response.add("cards", cards);

        return response;
    }
}
