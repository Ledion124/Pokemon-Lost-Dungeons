package ch.bbw.zork;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests für {@link WorldBuilder}: Räume, Ausgänge, Einbahnwege, Sperren und Exit.
 */
class WorldBuilderTest {

	private WorldBuilder world;

	@BeforeEach
	void setUp() {
		world = new WorldBuilder(new Random(42));
	}

	@Test
	void hasThirteenRoomsInTableOrder() {
		List<Room> rooms = world.getRooms();

		assertEquals(13, rooms.size());
		assertEquals(WorldBuilder.ASHFALL_VALLEY, rooms.get(0).getName());
		assertEquals(WorldBuilder.EXIT, rooms.get(12).getName());
	}

	@Test
	void getRoomFindsRoomByName() {
		assertNotNull(world.getRoom("King's Pass"));
		assertEquals(WorldBuilder.KINGS_PASS, world.getRoom(WorldBuilder.KINGS_PASS).getName());
		assertNull(world.getRoom("Nowhere"));
	}

	@Test
	void startRoomIsAshfallValley() {
		assertSame(world.getRoom(WorldBuilder.ASHFALL_VALLEY), world.getStartRoom());
	}

	@Test
	void pokemonLevelsMatchPlan() {
		assertEquals(0, world.getRoom(WorldBuilder.ASHFALL_VALLEY).getPokemonLevel());
		assertEquals(30, world.getRoom(WorldBuilder.WHITE_PALACE).getPokemonLevel());
		assertEquals(50, world.getRoom(WorldBuilder.THE_ABYSS).getPokemonLevel());
		assertEquals(0, world.getRoom(WorldBuilder.GREENPATH).getPokemonLevel());
	}

	@Test
	void twoWayPathWorksInBothDirections() {
		Room ashfall = world.getRoom(WorldBuilder.ASHFALL_VALLEY);
		Room city = world.getRoom(WorldBuilder.CITY_OF_TEARS);

		assertSame(city, ashfall.nextRoom("north"));
		assertSame(ashfall, city.nextRoom("south"));
	}

	@Test
	void oneWayPathsHaveNoWayBack() {
		Room crossroads = world.getRoom(WorldBuilder.FORGOTTEN_CROSSROADS);
		Room fogCanyon = world.getRoom(WorldBuilder.FOG_CANYON);
		Room ashfall = world.getRoom(WorldBuilder.ASHFALL_VALLEY);
		Room kingsPass = world.getRoom(WorldBuilder.KINGS_PASS);
		Room treasures = world.getRoom(WorldBuilder.ANCIENT_TREASURES);

		assertTrue(crossroads.hasExitTo(fogCanyon));
		assertFalse(fogCanyon.hasExitTo(crossroads));

		assertTrue(fogCanyon.hasExitTo(ashfall));
		assertFalse(ashfall.hasExitTo(fogCanyon));

		assertTrue(kingsPass.hasExitTo(treasures));
		assertFalse(treasures.hasExitTo(kingsPass));
	}

	@Test
	void threeExitsAreLocked() {
		Room greenpath = world.getRoom(WorldBuilder.GREENPATH);
		Room whitePalace = world.getRoom(WorldBuilder.WHITE_PALACE);

		assertTrue(greenpath.isLocked("east"));
		assertTrue(whitePalace.isLocked("south"));
		assertTrue(whitePalace.isLocked("north"));
		assertEquals("A heavy locked door blocks the way down.", whitePalace.getLockMessage("south"));
		assertFalse(whitePalace.isLocked("east"));
	}

	@Test
	void onlyExitIsWinRoom() {
		for (Room room : world.getRooms()) {
			assertEquals(room.getName().equals(WorldBuilder.EXIT), room.isWinRoom(), room.getName());
		}
	}
}
