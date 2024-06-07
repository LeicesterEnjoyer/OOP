package oop.evolution.creatures;

import oop.evolution.World;

/**
 * The Diurnal interface represents an animal type that is active during the day.
 * It extends the AnimalType interface and defines the isActive method to return true during the day and false at night.
 */
public interface Diurnal extends AnimalType {

    /**
     * Determines if the animal is active based on the time of day.
     * 
     * @return  True if it is day time, false otherwise.
     */
    @Override
    default boolean isActive() {
        return World.getInstance().getTime().isDay();
    }
}
