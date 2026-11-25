package ch.bbw.zork;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für {@link WorldActions}: Benutzen von Gegenständen mit {@code use},
 * die Karte und die Starter-Auswahl. Geprüft wird der Zustand danach
 * (Ausgang offen, Item weg, Pokémon im Team) bzw. die Zeilen der Karte.
 */
class WorldActionsTest {

	/** Die Spielwelt, immer gleich dank festem Zufall. */
	private WorldBuilder world;

	/** Greenpath, hier ist der Ausgang east durch den Snorlax gesperrt. */
	private Room greenpath;

	/** White Palace, hier ist der Ausgang south durch die Tür gesperrt. */
	private Room whitePalace;

	/** Baut die Welt mit {@code new Random(42)}. */
	@BeforeEach
	void setUp() {
		world = new WorldBuilder(new Random(42));
		greenpath = world.getRoom(WorldBuilder.GREENPATH);
		whitePalace = world.getRoom(WorldBuilder.WHITE_PALACE);
	}

	/**
	 * Erstellt die Welt-Befehle für einen Spieler, ohne Eingaben.
	 *
	 * @param player der Spieler
	 * @return die Welt-Befehle
	 */
	private WorldActions actionsFor(Player player) {
		return actionsFor(player, "");
	}

	/**
	 * Erstellt die Welt-Befehle für einen Spieler mit vorgegebenen Eingaben.
	 *
	 * @param player der Spieler
	 * @param input  alle Eingabezeilen, getrennt mit {@code \n}
	 * @return die Welt-Befehle
	 */
	private WorldActions actionsFor(Player player, String input) {
		Parser parser = new Parser(new ByteArrayInputStream(input.getBytes()));
		return new WorldActions(parser, world, player);
	}

	/**
	 * Sucht die Zeile der Karte, die zu einem Raum gehört.
	 *
	 * @param lines    alle Zeilen der Karte
	 * @param roomName Name des Raums
	 * @return die passende Zeile
	 */
	private String lineFor(List<String> lines, String roomName) {
		for (String line : lines) {
			if (line.substring(2).startsWith(roomName + " ")) {
				return line;
			}
		}
		fail("Keine Zeile für " + roomName);
		return null;
	}

	/** Die Flöte in Greenpath öffnet den Ausgang east. */
	@Test
	@DisplayName("Flöte in Greenpath öffnet den Weg nach Osten")
	void fluteUnlocksGreenpathEast() {
		Player player = new Player(greenpath);
		player.getInventory().add(KeyItem.createFlute());

		actionsFor(player).useItem(new Command("use", "flute"));

		assertFalse(greenpath.isLocked("east"));
	}

	/** Nach dem Benutzen ist die Flöte nicht mehr im Inventar. */
	@Test
	@DisplayName("Benutzte Flöte ist aus dem Inventar weg")
	void usedFluteIsRemoved() {
		Player player = new Player(greenpath);
		player.getInventory().add(KeyItem.createFlute());

		actionsFor(player).useItem(new Command("use", "flute"));

		assertNull(player.getInventory().get("flute"));
	}

	/** Der Schlüssel im White Palace öffnet nur south, der Exit bleibt verschüttet. */
	@Test
	@DisplayName("Schlüssel öffnet im White Palace nur den Weg nach Süden")
	void keyUnlocksOnlyWhitePalaceSouth() {
		Player player = new Player(whitePalace);
		player.getInventory().add(KeyItem.createKey());

		actionsFor(player).useItem(new Command("use", "key"));

		assertFalse(whitePalace.isLocked("south"));
		assertTrue(whitePalace.isLocked("north"));
	}

	/** Im falschen Raum passiert nichts und die Flöte bleibt im Inventar. */
	@Test
	@DisplayName("Flöte im falschen Raum bewirkt nichts")
	void fluteInWrongRoomDoesNothing() {
		Player player = new Player(whitePalace);
		player.getInventory().add(KeyItem.createFlute());

		actionsFor(player).useItem(new Command("use", "flute"));

		assertTrue(greenpath.isLocked("east"));
		assertNotNull(player.getInventory().get("flute"));
	}

	/** Ohne das Item im Inventar bleibt der Ausgang gesperrt. */
	@Test
	@DisplayName("Ohne Item im Inventar bleibt alles gesperrt")
	void missingItemDoesNothing() {
		Player player = new Player(greenpath);

		actionsFor(player).useItem(new Command("use", "flute"));

		assertTrue(greenpath.isLocked("east"));
	}

