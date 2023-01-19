package roombooking.model.components;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Person {

    private String fullName;
    private String email;
    private int id;

    /**
     * Construstor method for a class representing a Person.
     * 
     * @param name  Person's name
     * @param email Person's email address
     * @param id    Unique id
     */
    @JsonCreator
    public Person(@JsonProperty("name") String name, @JsonProperty("email") String email, @JsonProperty("id") int id)
            throws IllegalArgumentException {
        validateEmail(email);
        this.fullName = name;
        this.email = email.toLowerCase();
        this.id = id;
    }

    /**
     * Method that checks that an email address is valid.
     * Checks for <username>@<domain> pattern.
     * 
     * @param str String (email) to validate
     * @throws IllegalArgumentException
     */
    public void validateEmail(String str) throws IllegalArgumentException {
        Pattern emailPattern = Pattern.compile("(\\w\\.?)+@([\\w-]+\\.)+[a-zA-z]+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPattern.matcher(str);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid email!");
        }
    }

    /**
     * Getter method for Person Name.
     * 
     * @return String
     */
    public String getName() {
        return this.fullName;
    }

    /**
     * Getter method for Person Id.
     * 
     * @return String
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Method to display Persons name and email in one string.
     * 
     * @return String
     */
    public String toDisplay() {
        return getName() + " (" + getEmail() + ")";
    }

    /**
     * Getter method for Person Id.
     * 
     * @return int
     */
    public int getId() {
        return this.id;
    }

}