package ch.bbw.zork;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse Pokedex.
 */
class PokedexTest {

    /**
     * Der Katalog enthält genau 20 Pokémon, ohne Arceus und Snorlax.
     */
    @Test
    void catalogHasTwentyPokemon() {
        List<String> names = Pokedex.getAllNames();
        assertEquals(20, names.size());
        assertFalse(names.contains("Arceus"));
        assertFalse(names.contains("Snorlax"));
    }

    /**
     * create erstellt das gewünschte Pokémon mit den Werten aus der Tabelle.
     */
    @Test
    void createReturnsPokemonWithCorrectValues() {
        Pokemon charmander = Pokedex.create("Charmander", 5);
        assertEquals("Charmander", charmander.getName());
        assertEquals(PokemonType.FIRE, charmander.getType());
        assertEquals(5, charmander.getLevel());
        assertEquals(30, charmander.getMaxHp());
        assertEquals(9, charmander.getAttack());
        assertEquals(8.5, charmander.getWeight(), 0.001);
    }

    /**
     * Ein unbekannter Name gibt null zurück.
     */
    @Test
    void createWithUnknownNameReturnsNull() {
        assertNull(Pokedex.create("Pikachu", 5));
    }

    /**
     * Jeder Name aus dem Katalog kann erstellt werden und hat den richtigen Namen.
     */
    @Test
    void everyNameCanBeCreated() {
        for (String name : Pokedex.getAllNames()) {
            Pokemon pokemon = Pokedex.create(name, 1);
            assertNotNull(pokemon, name);
            assertEquals(name, pokemon.getName());
        }
    }

    /**
     * Jeder Typ ausser Normal kommt genau 4-mal vor.
     */
    @Test
    void eachTypeHasFourPokemon() {
        int[] counts = new int[PokemonType.values().length];
        for (String name : Pokedex.getAllNames()) {
            counts[Pokedex.create(name, 1).getType().ordinal()]++;
        }
        assertEquals(4, counts[PokemonType.FIRE.ordinal()]);
        assertEquals(4, counts[PokemonType.GRASS.ordinal()]);
        assertEquals(4, counts[PokemonType.ICE.ordinal()]);
        assertEquals(4, counts[PokemonType.GROUND.ordinal()]);
        assertEquals(4, counts[PokemonType.ROCK.ordinal()]);
        assertEquals(0, counts[PokemonType.NORMAL.ordinal()]);
    }

    /**
     * createRandom erstellt ein Pokémon aus dem Katalog mit dem gewünschten Level und vollen HP.
     */
    @Test
    void createRandomReturnsCatalogPokemonWithLevel() {
        Pokemon pokemon = Pokedex.createRandom(7, new Random(42));
        assertTrue(Pokedex.getAllNames().contains(pokemon.getName()));
        assertEquals(7, pokemon.getLevel());
        assertEquals(pokemon.getMaxHp(), pokemon.getCurrentHp());
    }

    /**
     * Mit gleichem Startwert liefert createRandom das gleiche Pokémon.
     */
    @Test
    void createRandomWithSameSeedGivesSamePokemon() {
        Pokemon first = Pokedex.createRandom(3, new Random(123));
        Pokemon second = Pokedex.createRandom(3, new Random(123));
        assertEquals(first.getName(), second.getName());
    }

    /**
     * Jeder Aufruf erstellt ein neues Objekt, Schaden an einem betrifft das andere nicht.
     */
    @Test
    void createReturnsNewObjectEachTime() {
        Pokemon first = Pokedex.create("Geodude", 5);
        Pokemon second = Pokedex.create("Geodude", 5);
        assertNotSame(first, second);
        first.takeDamage(10);
        assertEquals(second.getMaxHp(), second.getCurrentHp());
    }

    /**
     * getAllNames gibt eine Kopie zurück: Änderungen daran verändern den Katalog nicht.
     */
    @Test
    void getAllNamesReturnsCopy() {
        Pokedex.getAllNames().clear();
        assertEquals(20, Pokedex.getAllNames().size());
    }
}
