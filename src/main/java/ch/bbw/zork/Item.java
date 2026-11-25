package ch.bbw.zork;

import java.util.Locale;

/**
 * Ein Gegenstand, der in einem Raum liegen oder vom Spieler getragen werden kann.
 * Hat einen Namen, eine Beschreibung und ein Gewicht in kg.
 */
public class Item {

    /** Name des Gegenstands, gleichzeitig das Befehlswort (z. B. "flute"). */
    private final String name;

    /** Kurze englische Beschreibung für die Spielausgabe. */
    private final String description;

    /** Gewicht in kg. */
    private final double weight;

    /**
     * Erstellt einen neuen Gegenstand.
     *
     * @param name        Name des Gegenstands (Befehlswort)
     * @param description kurze englische Beschreibung
     * @param weight      Gewicht in kg
     */
    public Item(String name, String description, double weight) {
        this.name = name;
        this.description = description;
        this.weight = weight;
    }

    /**
     * Gibt den Namen des Gegenstands zurück.
     *
     * @return der Name
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt die Beschreibung des Gegenstands zurück.
     *
     * @return die Beschreibung
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gibt das Gewicht des Gegenstands zurück.
     *
     * @return das Gewicht in kg
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Gibt Name und Gewicht mit 2 Nachkommastellen zurück, z. B. {@code flute (0.50 kg)}.
     * Der Punkt als Dezimaltrenner gilt unabhängig von der Spracheinstellung des Systems.
     *
     * @return Name und Gewicht als Text
     */
    @Override
    public String toString() {
        return name + " (" + String.format(Locale.ROOT, "%.2f kg", weight) + ")";
    }
}
