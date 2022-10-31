package tech.kucharski.makao.server.requests;

import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.Makao;
import tech.kucharski.makao.game.GamePhase;
import tech.kucharski.makao.server.Client;
import tech.kucharski.makao.server.InvalidRequestException;
import tech.kucharski.makao.server.Request;
import tech.kucharski.makao.server.messages.ClientInfoMessage;
import tech.kucharski.makao.server.messages.GameUpdatedMessage;
import tech.kucharski.makao.util.MessageValidator;

import java.util.UUID;

/**
 * Asks the server to change the name of the user.
 */
public class ChangeNameRequest implements Request {
    private final String name;
    private final UUID reqID;

    /**
     * @param jsonObject Request data
     * @throws InvalidRequestException When data is invalid.
     */
    public ChangeNameRequest(JsonObject jsonObject) throws InvalidRequestException {
        if (!new MessageValidator()
                .requirePrimitive("name", false)
                .requirePrimitive("uuid", false)
                .validate(jsonObject))
            throw new InvalidRequestException();
        try {
            this.name = jsonObject.get("name").getAsJsonPrimitive().getAsString();
            this.reqID = UUID.fromString(jsonObject.get("uuid").getAsJsonPrimitive().getAsString());
        } catch (IllegalArgumentException ignored) {
            throw new InvalidRequestException();
        }
    }

    @Override
    public void handle(@NotNull WebSocket socket) {
        final Client client = Makao.getInstance().getServer().getClient(socket.<UUID>getAttachment());
        if (client != null) {
            client.setName(name);
            new ClientInfoMessage(client).send(client.getSocket());
            Makao.getInstance().getGameManager().getClientGames(client.getUUID()).stream()
                    .filter(game -> game.getGameState() == GamePhase.PREPARING)
                    .forEach(game -> new GameUpdatedMessage(game).broadcast());
            Makao.getInstance().getServer().sendAck(socket, reqID);
        }
    }
}
