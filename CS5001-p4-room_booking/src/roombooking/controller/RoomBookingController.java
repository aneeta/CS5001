package controller;

import java.io.IOException;

import model.RoomBookingModel;

public class RoomBookingController {
    private RoomBookingModel model;

    public RoomBookingController(RoomBookingModel model) {
        this.model = model;
    }

    public void controlSave(String saveName) {
        try {
            model.save(saveName);
        } catch (IOException e) {
            // TODO
        }
    }

    public String controlLoad(String loadName) {
        try {
            model.load(loadName);
            return String.format("%s loaded successfully!", loadName);
        } catch (IOException | ClassNotFoundException err) {
            return String.format(
                    "%s could not be loaded due to the following:\n%s",
                    loadName, err.getMessage());
        }
    }

    public String controlAddNewPerson(String fullName, String email) {
        // try {
        // model.
        // }
        return null;
    }

    // public void controlClear() {
    // model.reset();
    // }

    // public void controlSubtract(String s) {
    // try {
    // model.subtract(Double.parseDouble(s));
    // } catch (NumberFormatException e) {
    // }
    // }

    // public void controlAdd(String s) {
    // try {
    // model.add(Double.parseDouble(s));
    // } catch (NumberFormatException e) {
    // }
    // }

    // public void controlMultiply(String s) {
    // try {
    // model.multiply(Double.parseDouble(s));
    // } catch (NumberFormatException e) {
    // }
    // }

    // public void controlDivide(String s) {
    // try {
    // model.divide(Double.parseDouble(s));
    // } catch (NumberFormatException e) {
    // }
    // }
}
