package ircserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
// import java.nio.channels.Channel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatServer {
    private Map<String,Channel> channels;
    private Map<String, User> users;
    private String errorMsgTemplate; // TODO

    private String serverName;
    private int port;
    private ServerSocket socket;


    public ChatServer(int port, String serverName) throws IOException {
        this.port = port;
        this.serverName = serverName;
        this.socket = new ServerSocket(port);
        this.channels = new HashMap<>();
        this.users = new HashMap<>();
    }

    public ServerSocket getServerSocket() {
        return this.socket;
    }

    public void addUser(User user) {
        this.users.put(user.getNick(), user);
    }

    public String setNick(User user, String nick) {
        String returnMsg = null;
        try {
            validateNick(nick);
            user.setNick(nick);
        } catch (Exception e) {
            returnMsg = String.format(":%s 400 * :Invalid nickname\n", serverName);
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
                ":%s 400 * :You are already registered\n", serverName);
            return returnMsg;
        }
        try {
            validateUserName(userName);
            user.register(userName, realName);
            addUser(user);
            returnMsg = String.format(
                ":%s 001 %s :Welcome to the IRC network, %s\n",
                serverName,
                user.getNick(),
                user.getNick()
            );
            return returnMsg;
        } catch (Exception e) {
            returnMsg = String.format(":%s 400 * :Invalid arguments to USER command\n", serverName);
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

    public String disconnectUser(User user) throws IOException {
        String returnMsg = null;
        if (user.isRegistered()) {
            returnMsg = String.format(
                ":%s QUIT\n", user.getNick());
        }

        for (Channel ch : user.getChannels()) {
            ch.removeUser(user);
            ch.message(returnMsg);
        }

        if (returnMsg != null) {
            user.bufferedWriter.write(returnMsg);
            user.bufferedWriter.flush();
        }
        
        user.cleanup();
        return returnMsg;
    }

    public String joinChannel(User user, String channelName) throws IOException {
        if (!user.isRegistered()) {
            return String.format(
                ":%s 400 * :You need to register first\n", serverName);
        }
        try {
            validateChannelName(channelName);
            Channel ch = getOrCreateChannel(channelName);
            ch.addUser(user);
            user.addChannel(ch);
            String returnMsg = String.format(
                ":%s JOIN %s\n",
                user.getNick(),
                channelName
                );
            ch.message(returnMsg);

        } catch (IllegalArgumentException e) {
            return String.format(
                ":%s 400 * :Invalid channel name\n", serverName);
        }
        
        return null;
    }

    public void validateChannelName(String channelName) throws IllegalArgumentException {
        Pattern p = Pattern.compile("^#\\w+$");
        Matcher m = p.matcher(channelName);
        if (!m.matches()) {
            throw new IllegalArgumentException();
        }
    }

    public Channel getOrCreateChannel(String channelName) {
        Channel ch = channels.get(channelName);
        if (!channelExists(channelName)) {
            ch = new ircserver.Channel(channelName);
            channels.put(channelName, ch);
        }
        return ch;
    }

    public String leaveChannel(User user, String channelName) throws IOException {
        if (!user.isRegistered()) {
            return String.format(
                ":%s 400 * :You need to register first\n", serverName);
        }
        Channel ch = channels.get(channelName);
        if (ch == null) {
            return String.format(":%s 400 * :No channel exists with that name", serverName);
        }
        ch.message(
            String.format(":%s PART %s", user.getNick(), channelName)
        );
        ch.removeUser(user);
        user.removeChannel(ch);
        if (!ch.hasUsers()) {
            channels.remove(ch);
        }
        return null;
    }

    public String privateMessage(User user, String target, String msg) throws IOException {
        String returnMsg = String.format(
            ":%s PRIVMSG %s :", user.getNick());
        returnMsg += msg;
        if (!user.isRegistered()) {
            return String.format(
                ":%s 400 * :You need to register first\n", serverName);
        }
        if (target.charAt(0) == '#') {
            if (!channelExists(target)) {
                return String.format(
                    ":%s 400 * :No channel exists with that name",
                    serverName
                );
            }
            Channel ch = channels.get(target);
            ch.message(String.format(returnMsg, target));
        } else {
            User targetUser = users.get(target);
            if (targetUser == null) {
                return String.format(
                    ":%s 400 * :No user exists with that name",
                    serverName
                );
            }

            targetUser.bufferedWriter.write(String.format(returnMsg, target));
            targetUser.bufferedWriter.flush();

        }
        return null;

    }

    public Boolean channelExists(String channelName) {
        Channel ch = channels.get(channelName);
        if (ch == null) {
            return false;
        }
        return true;
    }

    public String listChannelUsers(User user, String channel) {
        if (!user.isRegistered()) {
            return String.format(
                ":%s 400 * :You need to register first\n", serverName);
        }
        // TODO
        return null;

    }

    public String listChannels(User user) {
        if (!user.isRegistered()) {
            return String.format(
                ":%s 400 * :You need to register first\n", serverName);
        }
        String returnMsg = "";
        String returnMsgBase = String.format(
            ":%s 322 %s %s\n",
            serverName,
            user.getNick()
            );
        for (Channel ch : this.channels.values()) {
            returnMsg += String.format(returnMsgBase, ch.getName());
        }
        returnMsg += String.format(returnMsgBase, ":End of LIST");
        return returnMsg;
    }

    public String getTime() {
        return String.format(
            ":%s 391 * :%s\n",
            serverName,
            LocalDateTime.now()
        );
    }

    public String getInfo() {
        return String.format(
            ":%s 371 * :%s\n",
            serverName,
            "This is a IRC Chat Sever written by a CS5001 student."
        );
    }

    public String ping(String msg) {
        return String.format(
            "PONG %s\n", msg
        );
    }

    public String checkRegistered(User user) {
        if (!user.isRegistered()) {
            return String.format(
                ":%s 400 * :You need to register first\n", serverName);
        }
        return null;
    }

}
