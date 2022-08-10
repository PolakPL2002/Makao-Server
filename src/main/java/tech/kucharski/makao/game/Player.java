package tech.kucharski.makao.game;

import com.google.gson.JsonObject;
import tech.kucharski.makao.Makao;
import tech.kucharski.makao.server.Client;
import tech.kucharski.makao.util.JSONConvertible;

import java.util.List;
import java.util.UUID;

/**
 * A player of the game
 */
public class Player implements JSONConvertible {
    private final UUID uuid;

    /**
     * @param uuid ID of the player
     */
    public Player(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * @return ID of the player
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * @return JSON representation of the object.
     */
    @Override
    public JsonObject toJSONObject() {
        JsonObject obj = new JsonObject();

        obj.addProperty("uuid", uuid.toString());
        final Game game = Makao.getInstance().getGameManager().getGameByPlayerID(uuid);
        if (game != null) {
            final Deck deck = game.getDeck();
            if (deck != null) {
                final List<UUID> cards = deck.getPlayerCards(uuid);
                obj.addProperty("cardsRemaining", cards.size());
            } else {
                obj.addProperty("cardsRemaining", 0);
            }
        } else {
            obj.addProperty("cardsRemaining", 0);
        }

        final Client client = Makao.getInstance().getServer().getClient(Makao.getInstance().getGameManager().getClientID(uuid));
        obj.addProperty("avatar", client != null ? client.getAvatar().toString() : null);
        obj.addProperty("name", client != null ? client.getName() : "");

        return obj;
    }
}
