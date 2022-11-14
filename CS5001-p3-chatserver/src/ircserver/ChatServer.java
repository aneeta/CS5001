package ircserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Class representing the chat.
 */
public class ChatServer {
    private Map<String, Channel> channels;
    private Map<String, User> users;
    private String replyTemplate;
    private String errorTemplate;
    private ServerSocket socket;

    /**
     * ChatServer constructor.
     * 
     * @param port       server port
     * @param serverName server name
     * @throws IOException
     */
    public ChatServer(int port, String serverName) throws IOException {
        this.socket = new ServerSocket(port);
        this.channels = new HashMap<>();
        this.users = new HashMap<>();
        this.replyTemplate = String.format(":%s", serverName) + " %03d %s %s\n";
        this.errorTemplate = String.format(
                ":%s %d ",
                serverName,
                Return.ERROR.getCode()) + "%s :%s\n";
    }

    /**
     * Utility method to write non-empty output to a given user.
     * 
     * @param user user
     * @param msg  output string
     * @throws IOException
     */
    public static void write(User user, String msg) throws IOException {
        if (msg != null) {
            user.getWriter().write(msg);
            user.getWriter().flush();
        }
    }

    /**
     * Method corresponding to the NICK command.
     * 
     * @param user user
     * @param args array of arguments passed after the command
     * @throwsQUIT IOException
     */
    public void setNick(User user, String[] args) throws IOException {
        String nick = args[0];
        try {
            // check that given nick meets the rules:
            // - has to be 1-9 characters
            // - cannot start with a number
            // - contains alphanumerics & underscores
            validateString(nick, "^[a-zA-Z_]\\w{0,8}$");
            user.setNick(nick);
        } catch (IllegalArgumentException e) {
            String errorMsg = getErrorMsg(user, "Invalid nickname");
            write(user, errorMsg);
        }
    }

    /**
     * Method corresponding to the USER command.
     * 
     * @param user user
     * @param args array of arguments passed after the command
     * @throws IOException
     */
    public void registerUser(User user, String[] args) throws IOException {
        if (user.isRegistered()) {
            String errorMsg = getErrorMsg(user, "You are already registered");
            write(user, errorMsg);
            return;
        }
        try {
            // parse args
            String userName = args[0];
            String realName = getTextAfterColon(args);
            // check that given nick meets the rules:
            // - non whitespace characters
            validateString(userName, "^\\S+$");
            user.register(userName, realName);
            addUser(user);
            String returnMsgText = String.format(
                    ":Welcome to the IRC network, %s",
                    user.getNick());
            String returnMsg = String.format(
                    replyTemplate,
                    Return.NICK.getCode(),
                    user.getNick(),
                    returnMsgText);
            write(user, returnMsg);
        } catch (IndexOutOfBoundsException e) {
            String errorMsg = getErrorMsg(user, "Not enough arguments");
            write(user, errorMsg);
        } catch (IllegalArgumentException e) {
            String errorMsg = getErrorMsg(user, "Invalid arguments to USER command");
            write(user, errorMsg);
        }
    }

    /**
     * Method corresponding to the QUIT command.
     * 
     * @param user user
     * @throws IOException
     */
    public void disconnectUser(User user) throws IOException {
        String returnMsg = null;
        if (user.isRegistered()) {
            returnMsg = String.format(
                    ":%s QUIT\n", user.getNick());
        }
        // remove user from channels
        for (Channel ch : user.getChannels()) {
            ch.removeUser(user);
        }
        // notify all users about the QUIT
        for (User u : users.values()) {
            write(u, returnMsg);
        }
        users.remove(user.getNick(), user);
        // close the connection
        user.cleanup();
    }

    /**
     * Method corresponding to the JOIN command.
     * 
     * @param user user
     * @param args array of arguments passed after the command
     * @throws IOException
     */
    public void joinChannel(User user, String[] args) throws IOException {
        if (!user.isRegistered()) {
            String errorMsg = getErrorMsg(user, "You need to register first");
            write(user, errorMsg);
            return;
        }
        try {
            String channelName = args[0];
            // check that given nick meets the rules:
            // - starts with a #
            validateString(channelName, "^#\\w+$");
            Channel ch = getOrCreateChannel(channelName);
            ch.addUser(user);
            user.addChannel(ch);
            String returnMsg = String.format(
                    ":%s JOIN %s\n",
                    user.getNick(),
                    channelName);
            ch.message(returnMsg);

        } catch (IllegalArgumentException e) {
            String errorMsg = getErrorMsg(user, "Invalid channel name");
            write(user, errorMsg);
        }
    }

