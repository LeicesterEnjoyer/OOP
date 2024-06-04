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
            case DIURNAL_PREDATOR:
            case NOCTURNAL_PREDATOR:
            case NOCTURNAL_HERBIVOROUS:
                return new Plant();
            default:
                throw new IllegalArgumentException("Unsupported creature type: " + type);
        }
    }
}