import java.io.IOException;
import java.net.Socket;

import ircserver.ChatServer;
import ircserver.ClientHandler;


public class IrcServerMain {
    public static void main(String[] args) {
        if ((args.length < 2)) {
            // not enough arguments supplied
            showUsageAndExit();
        }

        String serverName = args[0];
        int port = 0;

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
                // waits until client requests a connection, then returns connection (socket)
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

    public static void showUsageAndExit() {
        System.out.println("Usage: java IrcServerMain <server_name> <port>");
        System.exit(1);
    }
}
