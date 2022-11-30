package roombooking.model.components;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import roombooking.model.components.exceptions.IllegalBookingException;

public class Booking {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Person owner;
    private Room venue;

    /**
     * Construstor method for a class representing a Booking.
     * 
     * @param date      date of the booking
     * @param startTime booking start time
     * @param startTime booking end time
     * @param owner     person making the booking
     * @param venue     booked room
     * @throws IllegalBookingException
     */
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

    /**
     * Method to validate that the Booking Start and End Time are valid.
     * Checks that start time is before (and not equal to) end time.
     * 
     * @throws IllegalBookingException
     */
    public void validateBooking() throws IllegalBookingException {
        int timeDiff = startTime.compareTo(endTime);
        if (timeDiff > 0) {
            // Check that start is less than end time
            throw new IllegalBookingException("Start Time cannot be after End Time!");
        } else if (timeDiff == 0) {
            // Check that booking has duration
            throw new IllegalBookingException("Booking does not have a duration!");
        }
    }

    /**
     * Getter method for Booking Start Time.
     * 
     * @return LocalTime
     */
    public LocalTime getStartTime() {
        return this.startTime;
    }

    /**
     * Getter method for Booking End Time.
     * 
     * @return LocalTime
     */
    public LocalTime getEndTime() {
        return this.endTime;
    }

    /**
     * Getter method for Booking Date.
     * 
     * @return LocalDate
     */
    public LocalDate getDate() {
        return this.date;
    }

    /**
     * Getter method for Booking Owner.
     * 
     * @return Person
     */
    public Person getOwner() {
        return this.owner;
    }

    /**
     * Validation method to checked that a passed list of people included the
     * Booking owner.
     * 
     * @param owner
     * @param list
     * @return boolean
     */
    public boolean checkOwnerPresent(Person owner, List<Person> list) {
        return list.contains(owner);
    }

    /**
     * Getter method for Booking Venue.
     * 
     * @return Room
     */
    public Room getVenue() {
        return this.venue;
    }
}
