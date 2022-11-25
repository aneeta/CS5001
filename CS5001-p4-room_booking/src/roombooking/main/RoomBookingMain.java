package main;

import controller.RoomBookingController;

import model.RoomBookingModel;

import view.guiview.RoomBookingView;

public class RoomBookingMain {
    public static void main(String[] args) {
        // create Model
        RoomBookingModel model = new RoomBookingModel();

        // Create controller
        RoomBookingController controller = new RoomBookingController(model);

        if (args[0].toLowerCase().equals("gui")) {

            // Create View (GUI)
            new RoomBookingView(model, controller);

        }

    }
}