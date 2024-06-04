import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import oop.evolution.World;

/**
 * A class containing unit tests for the {@link World} class.
 */
public class WorldTest {
    private World world;

    /**
     * Resets the singleton instance of the World class before each test.
     *
     * @throws Exception    If there's an error during the process of resetting the singleton instance.
     */
    @BeforeEach
    public void resetSingleton() throws Exception {
        Field instance = World.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
        world = World.getInstance();
    }

    /**
     * Cleans up after each test by resetting the singleton instance of the World class.
     *
     * @throws Exception    If there's an error during the cleanup process.
     */
    @AfterEach
    public void cleanup() throws Exception {
        resetSingleton();
    }

    /**
     * Tests that the {@code getProperty} method returns the correct value for a valid key.
     */
    @Test
    public void testGetPropertyValidKey() {
        assertEquals(20, World.getProperty("MAX_CELL_WATER_LEVEL"));
    }

    /**
     * Tests that the {@code getProperty} method throws an {@link IllegalArgumentException}
     * for an invalid key and that the exception message is as expected.
     */
    @Test
    public void testGetPropertyInvalidKey() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            World.getProperty("INVALID_KEY");
        });
        assertEquals("Invalid key: INVALID_KEY", exception.getMessage());
    }

    /**
     * Tests that the {@code rainOnCell} method correctly adds water to a cell and
     * updates its water level.
     */
    @Test
    public void testRainOnCell() {
        world.rainOnCell(0, 0, 10);
        assertEquals(15, world.getCellWaterLevel(0, 0));
    }

    /**
     * Tests that the {@code rainOnCell} method throws an {@link IllegalArgumentException}
     * for invalid cell coordinates and that the exception message is as expected.
     */
    @Test
    public void testRainOnCellInvalidCoordinates() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            world.rainOnCell(-1, 0, 10);
        });
        assertEquals("Invalid cell coordinates", exception.getMessage());
        
        exception = assertThrows(IllegalArgumentException.class, () -> {
            world.rainOnCell(0, -1, 10);
        });
        assertEquals("Invalid cell coordinates", exception.getMessage());
        
        exception = assertThrows(IllegalArgumentException.class, () -> {
            world.rainOnCell(10, 0, 10);
        });
        assertEquals("Invalid cell coordinates", exception.getMessage());
        
        exception = assertThrows(IllegalArgumentException.class, () -> {
            world.rainOnCell(0, 10, 10);
        });
        assertEquals("Invalid cell coordinates", exception.getMessage());
    }
}
