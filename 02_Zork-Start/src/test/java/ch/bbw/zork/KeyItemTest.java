package ch.bbw.zork;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests für {@link KeyItem}: Konstruktor, Vererbung und die Fabrikmethoden.
 */
class KeyItemTest {

    /** Der Konstruktor speichert Zielraum und Richtung. */
    @Test
    @DisplayName("Konstruktor speichert Zielraum und Richtung")
    void constructorSetsTargetRoomAndDirection() {
        KeyItem item = new KeyItem("lever", "a rusty lever", 1.0, "Hall", "west");

        assertEquals("Hall", item.getTargetRoom());
        assertEquals("west", item.getDirection());
    }

    /** Ein KeyItem ist ein Item und behält Name, Beschreibung und Gewicht. */
    @Test
    @DisplayName("KeyItem ist ein Item mit Name, Beschreibung und Gewicht")
    void keyItemIsAnItem() {
        Item item = new KeyItem("lever", "a rusty lever", 1.0, "Hall", "west");

        assertEquals("lever", item.getName());
        assertEquals("a rusty lever", item.getDescription());
        assertEquals(1.0, item.getWeight(), 0.0001);
    }

    /** Die Flöte heisst flute und wiegt 0.5 kg. */
    @Test
    @DisplayName("Flöte heisst flute und wiegt 0.5 kg")
    void fluteHasNameAndWeight() {
        KeyItem flute = KeyItem.createFlute();

        assertEquals("flute", flute.getName());
        assertEquals(0.5, flute.getWeight(), 0.0001);
    }

    /** Die Flöte öffnet im Greenpath den Ausgang east. */
    @Test
    @DisplayName("Flöte öffnet Greenpath east")
    void fluteOpensGreenpathEast() {
        KeyItem flute = KeyItem.createFlute();

        assertEquals("Greenpath", flute.getTargetRoom());
        assertEquals("east", flute.getDirection());
    }

    /** Der Schlüssel heisst key und wiegt 0.2 kg. */
    @Test
    @DisplayName("Schlüssel heisst key und wiegt 0.2 kg")
    void keyHasNameAndWeight() {
        KeyItem key = KeyItem.createKey();

        assertEquals("key", key.getName());
        assertEquals(0.2, key.getWeight(), 0.0001);
    }

    /** Der Schlüssel öffnet im White Palace den Ausgang south. */
    @Test
    @DisplayName("Schlüssel öffnet White Palace south")
    void keyOpensWhitePalaceSouth() {
        KeyItem key = KeyItem.createKey();

        assertEquals("White Palace", key.getTargetRoom());
        assertEquals("south", key.getDirection());
    }

    /** Jeder Aufruf der Fabrikmethode liefert ein neues Objekt. */
    @Test
    @DisplayName("Fabrikmethode liefert jedes Mal ein neues Objekt")
    void factoryCreatesNewObjects() {
        assertNotSame(KeyItem.createFlute(), KeyItem.createFlute());
        assertNotSame(KeyItem.createKey(), KeyItem.createKey());
    }

    /** toString funktioniert auch für KeyItems. */
    @Test
    @DisplayName("toString zeigt Name und Gewicht")
    void toStringShowsNameAndWeight() {
        assertEquals("flute (0.50 kg)", KeyItem.createFlute().toString());
        assertEquals("key (0.20 kg)", KeyItem.createKey().toString());
    }
}
