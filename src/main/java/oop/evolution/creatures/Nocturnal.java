package oop.evolution.creatures;

import oop.evolution.World;

/**
 * The Nocturnal interface represents an animal type that is active during the night.
 * It extends the AnimalType interface and defines the isActive method to return true at night and false during the day.
 */
public interface Nocturnal extends AnimalType {

    /**
     * Determines if the animal is active based on the time of day.
     * 
     * @return  True if it is night time, false otherwise.
     */
    @Override
    default boolean isActive() {
        return !World.getInstance().getTime().isDay();
    }
}
