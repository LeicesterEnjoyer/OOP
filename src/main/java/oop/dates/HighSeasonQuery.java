package oop.dates;

import java.time.temporal.TemporalAccessor;

/**
 * An abstract class representing a query to determine whether a given date falls within the high season.
 */
abstract sealed class HighSeasonQuery permits SummerHighQuery, WinterHighQuery {
    /** 
     * The starting month of the high season. 
     */
    protected int fromMonth;
    /** 
     * The ending month of the high season. 
     */
    protected int toMonth;

    /**
     * Checks whether the given temporal accessor falls within the high season.
     *
     * @param temporal      The temporal accessor representing the date to check.
     * @return              If the date falls within the high season the returned result would be true, otherwise - false.
     */
    public Boolean queryFrom(TemporalAccessor temporal) {
        int month = temporal.get(java.time.temporal.ChronoField.MONTH_OF_YEAR);
        
        if (fromMonth < toMonth)
            return fromMonth <= month && month <= toMonth;
        return fromMonth <= month || month <= toMonth;
    }
}