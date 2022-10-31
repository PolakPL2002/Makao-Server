package tech.kucharski.makao.server.requests.game;

import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.Makao;
import tech.kucharski.makao.game.Game;
import tech.kucharski.makao.server.InvalidRequestException;
import tech.kucharski.makao.server.Request;
import tech.kucharski.makao.server.messages.responses.ErrorResponse;
import tech.kucharski.makao.util.MessageValidator;

import java.util.UUID;

/**
 * Starts a game.
 */
public class StartGameRequest implements Request {
    private final UUID reqID, playerID;

    /**
     * @param jsonObject Request data
     * @throws InvalidRequestException When data is invalid.
     */
    public StartGameRequest(JsonObject jsonObject) throws InvalidRequestException {
        if (!new MessageValidator()
                .requirePrimitive("playerID", false)
                .requirePrimitive("uuid", false)
                .validate(jsonObject))
            throw new InvalidRequestException();
        try {
            this.reqID = UUID.fromString(jsonObject.get("uuid").getAsJsonPrimitive().getAsString());
            this.playerID = UUID.fromString(jsonObject.get("playerID").getAsJsonPrimitive().getAsString());
        } catch (IllegalArgumentException ignored) {
            throw new InvalidRequestException();
        }
    }

    @Override
    public void handle(@NotNull WebSocket socket) {
        final Game game = Makao.getInstance().getGameManager().getGameByPlayerID(playerID);
        if (game == null) {
            Makao.getInstance().getServer().sendError(socket, reqID, ErrorResponse.NOT_FOUND);
            return;
        }
        if (!game.checkAdmin(playerID)) {
            Makao.getInstance().getServer().sendError(socket, reqID, ErrorResponse.FORBIDDEN);
            return;
        }
        try {
            game.startGame();
            Makao.getInstance().getServer().sendAck(socket, reqID);
        } catch (IllegalStateException e) {
            Makao.getInstance().getServer().sendError(socket, reqID, ErrorResponse.FORBIDDEN);
        }
    }
}
