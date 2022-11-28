package roombooking.main;

import roombooking.controller.BookingSystemController;
import roombooking.model.BookingSystemModel;
import roombooking.views.cli.BookingSystemCli;
import roombooking.views.gui.BookingSystemGui;

public class RoomBookingMain {
    /**
     * @param args
     */
    public static void main(String[] args) {
        // create Model
        BookingSystemModel model = new BookingSystemModel();

        // Create controller
        BookingSystemController controller = new BookingSystemController(model);

        // BookingSystemGui gui = new BookingSystemGui(model, controller);

        // BookingSystemCli cli = new BookingSystemCli(controller, model);
        BookingSystemCli cli = new BookingSystemCli(controller);
        cli.run();

    }
}