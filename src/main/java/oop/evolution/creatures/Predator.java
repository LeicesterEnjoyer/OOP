package oop.evolution.creatures;

import java.util.HashMap;

import oop.evolution.Customizable;

/**
 * The Predator class represents a predator animal in the evolution simulation.
 * It extends the Animal class and implements the Customizable interface.
 */
public class Predator extends Animal implements Customizable {

    /**
     * The properties loaded from the properties file.
     */
    private static final HashMap<String, Integer> PROPERTIES = Customizable.loadProperties("src/main/resources/creatures/predators.properties");

    /**
     * Constructs a new Predator with default properties.
     */
    public Predator() {
        super();
    }

    /**
     * Constructs a new Predator inheriting properties from a parent Predator.
     *
     * @param parent The parent Predator from which to inherit properties.
     */
    public Predator(Predator parent) {
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
