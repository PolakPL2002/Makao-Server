package tech.kucharski.makao.server.requests.game;

import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.Makao;
import tech.kucharski.makao.game.Game;
import tech.kucharski.makao.server.InvalidRequestException;
import tech.kucharski.makao.server.Request;
import tech.kucharski.makao.server.messages.responses.ErrorResponse;

import java.util.UUID;

import static tech.kucharski.makao.util.Utilities.validatePrimitives;

/**
 * Makes player join a game.
 */
public class JoinRequest implements Request {
    private final UUID reqID, gameID;

    /**
     * @param jsonObject Request data
     * @throws InvalidRequestException When data is invalid.
     */
    public JoinRequest(JsonObject jsonObject) throws InvalidRequestException {
        if (!validatePrimitives(jsonObject, new String[]{"uuid", "gameID"}))
            throw new InvalidRequestException();
        try {
            this.reqID = UUID.fromString(jsonObject.get("uuid").getAsJsonPrimitive().getAsString());
            this.gameID = UUID.fromString(jsonObject.get("gameID").getAsJsonPrimitive().getAsString());
        } catch (IllegalArgumentException ignored) {
            throw new InvalidRequestException();
        }
    }

    @Override
    public void handle(@NotNull WebSocket socket) {
        final Game game = Makao.getInstance().getGameManager().getGame(gameID);
        if (game == null) {
            Makao.getInstance().getServer().sendError(socket, reqID, ErrorResponse.NOT_FOUND);
            return;
        }
        if (game.hasClient(socket.getAttachment())) {
            Makao.getInstance().getServer().sendError(socket, reqID, ErrorResponse.FORBIDDEN);
            return;
        }
        try {
            game.addPlayer(socket.getAttachment());
        } catch (IllegalStateException e) {
            Makao.getInstance().getServer().sendError(socket, reqID, ErrorResponse.FORBIDDEN);
            return;
        }
        Makao.getInstance().getServer().sendAck(socket, reqID);
    }
}
