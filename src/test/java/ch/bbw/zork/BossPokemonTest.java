package ch.bbw.zork;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse BossPokemon.
 */
class BossPokemonTest {

    /** Ein Boss (Normal, 40 HP, 8 ATK, 320 kg, Level 50), wird vor jedem Test neu erstellt. */
    private BossPokemon boss;

    /**
     * Erstellt vor jedem Test einen frischen Boss direkt über den Konstruktor.
     */
    @BeforeEach
    void setUp() {
        boss = new BossPokemon("Arceus", PokemonType.NORMAL, 40, 8, 320.0, 50);
    }

    /**
     * Der Konstruktor gibt alle Werte an Pokemon weiter, der Boss startet mit vollen HP.
     */
    @Test
    @DisplayName("Konstruktor gibt die Werte an Pokemon weiter")
    void constructorPassesValuesToPokemon() {
        assertEquals("Arceus", boss.getName());
        assertEquals(PokemonType.NORMAL, boss.getType());
        assertEquals(50, boss.getLevel());
        assertEquals(240, boss.getMaxHp());
        assertEquals(240, boss.getCurrentHp());
        assertEquals(48, boss.getAttack());
    }

    /**
     * Ein Boss-Pokémon kann nicht verscheucht werden.
     */
    @Test
    @DisplayName("Boss kann nicht verscheucht werden")
    void bossCannotBeScared() {
        assertFalse(boss.canBeScared());
    }

    /**
     * Über eine Pokemon-Variable wird trotzdem die Boss-Version von canBeScared aufgerufen (Polymorphie).
     */
    @Test
    @DisplayName("Polymorphie: als Pokemon gespeichert, trotzdem nicht verscheuchbar")
    void canBeScaredIsOverriddenEvenAsPokemon() {
        Pokemon pokemon = boss;
        Pokemon normal = new Pokemon("Charmander", PokemonType.FIRE, 20, 6, 8.5, 5);
        assertFalse(pokemon.canBeScared());
        assertTrue(normal.canBeScared());
    }

    /**
     * Der Sieg-Text nennt den Namen des Bosses und den freien Weg zum Ausgang.
     */
    @Test
    @DisplayName("Sieg-Text nennt den Namen und den Ausgang")
    void defeatMessageMentionsNameAndExit() {
        String message = boss.getDefeatMessage();
        assertTrue(message.startsWith("Arceus"));
        assertTrue(message.contains("exit"));
    }

    /**
     * Der Boss erbt das Kampfverhalten: Schaden zieht HP ab, bei 0 HP ist er besiegt.
     */
    @Test
    @DisplayName("Boss nimmt Schaden und kann besiegt werden")
    void bossCanTakeDamageAndFaint() {
        boss.takeDamage(40);
        assertEquals(200, boss.getCurrentHp());
        boss.takeDamage(1000);
        assertTrue(boss.isFainted());
    }

    /**
     * Normal ist neutral: der Boss macht gegen jeden Typ normalen Schaden (48).
     */
    @Test
    @DisplayName("Boss macht gegen jeden Typ normalen Schaden")
    void bossDoesNeutralDamage() {
        Pokemon charmander = new Pokemon("Charmander", PokemonType.FIRE, 20, 6, 8.5, 5);
        Pokemon geodude = new Pokemon("Geodude", PokemonType.ROCK, 28, 6, 20.0, 5);
        assertEquals(48, boss.getDamageAgainst(charmander));
        assertEquals(48, boss.getDamageAgainst(geodude));
    }
}