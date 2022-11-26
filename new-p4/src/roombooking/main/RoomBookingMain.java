package roombooking.main;

import roombooking.controller.BookingSystemController;
import roombooking.model.BookingSystemModel;
import roombooking.views.cli.BookingSystemCli;

public class RoomBookingMain {
    /**
     * @param args
     */
    public static void main(String[] args) {
        // create Model
        BookingSystemModel model = new BookingSystemModel();

        // Create controller
        BookingSystemController controller = new BookingSystemController(model);

        // BookingSystemCli cli = new BookingSystemCli(controller, model);
        BookingSystemCli cli = new BookingSystemCli(controller);
        cli.run();

    }
}