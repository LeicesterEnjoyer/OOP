package oop.evolution.creatures;

import java.util.ArrayList;
import java.util.List;

import oop.evolution.World;

/**
 * The Animal class represents an animal in the evolution simulation.
 * It extends the Creature class and implements the behavior specific to animals.
 */
public non-sealed class Animal extends Creature {
    /**
     * The evolution characteristics for animals.
     */
    protected static final ArrayList<String> EVOLUTION_CHARACTERISTICS = new ArrayList<>();

    static {
        EVOLUTION_CHARACTERISTICS.add("GROW_WITH");
        EVOLUTION_CHARACTERISTICS.add("INACTIVE_GROW_WITH");
        EVOLUTION_CHARACTERISTICS.add("ENERGY_INCREASE");
        EVOLUTION_CHARACTERISTICS.add("DEFENCE");
        EVOLUTION_CHARACTERISTICS.add("ATTACK");
    }

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

    /**
     * Attempts to move the animal to an adjacent cell up to 10 times in 1 second.
     * Terminates execution if unsuccessful.
     */
    protected synchronized void move() {
        for (int attempts = 0; attempts < 10; attempts++) {
            if (World.getInstance().moveToNeighbourCell(this))
                return;

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    @Override
    protected synchronized void evolve() { }

    @Override
    protected synchronized void replicate() {
        if (isAdult.get() && creatureCharacteristics.get("ENERGY") > 0 && canReplicate() && isActive()) {
            Animal newAnimal = new Animal(this);

            creatureCharacteristics.put("SIZE", PROPERTIES.get("SIZE"));
            creatureCharacteristics.put("ENERGY", PROPERTIES.get("ENERGY"));
        }
    }

    @Override
    protected synchronized void feed() { }

    /**
     * Checks if the animal is in its active period.
     *
     * @return  True if the animal is in its active period, otherwise false.
     */
    private boolean isActive() {
        
        return true;
    }

    /**
     * Checks if the animal can replicate.
     *
     * @return  True if the animal can replicate, otherwise false.
     */
    private boolean canReplicate() {
        return position.get() != null && position.get().hasSpaceFor(this);
    }

    @Override
    public List<String> getEvolutionCharacteristics() {
        return EVOLUTION_CHARACTERISTICS;
    }
}
