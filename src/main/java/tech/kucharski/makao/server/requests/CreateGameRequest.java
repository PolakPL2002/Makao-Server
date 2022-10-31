package tech.kucharski.makao.server.requests;

import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.Makao;
import tech.kucharski.makao.game.exceptions.PlayerInGameException;
import tech.kucharski.makao.server.InvalidRequestException;
import tech.kucharski.makao.server.Request;
import tech.kucharski.makao.util.MessageValidator;

import java.util.UUID;

import static tech.kucharski.makao.server.messages.responses.ErrorResponse.FORBIDDEN;

/**
 * Creates a new game.
 */
public class CreateGameRequest implements Request {
    private final UUID reqID;

    /**
     * @param jsonObject Request data
     * @throws InvalidRequestException When data is invalid.
     */
    public CreateGameRequest(JsonObject jsonObject) throws InvalidRequestException {
        if (!new MessageValidator()
                .requirePrimitive("uuid", false)
                .validate(jsonObject))
            throw new InvalidRequestException();
        try {
            this.reqID = UUID.fromString(jsonObject.get("uuid").getAsJsonPrimitive().getAsString());
        } catch (IllegalArgumentException ignored) {
            throw new InvalidRequestException();
        }
    }

    @Override
    public void handle(@NotNull WebSocket socket) {
        //Currently a player is limited to being in one game at a time
        try {
            Makao.getInstance().getGameManager().createGame(socket.getAttachment());
        } catch (PlayerInGameException e) {
            Makao.getInstance().getServer().sendError(socket, reqID, FORBIDDEN);
            return;
        }
        Makao.getInstance().getServer().sendAck(socket, reqID);
    }
}
