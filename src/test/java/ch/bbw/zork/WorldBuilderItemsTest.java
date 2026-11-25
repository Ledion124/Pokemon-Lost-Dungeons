package ch.bbw.zork;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests für die Gegenstände in der Welt, die {@link WorldBuilder} mit placeItems verteilt.
 *
 * @author Silvan Castelberg
 */
class WorldBuilderItemsTest {

    /** Die Spielwelt, immer mit demselben Zufall gebaut. */
    private WorldBuilder world;

    /** Erstellt vor jedem Test eine Welt mit {@code new Random(42)}. */
    @BeforeEach
    void setUp() {
        world = new WorldBuilder(new Random(42));
    }

    /** Der Schlüssel liegt offen in den Queen's Gardens. */
    @Test
    @DisplayName("Schlüssel liegt in Queen's Gardens")
    void keyLiesInQueensGardens() {
        Item key = world.getRoom(WorldBuilder.QUEENS_GARDENS).getItem("key");

        assertInstanceOf(KeyItem.class, key);
        assertEquals(WorldBuilder.WHITE_PALACE, ((KeyItem) key).getTargetRoom());
    }

    /** Ausser in den Queen's Gardens liegt am Anfang nirgends ein Item offen herum. */
    @Test
    @DisplayName("Nur in Queen's Gardens liegt am Anfang ein Item")
    void onlyQueensGardensHasItems() {
        for (Room room : world.getRooms()) {
            int expected = room.getName().equals(WorldBuilder.QUEENS_GARDENS) ? 1 : 0;
            assertEquals(expected, room.getItems().size(), room.getName());
        }
    }

    /** Jeder Raum ausser Exit hat Stellen zum Untersuchen, Exit hat keine. */
    @Test
    @DisplayName("Jeder Raum ausser Exit hat Verstecke")
    void everyRoomExceptExitHasHidingSpots() {
        for (Room room : world.getRooms()) {
            if (room.getName().equals(WorldBuilder.EXIT)) {
                assertTrue(room.getHidingSpots().isEmpty());
            } else {
                assertFalse(room.getHidingSpots().isEmpty(), room.getName());
            }
        }
    }

    /** Die Verstecke aus der Tabelle in Idee.md sind da, z. B. im Ashfall Valley. */
    @Test
    @DisplayName("Ashfall Valley hat ash, stump und crack")
    void ashfallValleyHasHidingSpotsFromTable() {
        Room ashfallValley = world.getRoom(WorldBuilder.ASHFALL_VALLEY);

        assertEquals(3, ashfallValley.getHidingSpots().size());
        assertNotNull(ashfallValley.getHidingSpot("ash"));
        assertNotNull(ashfallValley.getHidingSpot("stump"));
        assertNotNull(ashfallValley.getHidingSpot("crack"));
    }

    /** Die Flöte ist in der ganzen Welt genau einmal versteckt. */
    @Test
    @DisplayName("Flöte ist genau einmal versteckt")
    void fluteIsHiddenExactlyOnce() {
        int hidden = 0;
        for (Room room : world.getRooms()) {
            for (HidingSpot spot : room.getHidingSpots()) {
                if (spot.hasHiddenItem()) {
                    hidden++;
                }
            }
        }
        assertEquals(1, hidden);
    }

    /** Was versteckt ist, ist wirklich die Flöte. */
    @Test
    @DisplayName("Das versteckte Item ist die Flöte")
    void hiddenItemIsFlute() {
        Item found = findFluteSpot(world).search();

        assertInstanceOf(KeyItem.class, found);
        assertEquals("flute", found.getName());
    }

    /** Die Flöte liegt immer in einem der 7 Räume, die man vor Relaxo erreicht. */
    @Test
    @DisplayName("Flöte liegt in einem der 7 erreichbaren Räume")
    void fluteIsInReachableRoom() {
        List<String> fluteRooms = List.of(
                WorldBuilder.ASHFALL_VALLEY, WorldBuilder.CITY_OF_TEARS, WorldBuilder.FORGOTTEN_CROSSROADS,
                WorldBuilder.FOG_CANYON, WorldBuilder.WHITE_PALACE, WorldBuilder.ANCIENT_TREASURES,
                WorldBuilder.ROYALE_WATERWAYS);

        for (int seed = 0; seed < 50; seed++) {
            WorldBuilder randomWorld = new WorldBuilder(new Random(seed));
            assertTrue(fluteRooms.contains(findFluteRoom(randomWorld).getName()), "seed " + seed);
        }
    }

    /** Mit demselben Zufall liegt die Flöte immer am selben Ort. */
    @Test
    @DisplayName("Mit Random(42) liegt die Flöte immer am selben Ort")
    void sameSeedSamePlace() {
        WorldBuilder other = new WorldBuilder(new Random(42));

        assertEquals(findFluteRoom(world).getName(), findFluteRoom(other).getName());
        assertEquals(findFluteSpot(world).getName(), findFluteSpot(other).getName());
    }

    /**
     * Sucht die Stelle, in der die Flöte versteckt ist.
     *
     * @param world die Spielwelt
     * @return die Stelle mit der Flöte
     */
    private static HidingSpot findFluteSpot(WorldBuilder world) {
        for (Room room : world.getRooms()) {
            for (HidingSpot spot : room.getHidingSpots()) {
                if (spot.hasHiddenItem()) {
                    return spot;
                }
            }
        }
        return fail("Flöte nicht gefunden");
    }

    /**
     * Sucht den Raum, in dem die Flöte versteckt ist.
     *
     * @param world die Spielwelt
     * @return der Raum mit der Flöte
     */
    private static Room findFluteRoom(WorldBuilder world) {
        for (Room room : world.getRooms()) {
            for (HidingSpot spot : room.getHidingSpots()) {
                if (spot.hasHiddenItem()) {
                    return room;
                }
            }
        }
        return fail("Flöte nicht gefunden");
    }
}
