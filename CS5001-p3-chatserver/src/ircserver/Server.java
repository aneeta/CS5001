package ircserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private ServerSocket socket;
    private int port;
    private String name;

    public Server(int port, String name) throws IOException {
        this.port = port;
        this.name = name;
        this.socket = new ServerSocket(port);
    }

    // public ServerSocket createNewSocket(int port) {
    // ServerSocket newSocket = new ServerSocket(port);
    // this.sockets.add(newSocket);
    // return newSocket;
    // }

    // public List<ServerSocket> getServerSocket() {
    // return this.sockets;
    // }

    public ServerSocket getServerSocket() {
        return this.socket;
    }

}
