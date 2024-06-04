package oop.evolution.creatures;

import java.util.HashMap;
import java.util.Random;

import oop.evolution.Customizable;
import oop.evolution.World;

/**
 * The Creator class is responsible for adding plants to the world at specified intervals.
 * It implements the Customizable interface and represents a thread.
 */
public class Creator implements Customizable, Runnable {
    /**
     * The properties loaded from the properties file.
     */
    private static final HashMap<String, Integer> PROPERTIES = Customizable.loadProperties("src/main/resources/creatures/creator.properties");
    
    /**
     * The world instance to add plants to.
     */
    private final World world;

    /**
     * Constructor for the Creator class.
     */
    public Creator() {
        this.world = World.getInstance();
    }

    /**
     * The run method adds a plant to a random free cell in the world if the maximum number of plants is not reached.
     * Then it sleeps for the specified creation period.
     */
    @Override
    public void run() {
        Random random = new Random();

        while (true) {
            if (world.getPlantNumber().get() < World.getProperty("MAX_PLANTS")) {
                int x = random.nextInt(World.getProperty("BOARD_SIZE"));
                int y = random.nextInt(World.getProperty("BOARD_SIZE"));

                Plant newPlant = new Plant();
                world.addPlant(newPlant, x, y);
            }

            try {
                Thread.sleep(PROPERTIES.get("CREATION_PERIOD"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
