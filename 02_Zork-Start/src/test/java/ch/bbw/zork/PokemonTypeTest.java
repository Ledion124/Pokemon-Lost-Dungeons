package ch.bbw.zork;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Tests für {@link PokemonType}: Anzeigename und Schadensfaktoren.
 */
class PokemonTypeTest {

    /** Feuer macht doppelten Schaden gegen Pflanze. */
    @Test
    void fireIsStrongAgainstGrass() {
        assertEquals(2.0, PokemonType.FIRE.getMultiplier(PokemonType.GRASS));
    }

    /** Feuer macht nur halben Schaden gegen Gestein. */
    @Test
    void fireIsWeakAgainstRock() {
        assertEquals(0.5, PokemonType.FIRE.getMultiplier(PokemonType.ROCK));
    }

    /** Gleicher Typ gegen gleichen Typ ergibt normalen Schaden. */
    @Test
    void sameTypeIsNeutral() {
        assertEquals(1.0, PokemonType.FIRE.getMultiplier(PokemonType.FIRE));
    }

    /** Normal als Angreifer macht immer normalen Schaden. */
    @Test
    void normalAttackerIsAlwaysNeutral() {
        assertEquals(1.0, PokemonType.NORMAL.getMultiplier(PokemonType.ICE));
    }

    /** Normal als Verteidiger bekommt immer normalen Schaden. */
    @Test
    void normalDefenderIsAlwaysNeutral() {
        assertEquals(1.0, PokemonType.ICE.getMultiplier(PokemonType.NORMAL));
    }

    /** Boden macht doppelten Schaden gegen Feuer. */
    @Test
    void groundIsStrongAgainstFire() {
        assertEquals(2.0, PokemonType.GROUND.getMultiplier(PokemonType.FIRE));
    }

    /** Pflanze macht nur halben Schaden gegen Eis. */
    @Test
    void grassIsWeakAgainstIce() {
        assertEquals(0.5, PokemonType.GRASS.getMultiplier(PokemonType.ICE));
    }

    /** Der Anzeigename ist der englische Name in normaler Schreibweise. */
    @Test
    void displayNameIsReadable() {
        assertEquals("Ground", PokemonType.GROUND.getDisplayName());
    }
}
