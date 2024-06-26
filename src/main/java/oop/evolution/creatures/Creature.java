package oop.evolution.creatures;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import oop.evolution.World;
import oop.evolution.WorldCell;

/**
 * The abstract base class for all creatures in the evolution simulation.
 * This class defines common behavior and attributes for creatures.
 */
public abstract sealed class Creature permits Plant, Animal {
    /**
     * The properties shared by all creature instances.
     */
    protected static final HashMap<String, Integer> PROPERTIES = new HashMap<>();

    /**
     * The characteristics specific to each creature instance.
     */
    protected ConcurrentHashMap<String, Integer> creatureCharacteristics = new ConcurrentHashMap<>();

    /**
     * Flag indicating whether the creature is an adult or not.
     */
    protected AtomicBoolean isAdult = new AtomicBoolean();

    /**
     * Thread for simulating growth process of the creature.
     */
    protected Thread growThread;

    /**
     * Thread for simulating evolution process of the creature.
     */
    protected Thread evolveThread;

    /**
     * Thread for simulating replication process of the creature.
     */
    protected Thread replicateThread;

    /**
     * Thread for simulating feeding process of the creature.
     */
    protected Thread feedThread;

    /**
     * Flag to indicate if growth process should stop.
     */
    private volatile boolean growStopRequested = false;

    /**
     * Flag to indicate if evolution process should stop.
     */
    private volatile boolean evolveStopRequested = false;

    /**
     * Flag to indicate if replication process should stop.
     */
    private volatile boolean replicateStopRequested = false;

    /**
     * Flag to indicate if feeding process should stop.
     */
    private volatile boolean feedStopRequested = false;

    /**
     * The position of the creature in the world.
     */
    protected AtomicReference<WorldCell> position = new AtomicReference<>();

    /**
     * Default constructor for creating a creature.
     * Initializes creature characteristics with default properties and starts its life processes.
     */
    public Creature() {
        creatureCharacteristics.putAll(PROPERTIES);
            
        startLiving();
    }

    /**
     * Constructor for creating a creature with parent characteristics.
     * Initializes creature characteristics with parent's characteristics and starts its life processes.
     *
     * @param parent    The parent creature whose characteristics to inherit.
     */
    public Creature(Creature parent) {
        creatureCharacteristics.putAll(PROPERTIES);
        creatureCharacteristics.putAll(parent.creatureCharacteristics);
        
        if (parent instanceof Plant && parent.position.get().addPlant(this))
            startLiving();
    }

    /**
     * Sets the position of the creature in the world.
     *
     * @param newPosition   The new position of the creature.
     */
    public void setPosition(WorldCell newPosition) {
        this.position.set(newPosition);
    }

    public WorldCell getPosition() {
        return position.get();
    }

    /**
     * Abstract method to get evolution characteristics specific to each creature type.
     *
     * @return A list of characteristics that can evolve for the creature.
     */
    protected abstract List<String> getEvolutionCharacteristics();

    /**
     * Abstract method representing the growth process of the creature.
     * Subclasses must implement this method to define creature growth behavior.
     */
    protected abstract void grow();

    /**
     * Abstract method representing the evolution process of the creature.
     * Subclasses must implement this method to define creature evolution behavior.
     */
    protected synchronized void evolve() {
        if (!isAdult.get()) 
            return;

        Random random = new Random();
        String characteristicToEvolve = getEvolutionCharacteristics().get(random.nextInt(getEvolutionCharacteristics().size()));
        creatureCharacteristics.put(characteristicToEvolve, creatureCharacteristics.get(characteristicToEvolve) + 1);
    }

    /**
     * Abstract method representing the replication process of the creature.
     * Subclasses must implement this method to define creature replication behavior.
     */
    protected abstract void replicate();

    /**
     * Abstract method representing the feeding process of the creature.
     * Subclasses must implement this method to define creature feeding behavior.
     */
    protected abstract void feed();

    /**
     * Starts the life processes of the creature by creating and starting threads for each process.
     * Each process runs in an infinite loop, executing its corresponding method and then sleeping for 1000 milliseconds.
     */
    protected void startLiving() {
        growThread = new Thread(() -> {
            while (!growStopRequested)
                try {
                    Thread.sleep(PROPERTIES.get("GROW_PERIOD"));
                    grow();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
        });
        growThread.start();

        evolveThread = new Thread(() -> {
            while (!evolveStopRequested)
                try {
                    Thread.sleep(PROPERTIES.get("EVOLVE_PERIOD"));
                    evolve();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
        });
        evolveThread.start();

        replicateThread = new Thread(() -> {
            while (!replicateStopRequested)
                try {
                    Thread.sleep(PROPERTIES.get("REPLICATE_PERIOD"));
                    replicate();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
        });
        replicateThread.start();

        feedThread = new Thread(() -> {
            while (!feedStopRequested)
                try {
                    Thread.sleep(PROPERTIES.get("FEED_PERIOD"));
                    feed();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
        });
        feedThread.start();
    }

    /**
     * Stops the life processes of the creature by interrupting all threads associated with it.
     */
    public void killCreature() {
        growStopRequested = true;
        evolveStopRequested = true;
        replicateStopRequested = true;
        feedStopRequested = true;

        growThread.interrupt();
        evolveThread.interrupt();
        replicateThread.interrupt();
        feedThread.interrupt();

        WorldCell currentPosition = position.get();
        if (currentPosition != null)
            currentPosition.removeCreature(this);

        World.getInstance().removeCreature(this);
    }

    /**
     * Gets the adult status of the creature.
     *
     * @return  The adult status of the creature as an AtomicBoolean.
     */
    public AtomicBoolean getIsAdult() {
        return isAdult;
    }

    public void setCreatureCharacteristic(String type, int newValue) {
        creatureCharacteristics.put(type, newValue);
    }

    public int getCreatureCharacteristic(String type) {
        return creatureCharacteristics.get(type);
    }   
}