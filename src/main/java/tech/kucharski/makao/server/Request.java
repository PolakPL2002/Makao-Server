package tech.kucharski.makao.server;

import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;

/**
 * A request from the client.
 */
public interface Request {
    /**
     * @param socket Socket that sent the request.
     */
    void handle(@NotNull WebSocket socket);
}
