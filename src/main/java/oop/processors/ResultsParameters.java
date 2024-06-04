package oop.processors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * The ResultsParameters class represents parameters used for processing results.
 * It contains static HashMaps to store limits and process types.
 */
public class ResultsParameters {
    /**
     * A HashMap containing limits data.
     * Each entry maps a parameter key to a HashMap of Limits and their corresponding float values.
     */
    private static final HashMap<String, HashMap<Limits, Float>> LIMITS = new HashMap<>();

    /**
     * A HashMap containing process types data.
     * Each entry maps a parameter key to a ProcessTypes enum value.
     */
    private static final HashMap<String, ProcessTypes> PROCESS_TYPES = new HashMap<>();

    /**
     * Loads data from a file into the PROCESS_TYPES map.
     *
     * @param filePath The path to the file containing process types data.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public static void loadProcessTypesFromFile(String filePath) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.map(line -> line.split(","))
                 .skip(1)
                 .forEach(ResultsParameters::getProcessTypes);
        }
    }

    /**
     * Loads data from a file into the LIMITS map.
     *
     * @param filePath The path to the file containing limits data.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public static void loadLimitsFromFile(String filePath) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.map(line -> line.split(","))
                 .skip(1)
                 .forEach(ResultsParameters::addLimit);
        }
    }

    /**
     * Inserts a key-value pair into the PROCESS_TYPES map based on the input array.
     *
     * @param s     The array containing the key-value pair in the format [key, value].
     */
    private static void getProcessTypes(String[] s) {
        if (s.length != 2) 
            return;
        
        String key = s[0].trim();
        ProcessTypes value = ProcessTypes.valueOf(s[1].trim());
        PROCESS_TYPES.put(key, value);
    }

    /**
     * Adds a limit to the LIMITS map based on the input array.
     *
     * @param s     The array containing the limit data in the format [key, less_than, more_than].
     */
    private static void addLimit(String[] s) {
        if (s.length < 1 && s.length > 3) 
            return;

        String key = s[0].trim();
        HashMap<Limits, Float> limitMap = new HashMap<>();

        if (s.length >= 2 && !s[1].trim().isEmpty())
            limitMap.put(Limits.LESS_THAN, Float.parseFloat(s[1].trim()));
        if (s.length == 3 && !s[2].trim().isEmpty())
            limitMap.put(Limits.MORE_THAN, Float.parseFloat(s[2].trim()));

        LIMITS.put(key, limitMap);
    }

    /**
     * Returns the LIMITS attribute.
     *
     * @return  The HashMap containing limits data.
     */
    public static HashMap<String, HashMap<Limits, Float>> getLimits() {
        return LIMITS;
    }

    /**
     * Returns the PROCESS_TYPES attribute.
     *
     * @return  The HashMap containing process types data.
     */
    public static HashMap<String, ProcessTypes> getProcessTypes() {
        return PROCESS_TYPES;
    }
}