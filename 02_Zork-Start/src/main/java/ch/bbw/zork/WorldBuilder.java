package ch.bbw.zork;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Baut die Spielwelt: alle 13 Räume mit Beschreibung, Pokémon-Level,
 * Ausgängen und Sperren. Siehe docs/Idee.md (Räume, Ausgänge).
 */
public class WorldBuilder {

	/** Raumname: Startraum. */
	public static final String ASHFALL_VALLEY = "Ashfall Valley";
	/** Raumname: verlassene Stadt im Regen. */
	public static final String CITY_OF_TEARS = "City of Tears";
	/** Raumname: Kreuzung mit Einbahnweg nach Süden in den Fog Canyon. */
	public static final String FORGOTTEN_CROSSROADS = "Forgotten Crossroads";
	/** Raumname: Schlucht im Nebel, Einbahnweg nach Osten zurück ins Ashfall Valley. */
	public static final String FOG_CANYON = "Fog Canyon";
	/** Raumname: Palast mit den Sperren nach The Abyss (Süden) und Exit (Norden). */
	public static final String WHITE_PALACE = "White Palace";
	/** Raumname: Raum des Bosses Arceus. */
	public static final String THE_ABYSS = "The Abyss";
	/** Raumname: Schatzkammer. */
	public static final String ANCIENT_TREASURES = "Ancient Treasures";
	/** Raumname: Bergpass mit Einbahnweg nach Westen zu den Ancient Treasures. */
	public static final String KINGS_PASS = "King's Pass";
	/** Raumname: Garten, in dem der Schlüssel liegt. */
	public static final String QUEENS_GARDENS = "Queen's Gardens";
	/** Raumname: ruhiger Friedhof. */
	public static final String RESTING_GROUNDS = "Resting Grounds";
	/** Raumname: Pfad, auf dem Relaxo den Weg nach Osten versperrt. */
	public static final String GREENPATH = "Greenpath";
	/** Raumname: unterirdische Kanäle. */
	public static final String ROYALE_WATERWAYS = "Royale Waterways";
	/** Raumname: Ausgang, wer ihn betritt, gewinnt. */
	public static final String EXIT = "Exit";

	/** Zufallsgenerator, mit dem in Block 3 die Flöte versteckt wird. */
	private final Random random;

	/** Alle Räume in der Reihenfolge der Raumnamen-Tabelle. */
	private final List<Room> rooms = new ArrayList<>();

	/** Raum, in dem das Spiel beginnt (Ashfall Valley). */
	private final Room startRoom;

	/**
	 * Erstellt alle Räume, verbindet sie und setzt die Sperren.
	 *
	 * @param random Zufallsgenerator (wird später zum Verstecken der Flöte gebraucht)
	 */
	public WorldBuilder(Random random) {
		this.random = random;

		// Räume erstellen (Name, Beschreibung, Pokémon-Level), Reihenfolge wie Raumnamen-Tabelle im Plan
		Room ashfallValley = addRoom(ASHFALL_VALLEY,
				"A valley full of grey ash. Paths lead from here to every part of the world.", 0);
		Room cityOfTears = addRoom(CITY_OF_TEARS,
				"An abandoned city where the rain never stops.", 5);
		Room forgottenCrossroads = addRoom(FORGOTTEN_CROSSROADS,
				"Old, forgotten crossroads. A steep slope leads south into the fog.", 10);
		Room fogCanyon = addRoom(FOG_CANYON,
				"A canyon full of thick fog. You can barely see your own hand.", 20);
		Room whitePalace = addRoom(WHITE_PALACE,
				"A shining white palace. To the north lies the buried exit.", 30);
		Room theAbyss = addRoom(THE_ABYSS,
				"A dark abyss beneath the palace. It is silent and cold here.", 50);
		Room ancientTreasures = addRoom(ANCIENT_TREASURES,
				"A treasure chamber with ancient chests and coins.", 5);
		Room kingsPass = addRoom(KINGS_PASS,
				"A narrow mountain pass. A steep drop leads west to the treasures.", 20);
		Room queensGardens = addRoom(QUEENS_GARDENS,
				"An overgrown garden full of thorns and flowers.", 30);
		Room restingGrounds = addRoom(RESTING_GROUNDS,
				"A quiet graveyard where you can rest for a while.", 10);
		Room greenpath = addRoom(GREENPATH,
				"A green path through moss and ferns.", 0);
		Room royaleWaterways = addRoom(ROYALE_WATERWAYS,
				"Underground canals filled with murky water.", 5);
		Room exit = addRoom(EXIT,
				"The way out. Daylight shines in from above.", 0);

		// Ausgänge (north, east, south, west) laut Tabelle "Ausgänge" in Idee.md.
		// Einbahnwege sind nur in eine Richtung eingetragen.
		ashfallValley.setExits(cityOfTears, ancientTreasures, royaleWaterways, null);
		cityOfTears.setExits(null, null, ashfallValley, forgottenCrossroads);
		forgottenCrossroads.setExits(null, cityOfTears, fogCanyon, null);     // south = Einbahn
		fogCanyon.setExits(null, ashfallValley, null, whitePalace);           // east = Einbahn
		whitePalace.setExits(exit, fogCanyon, theAbyss, null);                // north = Einbahn
		theAbyss.setExits(whitePalace, null, null, null);
		ancientTreasures.setExits(null, null, null, ashfallValley);
		kingsPass.setExits(queensGardens, null, restingGrounds, ancientTreasures); // west = Einbahn
		queensGardens.setExits(null, null, kingsPass, null);
		restingGrounds.setExits(kingsPass, null, null, greenpath);
		greenpath.setExits(null, restingGrounds, null, royaleWaterways);
		royaleWaterways.setExits(ashfallValley, greenpath, null, null);

		// Sperren
		greenpath.lockExit("east", "A huge Snorlax is sleeping in the way. Maybe some music would wake it up.");
		whitePalace.lockExit("south", "A heavy locked door blocks the way down.");
		whitePalace.lockExit("north", "The exit is buried under rubble. Only a great power could clear it.");

		exit.setWinRoom(true);
		startRoom = ashfallValley;
	}

	/**
	 * Erstellt einen Raum und nimmt ihn in die Liste aller Räume auf.
	 *
	 * @param name         Name des Raums (eine der Konstanten oben)
	 * @param description  englische Beschreibung des Raums
	 * @param pokemonLevel Level der Pokémon, die hier spawnen (0 = keine)
	 * @return der neue Raum
	 */
	private Room addRoom(String name, String description, int pokemonLevel) {
		Room room = new Room(name, description, pokemonLevel);
		rooms.add(room);
		return room;
	}

	/**
	 * Gibt alle Räume als Kopie zurück. Änderungen an der Liste verändern die Welt nicht.
	 *
	 * @return Kopie aller Räume in der Reihenfolge der Raumnamen-Tabelle
	 */
	public List<Room> getRooms() {
		return new ArrayList<>(rooms);
	}

	/**
	 * Sucht einen Raum über seinen Namen.
	 *
	 * @param name Raumname, z. B. {@link #KINGS_PASS}
	 * @return der Raum oder null, wenn es keinen mit diesem Namen gibt
	 */
	public Room getRoom(String name) {
		for (Room room : rooms) {
			if (room.getName().equals(name)) {
				return room;
			}
		}
		return null;
	}

	/**
	 * Gibt den Raum zurück, in dem das Spiel beginnt.
	 *
	 * @return Startraum (Ashfall Valley)
	 */
	public Room getStartRoom() {
		return startRoom;
	}
}
