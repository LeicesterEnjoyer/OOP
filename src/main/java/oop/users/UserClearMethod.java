package oop.users;

import java.util.LinkedHashMap;

/**
 * The UserClearMethod interface represents a method for clearing user information.
 * Implementing classes must provide a way to process and clear user data stored in a LinkedHashMap.
 */
public interface UserClearMethod {
    /**
     * Clears user information stored in the provided LinkedHashMap.
     * Implementing classes define the specific logic for clearing user data.
     *
     * @param data  The LinkedHashMap containing user data to be cleared.
     */
    void getUserClearMethod(LinkedHashMap<String, String> data);
}