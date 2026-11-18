package ch.bbw.zork;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Gegenstände, die der Spieler bei sich trägt.
 * Namen werden ohne Beachtung der Gross-/Kleinschreibung verglichen.
 * Ein Maximalgewicht gibt es hier nicht, das prüft der Player.
 */
public class Inventory {

    /** Alle getragenen Gegenstände, in der Reihenfolge, in der sie aufgenommen wurden. */
    private final ArrayList<Item> items = new ArrayList<>();

    /**
     * Fügt einen Gegenstand hinzu.
     *
     * @param item der Gegenstand
     */
    public void add(Item item) {
        items.add(item);
    }

    /**
     * Entfernt den Gegenstand mit diesem Namen.
     *
     * @param name Name des Gegenstands (Gross-/Kleinschreibung egal)
     * @return der entfernte Gegenstand oder null, wenn es keinen mit diesem Namen gibt
     */
    public Item remove(String name) {
        Item item = get(name);
        if (item != null) {
            items.remove(item);
        }
        return item;
    }

    /**
     * Sucht den Gegenstand mit diesem Namen, ohne ihn zu entfernen.
     *
     * @param name Name des Gegenstands (Gross-/Kleinschreibung egal)
     * @return der Gegenstand oder null, wenn es keinen mit diesem Namen gibt
     */
    public Item get(String name) {
        if (name == null) {
            return null;
        }
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Gibt alle Gegenstände als Kopie zurück. Änderungen an der Liste
     * verändern das Inventar nicht.
     *
     * @return Kopie der Gegenstände
     */
    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * Berechnet das Gesamtgewicht aller Gegenstände.
     *
     * @return Gesamtgewicht in kg
     */
    public double getTotalWeight() {
        double total = 0.0;
        for (Item item : items) {
            total += item.getWeight();
        }
        return total;
    }

    /**
     * Prüft, ob das Inventar leer ist.
     *
     * @return true, wenn kein Gegenstand getragen wird
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
