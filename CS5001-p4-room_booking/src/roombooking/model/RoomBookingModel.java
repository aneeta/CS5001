package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import model.components.Building;
import model.components.Person;
import model.components.Room;
import model.components.RoomBooker;
import model.components.University;

public class RoomBookingModel {
    private static String DEFAULT_UNI_NAME = "University of St Andrews";

    private PropertyChangeSupport notifier; // tracks and notifies listeners
    private University university;

    public RoomBookingModel() {
        this.university = new University(DEFAULT_UNI_NAME);
        this.notifier = new PropertyChangeSupport(this);
    }

    public RoomBookingModel(Boolean load, String path) {
        this.notifier = new PropertyChangeSupport(this);
    }

    public void addListener(PropertyChangeListener listener) {
        notifier.addPropertyChangeListener(listener);
    }

    public String[][] getBookings() {
        // TODO
        return null;
    }

    public String[][] getBookings(
            List<LocalDate> dates,
            List<Building> buildings,
            List<Room> rooms,
            List<Person> owners) {
        // TODO
        return null;
    }

    
    private void update() {
        // notifier.firePropertyChange("", notifier, notifier);
    }

    public void save(String saveName) throws IOException {
        String savePath = getFormattedPath(saveName);
        FileOutputStream fos = new FileOutputStream(savePath);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(university);
        oos.close();
    }

    public void load(String loadName) throws IOException, ClassNotFoundException {
        String loadPath = getFormattedPath(loadName);
        FileInputStream fis = new FileInputStream(loadPath);
        ObjectInputStream ois = new ObjectInputStream(fis);
        University university = (University) ois.readObject();
        System.out.printf("Data loaded from %s", loadPath);
        this.university = university;
    }

    private String getFormattedPath(String path) {
        // how to make sure that pointing to right dir??
        // TODO
        // direct saved files into assets/data
        // assume we're in src
        return "roombooking/assets/data/" + path + ".ser";
    }

}
