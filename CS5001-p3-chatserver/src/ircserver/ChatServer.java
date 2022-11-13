package ircserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Channel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatServer {
    private Map<String,Channel> channels;
    private List<User> users;

    private String serverName;
    private int port;
    private ServerSocket socket;


    public ChatServer(int port, String serverName) throws IOException {
        this.port = port;
        this.serverName = serverName;
        this.socket = new ServerSocket(port);
        this.channels = new HashMap<>();
        this.users = new ArrayList<>();
    }

    public ServerSocket getServerSocket() {
        return this.socket;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public String setNick(User user, String nick) {
        String returnMsg = null;
        try {
            validateNick(nick);
            user.setNick(nick);
        } catch (Exception e) {
            returnMsg = String.format(":%s 400 * :Invalid nickname", serverName);
        }
        return returnMsg;
    }

    public void validateNick(String nick) throws IllegalArgumentException {
        Pattern p = Pattern.compile("^[a-zA-Z_]\\w{0,8}$");
        Matcher m = p.matcher(nick);
        if (!m.matches()) {
            throw new IllegalArgumentException();
        }
    }

    public String registerUser(User user, String userName, String realName) {
        String returnMsg;
        if (user.isRegistered()) {
            returnMsg = String.format(
                ":%s 400 * :You are already registered", serverName);
            return returnMsg;
        }
        try {
            validateUserName(userName);
            user.register(userName, realName);
            returnMsg = String.format(
                ":%s 001 %s :Welcome to the IRC network, %s",
                serverName,
                user.getNick(),
                user.getNick()
            );
            return returnMsg;
        } catch (Exception e) {
            returnMsg = String.format(":%s 400 * :Invalid arguments to USER command", serverName);
            return returnMsg;
        }
    }

    public void validateUserName(String username) throws IllegalArgumentException {
        Pattern p = Pattern.compile("^\\S+$");
        Matcher m = p.matcher(username);
        if (!m.matches()) {
            throw new IllegalArgumentException();
        }
    }

    public String disconnectUser(User user) {
        String returnMsg = null;
        if (user.isRegistered()) {
            returnMsg = String.format(
                ":%s QUIT", user.getNick());
        }
        
        user.cleanup();
        return returnMsg;
    }

    public String joinChannel(User user, String channelName) {
        // TODO
        return null;
    }

    public String leaveChannel(User user, String channelName) {
        // TODO
        return null;

    }

    public String privateMessage(User user, String target, String msg) {
        // TODO
        return null;

    }

    public String listChannelUsers(String channel) {
        // TODO
        return null;

    }

    public String listChannels() {
        // TODO
        return null;

    }

    public String getTime() {
        return String.format(
            ":%s 391 * :%s",
            serverName,
            LocalDateTime.now()
        );
    }

    public String getInfo() {
        return String.format(
            ":%s 371 * :%s",
            serverName,
            "This is a IRC Chat Sever written by a CS5001 student."
        );
    }

    public String ping(String msg) {
        return String.format(
            "PONG %s", msg
        );
    }

}
