package roombooking.model.components;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import roombooking.model.components.exceptions.IllegalBookingException;

public class Booking implements Serializable {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Person owner;
    private Room venue;

    @JsonCreator
    public Booking(@JsonProperty("date") LocalDate date, @JsonProperty("startTime") LocalTime startTime,
            @JsonProperty("endTime") LocalTime endTime, @JsonProperty("owner") Person owner,
            @JsonProperty("venue") Room venue)
            throws IllegalBookingException {
        this.owner = owner;
        this.venue = venue;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        validateBooking();
    }

    public void validateBooking() throws IllegalBookingException {
        int timeDiff = startTime.compareTo(endTime);
        if (timeDiff > 0) {
            // Check that start is less than end time
            throw new IllegalBookingException("Start Time cannot be after End Time!");
        } else if (timeDiff == 0) {
            // Check that booking has duration
            throw new IllegalBookingException("Booking does not have a duration!");
        }
        // Check that booking does not overlap with another
        for (Booking b : venue.getBookings()) {
            if (b.getDate().equals(this.date)) {
                int overlap = endTime.compareTo(b.getStartTime());
                if (overlap > 0) {
                    throw new IllegalBookingException("Booking overlaps with existing booking!");
                }
            }
        }
    }

    public LocalTime getStartTime() {
        return this.startTime;
    }

    public LocalTime getEndTime() {
        return this.endTime;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Person getOwner() {
        return this.owner;
    }

    public boolean checkOwnerPresent(Person owner, List<Person> list) {
        return list.contains(owner);
    }

    public Room getVenue() {
        return this.venue;
    }

}
