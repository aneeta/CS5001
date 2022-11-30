package roombooking.controller;

import roombooking.model.components.Booking;
import roombooking.model.components.Building;
import roombooking.model.components.Person;
import roombooking.model.components.Room;
import roombooking.model.components.exceptions.IllegalBookingException;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;

import javax.naming.NameNotFoundException;

import roombooking.model.BookingSystemModel;

public class BookingSystemController {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private BookingSystemModel model;

    public BookingSystemController(BookingSystemModel model) {
        this.model = model;
    }

    public String[][] controlGetPeople() {
        String[][] data = new String[model.getPeople().size() + 1][2];
        // add Headers
        data[0][0] = "Full Name";
        data[0][1] = "Email";
        // get data
        int i = 1;
        for (Person p : model.getPeople()) {
            data[i][0] = p.getName();
            data[i][1] = p.getEmail();
            i++;
        }
        return data;
    }

    public String[][] controlGetBookings(String[] dates, String[] rooms, String[] owners) {
        if (dates.length == 0 && rooms.length == 0 && owners.length == 0) {
            return controlGetBookings();
        }
        // Parse dates
        List<LocalDate> parsedDates = new ArrayList<>();
        for (int i = 0; i < dates.length; i++) {
            parsedDates.add(LocalDate.parse(dates[i], DATE_FORMATTER));
        }
        // Parse rooms
        List<Room> parsedRooms = model.getRooms().stream()
                .filter(r -> new ArrayList<String>(Arrays.asList(rooms)).contains(r.getName()))
                .collect(Collectors.toList());
        // Parse people
        List<Person> parsedPeople = model.getPeople().stream()
                .filter(p -> new ArrayList<>(Arrays.asList(owners)).contains(p.getName()))
                .collect(Collectors.toList());

        return controlParseBookings(model.getBookings(parsedDates, parsedRooms, parsedPeople));
    }

    public String[][] controlGetBookings() {
        return controlParseBookings(model.getBookings());
    }

    public String[][] controlParseBookings(List<Booking> bList) {

        String[][] data = new String[bList.size() + 1][5];
        // add Headers
        data[0][0] = "Date";
        data[0][1] = "Time";
        data[0][2] = "Building";
        data[0][3] = "Room";
        data[0][4] = "Owner";
        // get data
        int i = 1;
        for (Booking b : bList) {
            data[i][0] = b.getDate().format(DATE_FORMATTER);
            data[i][1] = String.format("%s - %s",
                    b.getStartTime().format(TIME_FORMATTER),
                    b.getEndTime().format(TIME_FORMATTER));
            data[i][2] = model.matchBuildingId(b.getVenue().getBuildingId()).getName();
            data[i][3] = b.getVenue().getName();
            data[i][4] = b.getOwner().toDisplay();
            i++;
        }
        return data;
    }

    public String controlGetInstitutionName() {
        return model.getName();
    }

    public String controlAddPerson(String name, String email) {
        try {
            model.addPerson(name, email);
            return String.format("%s added successfully!", name);
        } catch (Exception e) {
            // TODO
            return e.getMessage();
        }
    }

    public String controlAddBuilding(String buildingName, String address) {
        try {
            model.addBuilding(buildingName, address);
            return String.format("%s added successfully!", buildingName);
        } catch (Exception e) {
            // TODO
            return e.getMessage();
        }
    }

    public String controlSave(String in) {
        try {
            model.saveData(in);
            return String.format("Saved successfully to %s", in);
        } catch (Exception e) {
            // TODO: handle exception
            return e.getMessage();
        }
    }

    public String controlLoad(String in) {
        try {
            model.loadData(in);
            return String.format("Loaded successfully from %s", in);
        } catch (Exception e) {
            // TODO: handle exception
            return e.getMessage();
        }
    }

    public String controlRemovePerson(String name, String email) {
        try {
            Person target = parsePerson(name, email);
            model.removePerson(target);
            return "Person removed successfully!";
        } catch (Exception e) {
            // TODO: handle exception
            return e.getMessage();
        }
    }

