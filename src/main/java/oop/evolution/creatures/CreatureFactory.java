package oop.evolution.creatures;

/**
 * Factory class for creating creatures in the evolution simulation.
 */
public class CreatureFactory {
    /**
     * Creates a new creature of the specified type.
     *
     * @param type                          The type of creature to create.
     * @return                              A new creature instance.
     * @throws IllegalArgumentException     If the specified type is not supported.
     */
    public static Creature createCreature(CreaturesTypes type) {
        switch (type) {
            case DIURNAL_HERBIVOROUS:
                return new DiurnalHerbivorous();
            case DIURNAL_PREDATOR:
                return new DiurnalPredator();
            case NOCTURNAL_PREDATOR:
                return new NocturnalPredator();
            case NOCTURNAL_HERBIVOROUS:
                return new NocturnalHerbivorous();
            default:
                throw new IllegalArgumentException("Unsupported creature type: " + type);
        }
    }
}