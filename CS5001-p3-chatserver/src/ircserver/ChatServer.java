package ircserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ChatServer {
    private Map<String, Channel> channels;
    private Map<String, User> users;
    private String replyTemplate;
    private String errorTemplate;

    private ServerSocket socket;

    public ChatServer(int port, String serverName) throws IOException {
        this.socket = new ServerSocket(port);
        this.channels = new HashMap<>();
        this.users = new HashMap<>();
        this.replyTemplate = String.format(":%s", serverName) + " %03d %s %s\n";
        this.errorTemplate = String.format(":%s 400 ", serverName) + "%s :%s\n";
    }

    public ServerSocket getServerSocket() {
        return this.socket;
    }

    public static void write(User user, String msg) throws IOException {
        if (msg != null) {
            user.getWriter().write(msg);
            user.getWriter().flush();
        }
    }

    public void setNick(User user, String[] args) throws IOException {
        String nick = args[0];
        try {
            validateNick(nick);
            user.setNick(nick);
        } catch (IllegalArgumentException e) {
            String errorMsg = getErrorMsg(user, "Invalid nickname");
            write(user, errorMsg);
            // returnMsg = String.format(":%s 400 * :Invalid nickname\n", serverName);
        }
    }

    public void registerUser(User user, String[] args) throws IOException {
        if (user.isRegistered()) {
            String errorMsg = getErrorMsg(user, "You are already registered");
            write(user, errorMsg);
            return;
        }
        try {
            String userName = args[0];
            String realName = getTextAfterColon(args);
            validateUserName(userName);
            user.register(userName, realName);
            addUser(user);
            String returnMsgText = String.format(":Welcome to the IRC network, %s", user.getNick());
            String returnMsg = String.format(replyTemplate, 1, user.getNick(), returnMsgText);
            write(user, returnMsg);
            
        } catch (IndexOutOfBoundsException e) {
            String errorMsg = getErrorMsg(user, "Not enough arguments");
            write(user, errorMsg);
        } catch (IllegalArgumentException e) {
            String errorMsg = getErrorMsg(user, "Invalid arguments to USER command");
            write(user, errorMsg);
        }
    }

    public void disconnectUser(User user) throws IOException {
        String returnMsg = null;
        if (user.isRegistered()) {
            returnMsg = String.format(
                ":%s QUIT\n", user.getNick());
        }

        for (Channel ch : user.getChannels()) {
            ch.removeUser(user);
        }
        for (User u : users.values()) {
            write(u, returnMsg);
        }
        users.remove(user.getNick(), user);
        user.cleanup();
    }

    public void joinChannel(User user, String[] args) throws IOException {
        if (!user.isRegistered()) {
            String errorMsg = getErrorMsg(user, "You need to register first");
            write(user, errorMsg);
            return;
        }
        try {
            String channelName = args[0];
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
            String errorMsg = getErrorMsg(user, "Invalid channel name");
            write(user, errorMsg);
        }
    }

    public void leaveChannel(User user, String[] args) throws IOException {
        if (!user.isRegistered()) {
            String errorMsg = getErrorMsg(user, "You need to register first");
            write(user, errorMsg);
            return;
        }

        String channelName = args[0];
        Channel ch = channels.get(channelName);

        if (ch == null) {
            String errorMsg = getErrorMsg(user, "No channel exists with that name");
            write(user, errorMsg);
            return;
        }
        ch.message(
            String.format(":%s PART %s\n", user.getNick(), channelName)
        );
        ch.removeUser(user);
        user.removeChannel(ch);
        if (!ch.hasUsers()) {
            channels.remove(channelName, ch);
        }
    }

    public void privateMessage(User user, String[] args) throws IOException {

        if (!user.isRegistered()) {
            String errorMsg = getErrorMsg(user, "You need to register first");
            write(user, errorMsg);
            return;
        }

        try {
            String target = args[0];
            String msg = getTextAfterColon(args);
            String returnMsg = String.format(
            ":%s PRIVMSG %s :%s\n", user.getNick(), target, msg);

            if (target.charAt(0) == '#') {
                if (!channelExists(target)) {
                    String errorMsg = getErrorMsg(user, "No channel exists with that name");
                    write(user, errorMsg);
                    return;
                }
                Channel ch = channels.get(target);
                ch.message(String.format(returnMsg, target));
            } else {
                User targetUser = users.get(target);
                if (targetUser == null) {
                    String errorMsg = getErrorMsg(user, "No user exists with that name");
                    write(user, errorMsg);
                    return;
                }
                write(targetUser, returnMsg);
            }
        } catch (Exception e) {
            String errorMsg = getErrorMsg(user, "Invalid arguments to PRIVMSG command");
            write(user, errorMsg);
        }
    }

    public void listChannelUsers(User user, String[] args) throws IOException {
        if (!user.isRegistered()) {
            String errorMsg = getErrorMsg(user, "You need to register first");
            write(user, errorMsg);
            return;
        }
        String channel = args[0];
        if (!channelExists(channel)) {
            String errorMsg = getErrorMsg(user, "No channel exists with that name");
            write(user, errorMsg);
            return;
        }
        String returnMsgText = String.format(
            "= %s :%s",
            channel,
            String.join(" ", users.values().stream().map(User::getNick).collect(Collectors.toList())));
        String returnMsg = String.format(replyTemplate, 353, user.getNick(), returnMsgText);
        write(user, returnMsg);
    }

    public void listChannels(User user) throws IOException {
        if (!user.isRegistered()) {
            String errorMsg = getErrorMsg(user, "You need to register first");
            write(user, errorMsg);
            return;
        }
        String returnMsg = "";
        for (Channel ch : this.channels.values()) {
            returnMsg += String.format(
                replyTemplate,
                322,
                user.getNick(),
                ch.getName());
        }
        returnMsg += String.format(
            replyTemplate,
            323,
            user.getNick(),
            ":End of LIST");
        write(user, returnMsg);
    }

    public void getTime(User user) throws IOException {
        String  returnMsg = String.format(
            replyTemplate,
            391,
            user.getNick(),
            LocalDateTime.now()
        );
        write(user, returnMsg);
    }

    public void getInfo(User user) throws IOException {
        String  returnMsg = String.format(
            replyTemplate,
            371,
            user.getNick(),
            "This is a IRC Chat Sever written by a CS5001 student."
        );
        write(user, returnMsg);
    }

    public void ping(User user, String[] args) throws IOException {
        String msg = String.join(" ", args);
        write(user, String.format("PONG %s\n", msg));
    }

    String getErrorMsg(User user, String msg) {
        return String.format(
            errorTemplate, user.getNick(), msg);
    }

    String getTextAfterColon(String[] args) {
        String[] colonSplit = String.join(" ", args).split(":");
        return colonSplit[1];
    }

    

    void addUser(User user) {
        this.users.put(user.getNick(), user);
    }

    void validateNick(String nick) throws IllegalArgumentException {
        Pattern p = Pattern.compile("^[a-zA-Z_]\\w{0,8}$");
        Matcher m = p.matcher(nick);
        if (!m.matches()) {
            throw new IllegalArgumentException();
        }
    }

    void validateUserName(String username) throws IllegalArgumentException {
        Pattern p = Pattern.compile("^\\S+$");
        Matcher m = p.matcher(username);
        if (!m.matches()) {
            throw new IllegalArgumentException();
        }
    }

    void validateChannelName(String channelName) throws IllegalArgumentException {
        Pattern p = Pattern.compile("^#\\w+$");
        Matcher m = p.matcher(channelName);
        if (!m.matches()) {
            throw new IllegalArgumentException();
        }
    }

    Channel getOrCreateChannel(String channelName) {
        Channel ch = channels.get(channelName);
        if (!channelExists(channelName)) {
            ch = new ircserver.Channel(channelName);
            channels.put(channelName, ch);
        }
        return ch;
    }

    Boolean channelExists(String channelName) {
        Channel ch = channels.get(channelName);
        if (ch == null) {
            return false;
        }
        return true;
    }
}
