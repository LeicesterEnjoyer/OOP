package oop.evolution.creatures;

import java.util.HashMap;

import oop.evolution.Customizable;

/**
 * The Herbivorous class represents a herbivorous animal in the evolution simulation.
 * It extends the Animal class and implements the Customizable interface.
 */
public class Herbivorous extends Animal implements Customizable {

    /**
     * The properties loaded from the properties file.
     */
    private static final HashMap<String, Integer> PROPERTIES = Customizable.loadProperties("src/main/resources/creatures/herbivorouses.properties");

    /**
     * Constructs a new Herbivorous with default properties.
     */
    public Herbivorous() {
        super();
    }

    /**
     * Constructs a new Herbivorous inheriting properties from a parent Herbivorous.
     *
     * @param parent The parent Herbivorous from which to inherit properties.
     */
    public Herbivorous(Herbivorous parent) {
        super(parent);
    }

    @Override
    protected synchronized void grow() {
        super.grow();
    }

    @Override
    protected synchronized void evolve() {
        super.evolve();
    }

    @Override
    protected synchronized void replicate() {
        super.replicate();
    }

    @Override
    protected synchronized void feed() {
        super.feed();
    }
}
