package ircserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Channel {
    private String name;
    private List<User> users;

    public Channel(String name) {
        this.name = name;
        users = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void message(String msg) throws IOException {
        for (User u : users) {
            u.bufferedWriter.write(msg);
        }
    }
}
