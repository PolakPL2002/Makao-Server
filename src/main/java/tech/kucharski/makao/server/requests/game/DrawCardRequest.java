package tech.kucharski.makao.server.requests.game;

import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.Makao;
import tech.kucharski.makao.game.Game;
import tech.kucharski.makao.game.PlayerNotFoundException;
import tech.kucharski.makao.game.WrongTurnException;
import tech.kucharski.makao.server.InvalidRequestException;
import tech.kucharski.makao.server.Request;
import tech.kucharski.makao.server.messages.responses.ErrorResponse;

import java.util.UUID;

import static tech.kucharski.makao.util.Utilities.validatePrimitives;

/**
 * Draws a card in a game.
 */
public class DrawCardRequest implements Request {
    private final UUID reqID, playerID;

    /**
     * @param jsonObject Request data
     * @throws InvalidRequestException When data is invalid.
     */
    public DrawCardRequest(JsonObject jsonObject) throws InvalidRequestException {
        if (!validatePrimitives(jsonObject, new String[]{"uuid", "playerID"}))
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
        try {
            game.drawCard(playerID);
            Makao.getInstance().getServer().sendAck(socket, reqID);
        } catch (WrongTurnException | IllegalStateException e) {
            Makao.getInstance().getServer().sendError(socket, reqID, ErrorResponse.FORBIDDEN);
        } catch (PlayerNotFoundException e) {
            Makao.getInstance().getServer().sendError(socket, reqID, ErrorResponse.NOT_FOUND);
        }
    }
}
