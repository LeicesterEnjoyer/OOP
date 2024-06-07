package oop.evolution.creatures;

import java.lang.reflect.Method;

/**
 * The AnimalType interface defines behaviors specific to animals in the evolution simulation.
 */
public interface AnimalType {

    /**
     * Checks if the animal is active.
     *
     * @return  True if the animal is active, false otherwise.
     */
    boolean isActive();

    /**
     * Gets the growth value for the animal.
     *
     * @return  The growth value for the animal.
     */
    default int getGrowWith() {
        try {
            String propertyName = isActive() ? "GROW_WITH" : "INACTIVE_GROW_WITH";
            Method method = this.getClass().getMethod("getProperty", String.class);

            return (Integer) method.invoke(null, propertyName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get grow value", e);
        }
    }
}
