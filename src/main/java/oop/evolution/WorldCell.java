package oop.evolution;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import oop.evolution.creatures.Animal;
import oop.evolution.creatures.Creature;
import oop.evolution.creatures.Plant;

/**
 * A class that represents a cell in a world grid that can hold water.
 * Each cell has a water level, and there is a maximum limit to how much water a cell can hold.
 */
public class WorldCell {
    private int x;

    private int y;
    
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
    public WorldCell(int x, int y) {
        this.x = x;
        this.y = y;
        this.waterLevel = new AtomicInteger(PROPERTIES.get("CELL_WATER"));
    }

    /**
     * Adds a plant to the cell if there is room.
     *
     * @param plant     The plant to add.
     * @return          True if the plant was added, false otherwise.
     */
    public synchronized boolean addPlant(Creature plant) {
        if (!hasSpaceFor(plant))
            return false;
        
        plants.add(plant);
        plant.setPosition(this);

        return true;
    }

    public synchronized boolean hasSpaceFor(Creature creature) {
        if (creature instanceof Animal)    
            return animals.size() < PROPERTIES.get("CELL_ANIMALS");
        if (creature instanceof Plant)
            return plants.size() < PROPERTIES.get("CELL_PLANTS");
        
        return false;
    }

    /**
     * Adds an animal to the cell.
     *
     * @param animal    The animal to add.
     * @return          True if the animal was added, false otherwise.
     */
    public synchronized boolean addAnimal(Creature animal) {
        if (!hasSpaceFor(animal))
            return false;

        animals.add(animal);
        animal.setPosition(this);
        
        return true;
    }

    public void removeCreature(Creature creature) {
        if (plants.contains(creature))
            plants.remove(creature);
        else if (animals.contains(creature))
            animals.remove(creature);
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
     public synchronized void rain(int waterQuantity) {
        if (waterLevel.addAndGet(waterQuantity) > MAX_CELL_WATER_LEVEL) {
            for (Creature animal : animals)
                animal.killCreature();
            for (Creature plant : plants) 
                plant.killCreature();

            waterLevel.set(waterQuantity);

        } 
        else 
            waterLevel.addAndGet(waterQuantity);
    }

    public synchronized void eatPlant(Animal animal) {
        if (plants.isEmpty()) 
            return;

        Creature plant = plants.get(0);
        int attack = Animal.getProperty("ATTACK");
        int defence = Plant.getProperty("DEFENCE");

        if (attack > defence) {
            plants.remove(plant);
            animal.setCreatureCharacteristic("ENERGY", animal.getCreatureCharacteristic("ENERGY") + attack - defence);
        }
    }

    public synchronized void eatAnimal(Animal animal) {
        if (animals.isEmpty()) 
            return;

        Creature target = animals.get(0);
        int attack = Animal.getProperty("ATTACK");
        int defence = Animal.getProperty("DEFENCE");
        
        if (attack > defence) {
            animals.remove(target);
            animal.setCreatureCharacteristic("ENERGY", animal.getCreatureCharacteristic("ENERGY") + attack - defence);
        }
    }

    /**
     * Gets the current water level of the cell.
     *
     * @return  The current water level.
     */
    public int getWaterLevel() {
        return waterLevel.get();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}