package ch.bbw.zork;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests für {@link WorldBuilder}: Räume, Ausgänge, Einbahnwege, Sperren und Exit.
 */
class WorldBuilderTest {

	/** Die gebaute Spielwelt, wird vor jedem Test neu erstellt. */
	private WorldBuilder world;

	/** Baut vor jedem Test eine neue Welt mit festem Zufall. */
	@BeforeEach
	void setUp() {
		world = new WorldBuilder(new Random(42));
	}

	/** Es gibt genau 13 Räume in der Reihenfolge der Raumnamen-Tabelle. */
	@Test
	@DisplayName("13 Räume in der Reihenfolge der Tabelle")
	void hasThirteenRoomsInTableOrder() {
		List<Room> rooms = world.getRooms();

		assertEquals(13, rooms.size());
		assertEquals(WorldBuilder.ASHFALL_VALLEY, rooms.get(0).getName());
		assertEquals(WorldBuilder.EXIT, rooms.get(12).getName());
	}

	/** getRoom findet einen Raum über den Namen und gibt bei unbekanntem Namen null zurück. */
	@Test
	@DisplayName("getRoom findet Raum über den Namen, sonst null")
	void getRoomFindsRoomByName() {
		assertNotNull(world.getRoom("King's Pass"));
		assertEquals(WorldBuilder.KINGS_PASS, world.getRoom(WorldBuilder.KINGS_PASS).getName());
		assertNull(world.getRoom("Nowhere"));
	}

	/** Das Spiel startet in Ashfall Valley. */
	@Test
	@DisplayName("Startraum ist Ashfall Valley")
	void startRoomIsAshfallValley() {
		assertSame(world.getRoom(WorldBuilder.ASHFALL_VALLEY), world.getStartRoom());
	}

	/** Die Pokémon-Level stimmen mit der Tabelle im Plan überein (Stichproben). */
	@Test
	@DisplayName("Pokémon-Level stimmen mit dem Plan überein")
	void pokemonLevelsMatchPlan() {
		assertEquals(0, world.getRoom(WorldBuilder.ASHFALL_VALLEY).getPokemonLevel());
		assertEquals(30, world.getRoom(WorldBuilder.WHITE_PALACE).getPokemonLevel());
		assertEquals(50, world.getRoom(WorldBuilder.THE_ABYSS).getPokemonLevel());
		assertEquals(0, world.getRoom(WorldBuilder.GREENPATH).getPokemonLevel());
	}

	/** Ein normaler Weg (Ashfall Valley - City of Tears) geht in beide Richtungen. */
	@Test
	@DisplayName("Normaler Weg geht in beide Richtungen")
	void twoWayPathWorksInBothDirections() {
		Room ashfall = world.getRoom(WorldBuilder.ASHFALL_VALLEY);
		Room city = world.getRoom(WorldBuilder.CITY_OF_TEARS);

		assertSame(city, ashfall.nextRoom("north"));
		assertSame(ashfall, city.nextRoom("south"));
	}

	/** Bei den drei Einbahnwegen (ohne Exit) gibt es keinen Weg zurück. */
	@Test
	@DisplayName("Einbahnwege haben keinen Weg zurück")
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

	/** Greenpath east, White Palace south und north sind gesperrt, andere Ausgänge nicht. */
	@Test
	@DisplayName("Die drei Sperren sind gesetzt")
	void threeExitsAreLocked() {
		Room greenpath = world.getRoom(WorldBuilder.GREENPATH);
		Room whitePalace = world.getRoom(WorldBuilder.WHITE_PALACE);

		assertTrue(greenpath.isLocked("east"));
		assertTrue(whitePalace.isLocked("south"));
		assertTrue(whitePalace.isLocked("north"));
		assertEquals("A heavy locked door blocks the way down.", whitePalace.getLockMessage("south"));
		assertFalse(whitePalace.isLocked("east"));
	}

	/** Nur der Raum Exit ist der Gewinn-Raum. */
	@Test
	@DisplayName("Nur Exit ist der Gewinn-Raum")
	void onlyExitIsWinRoom() {
		for (Room room : world.getRooms()) {
			assertEquals(room.getName().equals(WorldBuilder.EXIT), room.isWinRoom(), room.getName());
		}
	}
}
