package tech.kucharski.makao.server.messages;

import com.google.gson.JsonObject;
import tech.kucharski.makao.server.Message;

/**
 * Message telling client that the server is still alive.
 */
public class HeartbeatMessage implements Message {
    /**
     * @return JSON object
     */
    @Override
    public JsonObject toJSONObject() {
        JsonObject response = new JsonObject();
        response.addProperty("req", "HEARTBEAT");

        return response;
    }
}
