package oop.evolution;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import oop.evolution.creatures.Animal;
import oop.evolution.creatures.Creator;
import oop.evolution.creatures.Creature;
import oop.evolution.draw.DrawWorld;
import oop.evolution.environment.DayAndNight;
import oop.evolution.environment.Weather;

/**
 * A class that represents a singleton world grid containing cells that can hold water.
 * The world is initialized based on properties loaded from a file.
 */
public class World {
    /**
     * The singleton instance of the World class.
     */
    private static World instance;
    /**
     * The properties loaded from the properties file.
     */
    private static final HashMap<String, Integer> PROPERTIES = Customizable.loadProperties("src/main/resources/world.properties");
    
    /**
     * An instance of the DayAndNight class representing the day and night cycle.
     */
    private final DayAndNight time;
    /**
     * An instance of the Weather class representing the weather cycle.
     */
    private final Weather weather;

    /**
     * The last rain information stored in a concurrent hash map.
     */
    private ConcurrentHashMap<String, Integer> lastRain = new ConcurrentHashMap<>();
    /**
     * An instance of the DrawWorld class representing the drawing tool.
     */
    private DrawWorld drawWorld;
    /**
     * The number of plants in the world.
     */
    private AtomicInteger plantNumber = new AtomicInteger(0);
    /**
     * The number of animals in the world.
     */
    private final AtomicInteger creatureNumber = new AtomicInteger(0);

    /**
     * The list of all animals that in the world.
     */
    private final List<Creature> animals = Collections.synchronizedList(new LinkedList<>());

    /**
     * The size of the board, as specified by the properties.
     */
    private static final int BOARD_SIZE = PROPERTIES.get("BOARD_SIZE");
    /**
     * The 2D array representing the cells in the world.
     */
    private final WorldCell[][] board = new WorldCell[BOARD_SIZE][BOARD_SIZE];

    {
        for (int i = 0; i < BOARD_SIZE; ++i)
            for (int j = 0; j < BOARD_SIZE; ++j)
                board[i][j] = new WorldCell();
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private World() {
        this.time = new DayAndNight();
        this.weather = new Weather();
    }

    /**
     * Starts the world by initializing and starting the weather, time, and creator threads,
     * and then displaying the world using the DrawWorld class.
     */
    public void startWorld() {
        Creator creator = new Creator();

        new Thread(weather).start();
        new Thread(time).start();
        new Thread(creator).start();
        
        drawWorld = new DrawWorld();
        drawWorld.show();
    }

    /**
     * Returns the singleton instance of the World class.
     * If the instance does not exist, it is created.
     *
     * @return  Тhe singleton instance of the World class.
     */
    public static synchronized World getInstance() {
        if (instance == null)
            instance = new World();
        
        return instance;
    }

    /**
     * Retrieves a property value by its key.
     *
     * @param key                       The key of the property.
     * @return                          The value of the property.
     * @throws IllegalArgumentException If the key does not exist in the properties.
     */
    public static Integer getProperty(String key) {
        if (!PROPERTIES.containsKey(key))
            throw new IllegalArgumentException("Invalid key: " + key);
        
        return PROPERTIES.get(key);
    }

    /**
     * Validates the given coordinates to ensure they are within the bounds of the board.
     *
     * @param x                         The x-coordinate.
     * @param y                         The y-coordinate.
     * @throws IllegalArgumentException If the coordinates are out of bounds.
     */
    private void validateCoordinates(int x, int y) {
        if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE)
            throw new IllegalArgumentException("Invalid cell coordinates");
    }

    /**
     * Adds water to the specified cell.
     *
     * @param x             The x-coordinate of the cell.
     * @param y             The y-coordinate of the cell.
     * @param waterQuantity The quantity of water to add.
     */
    public void rainOnCell(int x, int y, int waterQuantity) {
        validateCoordinates(x, y);
        board[x][y].rain(waterQuantity);
    }

    /**
     * Adds a new animal to the world.
     *
     * @param newAnimal The animal to add.
     * @return          True if the animal was successfully added, false if the maximum number of animals in the world has been reached.
     */
    public synchronized boolean addWorldAnimal(Animal newAnimal) {
        if (animals.size() >= getProperty("MAX_ANIMALS"))
            return false;
        else {
            animals.add(newAnimal);
            creatureNumber.incrementAndGet();
            return true;
        }
    }

    /**
     * Adds an animal to a specific cell on the board.
     *
     * @param animal    The animal to add.
     * @param x         The x-coordinate of the cell.
     * @param y         The y-coordinate of the cell.
     * @return          True if the animal was successfully added to the cell, false if the cell is full.
     */
    public boolean addAnimal(Creature animal, int x, int y) {
        if (board[x][y].addAnimal(animal)) {
            animal.setPosition(board[x][y]);
            return true;
        }

        return false;
    }

    /**
     * Adds a plant to the specified cell.
     *
     * @param plant The plant to add.
     * @param x     The x-coordinate of the cell.
     * @param y     The y-coordinate of the cell.
     * @return True if the plant was added, false otherwise.
     */
    public boolean addPlant(Creature plant, int x, int y) {
        validateCoordinates(x, y);
        boolean added = board[x][y].addPlant(plant);
        
        if (added)
            plantNumber.incrementAndGet();

        return added;
    }

    /**
     * Retrieves the water level of the specified cell.
     *
     * @param x The x-coordinate of the cell.
     * @param y The y-coordinate of the cell.
     * @return  The water level of the cell.
     */
    public int getCellWaterLevel(int x, int y) {
        validateCoordinates(x, y);
        return board[x][y].getWaterLevel();
    }

    /**
     * Sets the last rain information.
     *
     * @param x     The x-coordinate of the upper left cell of the rain area.
     * @param y     The y-coordinate of the upper left cell of the rain area.
     * @param area  The size of the rain area in number of cells.
     */
    public synchronized void setLastRain(int x, int y, int area) {
        lastRain.put("X", x);
        lastRain.put("Y", y);
        lastRain.put("AREA", area);
    }

    /**
     * Returns a copy of the last rain information.
     *
     * @return  The HashMap copy of the last rain attribute.
     */
    public synchronized HashMap<String, Integer> getLastRain() {
        return new HashMap<>(lastRain);
    }

    /**
     * Retrieves information about the creatures present in each cell of the world.
     *
     * @return  The 2D array representing creatures in each cell.
     */
    public HashMap<String, HashMap<String, Integer>>[][] getCreatures() {
        HashMap<String, HashMap<String, Integer>>[][] creatures = new HashMap[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; ++i)
            for (int j = 0; j < BOARD_SIZE; ++j)
                creatures[i][j] = board[i][j].getCreatures();
        
        return creatures;
    }

    /**
     * Retrieves the instance of the DayAndNight class representing the current time in the world.
     *
     * @return  The DayAndNight instance representing the current time.
     */
    public DayAndNight getTime() {
        return time;
    }

    /**
     * Retrieves the current number of plants.
     *
     * @return  The AtomicInteger representing the current number of plants.
     */
    public AtomicInteger getPlantNumber() {
        return plantNumber;
    }
}