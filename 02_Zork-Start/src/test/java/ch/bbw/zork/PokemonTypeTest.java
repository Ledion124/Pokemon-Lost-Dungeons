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

    /** Die Typ-Tabelle ist symmetrisch: Macht A gegen B doppelten Schaden, macht B gegen A halben. */
    @Test
    @DisplayName("Typ-Tabelle ist symmetrisch")
    void typeTableIsSymmetric() {
        for (PokemonType attacker : PokemonType.values()) {
            for (PokemonType defender : PokemonType.values()) {
                if (attacker.getMultiplier(defender) == 2.0) {
                    assertEquals(0.5, defender.getMultiplier(attacker), attacker + " gegen " + defender);
                }
            }
        }
    }

    /** Jeder der fünf normalen Typen ist gegen genau zwei Typen stark und gegen genau zwei schwach. */
    @Test
    @DisplayName("Jeder Typ ist gegen 2 stark und gegen 2 schwach")
    void eachTypeHasTwoStrengthsAndTwoWeaknesses() {
        for (PokemonType attacker : PokemonType.values()) {
            if (attacker == PokemonType.NORMAL) {
                continue;
            }
            int strong = 0;
            int weak = 0;
            for (PokemonType defender : PokemonType.values()) {
                double multiplier = attacker.getMultiplier(defender);
                if (multiplier == 2.0) {
                    strong++;
                } else if (multiplier == 0.5) {
                    weak++;
                }
            }
            assertEquals(2, strong, attacker.toString());
            assertEquals(2, weak, attacker.toString());
        }
    }

    /** Normal ist gegen jeden Typ und jeder Typ gegen Normal neutral. */
    @Test
    @DisplayName("Normal ist in beide Richtungen immer neutral")
    void normalIsAlwaysNeutralBothWays() {
        for (PokemonType type : PokemonType.values()) {
            assertEquals(1.0, PokemonType.NORMAL.getMultiplier(type));
            assertEquals(1.0, type.getMultiplier(PokemonType.NORMAL));
        }
    }
}