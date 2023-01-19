package tests;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import roombooking.controller.BookingSystemController;
import roombooking.model.BookingSystemModel;
import roombooking.model.components.Booking;
import roombooking.model.components.Building;
import roombooking.model.components.Person;
import roombooking.model.components.Room;

public class ControllerTest {

    private BookingSystemModel model;
    private BookingSystemController controller;

    @Before
    public void setUp() {
        model = new BookingSystemModel();
        model.setName("Sample University");
        controller = new BookingSystemController(model);
    }

    @Test
    public void testCanAddOnePerson() {
        String msg = controller.controlAddPerson("Anne", "anne@vealchurch.com");
        assertEquals(msg, "Anne added successfully!");
        assertArrayEquals(controller.controlGetPersonList(), new String[] { "Anne" });
    }

    @Test
    public void testCanAddPeople() {
        controller.controlAddPerson("Anne", "anne@vealchurch.com");
        controller.controlAddPerson("George Michael", "georgemichael@bananastand.com");
        controller.controlAddPerson("Buster", "byron@bluthcompany.com");
        assertArrayEquals(controller.controlGetPersonList(), new String[] { "Anne", "George Michael", "Buster" });
    }

    @Test
    public void testCannotAddPersonWithDuplicateEmail() {
        controller.controlAddPerson("Anne", "anne@vealchurch.com");
        String msg = controller.controlAddPerson("George Michael", "anne@vealchurch.com");
        assertNotEquals(msg, "George Michael added successfully!");
    }

    @Test
    public void testCanRemovePeople() {
        controller.controlAddPerson("Anne", "anne@vealchurch.com");
        controller.controlAddPerson("George Michael", "georgemichael@bananastand.com");
        controller.controlAddPerson("Buster", "byron@bluthcompany.com");

        String msg = controller.controlRemovePerson("Anne", "anne@vealchurch.com");
        assertEquals("Person removed successfully!", msg);
        assertArrayEquals(new String[] { "George Michael", "Buster" }, controller.controlGetPersonList());

        msg = controller.controlRemovePerson("George Michael", "georgemichael@bananastand.com");
        assertEquals("Person removed successfully!", msg);
        assertArrayEquals(new String[] { "Buster" }, controller.controlGetPersonList());
    }

    @Test
    public void testCanParsePerson() throws ParseException {
        controller.controlAddPerson("Anne", "anne@vealchurch.com");
        Person person = controller.parsePerson("Anne", "anne@vealchurch.com");
        assertEquals("Anne", person.getName());
        assertEquals("anne@vealchurch.com", person.getEmail());
    }

    @Test(expected = ParseException.class)
    public void testParseThrowsExceptionOnPersonNotFound() throws ParseException {
        controller.controlAddPerson("George Oscar", "gob@bluthcompany.com");
        controller.parsePerson("Larry", "larry@hotmail.com");
    }

    @Test
    public void testCanAddBuilding() {
        String msg = controller.controlAddBuilding("A", "KY16 9BL");
        assertEquals("A added successfully!", msg);
        assertArrayEquals(new String[] { "A" }, controller.controlGetBuildingList());
    }

    @Test
    public void testCanAddBuildings() {
        controller.controlAddBuilding("A", "KY16 9BL");
        controller.controlAddBuilding("B", "KY16 9BM");
        assertArrayEquals(new String[] { "A", "B" }, controller.controlGetBuildingList());
    }

    @Test
    public void testCannotAddBuildingWithDuplicateName() {
        controller.controlAddBuilding("A", "KY16 9BL");
        String msg = controller.controlAddBuilding("A", "KY16 9BM");
        assertNotEquals("A added successfully!", msg);
    }

    @Test
    public void testCanRemoveBuildings() {
        controller.controlAddBuilding("A", "KY16 9BL");
        controller.controlAddBuilding("B", "KY16 9BM");

        String msg = controller.controlRemoveBuilding("A");
        assertEquals(msg, "Building removed successfully!");
        assertArrayEquals(new String[] { "B" }, controller.controlGetBuildingList());

        msg = controller.controlRemoveBuilding("B");
        assertEquals(msg, "Building removed successfully!");
        assertArrayEquals(new String[] {}, controller.controlGetBuildingList());
    }

    @Test
    public void testCanParseBuilding() throws ParseException {
        controller.controlAddBuilding("A", "KY16 9BL");
        Building building = controller.parseBuilding("A");
        assertEquals("A", building.getName());
        assertEquals("KY16 9BL", building.getAddress());
    }

    @Test(expected = ParseException.class)
    public void testParseThrowsExceptionOnBuildingNotFound() throws ParseException {
        controller.controlAddBuilding("A", "KY16 9BL");
        controller.parseBuilding("B");
    }

    @Test
    public void testCanAddRoom() {
        controller.controlAddBuilding("A", "KY16 9BL");
        String msg = controller.controlAddRoom("1", "A");
        assertEquals("1 added successfully!", msg);
        assertArrayEquals(new String[] { "1" }, controller.controlGetRoomList());
    }

