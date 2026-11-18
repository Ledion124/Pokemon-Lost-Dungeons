package ch.bbw.zork;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests für {@link Inventory}: Hinzufügen, Entfernen, Suchen und Gewicht.
 */
class InventoryTest {

    /** Inventar, das getestet wird, vor jedem Test leer. */
    private Inventory inventory;

    /** Normaler Gegenstand für die Tests. */
    private Item rock;

    /** Schlüssel-Gegenstand (Flöte), um auch ein KeyItem zu testen. */
    private KeyItem flute;

    /** Erstellt vor jedem Test ein leeres Inventar und zwei Gegenstände. */
    @BeforeEach
    void setUp() {
        inventory = new Inventory();
        rock = new Item("rock", "a small grey rock", 1.5);
        flute = KeyItem.createFlute();
    }

    /** Ein neues Inventar ist leer und wiegt nichts. */
    @Test
    @DisplayName("Neues Inventar ist leer und wiegt 0 kg")
    void newInventoryIsEmpty() {
        assertTrue(inventory.isEmpty());
        assertTrue(inventory.getItems().isEmpty());
        assertEquals(0.0, inventory.getTotalWeight(), 0.0001);
    }

    /** add fügt Gegenstände in der richtigen Reihenfolge hinzu. */
    @Test
    @DisplayName("add fügt Gegenstände in Reihenfolge hinzu")
    void addKeepsOrder() {
        inventory.add(rock);
        inventory.add(flute);

        assertFalse(inventory.isEmpty());
        assertEquals(List.of(rock, flute), inventory.getItems());
    }

    /** get findet einen Gegenstand ohne Beachtung der Gross-/Kleinschreibung und lässt ihn im Inventar. */
    @Test
    @DisplayName("get findet Gegenstand unabhängig von Gross-/Kleinschreibung")
    void getIgnoresCase() {
        inventory.add(flute);

        assertSame(flute, inventory.get("FLUTE"));
        assertSame(flute, inventory.get("flute"));
        assertEquals(1, inventory.getItems().size());
    }

    /** get gibt null zurück, wenn es den Gegenstand nicht gibt. */
    @Test
    @DisplayName("get gibt null bei unbekanntem Gegenstand")
    void getUnknownReturnsNull() {
        inventory.add(rock);

        assertNull(inventory.get("key"));
        assertNull(inventory.get(null));
    }

    /** remove entfernt den Gegenstand und gibt ihn zurück. */
    @Test
    @DisplayName("remove entfernt Gegenstand und gibt ihn zurück")
    void removeReturnsItem() {
        inventory.add(rock);
        inventory.add(flute);

        assertSame(rock, inventory.remove("Rock"));
        assertNull(inventory.get("rock"));
        assertEquals(List.of(flute), inventory.getItems());
    }

    /** remove eines unbekannten Gegenstands gibt null zurück und ändert nichts. */
    @Test
    @DisplayName("remove eines unbekannten Gegenstands gibt null")
    void removeUnknownReturnsNull() {
        inventory.add(rock);

        assertNull(inventory.remove("flute"));
        assertEquals(1, inventory.getItems().size());
    }

    /** getTotalWeight zählt das Gewicht aller Gegenstände zusammen. */
    @Test
    @DisplayName("getTotalWeight summiert alle Gewichte")
    void totalWeightIsSum() {
        inventory.add(rock);
        inventory.add(flute);
        inventory.add(KeyItem.createKey());

        assertEquals(2.2, inventory.getTotalWeight(), 0.0001);
    }

    /** getItems gibt eine Kopie zurück, Änderungen daran wirken nicht aufs Inventar. */
    @Test
    @DisplayName("getItems gibt eine Kopie zurück")
    void getItemsReturnsCopy() {
        inventory.add(rock);

        List<Item> copy = inventory.getItems();
        copy.clear();

        assertEquals(1, inventory.getItems().size());
    }
}
