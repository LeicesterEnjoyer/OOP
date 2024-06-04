package oop.users;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import oop.dataformat.NumberFormatter;
import oop.files.CsvParser;
import oop.processors.Limits;
import oop.processors.ProcessTypes;
import oop.processors.RangeStatuses;
import oop.processors.RangeValidator;
import oop.processors.ValueProcessor;

/**
 * The User class represents a user entity with basic information.
 */
public sealed class User permits UserWithEmail{
    /**
     * Fields representing user information.
     */
    protected final String code;
    protected final String firstName;
    protected final String lastName;
    protected final int age;
    protected final Gender gender;
    protected final HashMap<String,  HashMap<String, HashMap<String, String>>> results = new HashMap<>();

    /** 
     * Constructs a User object using the provided data.
     *
     * @param data      The LinkedHashMap containing user data with keys "code", "firstName", "lastName", "age", and "gender".
     */
    public User(LinkedHashMap<String, String> data) {
        this.code = Optional.ofNullable(data.get("code")).orElse("");
        this.firstName = Optional.ofNullable(data.get("firstName")).orElse("");
        this.lastName = Optional.ofNullable(data.get("lastName")).orElse("");
        this.age = Optional.ofNullable(data.get("age"))
                           .map(String::trim)
                           .map(ageStr -> {
                                try {
                                    return Math.max(Integer.parseInt(ageStr), 0);
                                } catch (NumberFormatException e) {
                                    return 20;
                                }
                            })
                           .orElse(20); 
        this.gender = Optional.ofNullable(data.get("gender"))
                              .map(String::trim)
                              .filter(g -> !g.isEmpty())
                              .map(g -> g.contains("FEMALE") ? Gender.FEMALE : Gender.MALE)
                              .orElse(Gender.MALE);
    }