	/** Ein normales Item (kein KeyItem) öffnet nichts und bleibt im Inventar. */
	@Test
	@DisplayName("Normales Item öffnet keinen Ausgang")
	void normalItemDoesNotUnlock() {
		Player player = new Player(greenpath);
		player.getInventory().add(new Item("stone", "a grey stone", 1.0));

		actionsFor(player).useItem(new Command("use", "stone"));

		assertTrue(greenpath.isLocked("east"));
		assertNotNull(player.getInventory().get("stone"));
	}

	/** use ohne zweites Wort ändert nichts. */
	@Test
	@DisplayName("use ohne Item-Namen ändert nichts")
	void useWithoutSecondWordDoesNothing() {
		Player player = new Player(greenpath);
		player.getInventory().add(KeyItem.createFlute());

		actionsFor(player).useItem(new Command("use"));

		assertTrue(greenpath.isLocked("east"));
		assertNotNull(player.getInventory().get("flute"));
	}

	/** Die Karte hat für jeden der 13 Räume genau eine Zeile. */
	@Test
	@DisplayName("Karte hat eine Zeile pro Raum")
	void mapHasOneLinePerRoom() {
		Player player = new Player(world.getStartRoom());

		List<String> lines = actionsFor(player).mapLines();

		assertEquals(world.getRooms().size(), lines.size());
		assertEquals(13, lines.size());
	}

	/** Nur der aktuelle Raum ist mit > markiert. */
	@Test
	@DisplayName("Karte markiert nur den aktuellen Raum mit >")
	void mapMarksOnlyCurrentRoom() {
		Player player = new Player(greenpath);

		List<String> lines = actionsFor(player).mapLines();

		assertTrue(lineFor(lines, WorldBuilder.GREENPATH).startsWith(">"));
		assertEquals(1, lines.stream().filter(line -> line.startsWith(">")).count());
	}

	/** Die Karte zeigt gesperrte Ausgänge und Items im Raum. */
	@Test
	@DisplayName("Karte zeigt Sperren und Items")
	void mapShowsBlockedExitsAndItems() {
		Player player = new Player(world.getStartRoom());
		world.getRoom(WorldBuilder.QUEENS_GARDENS).addItem(KeyItem.createKey());

		List<String> lines = actionsFor(player).mapLines();

		assertTrue(lineFor(lines, WorldBuilder.GREENPATH).contains("Blocked: east"));
		assertTrue(lineFor(lines, WorldBuilder.QUEENS_GARDENS).contains("Items: key"));
	}

	/** Nach dem Öffnen zeigt die Karte den Ausgang nicht mehr als gesperrt. */
	@Test
	@DisplayName("Karte zeigt geöffneten Ausgang nicht mehr als gesperrt")
	void mapUpdatesAfterUnlock() {
		Player player = new Player(greenpath);
		player.getInventory().add(KeyItem.createFlute());
		WorldActions actions = actionsFor(player);

		actions.useItem(new Command("use", "flute"));

		assertTrue(lineFor(actions.mapLines(), WorldBuilder.GREENPATH).contains("Blocked: -"));
	}

	/** Eingabe 2 gibt Charmander mit Level 5. */
	@Test
	@DisplayName("Eingabe 2 wählt Charmander mit Level 5")
	void choosesCharmander() {
		Player player = new Player(world.getStartRoom());

		actionsFor(player, "2\n").chooseStarter();

		assertEquals(1, player.getPokemons().size());
		assertEquals("Charmander", player.getPokemons().get(1).getName());
		assertEquals(5, player.getPokemons().get(1).getLevel());
	}

	/** Nach ungültigen Eingaben wird nochmals gefragt, bis eine gültige kommt. */
	@Test
	@DisplayName("Ungültige Eingaben werden übersprungen")
	void invalidInputAsksAgain() {
		Player player = new Player(world.getStartRoom());

		actionsFor(player, "abc\n9\n0\n3\n").chooseStarter();

		assertEquals(1, player.getPokemons().size());
		assertEquals("Bulbasaur", player.getPokemons().get(1).getName());
	}

	/** Bei leerer Eingabe (Ende der Eingabe) gibt es Cubchoo. */
	@Test
	@DisplayName("Leere Eingabe wählt Cubchoo")
	void emptyInputChoosesCubchoo() {
		Player player = new Player(world.getStartRoom());

		actionsFor(player, "").chooseStarter();

		assertEquals(1, player.getPokemons().size());
		assertEquals("Cubchoo", player.getPokemons().get(1).getName());
	}
}