    public String controlRemoveBuilding(String buildingName) {
        try {
            Building target = parseBuilding(buildingName);
            model.removeBuilding(target);
            return "Building removed successfully!";
        } catch (Exception e) {
            // TODO: handle exception
            return e.getMessage();
        }
    }

    public String controlRemoveBooking(String date, String sTime, String eTime, String building, String rName,
            String owner) {
        try {
            Booking targBooking = parseBooking(date, sTime, eTime, building, rName, owner);
            model.removeBooking(targBooking);
            return "Booking removed successfully!";
        } catch (Exception e) {
            // TODO: handle exception
            return e.getMessage();
        }
    }

    public String controlRemoveRoom(String roomId, String roomBuilding) {
        try {
            Building b = parseBuilding(roomBuilding);
            Room targRoom = parseRoom(roomId, b);
            model.removeRoom(targRoom);
            return "Room removed successfully!";
        } catch (Exception e) {
            // TODO: handle exception
            return e.getMessage();
        }
    }

    public String controlAddRoom(String roomId, String roomBuilding) {
        try {
            Building targetBuilding = parseBuilding(roomBuilding);
            model.addRoom(roomId, targetBuilding);
            return String.format("%s added successfully!", roomId);
        } catch (Exception e) {
            // TODO
            return e.getMessage();
        }
    }

    public String[][] controlGetRooms() {
        String[][] data = new String[model.getRooms().size() + 1][2];
        // add Headers
        data[0][0] = "Name";
        data[0][1] = "Building";
        // get data
        int i = 1;
        for (Room p : model.getRooms()) {
            data[i][0] = p.getName();
            data[i][1] = model.matchBuildingId(p.getBuildingId()).getName();
            i++;
        }
        return data;
    }

    public String controlAddBooking(String bDate, String sTime, String eTime, String bBuilding, String rName,
            String bOwner) {
        try {
            Person owner = parsePerson(bOwner);
            Building building = parseBuilding(bBuilding);
            Room room = parseRoom(rName, building);
            LocalDate date = LocalDate.parse(bDate, DATE_FORMATTER);
            LocalTime start = LocalTime.parse(sTime, TIME_FORMATTER);
            LocalTime end = LocalTime.parse(eTime, TIME_FORMATTER);
            model.addBooking(date, start, end, building, room, owner);
            return "Booking added successfully!";
        } catch (Exception e) {
            // TODO: handle exception
            return e.getMessage();
        }
    }

    public String[][] controlListBuildings() {
        String[][] data = new String[model.getBuildings().size() + 1][2];
        // add Headers
        data[0][0] = "Building Name";
        data[0][1] = "Address";
        // get data
        int i = 1;
        for (Building p : model.getBuildings()) {
            data[i][0] = p.getName();
            data[i][1] = p.getAddress();
            i++;
        }
        return data;
    }

    public Room parseRoom(String name, Building building) throws ParseException {
        List<Room> parsedRooms = model.getRooms().stream()
                .filter(r -> r.getName().equals(name) && r.getBuildingId() == building.getId())
                .collect(Collectors.toList());
        return (Room) interpretReturn(parsedRooms, "Room");
    }

    public Room parseRoom(String name) throws ParseException {
        List<Room> parsedRooms = model.getRooms().stream()
                .filter(r -> r.getName().equals(name))
                .collect(Collectors.toList());
        return (Room) interpretReturn(parsedRooms, "Room");
    }

    public Building parseBuilding(String name) throws ParseException {
        List<Building> parsedBuildings = model.getBuildings().stream()
                .filter(b -> b.getName().equals(name))
                .collect(Collectors.toList());
        return (Building) interpretReturn(parsedBuildings, "Building");
    }

    public Person parsePerson(String name) throws ParseException {
        List<Person> parsedPersons = model.getPeople().stream()
                .filter(p -> p.getName().equals(name))
                .collect(Collectors.toList());
        return (Person) interpretReturn(parsedPersons, "Person");
    }