    /**
     * Generates user information file.
     * Writes user details along with their results to a text file.
     */
    public void generateUserInfo() {
        try (FileWriter writer = new FileWriter(String.format("src/main/resources/output/users/%s_%s_%s.txt", code, firstName, lastName))) {
            writer.write(String.format("%s %s, %d, %s\n", firstName, lastName, age, gender));

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
     * Sets results for a specific result type and value.
     * Processes the value based on the specified process method and limits.
     * Validates the value against the specified limits and determines the range status.
     *
     * @param resultType    The type of the result.
     * @param value         The value of the result.
     * @return              The hashmap containing the result type, formatted value, and status, or null if data is missing.
     */
    public HashMap<String, HashMap<String, String>> setResults(String resultType, Float value) {
        HashMap<String, HashMap<Limits, Float>> limits = getLimits("src/main/resources/laboratoryresults/limits.csv");
        HashMap<String, ProcessTypes> processMethods = getProcessMethods("src/main/resources/laboratoryresults/process_methods.csv");

        String formattedValue;
        try {
            formattedValue = NumberFormatter.formatFloat(ValueProcessor.processData(value, processMethods.get(resultType), limits.get(resultType)), 4);
        } catch (NullPointerException e) {
            return null;
        }
        RangeStatuses rangeStatus;
        try {
            rangeStatus = RangeValidator.validateRange(value, limits.get(resultType));
        } catch (NullPointerException e) {
            return null;
        }

        HashMap<String, String> resultMap = new HashMap<>();
        resultMap.put("VALUE", formattedValue);
        resultMap.put("STATUS", getRangeStatusString(rangeStatus));

        return new HashMap<>(Map.of(resultType, resultMap));
    }

    /**
     * Converts a RangeStatuses enum value to its corresponding string representation.
     *
     * @param rangeStatus   The RangeStatuses enum value.
     * @return              The string representation of the range status.
     */
    private String getRangeStatusString(RangeStatuses rangeStatus) {
        switch (rangeStatus) {
            case NORMAL:
                return "normal";
            case HIGHER:
                return "higher than normal";
            case LOWER:
                return "lower than normal";
            default:
                return "unknown";
        }
    }

    /**
     * Parses the limits data from a CSV file and returns it as a hashmap.
     * Constructs a hashmap with result types as keys and corresponding limits as values.
     *
     * @param filename  The path of the CSV file containing limits data.
     * @return          The hashmap containing result types and their corresponding limits.
     */
    private HashMap<String, HashMap<Limits, Float>> getLimits(String filename) {
        HashMap<String, HashMap<Limits, Float>> limitsMap = new HashMap<>();
        CsvParser csvParser = new CsvParser(filename, ",");

        for (LinkedHashMap<String, String> data : csvParser.fileData) {
            String type = Optional.ofNullable(data.get("type"))
                    .map(String::strip)
                    .map(String::toUpperCase)
                    .orElse("UNKNOWN");
            Float moreThan = Optional.ofNullable(data.get("more_than"))
                                     .filter(s -> !s.isEmpty())
                                     .map(Float::parseFloat)
                                     .orElse(null);
            Float lessThan = Optional.ofNullable(data.get("less_than"))
                                     .filter(s -> !s.isEmpty())
                                     .map(Float::parseFloat)
                                     .orElse(null);

            HashMap<Limits, Float> limits = new HashMap<>();
            if (moreThan != null)
                limits.put(Limits.MORE_THAN, moreThan);
            if (lessThan != null)
                limits.put(Limits.LESS_THAN, lessThan);

            limitsMap.put(type, limits);
        }
        return limitsMap;
    }

    /**
     * Parses the process methods data from a CSV file and returns it as a hashmap.
     * Constructs a hashmap with result types as keys and corresponding process types as values.
     *
     * @param filename  The path of the CSV file containing process methods data.
     * @return          The hashmap containing result types and their corresponding process types.
     */
    private static HashMap<String, ProcessTypes> getProcessMethods(String filename) {
        HashMap<String, ProcessTypes> processMethodsMap = new HashMap<>();
        CsvParser csvParser = new CsvParser(filename, ",");

        for (LinkedHashMap<String, String> data : csvParser.fileData) {
            String type = Optional.ofNullable(data.get("type"))
                                  .map(String::strip)
                                  .map(String::toUpperCase)
                                  .orElse("UNKNOWN");
            ProcessTypes processType = ProcessTypes.valueOf(data.getOrDefault("process_method", "NONE").toUpperCase());

            processMethodsMap.put(type, processType);
        }

        return processMethodsMap;
    }

    /**
     * Generates a unique user string by concatenating the first name and last name with a "|".
     *
     * @param userData  The LinkedHashMap containing user data with keys "firstName" and "lastName".
     * @return          The string representing the concatenation of the first name and last name separated by "|".
     */
    public static String uniqueUserStr(LinkedHashMap<String, String> userData) {
        String firstName = userData.getOrDefault("firstName", "");
        String lastName = userData.getOrDefault("lastName", "");
        return firstName + "|" + lastName;
    }

    /**
     * Clears the provided name by trimming whitespace and capitalizing the first letter.
     *
     * @param name  The name to be cleared.
     * @return      The cleared name with the first letter capitalized.
     */
    protected static String clearName(String name) {
        String trimmedName = name.trim();
    
        if (trimmedName.isEmpty())
            return trimmedName;
        
        String formattedName = trimmedName.substring(0, 1).toUpperCase() +
                                trimmedName.substring(1).toLowerCase();
        
        return formattedName;
    }

    /**
     * Clears the first name and last name fields in the given user data.
     * If either field is missing or contains only whitespace, it is replaced with "MISSING FIRST NAME" or "MISSING LAST NAME", respectively.
     *
     * @param userData      The LinkedHashMap containing user data with keys "firstName" and "lastName".
     */
    public static void clearUserInfo(LinkedHashMap<String, String> userData) {
        clearUserField(userData, "firstName");
        clearUserField(userData, "lastName");
    }

     /**
     * Clears the specified field in the provided user data.
     * If the field is missing or contains only whitespace, it is replaced with "MISSING FIRST NAME" or "MISSING LAST NAME".
     *
     * @param userData      The LinkedHashMap containing user data.
     * @param fieldName     The name of the field to clear ("firstName" or "lastName").
     */
    private static void clearUserField(LinkedHashMap<String, String> userData, String fieldName) {
        userData.computeIfPresent(fieldName, (key, value) ->
                Optional.ofNullable(value)
                        .filter(str -> !str.trim().isEmpty())
                        .map(User::clearName)
                        .orElse("MISSING " + (fieldName.equals("firstName") ? "FIRST" : "LAST") + " NAME"));
    }

    /**
     * Returns a string representation of the User object.
     *
     * @return  The string containing user information.
     */
    @Override
    public String toString() {
        return String.format("%s %s %s, age %d, %s", code, firstName, lastName, age, gender);
    }
}