    @Test
    public void testCanAddRooms() {
        controller.controlAddBuilding("A", "KY16 9BL");
        controller.controlAddRoom("1", "A");
        controller.controlAddRoom("2", "A");
        assertArrayEquals(new String[] { "1", "2" }, controller.controlGetRoomList());
    }

    @Test
    public void testCannotAddRoomToSameBuildingWithDuplicateName() {
        controller.controlAddBuilding("A", "KY16 9BL");
        controller.controlAddRoom("1", "A");
        String msg = controller.controlAddRoom("1", "A");
        assertNotEquals(msg, "1 added successfully!");
        assertArrayEquals(new String[] { "1" }, controller.controlGetRoomList());
    }

    @Test
    public void testCanAddRoomToDifferentBuildingWithSameName() {
        controller.controlAddBuilding("A", "KY16 9BL");
        controller.controlAddBuilding("B", "KY16 9BL");
        controller.controlAddRoom("1", "A");
        controller.controlAddRoom("1", "B");
        assertArrayEquals(new String[] { "1", "1" }, controller.controlGetRoomList());
    }

    @Test
    public void testCanRemoveRooms() {
        controller.controlAddBuilding("A", "KY16 9BL");
        controller.controlAddBuilding("B", "KY16 9BM");
        controller.controlAddRoom("1", "A");
        controller.controlAddRoom("2", "A");
        controller.controlAddRoom("1", "B");

        String msg = controller.controlRemoveRoom("1", "A");
        assertEquals(msg, "Room removed successfully!");
        assertArrayEquals(new String[] { "2", "1" }, controller.controlGetRoomList());

        msg = controller.controlRemoveRoom("1", "B");
        assertEquals(msg, "Room removed successfully!");
        assertArrayEquals(new String[] { "2" }, controller.controlGetRoomList());
    }

    @Test
    public void testCanParseRoom() throws ParseException {
        controller.controlAddBuilding("A", "KY16 9BL");
        controller.controlAddRoom("1", "A");
        Building building = controller.parseBuilding("A");
        Room room = controller.parseRoom("1", building);
        assertEquals("1", room.getName());
    }

    @Test(expected = ParseException.class)
    public void testParseThrowsExceptionOnRoomNotFound() throws ParseException {
        controller.controlAddBuilding("A", "KY16 9BL");
        Building building = controller.parseBuilding("A");
        controller.parseRoom("1", building);
    }

    @Test
    public void testCanAddOneBooking() {
        controller.controlAddPerson("Anne", "anne@vealchurch.com");
        controller.controlAddBuilding("A", "ABC 123");
        controller.controlAddRoom("1", "A");
        String msg = controller.controlAddBooking("11/11/2022", "09:00", "12:00", "A", "1", "Anne");
        assertEquals("Booking added successfully!", msg);
        assertArrayEquals(new String[][] {
                new String[] { "Date", "Time", "Building", "Room", "Owner" },
                new String[] { "11/11/2022", "09:00 - 12:00", "A", "1", "Anne (anne@vealchurch.com)" }
        }, controller.controlGetBookings());
    }

    @Test
    public void testCanAddBookings() {
        controller.controlAddPerson("Anne", "anne@vealchurch.com");
        controller.controlAddBuilding("A", "ABC 123");
        controller.controlAddRoom("1", "A");
        controller.controlAddBooking("11/11/2022", "09:00", "12:00", "A", "1", "Anne");
        controller.controlAddBooking("11/11/2022", "12:00", "14:00", "A", "1", "Anne");
        assertArrayEquals(new String[][] {
                new String[] { "Date", "Time", "Building", "Room", "Owner" },
                new String[] { "11/11/2022", "09:00 - 12:00", "A", "1", "Anne (anne@vealchurch.com)" },
                new String[] { "11/11/2022", "12:00 - 14:00", "A", "1", "Anne (anne@vealchurch.com)" }
        }, controller.controlGetBookings());
    }

    @Test
    public void testCannotAddBookingAtIntersectingTimes() {
        controller.controlAddPerson("Anne", "anne@vealchurch.com");
        controller.controlAddPerson("George Michael", "georgemichael@bananastand.com");
        controller.controlAddBuilding("A", "ABC 123");
        controller.controlAddRoom("1", "A");
        controller.controlAddBooking("11/11/2022", "09:00", "12:00", "A", "1", "Anne");
        controller.controlAddBooking("11/11/2022", "09:00", "12:00", "A", "1", "George Michael");
        controller.controlAddBooking("11/11/2022", "09:30", "14:00", "A", "1", "George Michael");
        controller.controlAddBooking("11/11/2022", "06:00", "11:30", "A", "1", "George Michael");
        assertArrayEquals(new String[][] {
                new String[] { "Date", "Time", "Building", "Room", "Owner" },
                new String[] { "11/11/2022", "09:00 - 12:00", "A", "1", "Anne (anne@vealchurch.com)" }
        }, controller.controlGetBookings());
    }

