package model.components;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Person implements Serializable {

    private String fullName;
    private String email;
    private List<Booking> bookings;

    public Person(String fullName, String email) {
        validateEmail(email);
        this.fullName = fullName;
        this.email = email;
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

    @Override
    public String toString() {
        return String.format("%s (%s)", fullName, email);
    }

}
