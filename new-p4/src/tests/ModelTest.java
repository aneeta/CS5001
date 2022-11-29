package tests;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import roombooking.model.BookingSystemModel;
import roombooking.model.components.Building;
import roombooking.model.components.exceptions.IllegalBookingException;

public class ModelTest {
    private BookingSystemModel model;

    @Before
    public void setUp() {
        model = new BookingSystemModel();
        model.setName("Test Uni");
    }

    @Test
    public void testBuildings() {
        String address = "North Haugh";
        // add one building
        model.addBuilding("Jack Cole Building", address);
        assertEquals(model.getBuildings().get(0).getName(), "Jack Cole Building");
        // add another building
        model.addBuilding("Powell Hall", address);
        assertEquals(model.getBuildings().get(0).getAddress(), address);
        assertEquals(model.getBuildings().get(1).getAddress(), address);
        assertEquals(model.getBuildings().size(), 2);
        // remove building
        model.removeBuilding(model.getBuildings().get(0));
        assertEquals(model.getBuildings().size(), 1);
        assertEquals(model.getBuildings().get(0).getName(), "Powell Hall");
    }

    @Test
    public void testRooms() {
        Building hall = new Building("ABH", "123 Test St", 20);
        model.addBuilding(hall.getName(), hall.getAddress());
        // add rooms
        model.addRoom("Bar", hall);
        model.addRoom("Study Room 1", hall);
        model.addRoom("Study Room 2", hall);
        assertArrayEquals(model.getRooms().toArray(), model.getRooms(hall).toArray());
        // remove room
        model.removeRoom(model.getRooms().get(0));
        assertEquals(model.getRooms().size(), 2);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testPeople() {
        model.addPerson("John Doe", "jd123@st-andrews.ac.uk");
        model.addPerson("Alice Smith", "as789@st-andrews.ac.uk");
        assertEquals(model.getPeople().size(), 2);
        model.removePerson(model.getPeople().get(0));
        assertEquals(model.getPeople().size(), 1);
        // malformed email exception
        model.addPerson("Joe", "");
    }

    @Test(expected = IllegalBookingException.class)
    public void testBookings() throws IllegalBookingException {
        model.addPerson("Ann", "a@gmail.com");
        model.addBuilding("Maths Department", "456 JUnit Rd");
        model.addRoom("Meeting Room", model.getBuildings().get(model.getBuildings().size() - 1));
        model.addBooking(
                LocalDate.of(2022, 11, 22),
                LocalTime.of(11, 0, 0),
                LocalTime.of(12, 0, 0),
                model.getBuildings().get(0),
                model.getRooms().get(0),
                model.getPeople().get(0));
        model.addBooking(
                LocalDate.of(2022, 11, 23),
                LocalTime.of(11, 0, 0),
                LocalTime.of(12, 0, 0),
                model.getBuildings().get(0),
                model.getRooms().get(0),
                model.getPeople().get(0));
        model.addBooking(
                LocalDate.of(2022, 11, 24),
                LocalTime.of(11, 0, 0),
                LocalTime.of(12, 0, 0),
                model.getBuildings().get(0),
                model.getRooms().get(0),
                model.getPeople().get(0));
        assertEquals(model.getBookings().size(), 3);
        model.removeBooking(model.getBookings().get(2));
        assertEquals(model.getBookings().size(), 2);
        // should throw illegal booking exception (Start time after end time)
        model.addBooking(
                LocalDate.of(2022, 11, 24),
                LocalTime.of(13, 0, 0),
                LocalTime.of(12, 0, 0),
                model.getBuildings().get(0),
                model.getRooms().get(0),
                model.getPeople().get(0));
    }

}
