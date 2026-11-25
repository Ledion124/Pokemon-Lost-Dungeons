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

    /** Die 7 Räume, in denen die Flöte versteckt sein kann. */
    private static final List<String> FLUTE_ROOMS = List.of(
            WorldBuilder.ASHFALL_VALLEY, WorldBuilder.CITY_OF_TEARS, WorldBuilder.FORGOTTEN_CROSSROADS,
            WorldBuilder.FOG_CANYON, WorldBuilder.WHITE_PALACE, WorldBuilder.ANCIENT_TREASURES,
            WorldBuilder.ROYALE_WATERWAYS);

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
        int flutes = 0;
        for (Room room : world.getRooms()) {
            for (HidingSpot spot : room.getHidingSpots()) {
                if (isFlute(spot.getHiddenItem())) {
                    flutes++;
                }
            }
        }
        assertEquals(1, flutes);
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
        for (int seed = 0; seed < 50; seed++) {
            WorldBuilder randomWorld = new WorldBuilder(new Random(seed));
            assertTrue(FLUTE_ROOMS.contains(findFluteRoom(randomWorld).getName()), "seed " + seed);
        }
    }

    /** Die fünf Fundsachen liegen im richtigen Versteck und haben das richtige Gewicht. */
    @Test
    @DisplayName("Fundsachen liegen am richtigen Ort mit richtigem Gewicht")
    void foundItemsAreInTheirHidingSpots() {
        assertHiddenItem(WorldBuilder.KINGS_PASS, "stones", "stone", 5.0);
        assertHiddenItem(WorldBuilder.RESTING_GROUNDS, "crypt", "tablet", 12.0);
        assertHiddenItem(WorldBuilder.QUEENS_GARDENS, "fountain", "ring", 0.1);
        assertHiddenItem(WorldBuilder.GREENPATH, "tree", "honey", 0.8);
        assertHiddenItem(WorldBuilder.THE_ABYSS, "bones", "skull", 1.5);
    }

    /** Die Fundsachen sind normale Items und öffnen keinen Ausgang. */
    @Test
    @DisplayName("Fundsachen sind keine KeyItems")
    void foundItemsAreNoKeyItems() {
        assertFalse(world.getRoom(WorldBuilder.RESTING_GROUNDS).getHidingSpot("crypt").getHiddenItem()
                instanceof KeyItem);
        assertFalse(world.getRoom(WorldBuilder.KINGS_PASS).getHidingSpot("stones").getHiddenItem()
                instanceof KeyItem);
    }

    /**
     * In den 7 Flöten-Räumen ist ausser der Flöte nie etwas versteckt, egal wo die Flöte liegt.
     * So kann keine Fundsache die Flöte überschreiben.
     */
    @Test
    @DisplayName("In den Flöten-Räumen ist nur die Flöte versteckt")
    void fluteRoomsHideOnlyTheFlute() {
        for (int seed = 0; seed < 50; seed++) {
            WorldBuilder randomWorld = new WorldBuilder(new Random(seed));
            for (String roomName : FLUTE_ROOMS) {
                for (HidingSpot spot : randomWorld.getRoom(roomName).getHidingSpots()) {
                    Item hidden = spot.getHiddenItem();
                    assertTrue(hidden == null || isFlute(hidden),
                            "seed " + seed + ", " + roomName + ", " + spot.getName());
                }
            }
        }
    }

    /** Die Fundsachen liegen bei jedem Zufall am gleichen Ort, nur die Flöte wechselt. */
    @Test
    @DisplayName("Fundsachen hängen nicht vom Zufall ab")
    void foundItemsDoNotDependOnRandom() {
        for (int seed = 0; seed < 20; seed++) {
            WorldBuilder randomWorld = new WorldBuilder(new Random(seed));
            assertEquals("tablet", randomWorld.getRoom(WorldBuilder.RESTING_GROUNDS)
                    .getHidingSpot("crypt").getHiddenItem().getName(), "seed " + seed);
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
                if (isFlute(spot.getHiddenItem())) {
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
                if (isFlute(spot.getHiddenItem())) {
                    return room;
                }
            }
        }
        return fail("Flöte nicht gefunden");
    }

    /**
     * Prüft, ob ein Gegenstand die Flöte ist.
     *
     * @param item der Gegenstand oder null
     * @return true, wenn es ein KeyItem mit dem Namen "flute" ist
     */
    private static boolean isFlute(Item item) {
        return item instanceof KeyItem && item.getName().equals("flute");
    }

    /**
     * Prüft, ob in einem Versteck der erwartete Gegenstand mit dem erwarteten Gewicht liegt.
     *
     * @param roomName Name des Raums
     * @param spotName Befehlswort des Verstecks
     * @param itemName erwarteter Name des Gegenstands
     * @param weight   erwartetes Gewicht in kg
     */
    private void assertHiddenItem(String roomName, String spotName, String itemName, double weight) {
        HidingSpot spot = world.getRoom(roomName).getHidingSpot(spotName);
        assertNotNull(spot, roomName + ", " + spotName);
        Item item = spot.getHiddenItem();
        assertNotNull(item, roomName + ", " + spotName);
        assertEquals(itemName, item.getName());
        assertEquals(weight, item.getWeight(), 0.0001);
    }
}
