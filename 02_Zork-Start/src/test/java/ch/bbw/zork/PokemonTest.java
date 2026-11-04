package ch.bbw.zork;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse Pokemon.
 */
class PokemonTest {

    /** Charmander auf Level 5, wird vor jedem Test neu erstellt. */
    private Pokemon charmander;

    /**
     * Erstellt vor jedem Test einen frischen Charmander (Fire, 20 HP, 6 ATK, 8.5 kg, Level 5).
     */
    @BeforeEach
    void setUp() {
        charmander = new Pokemon("Charmander", PokemonType.FIRE, 20, 6, 8.5, 5);
    }

    /**
     * Ein neues Pokémon startet mit vollen HP und berechnet HP und Angriff aus dem Level.
     */
    @Test
    @DisplayName("Neues Pokémon hat volle HP und richtige Werte")
    void newPokemonHasFullHpAndCorrectStats() {
        assertEquals(30, charmander.getMaxHp());
        assertEquals(30, charmander.getCurrentHp());
        assertEquals(9, charmander.getAttack());
    }

    /**
     * Die Getter geben die Werte aus dem Konstruktor zurück.
     */
    @Test
    @DisplayName("Getter geben die Werte aus dem Konstruktor zurück")
    void gettersReturnConstructorValues() {
        assertEquals("Charmander", charmander.getName());
        assertEquals(PokemonType.FIRE, charmander.getType());
        assertEquals(5, charmander.getLevel());
        assertEquals(8.5, charmander.getWeight(), 0.001);
    }

    /**
     * Im Pokéball wiegt ein Pokémon ein Zehntel seines echten Gewichts.
     */
    @Test
    @DisplayName("Gewicht im Pokéball ist ein Zehntel")
    void carryWeightIsTenthOfWeight() {
        assertEquals(0.85, charmander.getCarryWeight(), 0.001);
    }

    /**
     * Schaden zieht HP ab, das Pokémon ist danach noch nicht besiegt.
     */
    @Test
    @DisplayName("takeDamage zieht HP ab")
    void takeDamageReducesHp() {
        charmander.takeDamage(10);
        assertEquals(20, charmander.getCurrentHp());
        assertFalse(charmander.isFainted());
    }

    /**
     * Zu viel Schaden setzt die HP auf 0 und nicht ins Negative, das Pokémon ist besiegt.
     */
    @Test
    @DisplayName("takeDamage setzt HP nie unter 0")
    void takeDamageNeverBelowZero() {
        charmander.takeDamage(100);
        assertEquals(0, charmander.getCurrentHp());
        assertTrue(charmander.isFainted());
    }

    /**
     * Heilen setzt die HP wieder auf das Maximum.
     */
    @Test
    @DisplayName("heal setzt HP auf das Maximum")
    void healRestoresFullHp() {
        charmander.takeDamage(25);
        charmander.heal();
        assertEquals(30, charmander.getCurrentHp());
    }

    /**
     * Level-Up erhöht das Level um 2, steigert die Werte und heilt vollständig.
     */
    @Test
    @DisplayName("levelUp gibt +2 Level und volle HP")
    void levelUpAddsTwoLevelsAndHeals() {
        charmander.takeDamage(10);
        charmander.levelUp();
        assertEquals(7, charmander.getLevel());
        assertEquals(34, charmander.getMaxHp());
        assertEquals(34, charmander.getCurrentHp());
    }

    /**
     * Feuer gegen Pflanze macht doppelten Schaden (9 × 2 = 18).
     */
    @Test
    @DisplayName("Feuer gegen Pflanze macht doppelten Schaden")
    void fireDoesDoubleDamageAgainstGrass() {
        Pokemon bulbasaur = new Pokemon("Bulbasaur", PokemonType.GRASS, 22, 5, 6.9, 5);
        assertEquals(18, charmander.getDamageAgainst(bulbasaur));
    }

    /**
     * Feuer gegen Gestein macht nur halben Schaden, abgerundet (9 × 0.5 = 4.5 → 4).
     */
    @Test
    @DisplayName("Feuer gegen Gestein macht halben Schaden")
    void fireDoesHalfDamageAgainstRock() {
        Pokemon geodude = new Pokemon("Geodude", PokemonType.ROCK, 28, 6, 20.0, 5);
        assertEquals(4, charmander.getDamageAgainst(geodude));
    }

    /**
     * Jeder Angriff macht mindestens 1 Schaden, auch wenn die Rechnung 0 ergibt.
     */
    @Test
    @DisplayName("Schaden ist mindestens 1")
    void damageIsAtLeastOne() {
        Pokemon weak = new Pokemon("Weak", PokemonType.FIRE, 10, 1, 1.0, 0);
        Pokemon geodude = new Pokemon("Geodude", PokemonType.ROCK, 28, 6, 20.0, 5);
        assertEquals(1, weak.getDamageAgainst(geodude));
    }

    /**
     * Normale Pokémon können verscheucht werden.
     */
    @Test
    @DisplayName("Normales Pokémon kann verscheucht werden")
    void normalPokemonCanBeScared() {
        assertTrue(charmander.canBeScared());
    }

    /**
     * Die Textausgabe zeigt Name, Typ, Level, HP, Angriff und Tragegewicht mit Punkt.
     */
    @Test
    @DisplayName("toString zeigt alle Werte mit Punkt")
    void toStringShowsAllValues() {
        assertEquals("Charmander (Fire) Lv 5 - HP 30/30, ATK 9, 0.85 kg", charmander.toString());
    }
}