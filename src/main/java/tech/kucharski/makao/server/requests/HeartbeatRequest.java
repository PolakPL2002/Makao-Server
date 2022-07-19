package tech.kucharski.makao.server.requests;

import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.server.Client;
import tech.kucharski.makao.server.Request;
import tech.kucharski.makao.server.Server;

import java.util.UUID;

/**
 * Tells the server that client is still alive.
 */
public class HeartbeatRequest implements Request {
    /**
     * @param ignored Request data
     */
    public HeartbeatRequest(JsonObject ignored) {
    }

    @Override
    public void handle(@NotNull Server server, @NotNull WebSocket socket) {
        final Client client = server.getClient(socket.<UUID>getAttachment());
        if (client != null) {
            client.heartbeatReceived();
        }
    }
}
