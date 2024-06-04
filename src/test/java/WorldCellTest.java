import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import oop.evolution.WorldCell;

/**
 * A class containING unit tests for the {@link WorldCell} class.
 */
public class WorldCellTest {
    private WorldCell evolution;

    /**
     * Tests that the initial water level of a {@code WorldCell} is 5.
     */
    @Test
    public void testInitialWaterLevel() {
        evolution = new WorldCell();

        assertEquals(5, evolution.getWaterLevel());
    }

    /**
     * Tests that the {@code rain} method correctly adds water to a cell
     * when the resulting water level is within the allowed limit.
     */
    @Test
    public void testRainWithinLimit() {
        evolution = new WorldCell();
        evolution.rain(10);

        assertEquals(15, evolution.getWaterLevel());
    }

    /**
     * Tests that the {@code rain} method correctly handles cases where the
     * added water exceeds the maximum water level limit.
     */
    @Test
    public void testRainExceedingLimit() {
        evolution = new WorldCell();
        evolution.rain(25);

        assertEquals(30, evolution.getWaterLevel());
    }
}
