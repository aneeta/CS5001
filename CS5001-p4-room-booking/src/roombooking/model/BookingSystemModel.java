package roombooking.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import roombooking.model.components.Booking;
import roombooking.model.components.Building;
import roombooking.model.components.Person;
import roombooking.model.components.Room;
import roombooking.model.components.exceptions.IllegalBookingException;

public class BookingSystemModel {

    private ObjectMapper objectMapper;

    private final PropertyChangeSupport notifier;

    private String institutionName;
    private List<Building> buildings;
    private List<Room> rooms;
    private List<Person> people;
    private List<Booking> bookings;
    private Map<Integer, Building> buildingMapping;
    private Map<Integer, Room> roomMapping;
    private Map<Integer, Person> personMapping;

    private int id;

    /**
     * Construstor method for a Model class in the Room Booking System.
     */
    @JsonCreator
    public BookingSystemModel() {

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        institutionName = "[Unknown]";
        buildings = new ArrayList<>();
        rooms = new ArrayList<>();
        people = new ArrayList<>();
        bookings = new ArrayList<>();
        buildingMapping = new HashMap<>();
        roomMapping = new HashMap<>();
        personMapping = new HashMap<>();
        // used to track uniquness in case of non-unique names
        id = 0;

        this.notifier = new PropertyChangeSupport(this);

    }

    /** Register a listener so it will be notified of any changes. */
    public void addListener(PropertyChangeListener listener) {
        notifier.addPropertyChangeListener(listener);
    }

    /** Broadcast most recent change to all listeners */
    public void updateBuildings() {
        notifier.firePropertyChange("buildings", null, buildings);
        notifier.firePropertyChange("buildingMapping", null, buildingMapping);
    }

    public void updateRooms() {
        notifier.firePropertyChange("rooms", null, rooms);
        notifier.firePropertyChange("roomMapping", null, roomMapping);
    }

    public void updatePeople() {
        notifier.firePropertyChange("people", null, people);
        notifier.firePropertyChange("personMapping", null, personMapping);
    }

    public void updateBookings() {
        notifier.firePropertyChange("bookings", null, bookings);
    }

    public void updateName() {
        notifier.firePropertyChange("institutionName", null, institutionName);
    }

    /**
     * Setter method for Insistution (Model) Name.
     * 
     * @param name
     */
    public void setName(String name) {
        this.institutionName = name;
        updateName();
    }

    /**
     * Getter method for Insistution (Model) Name.
     * 
     * @return String
     */
    public String getName() {
        return this.institutionName;
    }

    /**
     * Method matching a Room based on its unique Id.
     * 
     * @param id
     * @return Room
     */
    public Room matchRoomId(int id) {
        return this.roomMapping.get(id);
    }

    /**
     * Method matching a Building based on its unique Id.
     * 
     * @param id
     * @return Building
     */
    public Building matchBuildingId(int id) {
        return this.buildingMapping.get(id);
    }

    /**
     * Method matching a Person based on its unique Id.
     * 
     * @param id
     * @return Person
     */
    public Person matchPersonId(int id) {
        return this.personMapping.get(id);
    }

    /**
     * Method to load the data from a save JSON file.
     * 
     * @param loadName absolute path to the file
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void loadData(String loadName) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(loadName);
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

        updateName();
        updateBuildings();
        updateRooms();
        updatePeople();
        updateBookings();
    }

    /**
     * Method to save the sessio data from a JSON file.
     * 
     * @param saveName absolute path to the save file
     * @throws IOException
     */
    public void saveData(String saveName) throws IOException {
        objectMapper.writeValue(new File(saveName), this);
    }

    /**
     * Method to add a new Person to the Booking System.
     * 
     * @param fullName Person's name
     * @param email    Person's email (has to be unique).
     */
    public void addPerson(String fullName, String email) {
        // check that the email has not been used before
        List<String> takenEmails = people.stream().map(x -> x.getEmail()).collect(Collectors.toList());
        if (takenEmails.contains(email)) {
            throw new IllegalArgumentException("Email already taken!");
        }
        Person newPerson = new Person(fullName, email, id);
        id++;
        this.people.add(newPerson);
        personMapping.put(newPerson.getId(), newPerson);
        updatePeople();
    }

    /**
     * Method to remove a Person from the Booking System.
     * 
     * @param person Person object
     */
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

    /**
     * Getter method for Bookings currently in the System.
     * 
     * @param p Person object
     * @return List<Booking>
     */
    public List<Booking> getBookings(Person p) {
        return bookings.stream()
                .filter(b -> b.getOwner().equals(p))
                .collect(Collectors.toList());
    }

    /**
     * Getter method for Bookings currently in the System.
     * 
     * @param r Room object
     * @return List<Booking>
     */
    public List<Booking> getBookings(Room r) {
        return bookings.stream()
                .filter(b -> b.getVenue().equals(r))
                .collect(Collectors.toList());
    }

    /**
     * Getter method for Bookings currently in the System.
     * 
     * @param d Date object
     * @return List<Booking>
     */
    public List<Booking> getBookings(LocalDate d) {
        return bookings.stream()
                .filter(b -> b.getDate().equals(d))
                .collect(Collectors.toList());
    }

