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

    public BufferedReader getReader() {
        return bufferedReader;
    }

    public BufferedWriter getWriter() {
        return bufferedWriter;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void register(String userName, String realName) {
        this.userName = userName;
        this.realName = realName;
        this.isRegistered = true;
    }

    public Boolean isRegistered() {
        return this.isRegistered;
    }

    public String getNick() {
        return this.nick;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getRealName() {
        return this.realName;
    }

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

    public List<Channel> getChannels() {
        return this.channels;
    }

    public void addChannel(Channel ch) {
        this.channels.add(ch);
    }

    public void removeChannel(Channel ch) {
        this.channels.remove(ch);
    }

}
