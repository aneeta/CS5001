package roombooking.model.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Building {

    private String name;
    private String address;
    private int id;

    /**
     * Construstor method for a class representing a Building.
     * 
     * @param name    building name
     * @param address building address
     * @param id      unique id
     */
    @JsonCreator
    public Building(@JsonProperty("name") String name, @JsonProperty("address") String address,
            @JsonProperty("id") int id) {
        this.name = name;
        this.address = address;
        this.id = id;
    }

    /**
     * Getter method for Building Name.
     * 
     * @return String
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter method for Building Address.
     * 
     * @return String
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Setter method for Building Name.
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter method for Building Address.
     * 
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Getter method for Building Id.
     * 
     * @return int
     */
    public int getId() {
        return this.id;
    }

}
