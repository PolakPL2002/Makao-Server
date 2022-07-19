package tech.kucharski.makao;

import org.jetbrains.annotations.NotNull;
import tech.kucharski.makao.server.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import static tech.kucharski.makao.util.Logger.*;

/**
 * Main game class
 */
public class Makao {
    private static Makao instance = null;
    private final Server server;

    /**
     * @param IP   Bind IP
     * @param port Bind port
     */
    Makao(@NotNull String IP, @NotNull String port) {
        if (instance != null) throw new RuntimeException("There can only be one Makao instance. Use getInstance().");
        instance = this;
        log("[Makao] Starting...");

        log("[Makao] Validating bind parameters...");

        int numericPort = 0;
        try {
            numericPort = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            fatal("[Makao] Provided port is not a valid number.");
        }
        if (numericPort < 1 || numericPort > 65535) {
            fatal("[Makao] Provided port is out of range 1-65535.");
        }

        Socket testSocket = new Socket();
        try {
            testSocket.bind(new InetSocketAddress(IP, numericPort));
            testSocket.close();
        } catch (IOException e) {
            error("[Makao] Failed to bind to port. Aborting...");
            fatal(e);
        }

        log("[Makao] Bind parameters successfully validated!");

        log("[Makao] Initializing game manager...");

        log("[Makao] Starting server...");
        server = new Server(new InetSocketAddress(IP, numericPort));
        server.start();
    }

    /**
     * @return Server instance
     */
    public Server getServer() {
        return server;
    }

    /**
     * @return Instance of Makao.
     */
    @NotNull
    public static Makao getInstance() {
        if (instance == null) new Makao("0.0.0.0", "62137");
        return instance;
    }
}
