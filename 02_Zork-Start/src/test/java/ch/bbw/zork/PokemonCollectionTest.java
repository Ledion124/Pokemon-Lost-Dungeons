package ch.bbw.zork;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse PokemonCollection.
 */
class PokemonCollectionTest {

    /** Sammlung, wird vor jedem Test neu und leer erstellt. */
    private PokemonCollection collection;

    /** Charmander (Fire, Level 5, 0.85 kg im Pokéball). */
    private Pokemon charmander;

    /** Bulbasaur (Grass, Level 5, 0.69 kg im Pokéball). */
    private Pokemon bulbasaur;

    /**
     * Erstellt vor jedem Test eine leere Sammlung und zwei Pokémon.
     */
    @BeforeEach
    void setUp() {
        collection = new PokemonCollection();
        charmander = new Pokemon("Charmander", PokemonType.FIRE, 20, 6, 8.5, 5);
        bulbasaur = new Pokemon("Bulbasaur", PokemonType.GRASS, 22, 5, 6.9, 5);
    }

    /**
     * Nach dem Hinzufügen zählt die Sammlung richtig.
     */
    @Test
    void addIncreasesSize() {
        collection.add(charmander);
        collection.add(bulbasaur);
        assertEquals(2, collection.size());
    }

    /**
     * Die Nummerierung beginnt bei 1: Nummer 1 ist das erste hinzugefügte Pokémon.
     */
    @Test
    void getUsesNumbersStartingAtOne() {
        collection.add(charmander);
        collection.add(bulbasaur);
        assertSame(charmander, collection.get(1));
        assertSame(bulbasaur, collection.get(2));
    }

    /**
     * Ungültige Nummern (0, negativ, zu gross) geben null zurück statt abzustürzen.
     */
    @Test
    void getWithInvalidNumberReturnsNull() {
        collection.add(charmander);
        assertNull(collection.get(0));
        assertNull(collection.get(-1));
        assertNull(collection.get(2));
    }

    /**
     * Entfernen gibt das entfernte Pokémon zurück, die übrigen rücken nach vorne.
     */
    @Test
    void removeReturnsRemovedPokemon() {
        collection.add(charmander);
        collection.add(bulbasaur);
        assertSame(charmander, collection.remove(1));
        assertEquals(1, collection.size());
        assertSame(bulbasaur, collection.get(1));
    }

    /**
     * Entfernen mit ungültiger Nummer gibt null zurück und ändert nichts.
     */
    @Test
    void removeWithInvalidNumberReturnsNull() {
        collection.add(charmander);
        assertNull(collection.remove(5));
        assertEquals(1, collection.size());
    }

    /**
     * getAll gibt eine Kopie zurück: Änderungen daran verändern die Sammlung nicht.
     */
    @Test
    void getAllReturnsCopy() {
        collection.add(charmander);
        List<Pokemon> all = collection.getAll();
        all.clear();
        assertEquals(1, collection.size());
    }

    /**
     * healAll heilt alle Pokémon wieder auf volle HP.
     */
    @Test
    void healAllRestoresAllPokemon() {
        collection.add(charmander);
        collection.add(bulbasaur);
        charmander.takeDamage(10);
        bulbasaur.takeDamage(100);
        collection.healAll();
        assertEquals(charmander.getMaxHp(), charmander.getCurrentHp());
        assertEquals(bulbasaur.getMaxHp(), bulbasaur.getCurrentHp());
    }

    /**
     * Das Gesamtgewicht ist die Summe der Gewichte im Pokéball (0.85 + 0.69).
     */
    @Test
    void totalWeightIsSumOfCarryWeights() {
        collection.add(charmander);
        collection.add(bulbasaur);
        assertEquals(1.54, collection.getTotalWeight(), 0.001);
    }

    /**
     * Eine leere Sammlung wiegt nichts.
     */
    @Test
    void emptyCollectionHasZeroWeight() {
        assertEquals(0.0, collection.getTotalWeight(), 0.001);
    }
}
