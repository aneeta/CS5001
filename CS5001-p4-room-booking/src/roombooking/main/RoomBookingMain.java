package roombooking.main;

import java.util.concurrent.ForkJoinPool;

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

<<<<<<< HEAD:CS5001-p4-room-booking/src/roombooking/main/RoomBookingMain.java
        // GUI
        BookingSystemGui gui = new BookingSystemGui(model, controller);

        // CLI
=======
        // Start up a Graphical Line Interface
        BookingSystemGui gui = new BookingSystemGui(model, controller);

        // Start up the Command Line Interface
>>>>>>> refs/rewritten/fix-names-p4:new-p4/src/roombooking/main/RoomBookingMain.java
        BookingSystemCli cli = new BookingSystemCli(controller);
        cli.run();

        // cli.run();

    }
}