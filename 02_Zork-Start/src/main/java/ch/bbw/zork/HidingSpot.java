package ch.bbw.zork;

/**
 * Eine Stelle in einem Raum, die der Spieler mit {@code search} untersuchen kann,
 * z. B. ein Aschehaufen. Darin kann ein Gegenstand versteckt sein.
 */
public class HidingSpot {

    /** Befehlswort, mit dem die Stelle untersucht wird (z. B. "ash"). */
    private final String name;

    /** Kurze englische Beschreibung (z. B. "a pile of ash"). */
    private final String description;

    /**
     * Erstellt eine neue, leere Stelle.
     *
     * @param name        Befehlswort, z. B. "ash"
     * @param description englische Beschreibung, z. B. "a pile of ash"
     */
    public HidingSpot(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Gibt das Befehlswort der Stelle zurück.
     *
     * @return das Befehlswort
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt die Beschreibung der Stelle zurück.
     *
     * @return die Beschreibung
     */
    public String getDescription() {
        return description;
    }
}
