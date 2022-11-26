package roombooking.model.components;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Building implements Serializable {

    private List<Room> rooms;
    private String name;
    private String address;

    @JsonCreator
    public Building(@JsonProperty("name") String name, @JsonProperty("address") String address) {
        this.name = name;
        this.address = address;
        this.rooms = new ArrayList<>();
    }

    public void addRoom(Room room) {
        this.rooms.add(room);
    }

    public void removeRoom(Room room) {
        this.rooms.remove(room);
    }

    public List<Room> getRooms() {
        return this.rooms;
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

    // public List<Map<String, String>> getBookings(List<LocalDate> dates,
    // List<String> rooms,
    // List<Person> owners) {
    // List<Map<String, String>> allData = new ArrayList<>();
    // for (String rName : rooms) {
    // List<Map<String, String>> data = this.rooms.get(rName).getBookings(dates,
    // owners);
    // allData.addAll(data);
    // }
    // return allData;
    // }

}
