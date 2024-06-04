package oop.dataformat;

import java.time.format.DateTimeFormatter;
import java.util.EnumMap;

/**
 * DateEnumMap extends EnumMap to store DateTimeFormatter instances for different date formats.
 * Each DateTimeFormatter instance is associated with a DateFormats enum value.
 */
public class DateEnumMap extends EnumMap<DateFormats, DateTimeFormatter> {
    /**
     * Constructs a new DateEnumMap with the specified enum class.
     */
    public DateEnumMap() {
        super(DateFormats.class);
    }

    /**
     * Adds a DateTimeFormatter for a specific date format to the map.
     * 
     * @param type    The DateFormats enum value to associate the DateTimeFormatter with.
     * @param format  The pattern string used to create the DateTimeFormatter.
     */
    public void addDateFormatter(DateFormats type, String format) {
        this.put(type, DateTimeFormatter.ofPattern(format));
    }
}