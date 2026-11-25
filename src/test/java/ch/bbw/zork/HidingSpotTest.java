package ch.bbw.zork;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests für {@link HidingSpot}: Name, Beschreibung, Verstecken und Untersuchen.
 */
class HidingSpotTest {

    /** Stelle, die getestet wird, vor jedem Test leer. */
    private HidingSpot ash;

    /** Erstellt vor jedem Test eine leere Stelle. */
    @BeforeEach
    void setUp() {
        ash = new HidingSpot("ash", "a pile of ash");
    }

    /** Der Konstruktor speichert Befehlswort und Beschreibung. */
    @Test
    @DisplayName("Konstruktor speichert Befehlswort und Beschreibung")
    void constructorSetsNameAndDescription() {
        assertEquals("ash", ash.getName());
        assertEquals("a pile of ash", ash.getDescription());
    }

    /** Eine neue Stelle ist leer. */
    @Test
    @DisplayName("Neue Stelle ist leer")
    void newSpotIsEmpty() {
        assertFalse(ash.hasHiddenItem());
    }

    /** Eine leere Stelle zu untersuchen liefert null. */
    @Test
    @DisplayName("search an leerer Stelle gibt null")
    void searchEmptySpotReturnsNull() {
        assertNull(ash.search());
    }

    /** Nach hideItem ist ein Gegenstand versteckt. */
    @Test
    @DisplayName("hideItem versteckt einen Gegenstand")
    void hideItemStoresItem() {
        ash.hideItem(KeyItem.createFlute());

        assertTrue(ash.hasHiddenItem());
    }

    /** search gibt den versteckten Gegenstand zurück. */
    @Test
    @DisplayName("search findet den versteckten Gegenstand")
    void searchReturnsHiddenItem() {
        KeyItem flute = KeyItem.createFlute();
        ash.hideItem(flute);

        assertSame(flute, ash.search());
    }

    /** Nach dem Untersuchen ist die Stelle leer. */
    @Test
    @DisplayName("Nach search ist die Stelle leer")
    void spotIsEmptyAfterSearch() {
        ash.hideItem(KeyItem.createFlute());
        ash.search();

        assertFalse(ash.hasHiddenItem());
    }

    /** Wer zweimal sucht, findet beim zweiten Mal nichts mehr. */
    @Test
    @DisplayName("Zweites search findet nichts mehr")
    void secondSearchReturnsNull() {
        ash.hideItem(KeyItem.createFlute());
        ash.search();

        assertNull(ash.search());
    }
}
