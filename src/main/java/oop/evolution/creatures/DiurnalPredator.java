package oop.evolution.creatures;

/**
 * The DiurnalPredator class represents a predator animal that is active during the day.
 * It inherits from the Predator class and implements the Diurnal interface.
 */
public class DiurnalPredator extends Predator implements Diurnal {

    /**
     * Constructs a new DiurnalPredator with default properties.
     */
    public DiurnalPredator() {
        super();
    }

    /**
     * Constructs a new DiurnalPredator inheriting properties from a parent DiurnalPredator.
     * 
     * @param parent The parent DiurnalPredator from which to inherit properties.
     */
    public DiurnalPredator(DiurnalPredator parent) {
        super(parent);
    }
}
