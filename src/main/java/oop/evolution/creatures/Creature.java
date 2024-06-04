package oop.evolution.creatures;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import oop.evolution.WorldCell;

/**
 * The abstract base class for all creatures in the evolution simulation.
 * This class defines common behavior and attributes for creatures.
 */
public abstract sealed class Creature permits Plant {
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
        
        if (parent instanceof Plant && position.get().addPlant(this))
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

    /**
     * Abstract method representing the growth process of the creature.
     * Subclasses must implement this method to define creature growth behavior.
     */
    protected abstract void grow();

    /**
     * Abstract method representing the evolution process of the creature.
     * Subclasses must implement this method to define creature evolution behavior.
     */
    protected abstract void evolve();

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
            while (true)
                try {
                    Thread.sleep(PROPERTIES.get("GROW_PERIOD"));
                    grow();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
        });
        growThread.setPriority(Thread.MIN_PRIORITY);
        growThread.start();

        evolveThread = new Thread(() -> {
            while (true)
                try {
                    Thread.sleep(PROPERTIES.get("EVOLVE_PERIOD"));
                    evolve();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
        });
        evolveThread.setPriority(Thread.MIN_PRIORITY);
        evolveThread.start();

        replicateThread = new Thread(() -> {
            while (true)
                try {
                    Thread.sleep(PROPERTIES.get("REPLICATE_PERIOD"));
                    replicate();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
        });
        replicateThread.setPriority(Thread.MIN_PRIORITY);
        replicateThread.start();

        feedThread = new Thread(() -> {
            while (true)
                try {
                    Thread.sleep(PROPERTIES.get("FEED_PERIOD"));
                    feed();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
        });
        feedThread.setPriority(Thread.NORM_PRIORITY);
        feedThread.start();
    }

    /**
     * Stops the life processes of the creature by interrupting all threads associated with it.
     */
    public void killCreature() {
        growThread.interrupt();
        evolveThread.interrupt();
        replicateThread.interrupt();
        feedThread.interrupt();
    }

    /**
     * Gets the adult status of the creature.
     *
     * @return  The adult status of the creature as an AtomicBoolean.
     */
    public AtomicBoolean getIsAdult() {
        return isAdult;
    }
}