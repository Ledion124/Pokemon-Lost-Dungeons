package ch.bbw.zork;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse Battle.
 */
class BattleTest {

    /** Charmander (Fire, Level 5: 30 HP, 9 ATK). */
    private Pokemon charmander;

    /** Bulbasaur (Grass, Level 5: 33 HP, 7 ATK). */
    private Pokemon bulbasaur;

    /** Sehr schwaches Pokémon (Normal, Level 0: 1 HP, 1 ATK), fällt beim ersten Treffer. */
    private Pokemon weak;

    /**
     * Erstellt vor jedem Test frische Pokémon.
     */
    @BeforeEach
    void setUp() {
        charmander = new Pokemon("Charmander", PokemonType.FIRE, 20, 6, 8.5, 5);
        bulbasaur = new Pokemon("Bulbasaur", PokemonType.GRASS, 22, 5, 6.9, 5);
        weak = new Pokemon("Weak", PokemonType.NORMAL, 1, 1, 1.0, 0);
    }

    /**
     * Feuer gegen Pflanze: Charmander gewinnt, der Log zeigt doppelten Schaden im richtigen Format.
     */
    @Test
    @DisplayName("Typ-Vorteil verdoppelt den Schaden, Log im richtigen Format")
    void typeAdvantageDoublesDamage() {
        Battle battle = new Battle(List.of(charmander), bulbasaur);

        assertTrue(battle.fight());
        List<String> log = battle.getLog();
        assertEquals(3, log.size());
        assertEquals("Charmander hits Bulbasaur for 18 damage (Bulbasaur: 15/33 HP)", log.get(0));
        assertEquals("Bulbasaur hits Charmander for 3 damage (Charmander: 27/30 HP)", log.get(1));
        assertEquals("Charmander hits Bulbasaur for 18 damage (Bulbasaur: 0/33 HP)", log.get(2));
    }

    /**
     * Der Spieler greift zuerst an: ein Gegner mit 1 HP fällt, ohne zurückzuschlagen.
     */
    @Test
    @DisplayName("Spieler greift zuerst an, Gegner mit 1 HP schlägt nicht zurück")
    void playerAttacksFirst() {
        Battle battle = new Battle(List.of(charmander), weak);

        assertTrue(battle.fight());
        assertEquals(1, battle.getLog().size());
        assertTrue(battle.getLog().get(0).startsWith("Charmander hits Weak"));
    }

    /**
     * Fällt das erste Pokémon, kämpft das nächste im Team weiter und gewinnt.
     */
    @Test
    @DisplayName("Teamwechsel: nach einem K.o. kämpft das nächste Pokémon")
    void nextPokemonFightsAfterKnockout() {
        Battle battle = new Battle(List.of(weak, charmander), bulbasaur);

        assertTrue(battle.fight());
        List<String> log = battle.getLog();
        int fainted = log.indexOf("Weak fainted!");
        assertTrue(fainted >= 0);
        assertTrue(log.get(fainted + 1).startsWith("Charmander hits Bulbasaur"));
    }

    /**
     * Bei Sieg bekommen nur Pokémon, die wirklich gekämpft haben, +2 Level.
     */
    @Test
    @DisplayName("Level +2 nur für eingesetzte Pokémon")
    void onlyFightersLevelUp() {
        Battle battle = new Battle(List.of(charmander, bulbasaur), weak);

        assertTrue(battle.fight());
        assertEquals(7, charmander.getLevel());
        assertEquals(5, bulbasaur.getLevel());
    }

    /**
     * Auch ein Pokémon, das im Kampf gefallen ist, bekommt bei Sieg +2 Level.
     */
    @Test
    @DisplayName("Gefallenes, aber eingesetztes Pokémon levelt bei Sieg mit")
    void faintedFighterAlsoLevelsUp() {
        Battle battle = new Battle(List.of(weak, charmander), bulbasaur);

        assertTrue(battle.fight());
        assertEquals(2, weak.getLevel());
        assertEquals(7, charmander.getLevel());
    }

    /**
     * Nach dem Kampf ist das ganze Team wieder voll geheilt.
     */
    @Test
    @DisplayName("Nach dem Kampf ist das ganze Team geheilt")
    void wholeTeamIsHealedAfterBattle() {
        Battle battle = new Battle(List.of(weak, charmander), bulbasaur);

        battle.fight();
        assertFalse(weak.isFainted());
        assertEquals(weak.getMaxHp(), weak.getCurrentHp());
        assertEquals(charmander.getMaxHp(), charmander.getCurrentHp());
    }

    /**
     * Bei Sieg wird auch der Gegner geheilt, damit er gefangen werden kann.
     */
    @Test
    @DisplayName("Bei Sieg wird auch der Gegner geheilt")
    void enemyIsHealedAfterWin() {
        Battle battle = new Battle(List.of(charmander), bulbasaur);

        assertTrue(battle.fight());
        assertEquals(33, bulbasaur.getCurrentHp());
    }

    /**
     * Ist das ganze Team besiegt, verliert der Spieler: kein Level-Up, Team geheilt, Gegner nicht.
     */
    @Test
    @DisplayName("Niederlage: kein Level-Up, Team geheilt, Gegner nicht geheilt")
    void defeatWhenWholeTeamFaints() {
        Pokemon geodude = new Pokemon("Geodude", PokemonType.ROCK, 28, 6, 20.0, 5);
        Battle battle = new Battle(List.of(weak), geodude);

        assertFalse(battle.fight());
        assertTrue(battle.getLog().contains("Weak fainted!"));
        assertEquals(0, weak.getLevel());
        assertEquals(weak.getMaxHp(), weak.getCurrentHp());
        assertEquals(41, geodude.getCurrentHp());
    }

    /**
     * Gegen den Boss Arceus (Level 50) verliert ein Starter auf Level 5.
     */
    @Test
    @DisplayName("Starter verliert gegen Arceus")
    void starterLosesAgainstBoss() {
        Battle battle = new Battle(List.of(charmander), Pokedex.createBoss());

        assertFalse(battle.fight());
        assertEquals(5, charmander.getLevel());
    }

    /**
     * getLog gibt eine Kopie zurück: Änderungen daran verändern den Log des Kampfs nicht.
     */
    @Test
    @DisplayName("getLog gibt eine Kopie zurück")
    void getLogReturnsCopy() {
        Battle battle = new Battle(List.of(charmander), weak);
        battle.fight();

        battle.getLog().clear();
        assertEquals(1, battle.getLog().size());
    }

    /**
     * Der Kampf nutzt eine Kopie des Teams: Änderungen an der Originalliste stören ihn nicht.
     */
    @Test
    @DisplayName("Kampf nutzt eine Kopie der Team-Liste")
    void constructorCopiesTeam() {
        List<Pokemon> team = new ArrayList<>(List.of(charmander));
        Battle battle = new Battle(team, weak);
        team.clear();

        assertTrue(battle.fight());
    }
}
