package ch.bbw.zork;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests für {@link PokemonType}: Anzeigename und Schadensfaktoren.
 */
class PokemonTypeTest {

    /** Feuer macht doppelten Schaden gegen Pflanze. */
    @Test
    @DisplayName("Feuer gegen Pflanze gibt 2.0")
    void fireIsStrongAgainstGrass() {
        assertEquals(2.0, PokemonType.FIRE.getMultiplier(PokemonType.GRASS));
    }

    /** Feuer macht nur halben Schaden gegen Gestein. */
    @Test
    @DisplayName("Feuer gegen Gestein gibt 0.5")
    void fireIsWeakAgainstRock() {
        assertEquals(0.5, PokemonType.FIRE.getMultiplier(PokemonType.ROCK));
    }

    /** Gleicher Typ gegen gleichen Typ ergibt normalen Schaden. */
    @Test
    @DisplayName("Gleicher Typ gibt 1.0")
    void sameTypeIsNeutral() {
        assertEquals(1.0, PokemonType.FIRE.getMultiplier(PokemonType.FIRE));
    }

    /** Normal als Angreifer macht immer normalen Schaden. */
    @Test
    @DisplayName("Normal als Angreifer gibt immer 1.0")
    void normalAttackerIsAlwaysNeutral() {
        assertEquals(1.0, PokemonType.NORMAL.getMultiplier(PokemonType.ICE));
    }

    /** Normal als Verteidiger bekommt immer normalen Schaden. */
    @Test
    @DisplayName("Normal als Verteidiger gibt immer 1.0")
    void normalDefenderIsAlwaysNeutral() {
        assertEquals(1.0, PokemonType.ICE.getMultiplier(PokemonType.NORMAL));
    }

    /** Boden macht doppelten Schaden gegen Feuer. */
    @Test
    @DisplayName("Boden gegen Feuer gibt 2.0")
    void groundIsStrongAgainstFire() {
        assertEquals(2.0, PokemonType.GROUND.getMultiplier(PokemonType.FIRE));
    }

    /** Pflanze macht nur halben Schaden gegen Eis. */
    @Test
    @DisplayName("Pflanze gegen Eis gibt 0.5")
    void grassIsWeakAgainstIce() {
        assertEquals(0.5, PokemonType.GRASS.getMultiplier(PokemonType.ICE));
    }

    /** Der Anzeigename ist der englische Name in normaler Schreibweise. */
    @Test
    @DisplayName("Anzeigename ist lesbar, z. B. Fire")
    void displayNameIsReadable() {
        assertEquals("Ground", PokemonType.GROUND.getDisplayName());
    }
}