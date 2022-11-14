import java.io.IOException;
import java.net.Socket;

import ircserver.ChatServer;
import ircserver.ClientHandler;

/**
 * Class cntaining Main method.
 */
public class IrcServerMain {

    /** Main method - start the ICR server.
     * @param args input arg@id:vscjava.vscode-java-packuments (port and server name)
     */
    public static void main(String[] args) {
        if ((args.length < 2)) {
            // not enough arguments supplied
            showUsageAndExit();
        }

        String serverName = args[0];
        int/** Method to wait for user input & act on it.
        * @throws IOException
        * @throws DisconnectedException
        */ port = 0;

        try {
            port = Integer.parseInt(args[1]);
        } catch (Exception e) {
            showUsageAndExit();
        }

        try {
            ChatServer server = new ChatServer(port, serverName);
            System.out.printf("Started %s on port %d\n", serverName, port);

            // decided to not use Executor.newFixedThreadPool
            // because it forces a choice of maximum threads
            // so I would have to decide of a hard limit on
            // connections to handle at once
            while (true) {
                // return a connection upon client request
                try {
                    Socket connection = server.getServerSocket().accept();
                    System.out.println("Server got new connection request from " + connection.getInetAddress());
                    // create new handler for this connection
                    ClientHandler ch = new ClientHandler(connection, server);
                    // start handler thread
                    ch.start();
                } catch (IOException err) {
                    System.out.printf("Error Connecting: %s\n", err.getMessage());
                }
            }
        } catch (IOException err) {
            System.out.printf("Error Creating Server: %s \n", err.getMessage());
        }
    }

    /**
     * Utility method to display help when arguments are passed incorrectly.
     */
    public static void showUsageAndExit() {
        System.out.println("Usage: java IrcServerMain <server_name> <port>");
        System.exit(1);
    }
}
