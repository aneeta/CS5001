package ircserver;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import ircserver.exceptions.DisconnectedException;

/**
 * Class to handle requests from the connected client(s).
 */
public class ClientHandler extends Thread {

    private ChatServer chatServer;
    private User user;
    private Socket client;

    /**
     * ClientHandler constructor.
     * @param client client connection
     * @param chatServer chat server object
     */
    public ClientHandler(Socket client, ChatServer chatServer) {
        this.client = client;
        this.chatServer = chatServer;
        try {
            this.user = new User(client);
        } catch (IOException ioe) {
            System.out.println(
                "Exception in ClientHandeler: " + ioe.getMessage()
            );
        }
    }

    /**
     * Method to listen to client connection and clean up on disconnect.
     */
    public void run() {
        System.out.println("new ConnectionHandler thread started .... ");
        try {
            handleCommand();
        } catch (IOException | DisconnectedException e) {
            // exit cleanly for any Exception (including IOException, ClientDisconnectedException)
            System.out.println("ConnectionHandler:run " + e.getMessage());
            user.cleanup(); // cleanup and exit
        }
    }

    /** Method to wait for user input & act on it.
     * @throws IOException
     * @throws DisconnectedException
     */
    public void handleCommand() throws IOException, DisconnectedException {
        while (client.isConnected()) {
            // wait until client gives an input
            String line = null;
            while (line == null) {
                // get data from client over socket
                line = user.getReader().readLine();
            }
            // act according to the command
            runCommand(line);
        }
    }

    /** Method to map input command to the right action.
     * @param line client raw input
     * @throws IOException
     */
    public void runCommand(String line) throws IOException {
        // parse input line
        String[] args = line.split(" ");
        String[] methodArgs = Arrays.copyOfRange(args, 1, args.length);

        // call a method corresponding to the parsed command
        switch (args[0].toUpperCase()) {
            case "NICK":
                chatServer.setNick(user, methodArgs);
                break;
            case "USER":
                chatServer.registerUser(user, methodArgs);
                break;
            case "QUIT":
                chatServer.disconnectUser(user);
                break;
            case "JOIN":
                chatServer.joinChannel(user, methodArgs);
                break;
            case "PART":
                chatServer.leaveChannel(user, methodArgs);
                break;
            case "PRIVMSG":
                chatServer.privateMessage(user, methodArgs);
                break;
            case "NAMES":
                chatServer.listChannelUsers(user, methodArgs);
                break;
            case "LIST":
                chatServer.listChannels(user);
                break;
            case "TIME":
                chatServer.getTime(user);
                break;
            case "INFO":
                chatServer.getInfo(user);
                break;
            case "PING":
                chatServer.ping(user, methodArgs);
                break;
            default:
                // unrecognized command - ignore
                break;
        }
    }
}