    @Test
    public void testCanAddBookingAtIntersectingTimesInSeparateRooms() {
        controller.controlAddPerson("Anne", "anne@vealchurch.com");
        controller.controlAddPerson("George Michael", "georgemichael@bananastand.com");
        controller.controlAddBuilding("A", "ABC 123");
        controller.controlAddRoom("1", "A");
        controller.controlAddRoom("2", "A");
        controller.controlAddBooking("11/11/2022", "09:00", "12:00", "A", "1", "Anne");
        controller.controlAddBooking("11/11/2022", "09:00", "12:00", "A", "2", "George Michael");
        assertArrayEquals(new String[][] {
                new String[] { "Date", "Time", "Building", "Room", "Owner" },
                new String[] { "11/11/2022", "09:00 - 12:00", "A", "1", "Anne (anne@vealchurch.com)" },
                new String[] { "11/11/2022", "09:00 - 12:00", "A", "2",
                        "George Michael (georgemichael@bananastand.com)" }
        }, controller.controlGetBookings());
    }

    @Test
    public void testCanAddBookingAtIntersectingTimesOnDifferentDays() {
        controller.controlAddPerson("Anne", "anne@vealchurch.com");
        controller.controlAddPerson("George Michael", "georgemichael@bananastand.com");
        controller.controlAddBuilding("A", "ABC 123");
        controller.controlAddRoom("1", "A");
        controller.controlAddBooking("11/11/2022", "09:00", "12:00", "A", "1", "Anne");
        controller.controlAddBooking("12/11/2022", "09:00", "12:00", "A", "1", "George Michael");
        assertArrayEquals(new String[][] {
                new String[] { "Date", "Time", "Building", "Room", "Owner" },
                new String[] { "11/11/2022", "09:00 - 12:00", "A", "1", "Anne (anne@vealchurch.com)" },
                new String[] { "12/11/2022", "09:00 - 12:00", "A", "1",
                        "George Michael (georgemichael@bananastand.com)" }
        }, controller.controlGetBookings());
    }

    @Test
    public void testCanRemoveBooking() {
        controller.controlAddPerson("Anne", "anne@vealchurch.com");
        controller.controlAddBuilding("A", "ABC 123");
        controller.controlAddRoom("1", "A");
        controller.controlAddBooking("11/11/2022", "09:00", "12:00", "A", "1", "Anne");
        String msg = controller.controlAddBooking("11/11/2022", "12:00", "14:00", "A", "1", "Anne");
        assertEquals("Booking added successfully!", msg);
        assertArrayEquals(new String[][] {
                new String[] { "Date", "Time", "Building", "Room", "Owner" },
                new String[] { "11/11/2022", "09:00 - 12:00", "A", "1", "Anne (anne@vealchurch.com)" },
                new String[] { "11/11/2022", "12:00 - 14:00", "A", "1", "Anne (anne@vealchurch.com)" }
        }, controller.controlGetBookings());

        msg = controller.controlRemoveBooking("11/11/2022", "09:00", "12:00", "A", "1", "Anne");
        assertEquals("Booking removed successfully!", msg);
        assertArrayEquals(new String[][] {
                new String[] { "Date", "Time", "Building", "Room", "Owner" },
                new String[] { "11/11/2022", "12:00 - 14:00", "A", "1", "Anne (anne@vealchurch.com)" }
        }, controller.controlGetBookings());
    }

    @Test
    public void testCanParseBooking() throws ParseException {
        controller.controlAddPerson("Anne", "anne@vealchurch.com");
        controller.controlAddBuilding("A", "ABC 123");
        controller.controlAddRoom("1", "A");
        controller.controlAddBooking("11/11/2022", "09:00", "12:00", "A", "1", "Anne");

        Booking booking = controller.parseBooking("11/11/2022", "09:00", "12:00", "A", "1", "Anne");
        assertEquals("11/11/2022", booking.getDate().format(BookingSystemController.DATE_FORMATTER));
        assertEquals("09:00", booking.getStartTime().format(BookingSystemController.TIME_FORMATTER));
        assertEquals("12:00", booking.getEndTime().format(BookingSystemController.TIME_FORMATTER));
        assertEquals("1", booking.getVenue().getName());
        assertEquals("Anne", booking.getOwner().getName());
    }

    @Test(expected = ParseException.class)
    public void testParseThrowsExceptionOnBookingNotFound() throws ParseException {
        controller.controlAddPerson("Anne", "anne@vealchurch.com");
        controller.controlAddBuilding("A", "ABC 123");
        controller.controlAddRoom("1", "A");
        controller.controlAddBooking("11/11/2022", "09:00", "12:00", "A", "1", "Anne");

        controller.parseBooking("12/11/2022", "09:00", "12:00", "A", "1", "Anne");
    }

}
