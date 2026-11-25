package ch.bbw.zork;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests für {@link PokemonActions}. Die Eingaben des Spielers kommen
 * aus einem String statt von der Konsole.
 *
 * @author Ledion Sakaj
 */
class PokemonActionsTest {

    /** Die Spielwelt, immer mit demselben Zufall gebaut. */
    private WorldBuilder world;

    /** Der Spieler, startet in City of Tears (Level 5). */
    private Player player;

    /** Erstellt vor jedem Test eine Welt und einen Spieler mit einem starken Larvitar. */
    @BeforeEach
    void setUp() {
        world = new WorldBuilder(new Random(42));
        player = new Player(world.getRoom(WorldBuilder.CITY_OF_TEARS));
        player.getPokemons().add(Pokedex.create("Larvitar", 60));
    }

    /**
     * Erstellt die Pokémon-Befehle mit einer festen Eingabe.
     *
     * @param input alles, was der Spieler eintippt, Zeilen mit \n getrennt
     * @return neue Pokémon-Befehle
     */
    private PokemonActions actions(String input) {
        Parser parser = new Parser(new ByteArrayInputStream(input.getBytes()));
        return new PokemonActions(parser, world, player, new Random(42));
    }

    /** spawnAll setzt in jeden Spawn-Raum ein Pokémon und sonst nirgends. */
    @Test
    @DisplayName("spawnAll füllt genau die Spawn-Räume")
    void spawnAllFillsOnlySpawnRooms() {
        actions("").spawnAll();
        for (Room room : world.getRooms()) {
            assertEquals(room.isSpawnRoom(), room.hasWildPokemon(), room.getName());
        }
    }

    /** Ein gespawntes Pokémon hat das Level des Raums. */
    @Test
    @DisplayName("Gespawntes Pokémon hat das Level des Raums")
    void spawnedPokemonHasRoomLevel() {
        actions("").spawnAll();
        Room fogCanyon = world.getRoom(WorldBuilder.FOG_CANYON);
        assertEquals(fogCanyon.getPokemonLevel(), fogCanyon.getWildPokemon().getLevel());
    }

    /** Beim Betreten von The Abyss steht Arceus da, solange er nicht besiegt ist. */
    @Test
    @DisplayName("onEnterRoom setzt den Boss in The Abyss")
    void onEnterAbyssSetsBoss() {
        Room abyss = world.getRoom(WorldBuilder.THE_ABYSS);
        actions("").onEnterRoom(abyss);
        assertInstanceOf(BossPokemon.class, abyss.getWildPokemon());
    }

    /** scare entfernt ein normales Pokémon aus dem Raum. */
    @Test
    @DisplayName("scare verscheucht ein normales Pokémon")
    void scareRemovesPokemon() {
        Room room = player.getCurrentRoom();
        room.setWildPokemon(Pokedex.create("Diglett", 5));
        actions("").scare();
        assertFalse(room.hasWildPokemon());
    }

    /** Der Boss lässt sich nicht verscheuchen. */
    @Test
    @DisplayName("scare verscheucht den Boss nicht")
    void scareDoesNotRemoveBoss() {
        Room room = player.getCurrentRoom();
        room.setWildPokemon(Pokedex.createBoss());
        actions("").scare();
        assertTrue(room.hasWildPokemon());
    }

    /** Bei einer ungültigen Teamauswahl wird nicht gekämpft. */
    @Test
    @DisplayName("Ungültiges Team bricht den Kampf ab")
    void invalidTeamCancelsFight() {
        Room room = player.getCurrentRoom();
        room.setWildPokemon(Pokedex.create("Diglett", 5));
        assertFalse(actions("1 1\n").fight());
        assertTrue(room.hasWildPokemon());
        assertEquals(1, player.getPokemons().size());
    }

    /** Nach einem Sieg ist das Pokémon weg aus dem Raum und in der Sammlung. */
    @Test
    @DisplayName("Sieg fängt das Pokémon")
    void winCatchesPokemon() {
        Room room = player.getCurrentRoom();
        Pokemon diglett = Pokedex.create("Diglett", 5);
        room.setWildPokemon(diglett);
        assertFalse(actions("1\n").fight());
        assertFalse(room.hasWildPokemon());
        assertSame(diglett, player.getPokemons().get(2));
    }

    /** Ein Sieg gegen den Boss öffnet den Ausgang im White Palace, er wird nicht gefangen. */
    @Test
    @DisplayName("Sieg gegen den Boss öffnet den Exit")
    void bossWinUnlocksExit() {
        player.getPokemons().add(Pokedex.create("Magmar", 60));
        Room room = player.getCurrentRoom();
        room.setWildPokemon(Pokedex.createBoss());
        assertFalse(actions("1 2\n").fight());
        assertFalse(world.getRoom(WorldBuilder.WHITE_PALACE).isLocked("north"));
        assertEquals(2, player.getPokemons().size());
    }

    /** Verliert der Spieler, gibt fight true zurück, damit Game neu startet. */
    @Test
    @DisplayName("Niederlage gibt true zurück")
    void lossReturnsTrue() {
        player.getPokemons().remove(1);
        player.getPokemons().add(Pokedex.create("Diglett", 1));
        player.getCurrentRoom().setWildPokemon(Pokedex.createBoss());
        assertTrue(actions("1\n").fight());
    }

    /** release entfernt ein Pokémon, aber nie das letzte. */
    @Test
    @DisplayName("release entfernt ein Pokémon, aber nicht das letzte")
    void releaseKeepsLastPokemon() {
        player.getPokemons().add(Pokedex.create("Diglett", 5));
        PokemonActions actions = actions("");
        actions.release(new Command("release", "2"));
        assertEquals(1, player.getPokemons().size());
        actions.release(new Command("release", "1"));
        assertEquals(1, player.getPokemons().size());
    }
}
