package model.components;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import model.exceptions.IllegalBookingException;

public class Booking {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Person owner;
    private Room venue;

    public Booking(LocalDate date, LocalTime startTime, LocalTime endTime, Person owner, Room venue)
            throws IllegalBookingException {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.owner = owner;
        this.venue = venue;
        validateStartEnd();
    }

    public void validateStartEnd() throws IllegalBookingException {

        // TODO

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

}
