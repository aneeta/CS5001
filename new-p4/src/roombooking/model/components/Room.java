package roombooking.model.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Room implements Serializable {

    private Building building;
    private List<Booking> bookings;
    private String roomId;

    @JsonCreator
    public Room(@JsonProperty("roomId") String roomId, @JsonProperty("building") Building building) {
        this.roomId = roomId;
        this.building = building;
        this.bookings = new ArrayList<>();
    }

    public Building getBuilding() {
        return building;
    }

    public void addBooking(Booking b) {
        this.bookings.add(b);
    }

    public void removeBooking(Booking b) {
        this.bookings.remove(b);
    }

    public List<Booking> getBookings() {
        return this.bookings;
    }

    public String getName() {
        return this.roomId;
    }
}