    /**
     * Getter method for People currently in the System.
     * 
     * @return List<Person>
     */
    public List<Person> getPeople() {
        return this.people;
    }

    /**
     * Method to add a new Building to the Booking System.
     * 
     * @param name    Building name
     * @param address Building address
     */
    public void addBuilding(String name, String address) {
        // check if name is already used
        List<String> buildingNames = buildings.stream().map(x -> x.getName()).collect(Collectors.toList());
        if (buildingNames.contains(name)) {
            throw new IllegalArgumentException("Building name already taken!");
        }

        Building newBuilding = new Building(name, address, id);
        id++;
        this.buildings.add(newBuilding);
        this.buildingMapping.put(newBuilding.getId(), newBuilding);
        updateBuildings();
    }

    /**
     * Method to remove a Building from the Booking System.
     * 
     * @param building
     */
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

    /**
     * Getter method for Buildings currently in the System.
     * 
     * @return List<Building>
     */
    public List<Building> getBuildings() {
        return this.buildings;
    }

    /**
     * Method to add a new Room from the Booking System.
     * 
     * @param name     Room name
     * @param building Associated Building Object
     */
    public void addRoom(String name, Building building) {
        List<String> roomNames = getRooms(building).stream().map(x -> x.getName()).collect(Collectors.toList());
        if (roomNames.contains(name)) {
            throw new IllegalArgumentException("Room name already taken!");
        }
        Room r = new Room(name, building.getId(), id);
        id++;
        roomMapping.put(r.getId(), r);
        this.rooms.add(r);
        updateRooms();
    }

    /**
     * Method to remove a Room from the Booking System.
     * 
     * @param r Room object
     */
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

    /**
     * Getter method for Rooms currently in the System.
     * 
     * @return List<Room>
     */
    public List<Room> getRooms() {
        return this.rooms;
    }

    /**
     * Getter method for Rooms currently in the System.
     * 
     * @param b Building Object
     * @return List<Room>
     */
    public List<Room> getRooms(Building b) {
        return rooms.stream()
                .filter(r -> r.getBuildingId() == b.getId())
                .collect(Collectors.toList());
    }

    /**
     * @param date
     * @param start
     * @param end
     * @param building
     * @param room
     * @param owner
     * @throws IllegalBookingException
     */
    public void addBooking(LocalDate date, LocalTime start, LocalTime end, Building building, Room room, Person owner)
            throws IllegalBookingException {
        Booking newBooking = new Booking(date, start, end, owner, room);
        // Check that booking does not overlap with another
        List<Booking> roomBookings = this.bookings.stream().filter(x -> x.getVenue().equals(room))
                .collect(Collectors.toList());
        for (Booking b : roomBookings) {
            if (b.getDate().equals(newBooking.getDate())) {
                if ((b.getStartTime().compareTo(newBooking.getStartTime()) < 0
                        && newBooking.getStartTime().compareTo(b.getEndTime()) < 0)
                        ||
                        (newBooking.getStartTime().compareTo(b.getStartTime()) < 0
                                && b.getStartTime().compareTo(newBooking.getEndTime()) < 0)
                        ||
                        (newBooking.getStartTime().compareTo(b.getStartTime()) == 0
                                && newBooking.getEndTime().compareTo(b.getEndTime()) == 0)) {
                    throw new IllegalBookingException("Booking overlaps with existing booking!");
                }
            }
        }
        this.bookings.add(newBooking);
        updateBookings();
    }

    /**
     * Method to remove a Booking from the Booking System.
     * 
     * @param booking Booking object
     */
    public void removeBooking(Booking booking) {
        this.bookings.remove(booking);
        updateBookings();
    }

    /**
     * Getter method for all Bookings currently in the System.
     * 
     * @return List<Booking>
     */
    public List<Booking> getBookings() {
        return this.bookings;
    }

    /**
     * Getter method for Bookings currently in the System.
     * 
     * @param dates
     * @param rooms
     * @param owners
     * @return List<Booking>
     */
    public List<Booking> getBookings(List<LocalDate> dates, List<Room> rooms, List<Person> owners) {

        return this.bookings.stream()
                .filter(b -> dates.contains(b.getDate()) && rooms.contains(b.getVenue())
                        && owners.contains(b.getOwner()))
                .collect(Collectors.toList());
    }

    /**
     * Getter method for a dictionary of Buildings to their unique ids.
     * Used for matching objects.
     * 
     * @return Map<Integer, Building>
     */
    public Map<Integer, Building> getBuildingsMap() {
        return this.buildingMapping;
    }

    /**
     * Getter method for a dictionary of Persons to their unique ids.
     * Used for matching objects.
     * 
     * @return Map<Integer, Person>
     */
    public Map<Integer, Person> getPersonMap() {
        return this.personMapping;
    }

    /**
     * Getter method for a dictionary of Rooms to their unique ids.
     * Used for matching objects.
     * 
     * @return Map<Integer, Room>
     */
    public Map<Integer, Room> getRoomMap() {
        return this.roomMapping;
    }

    /**
     * Getter method for model's Id variable.
     * 
     * @return int
     */
    public int getId() {
        return this.id;
    }

}
