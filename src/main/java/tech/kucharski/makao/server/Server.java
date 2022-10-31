package tech.kucharski.makao.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.java_websocket.WebSocket;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.kucharski.makao.Makao;
import tech.kucharski.makao.server.messages.ClientInfoMessage;
import tech.kucharski.makao.server.messages.HeartbeatMessage;
import tech.kucharski.makao.server.messages.HelloMessage;
import tech.kucharski.makao.server.messages.responses.AckResponse;
import tech.kucharski.makao.server.messages.responses.ErrorResponse;
import tech.kucharski.makao.util.MessageValidator;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.stream.Collectors;

import static tech.kucharski.makao.util.Logger.*;

/**
 * Makao server
 */
public class Server extends WebSocketServer {
    private final List<Client> clients = Collections.synchronizedList(new ArrayList<>());
    private final Timer heartbeatTimer = new Timer(true);
    private final UUID instanceUUID = UUID.randomUUID();
    private final Map<UUID, Client> uuidClientMap = Collections.synchronizedMap(new HashMap<>());
    private final Map<WebSocket, Client> webSocketClientMap = Collections.synchronizedMap(new HashMap<>());

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
        final Client client = getClient(conn);

        log(String.format("[Server] Connection with %s (%s) was closed.", conn.getRemoteSocketAddress(),
                client == null ? "no client" : client.getUUID()));

        //Update client state
        if (client != null)
            client.setSocket(conn);
    }

    /**
     * @param socket Socket of the client to find.
     * @return A client or null if not found.
     */
    @Nullable
    public Client getClient(@Nullable WebSocket socket) {
        return webSocketClientMap.get(socket);
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
        if (!new MessageValidator()
                .requirePrimitive("req", false)
                .requirePrimitive("uuid", false)
                .validate(jsonObject)) {
            warning("[Server] Invalid message received.");
            sendError(conn, null, ErrorResponse.BAD_REQUEST);
            return;
        }
        String req = jsonObject.get("req").getAsJsonPrimitive().getAsString().replace(".", "__");
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
            request.handle(jsonObject, conn);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            warning("[Server] Failed to handle request.");
            warning(e);
            sendError(conn, uuid, ErrorResponse.INTERNAL_SERVER_ERROR);
        } catch (InvalidRequestException e) {
            sendError(conn, uuid, ErrorResponse.BAD_REQUEST);
        }
    }

    /**
     * Sends error to socket
     *
     * @param conn  Websocket
     * @param uuid  Message UUID
     * @param error Error
     */
    public void sendError(@NotNull WebSocket conn, @Nullable UUID uuid, @NotNull String error) {
        new ErrorResponse(uuid, error).send(conn);
    }

    /**
     * Called after an opening handshake has been performed and the given websocket is ready to be written on.
     *
     * @param conn      The <tt>WebSocket</tt> instance this event is occurring on.
     * @param handshake The handshake of the websocket instance
     */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Client client = new Client(getUniqueClientID());

        log(String.format("[Server] Incoming connection %s (%s, %s)", conn.getRemoteSocketAddress(), client.getUUID(), client.getName()));

        clients.add(client);
        client.setSocket(conn);
        conn.setAttachment(client.getUUID());

        //Update caches
        uuidClientMap.put(client.getUUID(), client);
        webSocketClientMap.put(conn, client);

        new HelloMessage(client.getUUID(), instanceUUID).send(conn);
        new ClientInfoMessage(client).send(conn);
    }

    /**
     * @return A unique UUID in the space of client IDs.
     */
    @NotNull
    private UUID getUniqueClientID() {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (uuidClientMap.containsKey(uuid));
        return uuid;
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
//        synchronized (clients) {
//            for (int i = 0; i < 30; i++) {
//                Client client = new Client(getUniqueClientID());
//                uuidClientMap.put(client.getUUID(), client);
//                clients.add(client);
//            }
//            for (Client client : new ArrayList<>(clients)) {
//                try {
//                    final Game game = Makao.getInstance().getGameManager().createGame(client.getUUID());
//                    for (int i = 0; i < 6; i++) {
//                        Client client1 = new Client(getUniqueClientID());
//                        uuidClientMap.put(client1.getUUID(), client1);
//                        clients.add(client1);
//                        game.addPlayer(client1.getUUID());
//                    }
//                } catch (PlayerInGameException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
    }

    /**
     * Checks all connected clients for timeouts and sends heartbeat message to them.
     */
    private void heartbeat() {
        synchronized (clients) {
            clients.forEach(Client::checkTimeout);
            clients.stream()
                    .filter(client -> client.getClientState() == ClientState.CONNECTED &&
                            client.getSocket() != null && client.getSocket().isOpen())
                    .forEach(client -> new HeartbeatMessage().send(client.getSocket()));
        }
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
            toClient.setMessageID(client.getMessageID());

            client.setSocket(null);
            removeClient(client);

            webSocketClientMap.put(toClient.getSocket(), toClient);

            sendAck(toClient.getSocket(), reqID);
            new ClientInfoMessage(toClient).send(toClient.getSocket());
            log(String.format("[Server] Client %s is now known as %s.", client.getUUID(), toClient.getUUID()));
        }
    }

    /**
     * Removes a client.
     *
     * @param client Client to be removed.
     */
    private void removeClient(Client client) {
        if (client == null) return;
        Makao.getInstance().getGameManager().onClientRemoved(client);
        clients.remove(client);
        uuidClientMap.remove(client.getUUID());
        webSocketClientMap.remove(client.getSocket());
    }

    /**
     * @param clientID ID of the client to find.
     * @return A client or null if not found.
     */
    @Nullable
    public Client getClient(@Nullable UUID clientID) {
        return uuidClientMap.get(clientID);
    }

    /**
     * Acknowledges a message.
     *
     * @param socket Socket that the ack should be sent to.
     * @param reqID  The UUID of the message to be acknowledged.
     */
    public void sendAck(WebSocket socket, UUID reqID) {
        new AckResponse(reqID).send(socket);
    }

    /**
     * @return A list of all online clients.
     */
    public List<Client> getOnlineClients() {
        return clients.stream().filter(client -> client.getClientState() == ClientState.CONNECTED)
                .collect(Collectors.toList());
    }
}
