package tech.kucharski.makao.server.requests.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.Makao;
import tech.kucharski.makao.game.Game;
import tech.kucharski.makao.game.exceptions.CardNotFoundException;
import tech.kucharski.makao.game.exceptions.InvalidCardException;
import tech.kucharski.makao.game.exceptions.PlayerNotFoundException;
import tech.kucharski.makao.game.exceptions.WrongTurnException;
import tech.kucharski.makao.server.InvalidRequestException;
import tech.kucharski.makao.server.Request;
import tech.kucharski.makao.server.messages.responses.ErrorResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static tech.kucharski.makao.util.Logger.log;
import static tech.kucharski.makao.util.Utilities.*;

/**
 * Plays a card in a game.
 */
public class PlayCardRequest implements Request {
    private final List<UUID> cards;
    private final UUID reqID, playerID;
    private final String request;

    /**
     * @param jsonObject Request data
     * @throws InvalidRequestException When data is invalid.
     */
    public PlayCardRequest(JsonObject jsonObject) throws InvalidRequestException {
        if (!validatePrimitives(jsonObject, new String[]{"uuid", "playerID"}) ||
                !validateArrays(jsonObject, new String[]{"cards"}) ||
                !validatePrimitiveOrNull(jsonObject, new String[]{"request"}))
            throw new InvalidRequestException();

        try {
            this.reqID = UUID.fromString(jsonObject.get("uuid").getAsJsonPrimitive().getAsString());
            this.playerID = UUID.fromString(jsonObject.get("playerID").getAsJsonPrimitive().getAsString());
            final JsonArray cards = jsonObject.get("cards").getAsJsonArray();
            final List<UUID> cardUUIDs = new ArrayList<>();
            for (JsonElement element : cards) {
                cardUUIDs.add(UUID.fromString(element.getAsString()));
            }
            this.cards = cardUUIDs;
            this.request = jsonObject.get("request").isJsonNull() ? null : jsonObject.get("request")
                    .getAsJsonPrimitive().getAsString();
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
            game.playCard(playerID, cards, request);
            Makao.getInstance().getServer().sendAck(socket, reqID);
        } catch (WrongTurnException | InvalidCardException e) {
            log(e);
            Makao.getInstance().getServer().sendError(socket, reqID, ErrorResponse.FORBIDDEN);
        } catch (PlayerNotFoundException | CardNotFoundException e) {
            Makao.getInstance().getServer().sendError(socket, reqID, ErrorResponse.NOT_FOUND);
        }
    }
}
