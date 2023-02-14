package controller;

import java.io.IOException;

import javax.swing.Action;

import model.RoomBookingModel;
import view.cliview.RoomBookingCLI;
import view.guiview.RoomBookingView;

public class RoomBookingController {
    private RoomBookingModel model;

    private RoomBookingView viewGui;
    private RoomBookingCLI viewCli;

    private PropertyChangeSupport notifier; // tracks and notifies listeners

    public RoomBookingController(RoomBookingModel model) {
        this.model = model;
        this.viewGui = viewGui;
        this.viewCli = viewCli;
    }

    public void addListener(PropertyChangeListener listener) {
        notifier.addPropertyChangeListener(listener);
    }

    private void update() { // to call everytime something changes in the model so that the views can be
                            // updated
        notifier.firePropertyChange("model", lastModel, model); // update(varName, oldValue, newValue)
        lastModel = model; // reset for next change
    }

    public String controlSave(String saveName) {
        try {
            model.save(saveName);
            return String.format("System saved successfully to %s.", saveName);
        } catch (IOException err) {
            return String.format(
                    "%s could not be loaded due to the following:\n%s",
                    saveName, err.getMessage());
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

    public String controlAddPerson(String fullName, String email) {
        try {
            model.addPerson(fullName, email);
            return String.format("%s added successfully!", fullName);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    public String controlAddBuilding(String name, String address) {
        try {
            model.addBuilding(name, address);
            return String.format("%s added successfully!", name);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    public String controlAddRoom(String name, String building) {
        try {
            model.addRoom(name, building);
            return String.format("%s added successfully!", name);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    public String getInstitutionName() {
        return this.model.getInstitutionName();
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
