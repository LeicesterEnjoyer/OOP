package oop.evolution.creatures;

import java.util.HashMap;
import java.util.Random;

import oop.evolution.Customizable;
import oop.evolution.WorldCell;
import oop.evolution.World;

/**
 * Represents a plant creature in the evolution simulation.
 * The plant grows, evolves, replicates, and feeds according to defined properties.
 */
public final class Plant extends Creature {
    /**
     * The properties loaded from the properties file.
     */
    private static final HashMap<String, Integer> PROPERTIES = Customizable.loadProperties("src/main/resources/creatures/plants.properties");

    /**
     * Random number generator for evolution purposes.
     */
    private final Random random = new Random();

    /**
     * Constructs a new Plant with default properties.
     */
    public Plant() {
        super();
    }

    /**
     * Constructs a new Plant inheriting properties from a parent Plant.
     *
     * @param parent The parent Plant from which to inherit properties.
     */
    public Plant(Plant parent) {
        super(parent);
    }

    @Override
    protected synchronized void grow() {
        int energy = creatureCharacteristics.get("ENERGY");
        int growWith = PROPERTIES.get("GROW_WITH");
        int adultSize = PROPERTIES.get("ADULT_SIZE");

        if (energy > 0) {
            if (isDay() == false)
                growWith /= 2;

            creatureCharacteristics.put("SIZE", creatureCharacteristics.get("SIZE") + growWith);
            creatureCharacteristics.put("ENERGY", energy - 1);

            if (creatureCharacteristics.get("SIZE") >= adultSize)
                isAdult.set(true);
        }
    }

    @Override
    protected synchronized void evolve() {
        if (isAdult.get()) {
            String[] characteristics = {"GROW_WITH", "ENERGY_INCREASE", "DEFENCE"};
            
            String characteristicToEvolve = characteristics[random.nextInt(characteristics.length)];
            creatureCharacteristics.put(characteristicToEvolve, creatureCharacteristics.get(characteristicToEvolve) + 1);
        }
    }

    @Override
    protected synchronized void replicate() {
        if (isAdult.get() && creatureCharacteristics.get("ENERGY") > 0 && canReplicate()) {
            Plant newPlant = new Plant(this);
            
            creatureCharacteristics.put("SIZE", PROPERTIES.get("SIZE"));
            creatureCharacteristics.put("ENERGY", PROPERTIES.get("ENERGY"));
        }
    }

    @Override
    protected synchronized void feed() {
        WorldCell currentCell = position.get();
        
        if (isDay() && currentCell.getWaterLevel() > 0) {
            int energyIncrease = PROPERTIES.get("ENERGY_INCREASE");
            
            creatureCharacteristics.put("ENERGY", creatureCharacteristics.get("ENERGY") + energyIncrease);
            currentCell.rain(-1);
        }
    }

     /**
     * Checks if the plant can replicate in the current cell.
     * For simplicity, this method assumes that the cell has a method to check if it can add a new plant.
     *
     * @return {@code true} if the plant can replicate, otherwise {@code false}.
     */
    private boolean canReplicate() {
        WorldCell currentCell = position.get();
        return currentCell.addPlant(this);
    }

    /**
     * Determines whether it is currently night time.
     * For simplicity, this method is currently a stub and should be implemented based on actual time logic.
     *
     * @return {@code true} if it is night time, otherwise {@code false}.
     */
    private boolean isDay() {
        return World.getInstance().getTime().isDay();
    }
}