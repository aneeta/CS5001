package ircserver;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import ircserver.exceptions.DisconnectedException;

public class ClientHandler extends Thread {

    private ChatServer chatServer;
    private User user;
    private Socket client;


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

    // run method is invoked when the Thread's start method (ch.start(); in Server class) is invoked
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

    public void handleCommand() throws IOException, DisconnectedException {
        while (client.isConnected()) {
            String line = null;
            while (line == null) {
                line = user.getReader().readLine(); // get data from client over socket
            }
            mapCommand(line);
        }
    }

    public void mapCommand(String line) throws IOException {
        // String[] msg = line.split(":");
        String[] args = line.split(" ");
        String[] methodArgs = Arrays.copyOfRange(args, 1, args.length);
        // String returnMsg = null;

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
        }
    }
}
