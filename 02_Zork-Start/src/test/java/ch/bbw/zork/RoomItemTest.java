package ch.bbw.zork;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests für den Item- und Versteck-Teil von {@link Room}.
 */
class RoomItemTest {

    /** Raum, der getestet wird, vor jedem Test ohne Items und Verstecke. */
    private Room room;

    /** Flöte für die Tests. */
    private KeyItem flute;

    /** Schlüssel für die Tests. */
    private KeyItem key;

    /** Erstellt vor jedem Test einen leeren Raum und zwei Gegenstände. */
    @BeforeEach
    void setUp() {
        room = new Room("Hall", "A big hall.", 0);
        flute = KeyItem.createFlute();
        key = KeyItem.createKey();
    }

    // ===================== Items =====================

    /** Ein neuer Raum hat keine Gegenstände. */
    @Test
    @DisplayName("Neuer Raum hat keine Items")
    void newRoomHasNoItems() {
        assertTrue(room.getItems().isEmpty());
    }

    /** addItem legt Gegenstände in der richtigen Reihenfolge in den Raum. */
    @Test
    @DisplayName("addItem legt Items in Reihenfolge in den Raum")
    void addItemKeepsOrder() {
        room.addItem(flute);
        room.addItem(key);

        assertEquals(List.of(flute, key), room.getItems());
    }

    /** getItem findet einen Gegenstand ohne Beachtung der Gross-/Kleinschreibung und lässt ihn liegen. */
    @Test
    @DisplayName("getItem findet Item unabhängig von Gross-/Kleinschreibung")
    void getItemIgnoresCase() {
        room.addItem(flute);

        assertSame(flute, room.getItem("Flute"));
        assertEquals(1, room.getItems().size());
    }

    /** getItem gibt null zurück, wenn der Gegenstand nicht im Raum liegt. */
    @Test
    @DisplayName("getItem gibt null bei unbekanntem Item")
    void getItemUnknownReturnsNull() {
        room.addItem(flute);

        assertNull(room.getItem("key"));
        assertNull(room.getItem(null));
    }

    /** removeItem nimmt den Gegenstand aus dem Raum und gibt ihn zurück. */
    @Test
    @DisplayName("removeItem nimmt Item aus dem Raum")
    void removeItemReturnsItem() {
        room.addItem(flute);
        room.addItem(key);

        assertSame(key, room.removeItem("KEY"));
        assertEquals(List.of(flute), room.getItems());
    }

    /** removeItem eines unbekannten Gegenstands gibt null zurück und ändert nichts. */
    @Test
    @DisplayName("removeItem eines unbekannten Items gibt null")
    void removeItemUnknownReturnsNull() {
        room.addItem(flute);

        assertNull(room.removeItem("key"));
        assertEquals(1, room.getItems().size());
    }

    /** getItems gibt eine Kopie zurück, Änderungen daran wirken nicht auf den Raum. */
    @Test
    @DisplayName("getItems gibt eine Kopie zurück")
    void getItemsReturnsCopy() {
        room.addItem(flute);

        room.getItems().clear();

        assertEquals(1, room.getItems().size());
    }

    // ===================== Verstecke =====================

    /** Ein neuer Raum hat keine Stellen zum Untersuchen. */
    @Test
    @DisplayName("Neuer Raum hat keine Verstecke")
    void newRoomHasNoHidingSpots() {
        assertTrue(room.getHidingSpots().isEmpty());
    }

    /** addHidingSpot fügt Stellen in der richtigen Reihenfolge hinzu. */
    @Test
    @DisplayName("addHidingSpot fügt Verstecke in Reihenfolge hinzu")
    void addHidingSpotKeepsOrder() {
        HidingSpot ash = new HidingSpot("ash", "a pile of ash");
        HidingSpot stump = new HidingSpot("stump", "an old tree stump");
        room.addHidingSpot(ash);
        room.addHidingSpot(stump);

        assertEquals(List.of(ash, stump), room.getHidingSpots());
    }

    /** getHidingSpot findet eine Stelle ohne Beachtung der Gross-/Kleinschreibung. */
    @Test
    @DisplayName("getHidingSpot findet Versteck unabhängig von Gross-/Kleinschreibung")
    void getHidingSpotIgnoresCase() {
        HidingSpot ash = new HidingSpot("ash", "a pile of ash");
        room.addHidingSpot(ash);

        assertSame(ash, room.getHidingSpot("ASH"));
    }

    /** getHidingSpot gibt null zurück, wenn es die Stelle nicht gibt. */
    @Test
    @DisplayName("getHidingSpot gibt null bei unbekanntem Versteck")
    void getHidingSpotUnknownReturnsNull() {
        room.addHidingSpot(new HidingSpot("ash", "a pile of ash"));

        assertNull(room.getHidingSpot("crack"));
        assertNull(room.getHidingSpot(null));
    }

    /** getHidingSpots gibt eine Kopie zurück, Änderungen daran wirken nicht auf den Raum. */
    @Test
    @DisplayName("getHidingSpots gibt eine Kopie zurück")
    void getHidingSpotsReturnsCopy() {
        room.addHidingSpot(new HidingSpot("ash", "a pile of ash"));

        room.getHidingSpots().clear();

        assertEquals(1, room.getHidingSpots().size());
    }
}
