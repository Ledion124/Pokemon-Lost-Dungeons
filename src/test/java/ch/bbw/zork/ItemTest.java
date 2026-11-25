package ch.bbw.zork;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests für {@link Item}: Konstruktor, Getter und toString.
 */
class ItemTest {

    /** Der Konstruktor speichert den Namen. */
    @Test
    @DisplayName("Konstruktor speichert den Namen")
    void constructorSetsName() {
        Item item = new Item("rock", "a small grey rock", 1.5);

        assertEquals("rock", item.getName());
    }

    /** Der Konstruktor speichert die Beschreibung. */
    @Test
    @DisplayName("Konstruktor speichert die Beschreibung")
    void constructorSetsDescription() {
        Item item = new Item("rock", "a small grey rock", 1.5);

        assertEquals("a small grey rock", item.getDescription());
    }

    /** Der Konstruktor speichert das Gewicht. */
    @Test
    @DisplayName("Konstruktor speichert das Gewicht")
    void constructorSetsWeight() {
        Item item = new Item("rock", "a small grey rock", 1.5);

        assertEquals(1.5, item.getWeight(), 0.0001);
    }

    /** toString zeigt Name und Gewicht mit 2 Nachkommastellen. */
    @Test
    @DisplayName("toString zeigt Name und Gewicht mit 2 Nachkommastellen")
    void toStringShowsNameAndWeight() {
        Item item = new Item("flute", "an old wooden flute", 0.5);

        assertEquals("flute (0.50 kg)", item.toString());
    }

    /** toString rundet das Gewicht auf 2 Nachkommastellen. */
    @Test
    @DisplayName("toString rundet das Gewicht auf 2 Nachkommastellen")
    void toStringRoundsWeight() {
        Item item = new Item("feather", "a light feather", 0.126);

        assertEquals("feather (0.13 kg)", item.toString());
    }

    /** Ein Gegenstand ohne Gewicht wird mit 0.00 kg angezeigt. */
    @Test
    @DisplayName("Gewicht 0 wird als 0.00 kg angezeigt")
    void zeroWeightIsShown() {
        Item item = new Item("note", "a scrap of paper", 0.0);

        assertEquals("note (0.00 kg)", item.toString());
    }
}
