package tech.kucharski.makao;

import static tech.kucharski.makao.util.Logger.log;

/**
 * Launcher class
 */
public class Main {
    /**
     * @param args Program arguments
     */
    public static void main(String[] args) {
        log("[Launcher] Hello! Preparing everything, be done in a second...");
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "OFF");

        boolean nextIP = false, nextPort = false;
        String IP = "0.0.0.0", port = "62137";
        for (String s : args) {
            if (nextIP) {
                log("[Launcher] Changed bind IP to " + s);
                IP = s;
                nextIP = false;
                continue;
            } else if (nextPort) {
                log("[Launcher] Changed bind port to " + s);
                port = s;
                nextPort = false;
                continue;
            }
            if (s.equalsIgnoreCase("--ip")) {
                nextIP = true;
            } else if (s.equalsIgnoreCase("--port")) {
                nextPort = true;
            }
        }
        new Makao(IP, port);
    }
}
