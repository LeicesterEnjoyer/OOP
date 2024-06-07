package oop.evolution.creatures;

/**
 * The NocturnalHerbivorous class represents a herbivorous animal that is active during the night.
 * It inherits from the Herbivorous class and implements the Nocturnal interface.
 */
public class NocturnalHerbivorous extends Herbivorous implements Nocturnal {

    /**
     * Constructs a new NocturnalHerbivorous with default properties.
     */
    public NocturnalHerbivorous() {
        super();
    }

    /**
     * Constructs a new NocturnalHerbivorous inheriting properties from a parent NocturnalHerbivorous.
     * 
     * @param parent The parent NocturnalHerbivorous from which to inherit properties.
     */
    public NocturnalHerbivorous(NocturnalHerbivorous parent) {
        super(parent);
    }
}
