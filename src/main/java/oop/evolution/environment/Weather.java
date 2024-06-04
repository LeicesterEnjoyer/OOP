package oop.evolution.environment;

import java.util.HashMap;
import java.util.Random;

import oop.evolution.Customizable;
import oop.evolution.World;

/**
 * A class representing the weather simulation in the world.
 * It implements the Runnable interface to be executed as a separate thread.
 */
public class Weather implements Runnable, Customizable {
    /**
     * The properties loaded from the weather properties file.
     */
    private static final HashMap<String, Integer> PROPERTIES = Customizable.loadProperties("src/main/resources/environment/weather.properties");

    /**
     * The maximum value of rain quantity.
     */
    private static final int RAIN_MAX_VALUE = PROPERTIES.get("RAIN_MAX_VALUE");
    /**
     * The size of the area where rain can fall.
     */
    private static final int RAIN_AREA = PROPERTIES.get("RAIN_AREA");
    /**
     * The maximum interval between rain occurrences.
     */
    private static final int RAIN_MAX_INTERVAL = PROPERTIES.get("RAIN_MAX_INTERVAL");

    /**
     * Random number generator for generating random rain amounts and intervals.
     */
    private final Random random = new Random();

    /**
     * Simulates rain falling on random cells within the world.
     */
    private void rain() {
        int rainAmount = random.nextInt(RAIN_MAX_VALUE) + 1;
        int areaSize = random.nextInt(RAIN_AREA) + 1;

        int boardSize = World.getProperty("BOARD_SIZE");
        int maxPosition = boardSize - areaSize;

        int startX = random.nextInt(maxPosition + 1);
        int startY = random.nextInt(maxPosition + 1);

        for (int x = startX; x < startX + areaSize; x++)
            for (int y = startY; y < startY + areaSize; y++)
                World.getInstance().rainOnCell(x, y, rainAmount);

        World.getInstance().setLastRain(startX, startY, areaSize);
    }

    /**
     * The run method that executes the weather simulation.
     * It runs in an infinite loop, generating rain at random intervals.
     */
    @Override
    public void run() {
        while (true)
            try {
                rain();
                Thread.sleep(random.nextInt(1000, RAIN_MAX_INTERVAL + 1));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
                break;
            }
    }
}