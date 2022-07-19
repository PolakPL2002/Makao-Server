package tech.kucharski.makao.server.requests;

import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.server.InvalidRequestException;
import tech.kucharski.makao.server.Request;
import tech.kucharski.makao.server.Server;

import java.util.UUID;

import static tech.kucharski.makao.util.Utilities.validatePrimitives;

/**
 * Changes ID of the client after reconnect.
 */
public class AuthRequest implements Request {
    private final UUID clientID;
    private final UUID reqID;

    /**
     * @param jsonObject Request data
     * @throws InvalidRequestException When data is invalid.
     */
    public AuthRequest(JsonObject jsonObject) throws InvalidRequestException {
        if (!validatePrimitives(jsonObject, new String[]{"clientID", "uuid"}))
            throw new InvalidRequestException();
        try {
            this.clientID = UUID.fromString(jsonObject.get("clientID").getAsJsonPrimitive().getAsString());
            this.reqID = UUID.fromString(jsonObject.get("uuid").getAsJsonPrimitive().getAsString());
        } catch (IllegalArgumentException ignored) {
            throw new InvalidRequestException();
        }
    }

    @Override
    public void handle(@NotNull Server server, @NotNull WebSocket socket) {
        server.changeClientID(socket.getAttachment(), clientID, reqID);
    }
}
