package oop.dates;

import java.time.temporal.TemporalAccessor;

/**
 * An implementation of {@code HighSeasonQuery} representing the high season during summer.
 */
public final class SummerHighQuery extends HighSeasonQuery {
    /**
     * Checks whether the given temporal accessor falls within the summer high season (June to September).
     *
     * @param temporal      The temporal accessor representing the date to check.
     * @return              If the date falls within the high season the returned result would be true, otherwise - false.
     */
    @Override
    public Boolean queryFrom(TemporalAccessor temporal) {
        fromMonth = 6;
        toMonth = 9;

        return super.queryFrom(temporal);
    }
}