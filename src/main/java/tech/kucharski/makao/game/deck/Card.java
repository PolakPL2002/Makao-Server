package tech.kucharski.makao.game.deck;

import com.google.gson.JsonObject;
import tech.kucharski.makao.util.JSONConvertible;

import java.util.UUID;

/**
 * Card
 */
public class Card implements JSONConvertible {
    private final CardType type;
    private final UUID uuid;

    /**
     * @param uuid Card UUID
     * @param type Card type
     */
    public Card(UUID uuid, CardType type) {
        this.uuid = uuid;
        this.type = type;
    }

    /**
     * @return Card type
     */
    public CardType getType() {
        return type;
    }

    /**
     * @return JSON object
     */
    public JsonObject toJSONObject() {
        JsonObject obj = new JsonObject();

        obj.addProperty("uuid", getUUID().toString());

        obj.add("type", type.toJSONObject());
        return obj;
    }

    /**
     * @return Card UUID
     */
    public UUID getUUID() {
        return uuid;
    }

}
