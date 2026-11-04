package ch.bbw.zork;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests für {@link Room}: Ausgänge, Sperren, hasExitTo und die Beschreibungen.
 */
class RoomTest {

	/** Raum, dessen Ausgänge und Sperren getestet werden. */
	private Room hall;

	/** Nachbarraum im Norden. */
	private Room north;

	/** Nachbarraum im Osten. */
	private Room east;

	/** Nachbarraum im Süden. */
	private Room south;

	/** Nachbarraum im Westen. */
	private Room west;

	/** Erstellt vor jedem Test frische Räume ohne Ausgänge. */
	@BeforeEach
	void setUp() {
		hall = new Room("Hall", "A big hall.", 5);
		north = new Room("North", "North room.", 0);
		east = new Room("East", "East room.", 0);
		south = new Room("South", "South room.", 0);
		west = new Room("West", "West room.", 0);
	}

	/** Der Konstruktor speichert Name, Beschreibung und Level, ein neuer Raum ist kein Gewinn-Raum. */
	@Test
	@DisplayName("Konstruktor speichert Name, Beschreibung und Level")
	void constructorSetsNameDescriptionAndLevel() {
		assertEquals("Hall", hall.getName());
		assertEquals("A big hall.", hall.getDescription());
		assertEquals(5, hall.getPokemonLevel());
		assertFalse(hall.isWinRoom());
	}

	/** null bei setExits wird nicht als Ausgang eingetragen. */
	@Test
	@DisplayName("null wird nicht als Ausgang eingetragen")
	void nullExitIsNoExit() {
		hall.setExits(north, null, null, null);

		assertSame(north, hall.nextRoom("north"));
		assertNull(hall.nextRoom("east"));
		assertEquals("Exits: north", hall.exitString());
	}

	/** Ein gesperrter Ausgang blockiert hasExitTo, nextRoom liefert den Raum aber trotzdem. */
	@Test
	@DisplayName("Gesperrter Ausgang blockiert hasExitTo, nextRoom liefert Raum trotzdem")
	void lockedExitBlocksHasExitToButNextRoomStillWorks() {
		hall.setExits(north, null, null, null);
		hall.lockExit("north", "Blocked!");

		assertTrue(hall.isLocked("north"));
		assertEquals("Blocked!", hall.getLockMessage("north"));
		assertFalse(hall.hasExitTo(north));
		assertSame(north, hall.nextRoom("north"));
	}

	/** Nach unlockExit ist der Ausgang wieder offen und hat keinen Sperrtext mehr. */
	@Test
	@DisplayName("unlockExit öffnet den Ausgang wieder")
	void unlockExitOpensItAgain() {
		hall.setExits(north, null, null, null);
		hall.lockExit("north", "Blocked!");
		hall.unlockExit("north");

		assertFalse(hall.isLocked("north"));
		assertNull(hall.getLockMessage("north"));
		assertTrue(hall.hasExitTo(north));
	}

	/** Ein Ausgang, der nur auf einer Seite eingetragen ist, führt nicht zurück (Einbahnweg). */
	@Test
	@DisplayName("Einbahnweg führt nicht zurück")
	void oneWayExitHasNoWayBack() {
		hall.setExits(null, null, south, null);

		assertTrue(hall.hasExitTo(south));
		assertFalse(south.hasExitTo(hall));
	}

	/** exitString listet die Ausgänge in der Reihenfolge north, east, south, west und markiert gesperrte. */
	@Test
	@DisplayName("exitString in fester Reihenfolge, gesperrte mit (blocked)")
	void exitStringHasFixedOrderAndMarksBlockedExits() {
		hall.setExits(north, east, south, west);
		hall.lockExit("south", "Blocked!");

		assertEquals("Exits: north east south(blocked) west", hall.exitString());
	}

	/** longDescription hat das Format aus dem Plan: Name, Beschreibung, dann die Ausgänge. */
	@Test
	@DisplayName("longDescription mit Name, Beschreibung und Ausgängen")
	void longDescriptionHasNameDescriptionAndExits() {
		hall.setExits(null, east, null, null);

		assertEquals("You are in Hall. A big hall.\nExits: east", hall.longDescription());
	}

	/** setWinRoom(true) macht den Raum zum Gewinn-Raum. */
	@Test
	@DisplayName("setWinRoom macht den Raum zum Gewinn-Raum")
	void winRoomCanBeSet() {
		hall.setWinRoom(true);

		assertTrue(hall.isWinRoom());
	}
}
