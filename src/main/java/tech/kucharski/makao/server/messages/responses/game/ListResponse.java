package tech.kucharski.makao.server.messages.responses.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.game.Game;
import tech.kucharski.makao.server.Message;

import java.util.List;
import java.util.UUID;

/**
 * Response for {@link tech.kucharski.makao.server.requests.game.ListRequest}.
 */
public class ListResponse implements Message {
    @NotNull
    private final List<Game> games;
    @NotNull
    private final UUID requestUUID;

    /**
     * @param requestUUID UUID of request this response is to.
     * @param games       List of the games
     */
    public ListResponse(@NotNull UUID requestUUID, @NotNull List<Game> games) {
        this.requestUUID = requestUUID;
        this.games = games;
    }

    /**
     * @return JSON object
     */
    @Override
    public JsonObject toJSONObject() {
        JsonObject response = new JsonObject();
        response.addProperty("success", true);
        response.addProperty("req", requestUUID.toString());

        JsonArray games = new JsonArray();
        this.games.forEach(game -> games.add(game.toJSONObject()));
        response.add("games", games);

        return response;
    }
}
