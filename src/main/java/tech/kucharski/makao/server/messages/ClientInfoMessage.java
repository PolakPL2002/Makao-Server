package tech.kucharski.makao.server.messages;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.server.Client;
import tech.kucharski.makao.server.Message;

/**
 * Sent when client data changes.
 */
public class ClientInfoMessage implements Message {
    @NotNull
    private final Client client;

    /**
     * @param client Client
     */
    public ClientInfoMessage(@NotNull Client client) {
        this.client = client;
    }

    /**
     * @return JSON object
     */
    @Override
    public JsonObject toJSONObject() {
        JsonObject response = new JsonObject();
        response.addProperty("req", "CLIENT_INFO");
        response.addProperty("avatar", client.getAvatar().toString());
        response.addProperty("name", client.getName());

        return response;
    }
}
