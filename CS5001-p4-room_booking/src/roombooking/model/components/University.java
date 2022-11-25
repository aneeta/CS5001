package model.components;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class University implements Serializable {

    private Map<String, Building> buildings;
    private Map<String, Person> people;
    private String name;

    public University(String name) {
        this.buildings = new HashMap<>();
        this.people = new HashMap<>();
        this.name = name;

    }

    // public String[][] getBookings() {

    // }

    // public String[][] getBookings(
    // List<LocalDate> dates,
    // List<Building> buildings,
    // List<Room> rooms,
    // List<Person> owners) {
    // List<List<String>> data = new ArrayList<>();
    // for (String bName : buildings) {
    // this.buildings.get(bName).getBookings(dates, rooms, owners);
    // }

    // // TODO
    // return null;
    // }

    public List<Map<String, String>> getBookings(
            List<LocalDate> dates,
            List<String> buildings,
            List<String> rooms,
            List<Person> owners) {
        List<Map<String, String>> allData = new ArrayList<>();
        for (String bName : buildings) {
            List<Map<String, String>> data = this.buildings.get(bName).getBookings(dates, rooms, owners);
            allData.addAll(data);
        }
        return allData;
    }

    // @Override
    // public String toString() {
    // return
    // }

}
