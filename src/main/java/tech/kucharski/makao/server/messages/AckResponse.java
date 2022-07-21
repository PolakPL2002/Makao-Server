package tech.kucharski.makao.server.messages;

import com.google.gson.JsonObject;
import tech.kucharski.makao.server.Message;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Empty response
 */
public class AckResponse implements Message {
    @Nullable
    private final UUID requestUUID;

    /**
     * @param requestUUID UUID of request this response is to.
     */
    public AckResponse(@Nullable UUID requestUUID) {
        this.requestUUID = requestUUID;
    }

    /**
     * @return JSON object
     */
    @Override
    public JsonObject toJSONObject() {
        JsonObject response = new JsonObject();
        response.addProperty("success", true);
        response.addProperty("req", requestUUID == null ? null : requestUUID.toString());

        return response;
    }
}
