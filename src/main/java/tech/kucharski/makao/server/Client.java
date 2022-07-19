package tech.kucharski.makao.server;

import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.UUID;

import static tech.kucharski.makao.util.Logger.warning;

/**
 * A network client.
 */
public class Client {
    private static final long TIMEOUT_MS = 10_000;
    private final UUID uuid;
    private ClientState clientState = ClientState.DISCONNECTED;
    private Date lastHeartbeat = new Date();
    private WebSocket socket = null;

    /**
     * @param uuid Client UUID
     */
    public Client(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Checks if the timeout has passed.
     */
    public void checkTimeout() {
        if (clientState == ClientState.CONNECTED) {
            if (new Date().getTime() - lastHeartbeat.getTime() > TIMEOUT_MS) {
                warning("Client timed out.");
                clientState = ClientState.DISCONNECTED;
                if (socket != null) {
                    socket.close();
                    socket = null;
                }
            }
        }
    }

    /**
     * @return Client state
     */
    public ClientState getClientState() {
        return clientState;
    }

    /**
     * @return When last heartbeat was received.
     */
    public Date getLastHeartbeat() {
        return lastHeartbeat;
    }

    /**
     * @return Socket of the client.
     */
    public WebSocket getSocket() {
        return socket;
    }

    /**
     * @param socket Socket of the client.
     */
    public void setSocket(WebSocket socket) {
        this.socket = socket;
        if (socket != null && socket.isOpen()) {
            clientState = ClientState.CONNECTED;
        } else {
            clientState = ClientState.DISCONNECTED;
        }
    }

    /**
     * @return UUID of the client.
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * This should be called when the client sends heartbeat to reset the timeout.
     */
    public void heartbeatReceived() {
        lastHeartbeat = new Date();
    }
}
