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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
    private Map<Integer, Building> buildingMapping;
    private Map<Integer, Room> roomMapping;
    private Map<Integer, Person> personMapping;

    private String institutionNamePrev;
    private List<Building> buildingsPrev;
    private List<Room> roomsPrev;
    private List<Person> peoplePrev;
    private List<Booking> bookingsPrev;
    private Map<Integer, Building> buildingMappingPrev;
    private Map<Integer, Room> roomMappingPrev;
    private Map<Integer, Person> personMappingPrev;

    private int id;

    @JsonCreator
    public BookingSystemModel() {
        this.notifier = new PropertyChangeSupport(this);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        // objectMapper.findAndRegisterModules();

        institutionName = "[Unknown]";
        buildings = new ArrayList<>();
        rooms = new ArrayList<>();
        people = new ArrayList<>();
        bookings = new ArrayList<>();
        buildingMapping = new HashMap<>();
        roomMapping = new HashMap<>();
        personMapping = new HashMap<>();
        institutionNamePrev = "[Unknown]";
        buildingsPrev = new ArrayList<>();
        roomsPrev = new ArrayList<>();
        peoplePrev = new ArrayList<>();
        bookingsPrev = new ArrayList<>();
        buildingMappingPrev = new HashMap<>();
        roomMappingPrev = new HashMap<>();
        personMappingPrev = new HashMap<>();
        id = 0;
    }

    /** Register a listener so it will be notified of any changes. */
    public void addListener(PropertyChangeListener listener) {
        notifier.addPropertyChangeListener(listener);
    }

    /** Broadcast most recent change to all listeners */
    public void updateBuildings() {
        notifier.firePropertyChange("buildings", buildingsPrev, buildings);
        buildingsPrev = buildings;
        notifier.firePropertyChange("buildingMapping", buildingMappingPrev, buildingMapping);
        buildingMappingPrev = buildingMapping;

    }

    public void updateRooms() {
        notifier.firePropertyChange("rooms", roomsPrev, rooms);
        roomsPrev = rooms;
        notifier.firePropertyChange("roomMapping", roomMappingPrev, roomMapping);
        roomMappingPrev = roomMapping;
    }

    public void updatePeople() {
        notifier.firePropertyChange("people", peoplePrev, people);
        peoplePrev = people;
        notifier.firePropertyChange("personMapping", personMappingPrev, personMapping);
        personMappingPrev = personMapping;
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

    public Room matchRoomId(int id) {
        return this.roomMapping.get(id);
    }

    public Building matchBuildingId(int id) {
        return this.buildingMapping.get(id);
    }

    public Person matchPersonId(int id) {
        return this.personMapping.get(id);
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
        buildingMapping = loadedModel.getBuildingsMap();
        personMapping = loadedModel.getPersonMap();
        roomMapping = loadedModel.getRoomMap();
        id = loadedModel.getId();

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
        Person newPerson = new Person(fullName, email, id);
        id++;
        this.people.add(newPerson);
        personMapping.put(newPerson.getId(), newPerson);
        updatePeople();
    }

    public void removePerson(Person person) {
        this.people.remove(person);
        this.personMapping.remove(person.getId());
        updatePeople();
        // remove all bookings held by that person
        for (Booking b : getBookings(person)) {
            this.bookings.remove(b);
            updateBookings();
        }
    }

    public List<Booking> getBookings(Person p) {
        return bookings.stream()
                .filter(b -> b.getOwner().equals(p))
                .collect(Collectors.toList());
    }

    public List<Booking> getBookings(Room r) {
        return bookings.stream()
                .filter(b -> b.getVenue().equals(r))
                .collect(Collectors.toList());
    }

    public List<Booking> getBookings(LocalDate d) {
        return bookings.stream()
                .filter(b -> b.getDate().equals(d))
                .collect(Collectors.toList());
    }

    public List<Person> getPeople() {
        return this.people;
    }

    public void addBuilding(String name, String address) {
        Building newBuilding = new Building(name, address, id);
        id++;
        this.buildings.add(newBuilding);
        this.buildingMapping.put(newBuilding.getId(), newBuilding);
        updateBuildings();
    }

    public void removeBuilding(Building building) {
        this.buildings.remove(building);
        this.buildingMapping.remove(building.getId());
        updateBuildings();
        // remove rooms in that building
        for (Room r : getRooms(building)) {
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
        Room r = new Room(name, building.getId(), id);
        id++;
        roomMapping.put(r.getId(), r);
        this.rooms.add(r);
        updateRooms();
    }

    public void removeRoom(Room r) {
        this.rooms.remove(r);
        this.roomMapping.remove(r.getId());
        // remove bookings tied to this room
        bookings = this.bookings.stream()
                .filter(b -> b.getVenue() != r)
                .collect(Collectors.toList());

        updateRooms();
        updateBookings();
    }

    public List<Room> getRooms() {
        return this.rooms;
    }

    public List<Room> getRooms(Building b) {
        return rooms.stream()
                .filter(r -> r.getBuildingId() == b.getId())
                .collect(Collectors.toList());
    }

    public void addBooking(LocalDate date, LocalTime start, LocalTime end, Building building, Room room, Person owner)
            throws IllegalBookingException {
        Booking newBooking = new Booking(date, start, end, owner, room);
        // Check that booking does not overlap with another
        for (Booking b : this.bookings) {
            if (b.getDate().equals(newBooking.getDate())) {
                int overlap = newBooking.getEndTime().compareTo(b.getStartTime());
                if (overlap > 0) {
                    throw new IllegalBookingException("Booking overlaps with existing booking!");
                }
            }
        }
        // int idx = this.people.indexOf(owner);
        // this.people.get(idx).addBooking(newBooking);
        this.bookings.add(newBooking);
        updateBookings();
        // updatePeople();
    }

    public void removeBooking(Booking booking) {
        this.bookings.remove(booking);
        // int idx = this.people.indexOf(booking.getOwner());
        // this.people.get(idx).removeBooking(booking);
        updateBookings();
        // updatePeople();
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

    public Map<Integer, Building> getBuildingsMap() {
        return this.buildingMapping;
    }

    public Map<Integer, Person> getPersonMap() {
        return this.personMapping;
    }

    public Map<Integer, Room> getRoomMap() {
        return this.roomMapping;
    }

    public int getId() {
        return this.id;
    }

}

    

    

    