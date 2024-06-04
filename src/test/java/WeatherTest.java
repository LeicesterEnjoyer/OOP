import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import oop.evolution.World;
import oop.evolution.environment.Weather;

/**
 * Unit tests for the Weather class, which simulates weather conditions in the world.
 */
public class WeatherTest {
    private Weather weather;

    /**
     * Sets up the test environment before each test method execution.
     * This method initializes the Weather instance to be tested.
     */
    @BeforeEach
    public void setUp() {
        weather = new Weather();
    }

    /**
     * Tests the rain method of the Weather class.
     * This method verifies that the rain method correctly updates the water levels of cells in the world.
     *
     * @throws Exception    If there's an error during the test execution.
     */
    @Test
    public void testRain() throws Exception {
        Method rainMethod = Weather.class.getDeclaredMethod("rain");
        rainMethod.setAccessible(true);

        Field instance = World.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
        World world = World.getInstance();

        rainMethod.invoke(weather);

        boolean rained = false;
        for (int i = 0; i < World.getProperty("BOARD_SIZE"); i++) {
            for (int j = 0; j < World.getProperty("BOARD_SIZE"); j++)
                if (world.getCellWaterLevel(i, j) > 0) {
                    rained = true;
                    break;
                }
            if (rained) 
                break;
        }

        assertTrue(rained, "Expected at least one cell to have been rained on.");
    }
}