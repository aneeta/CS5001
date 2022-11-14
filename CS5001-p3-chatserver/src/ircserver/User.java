package ircserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a single chat user.
 */
public class User {
    private Boolean isRegistered;
    private String nick;
    private String userName;
    private String realName;
    private List<Channel> channels;

    private Socket socket;
    private InputStream inStream;
    private OutputStream outStream;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    /**
     * User constructor.
     * @param socket client socket
     * @throws IOException
     */
    public User(Socket socket) throws IOException {
        isRegistered = false;
        nick = "*";
        this.socket = socket;
        this.channels = new ArrayList<>();
        try {
            this.inStream = socket.getInputStream(); // get data from client on this input stream
            this.outStream = socket.getOutputStream(); // to send data back to the client on this stream
            this.bufferedReader = new BufferedReader(new InputStreamReader(this.inStream)); // use buffered reader to read data
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.outStream)); // use buffered reader to read data
        } catch (IOException ioe) {
            System.out.println(
                "Exception in User: " + ioe.getMessage()
            );
        }
    }

    /**
     * Getter method for the Users's Reader Stream.
     * @return BufferedReader
     */
    public BufferedReader getReader() {
        return bufferedReader;
    }

    /**
     * Getter method for the Users's Writer Stream.
     * @return BufferedWriter
     */
    public BufferedWriter getWriter() {
        return bufferedWriter;
    }

    /**
     * Setter method for user's nick.
     * @param nick chosen nick
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * Method to collect the username, realname, and switch registration flag.
     * @param userName username (diffferent from nick)
     * @param realName real name
     */
    public void register(String userName, String realName) {
        this.userName = userName;
        this.realName = realName;
        this.isRegistered = true;
    }

    /**
     * Method to check registration status.
     * @return Boolean
     */
    public Boolean isRegistered() {
        return this.isRegistered;
    }

    /**
     * Getter method for user's nick.
     * @return String
     */
    public String getNick() {
        return this.nick;
    }

    /**
     * Getter method for user's username.
     * @return String
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Getter method for user's real name.
     * @return String
     */
    public String getRealName() {
        return this.realName;
    }

    /**
     * Method to cleanly disconnect the client.
     */
    public void cleanup() {
        System.out.println("User: ... cleaning up and exiting ... ");
        try {
            bufferedWriter.close();
            bufferedReader.close();
            inStream.close();
            outStream.close();
            socket.close();
        } catch (IOException ioe) {
            System.out.println("User:cleanup " + ioe.getMessage());
        }
    }

    /**
     * Getter method for user's channels.
     * @return List<Channel>
     */
    public List<Channel> getChannels() {
        return this.channels;
    }

    /**
     * Method to have the user join a channel.
     * @param ch channel object
     */
    public void addChannel(Channel ch) {
        this.channels.add(ch);
    }

    /**
     * Method to have the user leave a channel.
     * @param ch channel object
     */
    public void removeChannel(Channel ch) {
        this.channels.remove(ch);
    }
}
