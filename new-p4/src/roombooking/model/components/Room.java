package roombooking.model.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Room {

    private int buildingId;
    private String name;
    private int id;

    /**
     * Construstor method for a class representing a Room.
     * 
     * @param name       Room name
     * @param buildingId Id of a building a room is located in
     * @param id         Unique id
     */
    @JsonCreator
    public Room(@JsonProperty("name") String name, @JsonProperty("buildingId") int buildingId,
            @JsonProperty("id") int id) {
        this.buildingId = buildingId;
        this.name = name;
        this.id = id;

    }

    /**
     * Getter method for Room's Building Id.
     * 
     * @return int
     */
    public int getBuildingId() {
        return buildingId;
    }

    /**
     * Getter method for Room Name.
     * 
     * @return String
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter method for Room Id.
     * 
     * @return int
     */
    public int getId() {
        return this.id;
    }
}
