package tech.kucharski.makao.server;

import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.server.requests.*;
import tech.kucharski.makao.server.requests.game.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Supported requests
 */
public enum Requests {
    /**
     * Tells the server that client is still alive.
     */
    HEARTBEAT(HeartbeatRequest.class),
    /**
     * Changes ID of the client after reconnect.
     */
    AUTH(AuthRequest.class),
    /**
     * Lists joinable games.
     */
    GET_GAMES(GetGamesRequest.class),
    /**
     * Creates a game.
     */
    CREATE_GAME(CreateGameRequest.class),
    /**
     * Plays a card in a game.
     */
    GAME__PLAY_CARD(PlayCardRequest.class),
    /**
     * Plays a card in a game.
     */
    GAME__DRAW_CARD(DrawCardRequest.class),
    /**
     * Starts a game.
     */
    GAME__START_GAME(StartGameRequest.class),
    /**
     * Lists all games the client takes part in.
     */
    GAME__LIST(ListRequest.class),
    /**
     * Makes player join a game.
     */
    GAME__JOIN(JoinRequest.class),
    /**
     * Sends full update of the game to the client.
     */
    GAME__UPDATE(UpdateRequest.class),
    /**
     * Asks the server to change the avatar of the user.
     */
    CHANGE_AVATAR(ChangeAvatarRequest.class),
    /**
     * Asks the server to change the name of the user.
     */
    CHANGE_NAME(ChangeNameRequest.class);

    private final Class<? extends Request> clazz;

    /**
     * @param clazz Request class.
     */
    Requests(Class<? extends Request> clazz) {
        this.clazz = clazz;
    }

    /**
     * Handles the request.
     *
     * @param jsonObject Request data.
     * @param socket     Socket that sent the request.
     * @throws NoSuchMethodException     When request is invalid
     * @throws InvocationTargetException When request is invalid
     * @throws InstantiationException    When request is invalid
     * @throws IllegalAccessException    When request is invalid
     * @throws InvalidRequestException   When request is invalid
     */
    public void handle(JsonObject jsonObject, @NotNull WebSocket socket)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException,
            InvalidRequestException {
        final Constructor<? extends Request> constructor = getClazz().getDeclaredConstructor(JsonObject.class);
        final Request request;
        try {
            request = constructor.newInstance(jsonObject);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof InvalidRequestException)
                throw (InvalidRequestException) e.getTargetException();
            throw e;
        }
        request.handle(socket);
    }

    /**
     * @return Class of the request.
     */
    public Class<? extends Request> getClazz() {
        return clazz;
    }
}

