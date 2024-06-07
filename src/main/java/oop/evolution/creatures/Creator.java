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
            try {
                int plantCount = PROPERTIES.get("PLANT");
                for (int i = 0; i < plantCount; i++) {
                    if (world.getPlantNumber().get() < World.getProperty("MAX_PLANTS")) {
                        int x = random.nextInt(World.getProperty("BOARD_SIZE"));
                        int y = random.nextInt(World.getProperty("BOARD_SIZE"));

                        Plant newPlant = new Plant();
                        world.addPlant(newPlant, x, y);
                        Thread.sleep(PROPERTIES.get("CREATION_PERIOD"));
                    }
                }

                int dayHerbCount = PROPERTIES.get("DAY_HERB");
                for (int i = 0; i < dayHerbCount; i++) {
                    if (world.addWorldAnimal(new DiurnalHerbivorous())) {
                        int x = random.nextInt(World.getProperty("BOARD_SIZE"));
                        int y = random.nextInt(World.getProperty("BOARD_SIZE"));

                        world.addAnimal(new DiurnalHerbivorous(), x, y);
                        Thread.sleep(PROPERTIES.get("CREATION_PERIOD"));
                    }
                }

                int nightHerbCount = PROPERTIES.get("NIGHT_HERB");
                for (int i = 0; i < nightHerbCount; i++) {
                    if (world.addWorldAnimal(new NocturnalHerbivorous())) {
                        int x = random.nextInt(World.getProperty("BOARD_SIZE"));
                        int y = random.nextInt(World.getProperty("BOARD_SIZE"));

                        world.addAnimal(new NocturnalHerbivorous(), x, y);
                        Thread.sleep(PROPERTIES.get("CREATION_PERIOD"));
                    }
                }

                int dayPredCount = PROPERTIES.get("DAY_PRED");
                for (int i = 0; i < dayPredCount; i++) {
                    if (world.addWorldAnimal(new DiurnalPredator())) {
                        int x = random.nextInt(World.getProperty("BOARD_SIZE"));
                        int y = random.nextInt(World.getProperty("BOARD_SIZE"));

                        world.addAnimal(new DiurnalPredator(), x, y);
                        Thread.sleep(PROPERTIES.get("CREATION_PERIOD"));
                    }
                }

                int nightPredCount = PROPERTIES.get("NIGHT_PRED");
                for (int i = 0; i < nightPredCount; i++) {
                    if (world.addWorldAnimal(new NocturnalPredator())) {
                        int x = random.nextInt(World.getProperty("BOARD_SIZE"));
                        int y = random.nextInt(World.getProperty("BOARD_SIZE"));

                        world.addAnimal(new NocturnalPredator(), x, y);
                        Thread.sleep(PROPERTIES.get("CREATION_PERIOD"));
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
