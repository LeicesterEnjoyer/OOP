package oop.evolution;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import oop.evolution.creatures.Creature;

/**
 * A class that represents a cell in a world grid that can hold water.
 * Each cell has a water level, and there is a maximum limit to how much water a cell can hold.
 */
public class WorldCell {
    /**
     * The current water level in the cell.
     */
    private final AtomicInteger waterLevel;

    /**
     * The maximum water level a cell can hold, loaded from the 'world.properties' file.
     */
    private static int MAX_CELL_WATER_LEVEL;

    /**
     * The properties loaded from the properties file.
     */
    private static final HashMap<String, Integer> PROPERTIES = Customizable.loadProperties("src/main/resources/world.properties");

    /**
     * A thread-safe list to store plants in the cell.
     */
    private final List<Creature> plants = Collections.synchronizedList(new LinkedList<>());

    /**
     * A thread-safe list to store animals in the cell.
     */
    private final List<Creature> animals = Collections.synchronizedList(new LinkedList<>());

    /**
     * Constructor that initializes the water level with the value from the properties file.
     */
    public WorldCell() {
        this.waterLevel = new AtomicInteger(PROPERTIES.get("CELL_WATER"));
    }

    /**
     * Adds a plant to the cell if there is room.
     *
     * @param plant     The plant to add.
     * @return          True if the plant was added, false otherwise.
     */
    public synchronized boolean addPlant(Creature plant) {
        if (plants.size() >= PROPERTIES.get("CELL_PLANTS"))
            return false;
        
        plants.add(plant);
        plant.setPosition(this);

        return true;
    }

    /**
     * Adds an animal to the cell.
     *
     * @param animal    The animal to add.
     * @return          True if the animal was added, false otherwise.
     */
    public synchronized boolean addAnimal(Creature animal) {
        if (animals.size() >= PROPERTIES.get("CELL_ANIMALS"))
            return false;

        animals.add(animal);
        animal.setPosition(this);
        
        return true;
    }

    /**
     * Retrieves information about the types and stages of creatures present in the world cell.
     *
     * @return The HashMap containing information about creatures in the world cell.
     */
    public synchronized HashMap<String, HashMap<String, Integer>> getCreatures() {
        HashMap<String, HashMap<String, Integer>> creatures = new HashMap<>();
        
        HashMap<String, Integer> plantStages = new HashMap<>();
        plantStages.put("CHILD", 0);
        plantStages.put("ADULT", 0);

        synchronized (plants) {
            for (Creature plant : plants)
                if (plant.getIsAdult() != null) 
                    plantStages.put("ADULT", plantStages.get("ADULT") + 1);
                else
                    plantStages.put("CHILD", plantStages.get("CHILD") + 1);
        }
        
        HashMap<String, Integer> predStages = new HashMap<>();
        predStages.put("CHILD", 0);
        predStages.put("ADULT", 0);
        
        HashMap<String, Integer> herbStages = new HashMap<>();
        herbStages.put("CHILD", 0);
        herbStages.put("ADULT", 0);
        
        creatures.put("PLANT", plantStages);
        creatures.put("PRED", predStages);
        creatures.put("HERB", herbStages);
        
        return creatures;
    }


    /**
     * Adds water to the cell. If the new water level exceeds the maximum cell water level,
     * the water level is reset to the added water quantity plus a base amount (5).
     *
     * @param waterQuantity     The quantity of water to add.
     */
    public void rain(int waterQuantity) {
        int newWaterLevel = waterLevel.addAndGet(waterQuantity);
        
        if (newWaterLevel < 0)
            newWaterLevel = 0;

        if (newWaterLevel > MAX_CELL_WATER_LEVEL)
            waterLevel.set(waterQuantity + 5);
    }

    /**
     * Gets the current water level of the cell.
     *
     * @return  The current water level.
     */
    public int getWaterLevel() {
        return waterLevel.get();
    }
}