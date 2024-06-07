package oop.evolution.creatures;

/**
 * The Animal class represents an animal in the evolution simulation.
 * It extends the Creature class and implements the behavior specific to animals.
 */
public non-sealed class Animal extends Creature {
    /**
     * Constructs a new Animal with default properties.
     */
    public Animal() {
        super();
    }

    /**
     * Constructs a new Animal inheriting properties from a parent Animal.
     *
     * @param parent    The parent Animal from which to inherit properties.
     */
    public Animal(Animal parent) {
        super(parent);
    }

    /**
     * Retrieves the value associated with the specified key in the PROPERTIES map.
     *
     * @param key   The key whose associated value is to be returned.
     * @return      The value associated with the specified key.
     */
    public static Integer getProperty(String key) {
        return PROPERTIES.get(key);
    }

    @Override
    protected synchronized void grow() {
        int energy = creatureCharacteristics.get("ENERGY");
        int growWith = this instanceof AnimalType ? getGrowWith() : PROPERTIES.get("GROW_WITH");
        int adultSize = PROPERTIES.get("ADULT_SIZE");

        if (energy > 0) {
            creatureCharacteristics.put("SIZE", creatureCharacteristics.get("SIZE") + growWith);
            creatureCharacteristics.put("ENERGY", energy - 1);

            if (creatureCharacteristics.get("SIZE") >= adultSize)
                isAdult.set(true);
        }
    }

    /**
     * Gets the growth value for the animal. This method should be implemented if the Animal class implements AnimalType interface.
     *
     * @return  The growth value for the animal.
     */
    public int getGrowWith() {
        return PROPERTIES.get("GROW_WITH");
    }

    @Override
    protected synchronized void evolve() { }

    @Override
    protected synchronized void replicate() { }

    @Override
    protected synchronized void feed() { }
}
