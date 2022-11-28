package roombooking.model.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Room implements Serializable {

    private int buildingId;
    private String name;
    private int id;

    @JsonCreator
    public Room(@JsonProperty("name") String name, @JsonProperty("buildingId") int buildingId,
            @JsonProperty("id") int id) {
        this.buildingId = buildingId;
        this.name = name;
        this.id = id;

    }

    // public String getBuildingName() {
    // return buildingName;
    // }

    public int getBuildingId() {
        return buildingId;
    }

    // public void addBooking(Booking b) {
    // this.bookings.add(b);
    // }

    // public void removeBooking(Booking b) {
    // this.bookings.remove(b);
    // }

    // public List<Booking> getBookings() {
    // return this.bookings;
    // }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }
}
