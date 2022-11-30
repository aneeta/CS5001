package roombooking.main;

import roombooking.controller.BookingSystemController;
import roombooking.model.BookingSystemModel;
import roombooking.views.cli.BookingSystemCli;
import roombooking.views.gui.BookingSystemGui;

public class RoomBookingMain {
    /**
     * Method to run the Room Booking System.
     * 
     * @param args command line args (not used)
     */
    public static void main(String[] args) {
        // create Model
        BookingSystemModel model = new BookingSystemModel();

        // Create controller
        BookingSystemController controller = new BookingSystemController(model);

        // Start up a Graphical Line Interface
        BookingSystemGui gui = new BookingSystemGui(model, controller);

        // Start up the Command Line Interface
        BookingSystemCli cli = new BookingSystemCli(controller);
        cli.run();

    }
}