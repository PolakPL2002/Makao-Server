package tech.kucharski.makao.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.java_websocket.WebSocket;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.kucharski.makao.server.messages.AckResponse;
import tech.kucharski.makao.server.messages.ErrorResponse;
import tech.kucharski.makao.server.messages.HeartbeatMessage;
import tech.kucharski.makao.server.messages.HelloMessage;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static tech.kucharski.makao.util.Logger.*;
import static tech.kucharski.makao.util.Utilities.validatePrimitives;

/**
 * Makao server
 */
public class Server extends WebSocketServer {
    private final List<Client> clients = Collections.synchronizedList(new ArrayList<>());
    private final Timer heartbeatTimer = new Timer(true);
    private final UUID instanceUUID = UUID.randomUUID();

    /**
     * Creates a WebSocketServer that will attempt to bind/listen on the given <var>address</var>.
     *
     * @param inetSocketAddress The address to listen to
     */
    public Server(InetSocketAddress inetSocketAddress) {
        super(inetSocketAddress);
    }

    /**
     * Called after the websocket connection has been closed.
     *
     * @param conn   The <tt>WebSocket</tt> instance this event is occuring on.
     * @param code   The codes can be looked up here: {@link CloseFrame}
     * @param reason Additional information string
     * @param remote Whether remote closed connection
     **/
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        log("[Server] Connection with " + conn.getRemoteSocketAddress() + " was closed.");
    }

    /**
     * Called when errors occur. If an error causes the websocket connection to fail {@link #onClose(WebSocket, int, String, boolean)} will be called additionally.<br>
     * This method will be called primarily because of IO or protocol errors.<br>
     * If the given exception is an RuntimeException that probably means that you encountered a bug.<br>
     *
     * @param conn Can be null if the error does not belong to one specific websocket. For example if the servers port could not be bound.
     * @param ex   The exception causing this error
     **/
    @Override
    public void onError(WebSocket conn, Exception ex) {
        error("[Server] Exception in socket " + conn);
        error(ex);
    }

    /**
     * Callback for string messages received from the remote host
     *
     * @param conn    The <tt>WebSocket</tt> instance this event is occurring on.
     * @param message The UTF-8 decoded message that was received.
     **/
    @Override
    public void onMessage(WebSocket conn, String message) {
        final Client client = getClient(conn);
        if (client != null) {
            debug("[Server] Received message from " + client.getUUID() + ": " + message);
        } else {
            debug("[Server] Received message from " + conn.getRemoteSocketAddress() + ": " + message);
        }
        final JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        if (!validatePrimitives(jsonObject, new String[]{"req", "uuid"})) {
            warning("[Server] Invalid message received.");
            sendError(conn, null, ErrorResponse.BAD_REQUEST);
            return;
        }
        String req = jsonObject.get("req").getAsJsonPrimitive().getAsString();
        UUID uuid;
        try {
            uuid = UUID.fromString(jsonObject.get("uuid").getAsJsonPrimitive().getAsString());
        } catch (IllegalArgumentException ignored) {
            warning("[Server] Invalid message received.");
            sendError(conn, null, ErrorResponse.BAD_REQUEST);
            return;
        }
        final Requests request;
        try {
            request = Requests.valueOf(req);
        } catch (IllegalArgumentException ignored) {
            warning("[Server] Invalid message received.");
            sendError(conn, uuid, ErrorResponse.BAD_REQUEST);
            return;
        }
        try {
            request.handle(jsonObject, this, conn);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException | InvalidRequestException e) {
            warning("[Server] Failed to handle request.");
            warning(e);
            sendError(conn, uuid, ErrorResponse.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Sends error to socket
     *
     * @param conn  Websocket
     * @param uuid  Message UUID
     * @param error Error
     */
    private void sendError(@NotNull WebSocket conn, @Nullable UUID uuid, @NotNull String error) {
        new ErrorResponse(uuid, error).send(conn);
    }

    /**
     * @param socket Socket of the client to find.
     * @return A client or null if not found.
     */
    @Nullable
    public Client getClient(@NotNull WebSocket socket) {
        return clients.stream().filter(client -> client.getSocket() == socket).findAny().orElse(null);
    }

    /**
     * Called after an opening handshake has been performed and the given websocket is ready to be written on.
     *
     * @param conn      The <tt>WebSocket</tt> instance this event is occurring on.
     * @param handshake The handshake of the websocket instance
     */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        log("[Server] Incoming connection " + conn.getRemoteSocketAddress());
        Client client = new Client(getUniqueClientID());
        clients.add(client);
        client.setSocket(conn);
        conn.setAttachment(client.getUUID());
        new HelloMessage(client.getUUID(), instanceUUID).send(conn);
    }

    /**
     * @return A unique UUID in the space of client IDs.
     */
    @NotNull
    private UUID getUniqueClientID() {
        AtomicReference<UUID> uuid = new AtomicReference<>();
        do {
            uuid.set(UUID.randomUUID());
        } while (clients.stream().anyMatch(client -> client.getUUID().equals(uuid.get())));
        return uuid.get();
    }

    /**
     * Called when the server started up successfully.
     * <p>
     * If any error occurred, onError is called instead.
     */
    @Override
    public void onStart() {
        heartbeatTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                heartbeat();
            }
        }, 2500, 2500);
    }

    /**
     * Checks all connected clients for timeouts and sends heartbeat message to them.
     */
    private void heartbeat() {
        clients.forEach(Client::checkTimeout);
        clients.stream()
                .filter(client -> client.getClientState() == ClientState.CONNECTED && client.getSocket() != null &&
                        client.getSocket().isOpen())
                .forEach(client -> new HeartbeatMessage().send(client.getSocket()));
    }

    /**
     * Changes client ID after reconnect.
     *
     * @param from  Original client ID
     * @param to    Target client ID
     * @param reqID Request ID
     */
    public void changeClientID(UUID from, UUID to, UUID reqID) {
        synchronized (clients) {
            final Client client = getClient(from);
            if (client == null) {
                return;
            }
            if (from.equals(to)) {
                sendError(client.getSocket(), reqID, ErrorResponse.BAD_REQUEST);
                return;
            }
            final Client toClient = getClient(to);
            if (toClient == null) {
                log("[Server] Rejected client ID change due to unknown client ID.");
                sendError(client.getSocket(), reqID, ErrorResponse.FORBIDDEN);
                return;
            }
            if (toClient.getClientState() != ClientState.DISCONNECTED) {
                log("[Server] Rejected client ID change due to target being connected.");
                sendError(client.getSocket(), reqID, ErrorResponse.CLIENT_STILL_CONNECTED);
                return;
            }
            client.getSocket().setAttachment(to);

            toClient.heartbeatReceived();
            toClient.setSocket(client.getSocket());

            client.setSocket(null);
            removeClient(client);

            sendAck(toClient.getSocket(), reqID);
            log(String.format("[Server] Client %s is now known as %s.", client.getUUID(), toClient.getUUID()));
        }
    }

    /**
     * Acknowledges a message.
     *
     * @param socket Socket that the ack should be sent to.
     * @param reqID  The UUID of the message to be acknowledged.
     */
    private void sendAck(WebSocket socket, UUID reqID) {
        new AckResponse(reqID).send(socket);
    }

    /**
     * Removes a client.
     *
     * @param client Client to be removed.
     */
    private void removeClient(Client client) {
        if (client == null) return;
        //TODO Kick client from games
        clients.remove(client);
    }

    /**
     * @param clientID ID of the client to find.
     * @return A client or null if not found.
     */
    @Nullable
    public Client getClient(@NotNull UUID clientID) {
        return clients.stream().filter(client -> client.getUUID().equals(clientID)).findAny().orElse(null);
    }
}
