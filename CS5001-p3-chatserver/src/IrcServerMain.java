import java.net.Socket;


public class IrcServerMain {
    public static void main(String[] args) {
        if ((args.length < 2)) {
            // not enough arguments supplied
            showUsageAndExit();
        }

        String serverName = args[0];
        int port = Integer.parseInt(args[1]);

        if ((port < 0) || (port > 65535)) {
            // invalid port number 
            showUsageAndExit();
        }


        Server server = new Server();

        // decided to not use Executor.newFixedThreadPool
        // because it forces a choice of maximum threads
        // so I would have to decide of a hard limit on
        // connections to handle at once
        while (true) {
            // waits until client requests a connection, then returns connection (socket)
            Socket connection = server.getServerSocket().accept();
            System.out.println("Server got new connection request from " + conn.getInetAddress());
            // create new handler for this connection
            ClientHandler ch = new ClientHandler(connection);
            // start handler thread
            ch.start();
        }
    }

    public static showUsageAndExit() {
        System.out.println("Usage: java IrcServerMain <server_name> <port>");
        System.exit(1);
    }
}
