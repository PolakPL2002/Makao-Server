package tech.kucharski.makao.server.messages.responses;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.server.Message;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Response with error code
 */
public class ErrorResponse implements Message {
    /**
     * Bad request error code
     */
    public static final String BAD_REQUEST = "BAD_REQUEST";
    /**
     * Client still connected error code
     */
    public static final String CLIENT_STILL_CONNECTED = "CLIENT_STILL_CONNECTED";
    /**
     * Forbidden error code
     */
    public static final String FORBIDDEN = "FORBIDDEN";
    /**
     * Internal server error error code
     */
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    /**
     * Not found error code
     */
    public static final String NOT_FOUND = "NOT_FOUND";
    @NotNull
    private final String error;
    @Nullable
    private final UUID requestUUID;

    /**
     * @param error       Error code
     * @param requestUUID UUID of request this response is to.
     */
    public ErrorResponse(@Nullable UUID requestUUID, @NotNull String error) {
        this.requestUUID = requestUUID;
        this.error = error;
    }

    /**
     * @return JSON object
     */
    @Override
    public JsonObject toJSONObject() {
        JsonObject response = new JsonObject();
        response.addProperty("success", true);
        response.addProperty("req", requestUUID == null ? null : requestUUID.toString());
        response.addProperty("error", error);

        return response;
    }
}