    /**
     * Method corresponding to the PART command.
     * 
     * @param user user
     * @param args array of arguments passed after the command
     * @throws IOException
     */
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
                String.format(":%s PART %s\n", user.getNick(), channelName));
        ch.removeUser(user);
        user.removeChannel(ch);
        // if no one is left, close channel
        if (!ch.hasUsers()) {
            channels.remove(channelName, ch);
        }
    }

    /**
     * Method corresponding to the PRIVMSG command.
     * 
     * @param user user
     * @param args array of arguments passed after the command
     * @throws IOException
     */
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
                // Sending message to a channel
                if (!channelExists(target)) {
                    String errorMsg = getErrorMsg(user, "No channel exists with that name");
                    write(user, errorMsg);
                    return;
                }
                Channel ch = channels.get(target);
                ch.message(String.format(returnMsg, target));
            } else {
                // Sending message to a user
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

    /**
     * Method corresponding to the NAMES command.
     * 
     * @param user user
     * @param args array of arguments passed after the command
     * @throws IOException
     */
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
        String returnMsg = String.format(
                replyTemplate,
                Return.CHANNELS.getCode(),
                user.getNick(),
                returnMsgText);
        write(user, returnMsg);
    }

    /**
     * Method corresponding to the LIST command.
     * 
     * @param user user
     * @throws IOException
     */
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
                    Return.LIST.getCode(),
                    user.getNick(),
                    ch.getName());
        }
        returnMsg += String.format(
                replyTemplate,
                Return.LIST_END.getCode(),
                user.getNick(),
                ":End of LIST");
        write(user, returnMsg);
    }

    /**
     * Method corresponding to the TIME command.
     * 
     * @param user user
     * @throws IOException
     */
    public void getTime(User user) throws IOException {
        String returnMsg = String.format(
                replyTemplate,
                Return.TIME.getCode(),
                user.getNick(),
                ":" + LocalDateTime.now());
        write(user, returnMsg);
    }

    /**
     * Method corresponding to the INFO command.
     * 
     * @param user user
     * @throws IOException
     */
    public void getInfo(User user) throws IOException {
        String returnMsg = String.format(
                replyTemplate,
                Return.INFO.getCode(),
                user.getNick(),
                ":This is a IRC Chat Sever written by a CS5001 student.");
        write(user, returnMsg);
    }

    /**
     * Method corresponding to the PING command.
     * 
     * @param user user
     * @param args array of arguments passed after the command
     * @throws IOException
     */
    public void ping(User user, String[] args) throws IOException {
        String msg = String.join(" ", args);
        write(user, String.format("PONG %s\n", msg));
    }

    /**
     * Utility method to get Error string.
     * 
     * @param user user
     * @param msg  error message
     * @return String
     */
    public String getErrorMsg(User user, String msg) {
        return String.format(
                errorTemplate, user.getNick(), msg);
    }

    /**
     * Utility method to get text after ":".
     * Needed because of chat input format.
     * 
     * @param args array of arguments passed after the command
     * @return String
     */
    public String getTextAfterColon(String[] args) {
        String[] colonSplit = String.join(" ", args).split(":");
        return colonSplit[1];
    }

    /**
     * Utility method to add update the users list.
     * 
     * @param user user
     */
    public void addUser(User user) {
        this.users.put(user.getNick(), user);
    }

    /**
     * Utility method to validate a string base on a pattern.
     * Needed to check that nick, channel, and username meet the rules.
     * 
     * @param string target string
     * @param regex  patter
     * @throws IllegalArgumentException
     */
    public static void validateString(String string, String regex) throws IllegalArgumentException {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(string);
        if (!m.matches()) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Utility method to get channel object baset on channel name.
     * 
     * @param channelName channel name string
     * @return Channel
     */
    public Channel getOrCreateChannel(String channelName) {
        Channel ch = channels.get(channelName);
        if (!channelExists(channelName)) {
            ch = new ircserver.Channel(channelName);
            channels.put(channelName, ch);
        }
        return ch;
    }

    /**
     * Utility method to check if channel exists.
     * 
     * @param channelName channel name string
     * @return Boolean
     */
    public Boolean channelExists(String channelName) {
        Channel ch = channels.get(channelName);
        if (ch == null) {
            return false;
        }
        return true;
    }

    /**
     * Getter method for Chat's Server Socket.
     * 
     * @return ServerSocket
     */
    public ServerSocket getServerSocket() {
        return this.socket;
    }
}
