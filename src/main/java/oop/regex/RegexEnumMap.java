package oop.regex;

import java.util.EnumMap;
import java.util.regex.Pattern;

/**
 * RegexEnumMap extends EnumMap to store regular expression patterns for different formats.
 * Each regular expression pattern is associated with a FormatRegexList enum value.
 */
public class RegexEnumMap extends EnumMap<FormatRegexList, Pattern> {
    /**
     * Constructs a new RegexEnumMap with the specified enum class.
     */
    public RegexEnumMap() {
        super(FormatRegexList.class);
    }

    /**
     * Adds a regular expression pattern to the map.
     * 
     * @param type    The FormatRegexList enum value to associate the pattern with.
     * @param format  The regular expression pattern to add.
     */
    public void addRegexPattern(FormatRegexList type, String format) {
        this.put(type, Pattern.compile(format));
    }
}