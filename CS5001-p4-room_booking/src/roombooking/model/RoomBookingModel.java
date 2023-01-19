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

// Model entry point
public class RoomBookingModel {
    private static String DEFAULT_UNI_NAME = "University of St Andrews";

    private University university;
    private University lastUniversity;

    public RoomBookingModel() {
        this.university = new University(DEFAULT_UNI_NAME);
        this.notifier = new PropertyChangeSupport(this);
    }

    public Object[] getPeople() {
        return university.getPeople();
    }

    public Object[] getBuildings() {
        return university.getBuildings();
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

    public void save(String saveName) throws IOException {
        // String savePath = getFormattedPath(saveName);
        FileOutputStream fos = new FileOutputStream(saveName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(university);
        oos.close();
    }

    public void load(String loadName) throws IOException, ClassNotFoundException {
        // String loadPath = getFormattedPath(loadName);
        FileInputStream fis = new FileInputStream(loadName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        University university = (University) ois.readObject();
        System.out.printf("Data loaded from %s", loadName);
        this.university = university;
    }

    // private String getFormattedPath(String path) {
    // // how to make sure that pointing to right dir??
    // // TODO
    // // direct saved files into assets/data
    // // assume we're in src
    // return "roombooking/assets/data/" + path + ".ser";
    // }

    public void addPerson(String name, String email) throws IllegalArgumentException {
        this.university.addPerson(name, email);
        update();
    }

    public void addRoom(String name, String building) throws IllegalArgumentException {
        this.university.addRoom(building, name);
        update();
    }

    public void addBuilding(String name, String address) throws IllegalArgumentException {
        this.university.addBuilding(name, address);
        update();
    }

    public String getInstitutionName() {
        return this.university.getName();
    }

}
