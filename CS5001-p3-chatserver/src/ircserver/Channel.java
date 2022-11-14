package ircserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a single channel on the chat server.
 */
public class Channel {
    private String name;
    private List<User> users;

    /**
     * Constructor for Channel.
     * @param name channel name
     */
    public Channel(String name) {
        this.name = name;
        users = new ArrayList<>();
    }

    /** Getter method for Channel name.
     * @return String
     */
    public String getName() {
        return name;
    }

    /** Method to add user tp the Channel.
     * @param user User
     */
    public void addUser(User user) {
        users.add(user);
    }

    /** Method to remove user from the Channel.
     * @param user User
     */
    public void removeUser(User user) {
        users.remove(user);
    }

    /** Method to check if the Channel is empty.
     * @return Boolean
     */
    public Boolean hasUsers() {
        return (users.size() > 0);
    }

    /** Method writing a message to all users in the Channel.
     * @param msg message to write
     * @throws IOException
     */
    public void message(String msg) throws IOException {
        for (User u : users) {
            ChatServer.write(u, msg);
        }
    }
}
