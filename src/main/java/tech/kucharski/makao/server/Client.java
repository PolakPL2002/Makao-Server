package tech.kucharski.makao.server;

import com.github.javafaker.Faker;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.UUID;

import static tech.kucharski.makao.util.Logger.log;

/**
 * A network client.
 */
public class Client {
    private static final long TIMEOUT_MS = 10_000;
    private final Object lock = new Object();
    private final UUID uuid;
    @NotNull
    private UUID avatar;
    private ClientState clientState = ClientState.DISCONNECTED;
    private Date lastHeartbeat = new Date();
    private int messageID = 0;
    @NotNull
    private String name;
    private WebSocket socket = null;

    /**
     * @param uuid Client UUID
     */
    public Client(@NotNull UUID uuid) {
        this.uuid = uuid;
        avatar = UUID.randomUUID();
        name = Faker.instance().funnyName().name();
    }

    /**
     * Checks if the timeout has passed.
     */
    public void checkTimeout() {
        if (clientState == ClientState.CONNECTED) {
            if (new Date().getTime() - lastHeartbeat.getTime() > TIMEOUT_MS) {
                log("[Client] Client timed out.");
                clientState = ClientState.DISCONNECTED;
                if (socket != null) {
                    socket.close();
                    socket = null;
                }
            }
        }
    }

    /**
     * @return Client's avatar.
     */
    @NotNull
    public UUID getAvatar() {
        return avatar;
    }

    /**
     * @param avatar New avatar.
     */
    public void setAvatar(@NotNull UUID avatar) {
        this.avatar = avatar;
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
     * @return A lock object that can be used to synchronize sending messages.
     */
    public Object getLock() {
        return lock;
    }

    /**
     * @return Next message index
     */
    public int getMessageID() {
        return messageID++;
    }

    /**
     * @param messageID Next message index
     */
    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    /**
     * @return Client's name
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * @param name Client's name
     */
    public void setName(@NotNull String name) {
        this.name = name;
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
