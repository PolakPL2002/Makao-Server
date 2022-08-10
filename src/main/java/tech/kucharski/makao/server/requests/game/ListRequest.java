package tech.kucharski.makao.server.requests.game;

import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.Makao;
import tech.kucharski.makao.server.InvalidRequestException;
import tech.kucharski.makao.server.Request;
import tech.kucharski.makao.server.messages.responses.game.ListResponse;

import java.util.UUID;

import static tech.kucharski.makao.util.Utilities.validatePrimitives;

/**
 * Lists all games the client takes part in.
 */
public class ListRequest implements Request {
    private final UUID reqID;

    /**
     * @param jsonObject Request data
     * @throws InvalidRequestException When data is invalid.
     */
    public ListRequest(JsonObject jsonObject) throws InvalidRequestException {
        if (!validatePrimitives(jsonObject, new String[]{"uuid"}))
            throw new InvalidRequestException();
        try {
            this.reqID = UUID.fromString(jsonObject.get("uuid").getAsJsonPrimitive().getAsString());
        } catch (IllegalArgumentException ignored) {
            throw new InvalidRequestException();
        }
    }

    @Override
    public void handle(@NotNull WebSocket socket) {
        new ListResponse(reqID, Makao.getInstance().getGameManager().getClientGames(socket.getAttachment())).send(socket);
    }
}
