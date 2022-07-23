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
        final Client client = Makao.getInstance().getServer().getClient(conn);
        synchronized (client == null ? new Object() : client.getLock()) {
            conn.send(encode(conn));
        }
    }

    /**
     * @param conn Socket used to send the message.
     * @return JSON-encoded string
     */
    default String encode(@NotNull WebSocket conn) {
        final JsonObject jsonObject = toJSONObject();
        final Client client = Makao.getInstance().getServer().getClient(conn);
        if (client != null) {
            final int messageID = client.getMessageID();
            jsonObject.addProperty("_msgID", messageID);
        }
        return jsonObject.toString();
    }

    /**
     * @return JSON object
     */
    JsonObject toJSONObject();
}
