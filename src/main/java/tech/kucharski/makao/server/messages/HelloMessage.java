package tech.kucharski.makao.server.messages;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.server.Message;

import java.util.UUID;

/**
 * First message from the server to client.
 */
public class HelloMessage implements Message {
    @NotNull
    private final UUID clientID;
    @NotNull
    private final UUID serverID;

    /**
     * @param clientID UUID of the client.
     * @param serverID UUID of the server.
     */
    public HelloMessage(@NotNull UUID clientID, @NotNull UUID serverID) {
        this.clientID = clientID;
        this.serverID = serverID;
    }

    /**
     * @return JSON object
     */
    @Override
    public JsonObject toJSONObject() {
        JsonObject response = new JsonObject();
        response.addProperty("req", "HELLO");
        response.addProperty("clientID", clientID.toString());
        response.addProperty("serverID", serverID.toString());

        return response;
    }
}
