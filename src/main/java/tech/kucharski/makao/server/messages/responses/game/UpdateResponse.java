package tech.kucharski.makao.server.messages.responses.game;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.kucharski.makao.game.Game;
import tech.kucharski.makao.server.Message;

import java.util.UUID;

/**
 * Response for {@link tech.kucharski.makao.server.requests.game.UpdateRequest}.
 */
public class UpdateResponse implements Message {
    @NotNull
    private final Game game;
    @Nullable
    private final UUID playerID;
    @NotNull
    private final UUID requestUUID;

    /**
     * @param requestUUID UUID of request this response is to.
     * @param game        Game of the player
     * @param playerID    ID of the player
     */
    public UpdateResponse(@NotNull UUID requestUUID, @NotNull Game game, @Nullable UUID playerID) {
        this.requestUUID = requestUUID;
        this.game = game;
        this.playerID = playerID;
    }

    /**
     * @return JSON object
     */
    @Override
    public JsonObject toJSONObject() {
        JsonObject response = new JsonObject();
        response.addProperty("success", true);
        response.addProperty("req", requestUUID.toString());

        response.add("game", game.toFullJSONObject());
        response.addProperty("playerID", playerID == null ? null : playerID.toString());

        return response;
    }
}
