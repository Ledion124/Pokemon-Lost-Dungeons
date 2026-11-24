package ch.bbw.zork;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests für {@link ItemActions}: take und drop. Die Ausgabe auf der Konsole
 * wird abgefangen, damit auch die Meldungen geprüft werden können.
 */
class ItemActionsTest {

    /** Raum, in dem der Spieler steht. */
    private Room room;

    /** Der Spieler, startet in {@link #room}. */
    private Player player;

    /** Die Item-Befehle, die getestet werden. */
    private ItemActions actions;

    /** Fängt alles ab, was die Befehle ausgeben. */
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    /** Die echte Konsolenausgabe, wird nach jedem Test wiederhergestellt. */
    private final PrintStream originalOut = System.out;

    /** Erstellt vor jedem Test einen leeren Raum, einen Spieler und fängt die Ausgabe ab. */
    @BeforeEach
    void setUp() {
        room = new Room("Hall", "A big hall.", 0);
        player = new Player(room);
        actions = new ItemActions(player);
        System.setOut(new PrintStream(output, true));
    }

    /** Stellt nach jedem Test die normale Konsolenausgabe wieder her. */
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * Gibt die bisherige Ausgabe zurück, ohne Zeilenumbrüche am Ende.
     *
     * @return die Ausgabe als Text
     */
    private String output() {
        return output.toString().replace("\r\n", "\n").trim();
    }

    // ===================== take =====================

    /** take nimmt ein Item aus dem Raum ins Inventar. */
    @Test
    @DisplayName("take nimmt Item aus dem Raum ins Inventar")
    void takeMovesItemToInventory() {
        KeyItem flute = KeyItem.createFlute();
        room.addItem(flute);

        actions.takeItem(new Command("take", "flute"));

        assertSame(flute, player.getInventory().get("flute"));
        assertNull(room.getItem("flute"));
    }

    /** take ohne zweites Wort fragt nach und ändert nichts. */
    @Test
    @DisplayName("take ohne zweites Wort fragt 'Take what?'")
    void takeWithoutSecondWord() {
        room.addItem(KeyItem.createFlute());

        actions.takeItem(new Command("take"));

        assertEquals("Take what?", output());
        assertTrue(player.getInventory().isEmpty());
    }

    /** take eines Items, das nicht im Raum liegt, gibt eine Meldung mit dem Namen. */
    @Test
    @DisplayName("take eines fehlenden Items meldet 'There is no ... here.'")
    void takeUnknownItem() {
        actions.takeItem(new Command("take", "key"));

        assertEquals("There is no key here.", output());
        assertTrue(player.getInventory().isEmpty());
    }

    /** Ein zu schweres Item bleibt im Raum liegen. */
    @Test
    @DisplayName("Zu schweres Item bleibt im Raum")
    void takeTooHeavyItem() {
        room.addItem(new Item("boulder", "a huge boulder", 25.0));

        actions.takeItem(new Command("take", "boulder"));

        assertEquals("That is too heavy. Drop something or release a Pokémon first.", output());
        assertNotNull(room.getItem("boulder"));
        assertTrue(player.getInventory().isEmpty());
    }

    /** Ein Item, das genau bis zum Maximalgewicht reicht, kann noch genommen werden. */
    @Test
    @DisplayName("Item genau an der 20-kg-Grenze kann genommen werden")
    void takeItemUpToMaxWeight() {
        player.getInventory().add(new Item("rock", "a heavy rock", 19.5));
        room.addItem(KeyItem.createFlute());

        actions.takeItem(new Command("take", "flute"));

        assertNotNull(player.getInventory().get("flute"));
    }

    // ===================== drop =====================

    /** drop legt ein Item aus dem Inventar in den Raum. */
    @Test
    @DisplayName("drop legt Item aus dem Inventar in den Raum")
    void dropMovesItemToRoom() {
        KeyItem key = KeyItem.createKey();
        player.getInventory().add(key);

        actions.dropItem(new Command("drop", "KEY"));

        assertSame(key, room.getItem("key"));
        assertTrue(player.getInventory().isEmpty());
    }

    /** drop ohne zweites Wort fragt nach. */
    @Test
    @DisplayName("drop ohne zweites Wort fragt 'Drop what?'")
    void dropWithoutSecondWord() {
        actions.dropItem(new Command("drop"));

        assertEquals("Drop what?", output());
    }

    /** drop eines Items, das man nicht hat, gibt eine Meldung. */
    @Test
    @DisplayName("drop eines fehlenden Items meldet 'You don't have that.'")
    void dropUnknownItem() {
        actions.dropItem(new Command("drop", "flute"));

        assertEquals("You don't have that.", output());
        assertTrue(room.getItems().isEmpty());
    }
}
