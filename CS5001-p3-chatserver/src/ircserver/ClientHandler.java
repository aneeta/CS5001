package ircserver;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import ircserver.exceptions.DisconnectedException;
import ircserver.commands.SupportedCommands;

public class ClientHandler extends Thread {

    private ChatServer chatServer;
    private User user;
    private Socket client;

    public ClientHandler(Socket client, ChatServer chatServer) {
        this.client = client;
        
        try {
            this.chatServer = chatServer;
            this.user = new User(client);
            this.chatServer.addUser(this.user);

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

    // public void runHandle() throws IOException {
    //     try {
    //         // TODO
    //         handleCommand();

    //     } catch (DisconnectedException err) {
    //         System.out.println(
    //             "Exception in ClientHandeler:run : " + err.getMessage()
    //         );
    //         cleanup();
    //     }
    // }

    private void handleCommand() throws IOException, DisconnectedException {
        while (client.isConnected()) {
            String line = null;
            while (line == null) {
                line = user.bufferedReader.readLine(); // get data from client over socket
            }

            

            // if readLine fails we can deduce here that the connection to the client is broken
            // and shut down the connection on this side cleanly by throwing a DisconnectedException
            // which will be passed up the call stack to the nearest handler (catch block)
            // in the run method
            // if (line == null || line.equals("null") || line.equals("exit")) {
            //     throw new DisconnectedException(" ... client has closed the connection ... ");
            // }

            mapCommand(line);


            // assuming no exception, print out line received from client
            // System.out.println("ConnectionHandler: " + line);
        }
    }


    // public void runCommand(String line) {

    //     try {
    //         SupportedCommands.valueOf(args[0].toUpperCase());
    //         mapCommand(line);

    //     } catch (Exception e) {

    //         System.out.println("Unsupported command");
    //         // TODO print supported commands

    //     }
    // }

    public void mapCommand(String line) throws IOException {
        String[] msg = line.split(":");
        String[] args = msg[0].split(" ");
        String returnMsg = null;

        switch (args[0].toUpperCase()) {
            case "NICK":
                returnMsg = chatServer.setNick(user, args[1]);
                if (returnMsg != null) {
                    user.bufferedWriter.write(returnMsg);
                    user.bufferedWriter.flush();
                }
                break;
            case "USER":
                returnMsg = chatServer.registerUser(user, args[1], msg[1]);
                if (returnMsg != null) {
                    user.bufferedWriter.write(returnMsg);
                    user.bufferedWriter.flush();
                }
                break;
            case "QUIT":
                returnMsg = chatServer.disconnectUser(user);
                
                break;
            case "JOIN":
                returnMsg = chatServer.joinChannel(user, args[1]);
                if (returnMsg != null) {
                    user.bufferedWriter.write(returnMsg);
                    user.bufferedWriter.flush();
                }
                break;
            case "PART":
                returnMsg = chatServer.leaveChannel(user, args[1]);
                break;
            case "PRIVMSG":
                returnMsg = chatServer.privateMessage(user, args[1], msg[1]);
                break;
            case "NAMES":
                returnMsg = chatServer.listChannelUsers(user, args[1]);
                break;
            case "LIST":
                returnMsg = chatServer.listChannels(user);
                break;
            case "TIME":
                returnMsg = chatServer.getTime();
                if (returnMsg != null) {
                    user.bufferedWriter.write(returnMsg);
                    user.bufferedWriter.flush();
                }
                break;
            case "INFO":
                returnMsg = chatServer.getInfo();
                break;
            case "PING":
                returnMsg = chatServer.ping(args[1]); // TODO change to full message
                if (returnMsg != null) {
                    user.bufferedWriter.write(returnMsg);
                    user.bufferedWriter.flush();
                }
                break;
        }
    }

}
