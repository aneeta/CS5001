package model.components;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import model.exceptions.IllegalBookingException;

public class Room implements Serializable {
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
    private final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:m", Locale.ENGLISH);

    private Building building;
    private Map<LocalDate, List<Booking>> bookings;
    private String roomId;

    public Room(String roomId, Building building) {
        this.roomId = roomId;
        this.building = building;
        this.bookings = new HashMap<>();
    }

    public boolean checkIfBooked(LocalDate date, LocalTime startTime, LocalTime endTime) {
        // TODO
        return true;
    }

    public void addBooking(LocalDate date, LocalTime startTime, LocalTime endTime, Person owner)
            throws IllegalArgumentException, IllegalBookingException {
        if (checkIfBooked(date, startTime, endTime)) {
            String errorMsg = String.format(
                    "%s already booked! Choose a different date or time.", roomId);
            throw new IllegalBookingException(errorMsg);
        }
        if (bookings.containsKey(date)) {
            Booking booking = new Booking(date, startTime, endTime, owner, this);
            bookings.get(date).add(booking);
            owner.addBooking(booking);
        } else {
            Booking booking = new Booking(date, startTime, endTime, owner, this);
            bookings.put(date, new ArrayList<>());
            bookings.get(date).add(booking);
            owner.addBooking(booking);
        }

    }

    public void removeBooking(LocalDate date, Booking booking) {
        bookings.get(date).remove(booking);
    }

    public List<Map<String, String>> getBookings(List<LocalDate> dates, List<Person> owners) {
        List<Map<String, String>> data = new ArrayList<>();
        for (LocalDate date : dates) {
            for (Booking booking : this.bookings.get(date)) {
                Person owner = booking.getOwner();
                if (booking.checkOwnerPresent(owner, owners)) {
                    data.add(getRow(booking));
                }
            }
        }
        return data;
    }

    public Map<String, String> getRow(Booking booking) {
        Map<String, String> row = new HashMap<>();
        row.put("Date", booking.getDate().format(DATE_FORMATTER));
        row.put("Time", String.format(
                "%s - %s",
                booking.getStartTime().format(TIME_FORMATTER),
                booking.getEndTime().format(TIME_FORMATTER)));
        row.put("Owner", booking.getOwner().toString());
        row.put("Room", roomId);
        row.put("Building", building.getName());
        return row;
    }

    // public List<List<String>> getBookings(List<LocalDate> dates, List<Person>
    // owners) {
    // List<List<String>> data = new ArrayList<>();
    // for (LocalDate date : dates) {
    // for(Booking booking: this.rooms.get(date)) {

    // }
    // }

    // }

}