    public Person parsePerson(String name, String email) throws ParseException {
        List<Person> parsedPersons = model.getPeople().stream()
                .filter(p -> p.getName().equals(name) && p.getEmail().equals(email))
                .collect(Collectors.toList());
        return (Person) interpretReturn(parsedPersons, "Person");
    }

    public Booking parseBooking(String bDate, String sTime, String eTime, String bBuilding, String rName, String bOwner)
            throws ParseException {
        Person owner = parsePerson(bOwner);
        Building building = parseBuilding(bBuilding);
        Room room = parseRoom(rName, building);
        LocalDate date = LocalDate.parse(bDate, DATE_FORMATTER);
        LocalTime start = LocalTime.parse(sTime, TIME_FORMATTER);
        LocalTime end = LocalTime.parse(eTime, TIME_FORMATTER);

        System.out.printf("%s %s %s %s %s\n", owner, room, date, start, end);
        System.out.printf("there are %d bookings\n", model.getBookings().size());
        for (Booking booking : model.getBookings()) {
            System.out.printf("%s %s %s %s %s\n", booking.getOwner(), booking.getVenue(), booking.getDate(),
                    booking.getStartTime(), booking.getEndTime());
        }
        List<Booking> parsedBookings = model.getBookings().stream()
                .filter(b -> b.getOwner().equals(owner) && b.getDate().equals(date) && b.getVenue().equals(room)
                        && b.getStartTime().equals(start) && b.getEndTime().equals(end))
                .collect(Collectors.toList());
        return (Booking) interpretReturn(parsedBookings, "Booking");
    }

    public Object interpretReturn(List parsedItem, String objDesc) throws ParseException {
        if (parsedItem.size() == 0) {
            throw new ParseException("%s not found!".formatted(objDesc), 0);
        } else if (parsedItem.size() > 1) {
            throw new ParseException("Multiple matching %s entries found!".formatted(objDesc), 0); // TODO
                                                                                                   // explain/change
                                                                                                   // arbitrary 0
        } else {
            return parsedItem.get(0);
        }
    }

    public String[] controlGetDateList() {
        return model.getBookings().stream().map(x -> x.getDate().format(DATE_FORMATTER)).toArray(String[]::new);
        // return new String[] { "12/03/2023", "11/11/2023" };
    }

    public String[] controlGetPersonList() {
        return model.getPeople().stream().map(x -> x.getName()).toArray(String[]::new);
    }

    public String[] controlGetBuildingList() {
        return model.getBuildings().stream().map(x -> x.getName()).toArray(String[]::new);
    }

    public String[] controlGetRoomList() {
        return model.getRooms().stream().map(x -> x.getName()).toArray(String[]::new);
    }

    public String[] controlGetRoomBuildingList() {
        return model.getRooms().stream().map(x -> x.getName() + " (" + model.matchBuildingId(x.getBuildingId()) + ")")
                .toArray(String[]::new);
    }

    public String[] controlGetRoomList(String builString) {
        List<Building> parsedBuild = model.getBuildings().stream()
                .filter(p -> p.getName().equals(builString))
                .collect(Collectors.toList());

        // List<Room> filteredRooms = model.getBookings(ownerObj);
        return model.getRooms().stream().filter(x -> parsedBuild.contains(model.matchBuildingId(x.getBuildingId())))
                .toArray(String[]::new);
    }

    public String[] controlGetEmailList(String ownerString) {
        List<Person> parsedPersons = model.getPeople().stream()
                .filter(p -> p.getName().equals(ownerString))
                .collect(Collectors.toList());

        // List<Room> filteredRooms = model.getBookings(ownerObj);
        return parsedPersons.stream().map(x -> x.getEmail()).toArray(String[]::new);
    }

    public void validateTime(String in) throws IllegalBookingException {
        try {
            LocalTime.parse(in, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalBookingException("Invalid Time Format: please use %s".formatted(TIME_FORMATTER));
        }

    }

    public void validateDate(String in) throws IllegalBookingException {
        try {
            LocalDate.parse(in, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalBookingException("Invalid Date Format: please use %s".formatted(DATE_FORMATTER));
        }
    }

}
