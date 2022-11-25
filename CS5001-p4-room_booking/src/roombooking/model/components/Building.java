package model.components;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Building {

    private Map<String, Room> rooms;
    private String name;
    private String address;

    public Building(String name, String address) {
        this.name = name;
        this.address = address;
        this.rooms = new HashMap<>();
    }

    public List<Map<String, String>> getBookings(List<LocalDate> dates, List<String> rooms,
            List<Person> owners) {
        List<Map<String, String>> allData = new ArrayList<>();
        for (String rName : rooms) {
            List<Map<String, String>> data = this.rooms.get(rName).getBookings(dates, owners);
            allData.addAll(data);
        }
        return allData;
    }

    // public List<List<String>> getBookings(
    // List<LocalDate> dates,
    // List<Room> rooms,
    // List<Person> owners) {
    // List<List<String>> data = new ArrayList<>();
    // for (String rName : rooms) {
    // this.rooms.get(rName).getBookings(dates, rooms, owners);
    // }

    // }

    public void addRoom(Room room) {

        // TODO

    }

    public void addRoom(String roomId) {
        // TODO

    }

    public void removeRoom(Room room) {
        // TODO

    }

    public void removeRoom(String roomId) {
        // TODO

    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
