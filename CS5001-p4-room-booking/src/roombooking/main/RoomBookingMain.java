package roombooking.main;

import java.util.concurrent.ForkJoinPool;

import roombooking.controller.BookingSystemController;
import roombooking.model.BookingSystemModel;
import roombooking.views.cli.BookingSystemCli;
import roombooking.views.gui2.BookingSystemGui;

public class RoomBookingMain {
    /**
     * @param args
     */
    public static void main(String[] args) {
        // create Model
        BookingSystemModel model = new BookingSystemModel();

        // Create controller
        BookingSystemController controller = new BookingSystemController(model);

        // GUI
        BookingSystemGui gui = new BookingSystemGui(model, controller);

        // CLI
        BookingSystemCli cli = new BookingSystemCli(controller);
        cli.run();

        // cli.run();

    }
}