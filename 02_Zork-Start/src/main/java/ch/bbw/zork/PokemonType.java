package ch.bbw.zork;

/**
 * Die Typen der Pokémon. Ein Typ bestimmt, gegen welche anderen Typen ein
 * Pokémon im Kampf stark oder schwach ist.
 */
public enum PokemonType {
    /** Feuer-Typ. */
    FIRE("Fire"),
    /** Pflanzen-Typ. */
    GRASS("Grass"),
    /** Eis-Typ. */
    ICE("Ice"),
    /** Boden-Typ. */
    GROUND("Ground"),
    /** Gesteins-Typ. */
    ROCK("Rock"),
    /** Normal-Typ, nur für den Boss Arceus. Hat keine Stärken und Schwächen. */
    NORMAL("Normal");

    /** Englischer Name des Typs, der im Spiel angezeigt wird (z. B. "Fire"). */
    private final String displayName;

    /**
     * Erstellt einen Typ mit seinem Anzeigenamen.
     *
     * @param displayName englischer Name, der im Spiel angezeigt wird
     */
    PokemonType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gibt den Namen zurück, der im Spiel angezeigt wird.
     *
     * @return Anzeigename, z. B. "Fire"
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gibt zurück, wie stark dieser Typ gegen einen anderen Typ angreift.
     * Ist einer der beiden Typen {@link #NORMAL}, ist der Faktor immer 1.0.
     *
     * @param defender Typ des verteidigenden Pokémon
     * @return 2.0 bei Vorteil, 0.5 bei Nachteil, sonst 1.0
     */
    public double getMultiplier(PokemonType defender) {
        if (beats(defender)) {
            return 2.0;
        }
        if (defender.beats(this)) {
            return 0.5;
        }
        return 1.0;
    }

    private boolean beats(PokemonType other) {
        return switch (this) {
            case FIRE -> other == GRASS || other == ICE;
            case GRASS -> other == GROUND || other == ROCK;
            case ICE -> other == GRASS || other == GROUND;
            case GROUND -> other == FIRE || other == ROCK;
            case ROCK -> other == FIRE || other == ICE;
            default -> false;
        };
    }
}
