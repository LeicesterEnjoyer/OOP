package oop.users;

import java.util.LinkedHashMap;

/**
 * The UserGenerator interface defines a method for generating User objects from data.
 */
public interface UserGenerator {
    /**
     * Generates a User object using the provided data.
     *
     * @param data  The LinkedHashMap containing user data.
     * @return      The User object generated from the provided data.
     */
    User getUser(LinkedHashMap<String, String> data);
}