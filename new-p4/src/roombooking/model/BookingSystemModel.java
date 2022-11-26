package roombooking.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;

import roombooking.model.components.Booking;
import roombooking.model.components.Building;
import roombooking.model.components.Person;
import roombooking.model.components.Room;
import roombooking.model.components.exceptions.IllegalBookingException;

public class BookingSystemModel implements Serializable {

    private ObjectMapper objectMapper;

    private PropertyChangeSupport notifier;

    private String institutionName;
    private List<Building> buildings;
    private List<Room> rooms;
    private List<Person> people;
    private List<Booking> bookings;

    private String institutionNamePrev;
    private List<Building> buildingsPrev;
    private List<Room> roomsPrev;
    private List<Person> peoplePrev;
    private List<Booking> bookingsPrev;

    @JsonCreator
    public BookingSystemModel() {
        this.notifier = new PropertyChangeSupport(this);

        objectMapper = new ObjectMapper();

        institutionName = "[Unknown]";
        buildings = new ArrayList<>();
        rooms = new ArrayList<>();
        people = new ArrayList<>();
        bookings = new ArrayList<>();
        institutionNamePrev = "[Unknown]";
        buildingsPrev = new ArrayList<>();
        roomsPrev = new ArrayList<>();
        peoplePrev = new ArrayList<>();
        bookingsPrev = new ArrayList<>();
    }

    /** Register a listener so it will be notified of any changes. */
    public void addListener(PropertyChangeListener listener) {
        notifier.addPropertyChangeListener(listener);
    }

    /** Broadcast most recent change to all listeners */
    public void updateBuildings() {
        notifier.firePropertyChange("buildings", buildingsPrev, buildings);
        buildingsPrev = buildings;
    }

    public void updateRooms() {
        notifier.firePropertyChange("rooms", roomsPrev, rooms);
        roomsPrev = rooms;
    }

    public void updatePeople() {
        notifier.firePropertyChange("people", peoplePrev, people);
        peoplePrev = people;
    }

    public void updateBookings() {
        notifier.firePropertyChange("bookings", bookingsPrev, bookings);
        bookingsPrev = bookings;
    }

    public void updateName() {
        notifier.firePropertyChange("institutionName", institutionNamePrev, institutionName);
        institutionNamePrev = institutionName;
    }

    public void setName(String name) {
        this.institutionName = name;
        updateName();
    }

    public String getName() {
        return this.institutionName;
    }

    public void loadData(String loadName) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(loadName);
        // ObjectInputStream ois = new ObjectInputStream(fis);
        // ois.close();
        BookingSystemModel loadedModel = objectMapper.readValue(fis, BookingSystemModel.class);

        institutionName = loadedModel.getName();
        buildings = loadedModel.getBuildings();
        rooms = loadedModel.getRooms();
        people = loadedModel.getPeople();
        bookings = loadedModel.getBookings();

        // TODO
        updateName();
        updateBuildings();
        updateRooms();
        updatePeople();
        updateBookings();

    }

    public void saveData(String saveName) throws IOException {
        objectMapper.writeValue(new File(saveName), this);
        // FileOutputStream fos = new FileOutputStream(saveName);
        // ObjectOutputStream oos = new ObjectOutputStream(fos);
        // oos.writeObject(this);
        // oos.close();
    }

    public void addPerson(String fullName, String email) {
        this.people.add(new Person(fullName, email));
        updatePeople();
    }

    public void removePerson(Person person) {
        this.people.remove(person);
        updatePeople();
        // remove all bookings held by that person
        for (Booking b : person.getBookings()) {
            this.bookings.remove(b);
            updateBookings();
        }
    }

    public List<Person> getPeople() {
        return this.people;
    }

    public void addBuilding(String name, String address) {
        this.buildings.add(new Building(name, address));
        updateBuildings();
    }

    public void removeBuilding(Building building) {
        this.buildings.remove(building);
        updateBuildings();
        // remove rooms in that building
        for (Room r : building.getRooms()) {
            this.rooms.remove(r);
            // remove bookings tied to these rooms
            bookings = this.bookings.stream()
                    .filter(b -> b.getVenue() != r)
                    .collect(Collectors.toList());
        }
        updateRooms();
        updateBookings();
    }

    public List<Building> getBuildings() {
        return this.buildings;
    }

    public void addRoom(String name, Building building) {
        Room r = new Room(name, building);
        building.addRoom(r);
        updateBuildings();
        this.rooms.add(r);
        updateRooms();
    }

    public void removeRoom(Room r) {
        this.rooms.remove(r);
        // remove bookings tied to this room
        bookings = this.bookings.stream()
                .filter(b -> b.getVenue() != r)
                .collect(Collectors.toList());
        // remove room from its building
        r.getBuilding().removeRoom(r);

        updateRooms();
        updateBookings();
    }

    public List<Room> getRooms() {
        return this.rooms;
    }

    public List<Room> getRooms(Building building) {
        return building.getRooms();
    }

    public void addBooking(LocalDate date, LocalTime start, LocalTime end, Building building, Room room, Person owner)
            throws IllegalBookingException {
        Booking newBooking = new Booking(date, start, end, owner, room);
        int idx = this.people.indexOf(owner);
        this.people.get(idx).addBooking(newBooking);
        this.bookings.add(newBooking);
        updateBookings();
        updatePeople();
    }

    public void removeBooking(Booking booking) {
        this.bookings.remove(booking);
        int idx = this.people.indexOf(booking.getOwner());
        this.people.get(idx).removeBooking(booking);
        updateBookings();
        updatePeople();
    }

    public List<Booking> getBookings() {
        return this.bookings;
    }

    public List<Booking> getBookings(List<LocalDate> dates, List<Room> rooms, List<Person> owners) {

        return this.bookings.stream()
                .filter(b -> dates.contains(b.getDate()) && rooms.contains(b.getVenue())
                        && owners.contains(b.getOwner()))
                .collect(Collectors.toList());
    }

}
