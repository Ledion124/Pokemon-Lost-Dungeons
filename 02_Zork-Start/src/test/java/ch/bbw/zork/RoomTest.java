package ch.bbw.zork;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests für {@link Room}: Ausgänge, Sperren, hasExitTo und die Beschreibungen.
 */
class RoomTest {

	private Room hall;
	private Room north;
	private Room east;
	private Room south;
	private Room west;

	@BeforeEach
	void setUp() {
		hall = new Room("Hall", "A big hall.", 5);
		north = new Room("North", "North room.", 0);
		east = new Room("East", "East room.", 0);
		south = new Room("South", "South room.", 0);
		west = new Room("West", "West room.", 0);
	}

	@Test
	void constructorSetsNameDescriptionAndLevel() {
		assertEquals("Hall", hall.getName());
		assertEquals("A big hall.", hall.getDescription());
		assertEquals(5, hall.getPokemonLevel());
		assertFalse(hall.isWinRoom());
	}

	@Test
	void nullExitIsNoExit() {
		hall.setExits(north, null, null, null);

		assertSame(north, hall.nextRoom("north"));
		assertNull(hall.nextRoom("east"));
		assertEquals("Exits: north", hall.exitString());
	}

	@Test
	void lockedExitBlocksHasExitToButNextRoomStillWorks() {
		hall.setExits(north, null, null, null);
		hall.lockExit("north", "Blocked!");

		assertTrue(hall.isLocked("north"));
		assertEquals("Blocked!", hall.getLockMessage("north"));
		assertFalse(hall.hasExitTo(north));
		assertSame(north, hall.nextRoom("north"));
	}

	@Test
	void unlockExitOpensItAgain() {
		hall.setExits(north, null, null, null);
		hall.lockExit("north", "Blocked!");
		hall.unlockExit("north");

		assertFalse(hall.isLocked("north"));
		assertNull(hall.getLockMessage("north"));
		assertTrue(hall.hasExitTo(north));
	}

	@Test
	void oneWayExitHasNoWayBack() {
		hall.setExits(null, null, south, null);

		assertTrue(hall.hasExitTo(south));
		assertFalse(south.hasExitTo(hall));
	}

	@Test
	void exitStringHasFixedOrderAndMarksBlockedExits() {
		hall.setExits(north, east, south, west);
		hall.lockExit("south", "Blocked!");

		assertEquals("Exits: north east south(blocked) west", hall.exitString());
	}

	@Test
	void longDescriptionHasNameDescriptionAndExits() {
		hall.setExits(null, east, null, null);

		assertEquals("You are in Hall. A big hall.\nExits: east", hall.longDescription());
	}

	@Test
	void winRoomCanBeSet() {
		hall.setWinRoom(true);

		assertTrue(hall.isWinRoom());
	}
}
