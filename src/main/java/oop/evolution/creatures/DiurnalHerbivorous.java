package oop.evolution.creatures;

/**
 * The DiurnalHerbivorous class represents a herbivorous animal that is active during the day.
 * It inherits from the Herbivorous class and implements the Diurnal interface.
 */
public class DiurnalHerbivorous extends Herbivorous implements Diurnal {

    /**
     * Constructs a new DiurnalHerbivorous with default properties.
     */
    public DiurnalHerbivorous() {
        super();
    }

    /**
     * Constructs a new DiurnalHerbivorous inheriting properties from a parent DiurnalHerbivorous.
     * 
     * @param parent The parent DiurnalHerbivorous from which to inherit properties.
     */
    public DiurnalHerbivorous(DiurnalHerbivorous parent) {
        super(parent);
    }
}
