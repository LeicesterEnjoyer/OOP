package oop.evolution.environment;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import oop.evolution.Customizable;

/**
 * A class representing the day and night cycle in the world.
 * It implements the Runnable interface to be executed as a separate thread.
 */
public class DayAndNight implements Runnable, Customizable {
    /**
     * The properties loaded from the day and night properties file.
     */
    private static final HashMap<String, Integer> PROPERTIES = Customizable.loadProperties("src/main/resources/environment/day_and_night.properties");

    /**
     * The duration of a day in milliseconds.
     */
    private static final int DAY_DURATION = PROPERTIES.get("DAY_DURATION");

    /**
     * Atomic boolean representing whether it is currently day or night.
     */
    private final AtomicBoolean isDay;

    /**
     * Constructs a DayAndNight object with initial state set to day.
     */
    public DayAndNight() {
        this.isDay = new AtomicBoolean(true);
    }

    /**
     * Checks if it is currently day.
     *
     * @return true if it is day, false if it is night.
     */
    public boolean isDay() {
        return isDay.get();
    }

    /**
     * The run method that executes the day and night cycle.
     * It runs in an infinite loop, toggling between day and night at regular intervals.
     */
    @Override
    public void run() {
        while (true)
            try {
                isDay.set(!isDay.get());
                Thread.sleep((long) DAY_DURATION);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
                break;
            }
    }
}