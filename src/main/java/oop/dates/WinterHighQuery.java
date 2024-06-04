package oop.dates;

import java.time.temporal.TemporalAccessor;

/**
 * An implementation of {@code HighSeasonQuery} representing the high season during winter.
 */
public final class WinterHighQuery extends HighSeasonQuery {
    /**
     * Checks whether the given temporal accessor falls within the winter high season (November to March).
     *
     * @param temporal      The temporal accessor representing the date to check.
     * @return              If the date falls within the high season the returned result would be true, otherwise - false.
     */
    @Override
    public Boolean queryFrom(TemporalAccessor temporal) {
        fromMonth = 11;
        toMonth = 3;

        return super.queryFrom(temporal);
    }
}