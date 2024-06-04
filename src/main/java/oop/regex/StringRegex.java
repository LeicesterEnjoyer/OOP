package oop.regex;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

/**
 * Utility class for working with string regex patterns.
 */
public class StringRegex {
    /** 
     * Instance of RegexEnumMap to store pre-compiled regex patterns. 
     */
    public static final RegexEnumMap REGEX_PATTERNS = new RegexEnumMap();
    
    static {
        REGEX_PATTERNS.addRegexPattern(FormatRegexList.PERSONAL_NAME, "([A-Z][a-z]+)\\s+([A-Z][a-z]+)\\s+([A-Z][a-z]+)");
        REGEX_PATTERNS.addRegexPattern(FormatRegexList.EMAIL, "[A-Za-z0-9+_.-]+@[A-Za-z0-9+_.-]+");
        REGEX_PATTERNS.addRegexPattern(FormatRegexList.WHITE_SPACES, "\\s+");
    }

    /**
     * Finds all occurrences of personal names in the given data string.
     *
     * @param data                          The input string in which personal names are searched.
     * @return                              An ArrayList of HashMaps containing the extracted personal names. Each HashMap contains the keys: "FIRST_NAME", "SECOND_NAME", and "THIRD_NAME".
     * @throws InvalidParameterException    If the data is null.       
     */
    public static ArrayList<HashMap<String, String>> findAllPersonalNames(String data) {
        if (data == null)
            throw new InvalidParameterException("Invalid data!");

        ArrayList<HashMap<String, String>> personalNames = new ArrayList<>();
        Matcher matcher = REGEX_PATTERNS.get(FormatRegexList.PERSONAL_NAME).matcher(data);

        while (matcher.find() == true) {
            HashMap<String, String> fullName = new HashMap<>();
            
            fullName.put("FIRST_NAME", matcher.group(1));
            fullName.put("SECOND_NAME", matcher.group(2));
            fullName.put("THIRD_NAME", matcher.group(3));

            personalNames.add(fullName);
        }

        return personalNames;
    }

    /**
     * Removes extra white spaces from the given data string.
     *
     * @param data                          The input data string that can contain extra white spaces.
     * @return                              The input data string with extra white spaces removed.
     * @throws InvalidParameterException    If the data is null.
     */
    public static String clearWhiteSpaces(String data) {
        if (data == null)
            throw new InvalidParameterException("Invalid data!");

        return REGEX_PATTERNS.get(FormatRegexList.WHITE_SPACES).matcher(data).replaceAll(" ").trim();
    }

    /**
     * Finds all email addresses in the given data string.
     *
     * @param data                          The input string in which email addresses are searched.
     * @return                              An ArrayList of email addresses found in the input data string.
     * @throws InvalidParameterException    If the data is null.
     */
    public static ArrayList<String> findAllEmails(String data) {
        if (data == null) 
            throw new InvalidParameterException("Invalid data!");

        ArrayList<String> emails = new ArrayList<>();
        Matcher matcher = REGEX_PATTERNS.get(FormatRegexList.EMAIL).matcher(data);

        while (matcher.find() == true)
            emails.add(matcher.group());

        return emails;
    }
}