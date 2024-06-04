package oop.users;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * The UserWithEmail class represents a user entity with additional email and contact information.
 */
public final class UserWithEmail extends User{
    /**
     * Fields representing email and contact information.   
     */
    private final String email;
    private final String contactFirstName;
    private final String contactLastName;

    
    /**
     * Constructs a UserWithEmail object using the provided data.
     *
     * @param data  The LinkedHashMap containing user data with keys "code", "firstName", "lastName", "age", "gender", "email", "contactFirstName", and "contactLastName".
     */
    public UserWithEmail(LinkedHashMap<String, String> data) {
        super(data);

        email = data.get("email");
        contactFirstName = data.get("contactFirstName");
        contactLastName = data.get("contactLastName");
    }

    /**
     * Generates user information file.
     * Writes user details along with their results to a text file.
     */
    public void generateUserInfo() {
        try (FileWriter writer = new FileWriter("src/main/resources/output/emails/" + email + ".txt")) {
            writer.write(String.format("Dear %s %s\n", contactFirstName, contactLastName));
            writer.write(String.format("We are sending you the results of %s %s as requested:\n", firstName, lastName));

            for (String firstKey : results.keySet()) {
                HashMap<String, HashMap<String, String>> userResults = results.get(firstKey);

                for (String secondKey : userResults.keySet()) {
                    HashMap<String, String> resultMap = userResults.get(secondKey);
                    String value = String.format("%s: %s (%s)\n", secondKey, resultMap.get("VALUE"), resultMap.get("STATUS"));
                    writer.write(value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears user information and handles additional fields like contactFirstName, contactLastName, and email.
     * Calls User.clearUserInfo for the provided parameter.
     * Processes contactFirstName and contactLastName similarly to firstName and lastName in User class.
     * Handles email field by converting it to a valid format.
     *
     * @param userData  The LinkedHashMap containing user data.
     */
    public static void clearUserInfo(LinkedHashMap<String, String> userData) {
        User.clearUserInfo(userData);

        clearUserField(userData, "contactFirstName");
        clearUserField(userData, "contactLastName");
        clearEmail(userData);
    }
    
    /**
     * Clears the specified field in the provided user data.
     * If the field is missing or contains only whitespace, it is replaced with "MISSING CONTACT FIRST NAME" or "MISSING CONTACT LAST NAME".
     *
     * @param userData      The LinkedHashMap containing user data.
     * @param fieldName     The name of the field to clear ("contactFirstName" or "contactLastName").
     */
    private static void clearUserField(LinkedHashMap<String, String> userData, String fieldName) {
        userData.computeIfPresent(fieldName, (key, value) ->
                Optional.ofNullable(value)
                        .filter(str -> !str.trim().isEmpty())
                        .map(User::clearName)
                        .orElse("MISSING CONTACT " + (fieldName.equals("contactFirstName") ? "FIRST" : "LAST") + " NAME"));
    }

    /**
     * Clears and formats the email field in the provided user data.
     * If the email is missing or invalid, it is replaced with "support@oop.bg".
     *
     * @param userData  The LinkedHashMap containing user data.
     */
    private static void clearEmail(LinkedHashMap<String, String> userData) {
        userData.computeIfPresent("email", (key, value) ->
                Optional.ofNullable(value)
                        .filter(str -> !str.trim().isEmpty())
                        .map(String::toLowerCase)
                        .map(String::trim)
                        .orElse("support@oop.bg"));
    }

    /**
     * Returns a string representation of the UserWithEmail object.
     *
     * @return  The string containing user information including email and contact details.
     */
    @Override
    public String toString() {
        return super.toString() + " " + String.format("%s %s %s", contactFirstName, contactLastName, email);
    }
}