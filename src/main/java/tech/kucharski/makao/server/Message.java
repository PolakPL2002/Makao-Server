package tech.kucharski.makao.server;

import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.Makao;

/**
 * Message to the client.
 */
public interface Message {
    /**
     * Sends the message to all clients.
     */
    default void broadcast() {
        Makao.getInstance().getServer().getOnlineClients().forEach(client -> send(client.getSocket()));
    }

    /**
     * @param conn Socket used to send the message.
     */
    default void send(@NotNull WebSocket conn) {
        conn.send(encode());
    }

    /**
     * @return JSON-encoded string
     */
    default String encode() {
        return toJSONObject().toString();
    }

    /**
     * @return JSON object
     */
    JsonObject toJSONObject();
}
