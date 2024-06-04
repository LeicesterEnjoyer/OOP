package oop.dates;

import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

/**
 * A {@code TemporalAdjuster} implementation that adjusts the temporal object to the first day of its month.
 */
public class FirstMonthDayAdjuster implements TemporalAdjuster {
    /**
     * Adjusts the specified temporal object to the first day of its month.
     *
     * @param temporal                      The temporal object to adjust.
     * @return                              The adjusted temporal object with the day of month set to 1.
     * @throws IllegalArgumentException     If the temporal object is not of type LocalDate.
     */
    @Override
    public Temporal adjustInto(Temporal temporal) {
        if (!(temporal instanceof LocalDate))
            throw new IllegalArgumentException("Only LocalDate supported");
        
        LocalDate date = (LocalDate) temporal;
        return date.withDayOfMonth(1);
    }
}