package tests;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import roombooking.controller.BookingSystemController;
import roombooking.model.BookingSystemModel;

public class ControllerTest {

    private BookingSystemModel model;
    private BookingSystemController controller;

    @Before
    public void setUp() {
        model = new BookingSystemModel();
        model.setName("Sample University");
        controller = new BookingSystemController(model);
    }

    @Test(expected = ParseException.class)
    public void testPeople() throws ParseException {
        controller.controlAddPerson("Ann", "ann@hotmail.com");
        controller.controlAddPerson("Larry", "larry@hotmail.com");
        assertArrayEquals(controller.controlGetPersonList(), new String[] { "Ann", "Larry" });

        assertEquals(model.getPeople().size(), controller.controlGetPersonList().length);
        String msg = controller.controlRemovePerson("Ann", "ann@hotmail.com");
        assertTrue(msg.equals("Person removed successfully!"));

        controller.controlAddPerson("Larry", "larry@hotmail.com");
        assertTrue(controller.controlGetPersonList().length == 2);
        // multiple matches -> error
        controller.parsePerson("Larry", "larry@hotmail.com");
    }

    @Test
    public void testBuildings() {
        controller.controlAddBuilding("A", "KY16 9BL");
        controller.controlAddBuilding("B", "KY16 7LW");
        assertArrayEquals(controller.controlGetBuildingList(), new String[] { "A", "B" });
        String msg = controller.controlRemoveBuilding("A");
        assertTrue(msg.equals("Building removed successfully!"));
    }

    @Test
    public void testRooms() {
        controller.controlAddRoom("1", "A");
        controller.controlAddRoom("123", "B");
        assertEquals(controller.controlGetRoomList(), 2);

        String msg = controller.controlRemoveRoom("123", "B");
        assertTrue(msg.equals("Room removed successfully!"));
    }
}
