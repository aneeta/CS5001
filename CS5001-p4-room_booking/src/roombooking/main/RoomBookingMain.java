package main;

import controller.RoomBookingController;

import model.RoomBookingModel;
import view.cliview.RoomBookingCLI;
import view.guiview.RoomBookingView;

public class RoomBookingMain {
    public static void main(String[] args) {
        // create Model
        RoomBookingModel model = new RoomBookingModel();

        // Create controller
        RoomBookingController controller = new RoomBookingController(model);

        // RoomBookingView gui = new RoomBookingView(model, controller);

        RoomBookingCLI cli = new RoomBookingCLI();

    }
}