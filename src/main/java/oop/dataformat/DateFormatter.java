package oop.dataformat;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for formatting dates.
 */
public class DateFormatter {
    /** 
     * Instance of DateEnumMap to store date formats.
     */
    private static final DateEnumMap DATE_FORMATS = new DateEnumMap();

    /** 
     * The default date format.
     */
    private static final DateFormats DEFAULT_FORMAT = DateFormats.BG;

    static {
        DATE_FORMATS.addDateFormatter(DateFormats.BG, "dd.MM.yyyy");
        DATE_FORMATS.addDateFormatter(DateFormats.FR, "dd/MM/yyyy");
        DATE_FORMATS.addDateFormatter(DateFormats.MONTH_WITH_WORD, "dd MMM yyyy");
    }

    /**
     * Formats the given date according to the specified format.
     *
     * @param date          The LocalDate object to be formatted.
     * @param format        The desired format for the date.
     * @return              The formatted date as a string.
     */
    public static String formatDate(LocalDate date, DateFormats format) {
        if (date == null || format == null)
            throw new InvalidParameterException("Invalid date or format!");

        DateTimeFormatter formatter;
        if (DATE_FORMATS.containsKey(format) == true)
            formatter = DATE_FORMATS.get(format);
        else 
            formatter = DATE_FORMATS.get(DEFAULT_FORMAT);
        
        return date.format(formatter);
    }
}