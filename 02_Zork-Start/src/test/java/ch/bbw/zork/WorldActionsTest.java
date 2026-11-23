package ch.bbw.zork;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für {@link WorldActions}: Benutzen von Gegenständen mit {@code use}.
 * Geprüft wird der Zustand danach (Ausgang offen, Item weg), nicht die Ausgabe.
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
		Parser parser = new Parser(new ByteArrayInputStream(new byte[0]));
		return new WorldActions(parser, world, player);
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
}
