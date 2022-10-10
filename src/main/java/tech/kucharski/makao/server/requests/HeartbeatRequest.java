package tech.kucharski.makao.server.requests;

import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.Makao;
import tech.kucharski.makao.server.Client;
import tech.kucharski.makao.server.Request;

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
    public void handle(@NotNull WebSocket socket) {
        final Client client = Makao.getInstance().getServer().getClient(socket.<UUID>getAttachment());
        if (client != null) {
            client.heartbeatReceived();
        }
    }
}
