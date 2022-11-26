package roombooking.model.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Person implements Serializable {

    private String fullName;
    private String email;
    private List<Booking> bookings;

    @JsonCreator
    public Person(@JsonProperty("name") String name, @JsonProperty("email") String email)
            throws IllegalArgumentException {
        validateEmail(email);
        this.fullName = name;
        this.email = email;
        this.bookings = new ArrayList<>();
    }

    public void validateEmail(String name) throws IllegalArgumentException {
        Pattern emailPattern = Pattern.compile("(\\w\\.?)+@([\\w-]+\\.)+[a-zA-z]+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPattern.matcher(name);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid email!");
        }
    }

    public void addBooking(Booking booking) {
        this.bookings.add(booking);
    }

    public void removeBooking(Booking booking) {
        this.bookings.remove(booking);
    }

    public List<Booking> getBookings() {
        return this.bookings;
    }

    public String getName() {
        return this.fullName;
    }

    public String getEmail() {
        return this.email;
    }

    public String toDisplay() {
        return getName() + " (" + getEmail() + ")";
    }

}