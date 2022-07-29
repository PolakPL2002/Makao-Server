package tech.kucharski.makao.server.requests;

import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.Makao;
import tech.kucharski.makao.game.*;
import tech.kucharski.makao.server.InvalidRequestException;
import tech.kucharski.makao.server.Request;
import tech.kucharski.makao.server.messages.responses.ErrorResponse;

import java.util.UUID;

import static tech.kucharski.makao.util.Utilities.validatePrimitiveOrNull;
import static tech.kucharski.makao.util.Utilities.validatePrimitives;

/**
 * Creates a new game.
 */
public class PlayCardRequest implements Request {
    private final UUID reqID, playerID, card;
    private final String request;

    /**
     * @param jsonObject Request data
     * @throws InvalidRequestException When data is invalid.
     */
    public PlayCardRequest(JsonObject jsonObject) throws InvalidRequestException {
        if (!validatePrimitives(jsonObject, new String[]{"uuid", "playerID", "card"}))
            throw new InvalidRequestException();
        if (!validatePrimitiveOrNull(jsonObject, new String[]{"request"}))
            throw new InvalidRequestException();
        try {
            this.reqID = UUID.fromString(jsonObject.get("uuid").getAsJsonPrimitive().getAsString());
            this.playerID = UUID.fromString(jsonObject.get("playerID").getAsJsonPrimitive().getAsString());
            this.card = UUID.fromString(jsonObject.get("card").getAsJsonPrimitive().getAsString());
            this.request = jsonObject.get("request").isJsonNull() ? null : jsonObject.get("request")
                    .getAsJsonPrimitive().getAsString();
        } catch (IllegalArgumentException ignored) {
            throw new InvalidRequestException();
        }
    }

    @Override
    public void handle(@NotNull WebSocket socket) {
        final Game game = Makao.getInstance().getGameManager().getGame(playerID);
        if (game == null) {
            Makao.getInstance().getServer().sendError(socket, reqID, ErrorResponse.NOT_FOUND);
            return;
        }
        try {
            game.playCard(playerID, card, request);
            Makao.getInstance().getServer().sendAck(socket, reqID);
        } catch (WrongTurnException | InvalidCardException e) {
            Makao.getInstance().getServer().sendError(socket, reqID, ErrorResponse.FORBIDDEN);
        } catch (PlayerNotFoundException | CardNotFoundException e) {
            Makao.getInstance().getServer().sendError(socket, reqID, ErrorResponse.NOT_FOUND);
        }
    }
}
