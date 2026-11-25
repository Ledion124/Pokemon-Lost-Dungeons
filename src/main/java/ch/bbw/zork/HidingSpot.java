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

    /** Der versteckte Gegenstand oder null, wenn die Stelle leer ist. */
    private Item hiddenItem;

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

    /**
     * Versteckt einen Gegenstand an dieser Stelle.
     *
     * @param item der Gegenstand, der versteckt wird
     */
    public void hideItem(Item item) {
        this.hiddenItem = item;
    }

    /**
     * Untersucht die Stelle. Ein versteckter Gegenstand wird herausgenommen,
     * danach ist die Stelle leer.
     *
     * @return der versteckte Gegenstand oder null, wenn nichts (mehr) da ist
     */
    public Item search() {
        Item found = hiddenItem;
        hiddenItem = null;
        return found;
    }

    /**
     * Prüft, ob an dieser Stelle noch etwas versteckt ist.
     *
     * @return true, wenn ein Gegenstand versteckt ist
     */
    public boolean hasHiddenItem() {
        return hiddenItem != null;
    }
}
