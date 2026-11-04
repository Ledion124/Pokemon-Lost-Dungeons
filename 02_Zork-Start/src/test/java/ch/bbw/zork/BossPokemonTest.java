package ch.bbw.zork;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse BossPokemon und Pokedex.createBoss().
 */
class BossPokemonTest {

    /** Der Boss Arceus, wird vor jedem Test neu erstellt. */
    private BossPokemon arceus;

    /**
     * Erstellt vor jedem Test einen frischen Arceus über den Pokedex.
     */
    @BeforeEach
    void setUp() {
        arceus = Pokedex.createBoss();
    }

    /**
     * createBoss erstellt Arceus mit Typ Normal und Level 50.
     */
    @Test
    @DisplayName("createBoss erstellt Arceus, Normal, Level 50")
    void createBossReturnsArceus() {
        assertEquals("Arceus", arceus.getName());
        assertEquals(PokemonType.NORMAL, arceus.getType());
        assertEquals(50, arceus.getLevel());
        assertEquals(320.0, arceus.getWeight(), 0.001);
    }

    /**
     * Auf Level 50 hat Arceus 240 HP und 48 Angriff (Faktor 6) und startet mit vollen HP.
     */
    @Test
    @DisplayName("Arceus hat 240 HP und 48 Angriff")
    void bossHasStatsForLevelFifty() {
        assertEquals(240, arceus.getMaxHp());
        assertEquals(240, arceus.getCurrentHp());
        assertEquals(48, arceus.getAttack());
    }

    /**
     * Ein Boss-Pokémon kann nicht verscheucht werden.
     */
    @Test
    @DisplayName("Boss kann nicht verscheucht werden")
    void bossCannotBeScared() {
        assertFalse(arceus.canBeScared());
    }

    /**
     * Über eine Pokemon-Variable wird trotzdem die Boss-Version von canBeScared aufgerufen (Polymorphie).
     */
    @Test
    @DisplayName("Polymorphie: als Pokemon gespeichert, trotzdem nicht verscheuchbar")
    void canBeScaredIsOverriddenEvenAsPokemon() {
        Pokemon pokemon = arceus;
        assertFalse(pokemon.canBeScared());
        assertTrue(Pokedex.create("Charmander", 5).canBeScared());
    }

    /**
     * Der Sieg-Text nennt den Namen des Bosses und den freien Weg zum Ausgang.
     */
    @Test
    @DisplayName("Sieg-Text nennt Arceus und den Ausgang")
    void defeatMessageMentionsNameAndExit() {
        String message = arceus.getDefeatMessage();
        assertTrue(message.startsWith("Arceus"));
        assertTrue(message.contains("exit"));
    }

    /**
     * Der Boss erbt das Kampfverhalten: Schaden zieht HP ab, bei 0 HP ist er besiegt.
     */
    @Test
    @DisplayName("Boss nimmt Schaden und kann besiegt werden")
    void bossCanTakeDamageAndFaint() {
        arceus.takeDamage(40);
        assertEquals(200, arceus.getCurrentHp());
        arceus.takeDamage(1000);
        assertTrue(arceus.isFainted());
    }

    /**
     * Normal ist neutral: Arceus macht gegen jeden Typ normalen Schaden (48).
     */
    @Test
    @DisplayName("Arceus macht gegen jeden Typ normalen Schaden")
    void bossDoesNeutralDamage() {
        assertEquals(48, arceus.getDamageAgainst(Pokedex.create("Charmander", 5)));
        assertEquals(48, arceus.getDamageAgainst(Pokedex.create("Geodude", 5)));
    }

    /**
     * Arceus ist nicht im normalen Katalog und kann daher nicht zufällig erscheinen.
     */
    @Test
    @DisplayName("Arceus ist nicht im normalen Katalog")
    void bossIsNotInCatalog() {
        assertFalse(Pokedex.getAllNames().contains("Arceus"));
        assertNull(Pokedex.create("Arceus", 50));
    }

    /**
     * Jeder Aufruf von createBoss erstellt einen neuen Boss mit vollen HP.
     */
    @Test
    @DisplayName("createBoss erstellt jedes Mal einen neuen Boss")
    void createBossReturnsNewObjectEachTime() {
        arceus.takeDamage(100);
        BossPokemon fresh = Pokedex.createBoss();
        assertNotSame(arceus, fresh);
        assertEquals(240, fresh.getCurrentHp());
    }
}
