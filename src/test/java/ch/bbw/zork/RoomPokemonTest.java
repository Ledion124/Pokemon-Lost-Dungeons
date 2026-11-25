package ch.bbw.zork;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests für den Pokémon-Teil von {@link Room}.
 *
 * @author Ledion Sakaj
 */
class RoomPokemonTest {

    /** Raum, der getestet wird, vor jedem Test ohne Pokémon. */
    private Room room;

    /** Wildes Pokémon für die Tests. */
    private Pokemon charmander;

    /** Erstellt vor jedem Test einen leeren Spawn-Raum und ein Pokémon. */
    @BeforeEach
    void setUp() {
        room = new Room("Hall", "A big hall.", 5);
        charmander = Pokedex.create("Charmander", 5);
    }

    /** Ein neuer Raum hat kein wildes Pokémon. */
    @Test
    @DisplayName("Neuer Raum hat kein wildes Pokémon")
    void newRoomHasNoWildPokemon() {
        assertNull(room.getWildPokemon());
        assertFalse(room.hasWildPokemon());
    }

    /** setWildPokemon setzt das Pokémon, getWildPokemon gibt genau dieses zurück. */
    @Test
    @DisplayName("setWildPokemon setzt das Pokémon")
    void setWildPokemonStoresPokemon() {
        room.setWildPokemon(charmander);
        assertSame(charmander, room.getWildPokemon());
        assertTrue(room.hasWildPokemon());
    }

    /** removeWildPokemon macht den Raum wieder leer. */
    @Test
    @DisplayName("removeWildPokemon entfernt das Pokémon")
    void removeWildPokemonEmptiesRoom() {
        room.setWildPokemon(charmander);
        room.removeWildPokemon();
        assertNull(room.getWildPokemon());
        assertFalse(room.hasWildPokemon());
    }

    /** Ein Raum mit Level über 0 ist ein Spawn-Raum. */
    @Test
    @DisplayName("Raum mit Level > 0 ist Spawn-Raum")
    void roomWithLevelIsSpawnRoom() {
        assertTrue(room.isSpawnRoom());
    }

    /** Ein Raum mit Level 0 ist kein Spawn-Raum. */
    @Test
    @DisplayName("Raum mit Level 0 ist kein Spawn-Raum")
    void roomWithLevelZeroIsNoSpawnRoom() {
        assertFalse(new Room("Start", "The start.", 0).isSpawnRoom());
    }

    /** In The Abyss steht nur der Boss, darum ist es kein Spawn-Raum. */
    @Test
    @DisplayName("The Abyss ist kein Spawn-Raum")
    void abyssIsNoSpawnRoom() {
        assertFalse(new Room(WorldBuilder.THE_ABYSS, "Dark.", 50).isSpawnRoom());
    }

    /** longDescription zeigt das wilde Pokémon in der letzten Zeile. */
    @Test
    @DisplayName("longDescription zeigt das wilde Pokémon am Ende")
    void longDescriptionShowsWildPokemon() {
        room.setWildPokemon(charmander);
        String description = room.longDescription();
        assertTrue(description.endsWith("A wild " + charmander + " appears!"));
    }

    /** Ohne Pokémon steht in longDescription keine Pokémon-Zeile. */
    @Test
    @DisplayName("longDescription ohne Pokémon hat keine Pokémon-Zeile")
    void longDescriptionWithoutPokemon() {
        assertFalse(room.longDescription().contains("A wild"));
    }
}
