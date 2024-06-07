package oop.evolution.creatures;

/**
 * The NocturnalPredator class represents a predator animal that is active during the night.
 * It inherits from the Predator class and implements the Nocturnal interface.
 */
public class NocturnalPredator extends Predator implements Nocturnal {

    /**
     * Constructs a new NocturnalPredator with default properties.
     */
    public NocturnalPredator() {
        super();
    }

    /**
     * Constructs a new NocturnalPredator inheriting properties from a parent NocturnalPredator.
     * 
     * @param parent The parent NocturnalPredator from which to inherit properties.
     */
    public NocturnalPredator(NocturnalPredator parent) {
        super(parent);
    }
}
