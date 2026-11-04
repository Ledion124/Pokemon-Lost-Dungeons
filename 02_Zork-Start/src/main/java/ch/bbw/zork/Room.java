package ch.bbw.zork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ein Raum in der Spielwelt. Ein Raum hat einen Namen, eine Beschreibung,
 * ein Pokémon-Level und bis zu vier Ausgänge (north, east, south, west).
 * Einzelne Ausgänge können gesperrt sein (z. B. durch Relaxo oder eine Tür).
 *
 * Author:  Michael Kolling, Version: 1.1, Date: August 2000
 * refactoring: Rinaldo Lanza, September 2020
 */
public class Room {

	/** Alle Richtungen in der Reihenfolge, in der sie ausgegeben werden. */
	private static final String[] DIRECTIONS = {"north", "east", "south", "west"};

	/** Name des Raums, z. B. "Ashfall Valley". */
	private final String name;

	/** Englische Beschreibung für die Spielausgabe. */
	private final String description;

	/** Level der wilden Pokémon in diesem Raum (0 = hier spawnt nichts). */
	private final int pokemonLevel;

	/** true, wenn man durch Betreten dieses Raums gewinnt. */
	private boolean winRoom;

	/** Richtung -> Nachbarraum. Enthält nur echte Ausgänge, auch gesperrte. */
	private final Map<String, Room> exits = new HashMap<>();

	/** Richtung -> Sperrtext. Eine Richtung steht nur hier drin, solange sie gesperrt ist. */
	private final Map<String, String> lockMessages = new HashMap<>();

	/** Gegenstände, die offen im Raum liegen, in der Reihenfolge, in der sie hingelegt wurden. */
	private final List<Item> items = new ArrayList<>();

	/**
	 * Erstellt einen Raum ohne Ausgänge.
	 *
	 * @param name         Name des Raums, z. B. "Ashfall Valley"
	 * @param description  englische Beschreibung des Raums
	 * @param pokemonLevel Level der Pokémon, die hier spawnen (0 = keine)
	 */
	public Room(String name, String description, int pokemonLevel) {
		this.name = name;
		this.description = description;
		this.pokemonLevel = pokemonLevel;
	}

	/**
	 * Gibt den Namen des Raums zurück.
	 *
	 * @return Name des Raums
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gibt die Beschreibung des Raums zurück.
	 *
	 * @return Beschreibung des Raums
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gibt das Level der Pokémon zurück, die hier spawnen.
	 *
	 * @return Level der Pokémon in diesem Raum (0 = hier spawnt nichts)
	 */
	public int getPokemonLevel() {
		return pokemonLevel;
	}

	/**
	 * Prüft, ob dieser Raum der Gewinn-Raum ist.
	 *
	 * @return true, wenn man das Spiel gewinnt, sobald man diesen Raum betritt
	 */
	public boolean isWinRoom() {
		return winRoom;
	}

	/**
	 * Legt fest, ob dieser Raum der Gewinn-Raum ist.
	 *
	 * @param winRoom true für den Gewinn-Raum
	 */
	public void setWinRoom(boolean winRoom) {
		this.winRoom = winRoom;
	}

	/**
	 * Setzt alle vier Ausgänge auf einmal. {@code null} heisst: kein Ausgang in diese Richtung.
	 *
	 * @param north Raum im Norden oder null
	 * @param east  Raum im Osten oder null
	 * @param south Raum im Süden oder null
	 * @param west  Raum im Westen oder null
	 */
	public void setExits(Room north, Room east, Room south, Room west) {
		setExit("north", north);
		setExit("east", east);
		setExit("south", south);
		setExit("west", west);
	}

	/**
	 * Setzt einen einzelnen Ausgang. Bei {@code null} wird nichts eingetragen.
	 *
	 * @param direction Richtung (north, east, south, west)
	 * @param room      Nachbarraum oder null
	 */
	private void setExit(String direction, Room room) {
		if (room != null) {
			exits.put(direction, room);
		}
	}

	/**
	 * Gibt den Nachbarraum in einer Richtung zurück, auch wenn der Ausgang gesperrt ist.
	 * Die Sperre prüft {@link Game}.
	 *
	 * @param direction Richtung (north, east, south, west)
	 * @return Nachbarraum oder null, wenn es keinen Ausgang gibt
	 */
	public Room nextRoom(String direction) {
		return exits.get(direction);
	}

	/**
	 * Sperrt einen Ausgang. Der Ausgang bleibt bestehen, kann aber nicht benutzt werden.
	 *
	 * @param direction Richtung des Ausgangs
	 * @param message   Text, der beim Versuch durchzugehen angezeigt wird
	 */
	public void lockExit(String direction, String message) {
		lockMessages.put(direction, message);
	}

	/**
	 * Entsperrt einen Ausgang wieder.
	 *
	 * @param direction Richtung des Ausgangs
	 */
	public void unlockExit(String direction) {
		lockMessages.remove(direction);
	}

	/**
	 * Prüft, ob der Ausgang in diese Richtung gesperrt ist.
	 *
	 * @param direction Richtung des Ausgangs
	 * @return true, wenn der Ausgang in diese Richtung gesperrt ist
	 */
	public boolean isLocked(String direction) {
		return lockMessages.containsKey(direction);
	}

	/**
	 * Gibt den Text zurück, der bei einem gesperrten Ausgang angezeigt wird.
	 *
	 * @param direction Richtung des Ausgangs
	 * @return Sperrtext oder null, wenn der Ausgang offen ist
	 */
	public String getLockMessage(String direction) {
		return lockMessages.get(direction);
	}

	/**
	 * Prüft, ob ein offener Ausgang in den angegebenen Raum führt.
	 * Wird für {@code back} gebraucht: über Einbahnwege oder Sperren geht es nicht zurück.
	 *
	 * @param room Zielraum
	 * @return true, wenn ein nicht gesperrter Ausgang in diesen Raum führt
	 */
	public boolean hasExitTo(Room room) {
		for (Map.Entry<String, Room> exit : exits.entrySet()) {
			if (exit.getValue() == room && !isLocked(exit.getKey())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Legt einen Gegenstand offen in den Raum.
	 *
	 * @param item der Gegenstand
	 */
	public void addItem(Item item) {
		items.add(item);
	}

	/**
	 * Nimmt den Gegenstand mit diesem Namen aus dem Raum.
	 *
	 * @param name Name des Gegenstands (Gross-/Kleinschreibung egal)
	 * @return der entfernte Gegenstand oder null, wenn er nicht hier liegt
	 */
	public Item removeItem(String name) {
		Item item = getItem(name);
		if (item != null) {
			items.remove(item);
		}
		return item;
	}

	/**
	 * Sucht den Gegenstand mit diesem Namen, ohne ihn zu entfernen.
	 *
	 * @param name Name des Gegenstands (Gross-/Kleinschreibung egal)
	 * @return der Gegenstand oder null, wenn er nicht hier liegt
	 */
	public Item getItem(String name) {
		if (name == null) {
			return null;
		}
		for (Item item : items) {
			if (item.getName().equalsIgnoreCase(name)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * Gibt alle Gegenstände im Raum als Kopie zurück.
	 *
	 * @return Kopie der Gegenstände
	 */
	public List<Item> getItems() {
		return new ArrayList<>(items);
	}

	/**
	 * Lange Beschreibung mit Name, Beschreibung und Ausgängen.
	 * Weitere Zeilen (Items, Pokémon) werden später hinten angehängt.
	 *
	 * @return mehrzeilige Beschreibung des Raums
	 */
	public String longDescription() {
		StringBuilder stringBuilder = new StringBuilder("You are in " + name + ". " + description + "\n");
		stringBuilder.append(exitString());
		return stringBuilder.toString();
	}

	/**
	 * Alle Ausgänge in der Reihenfolge north, east, south, west, gesperrte mit "(blocked)".
	 *
	 * @return z. B. "Exits: north east south(blocked)"
	 */
	public String exitString() {
		StringBuilder stringBuilder = new StringBuilder("Exits:");
		for (String direction : DIRECTIONS) {
			if (exits.containsKey(direction)) {
				stringBuilder.append(" ").append(direction);
				if (isLocked(direction)) {
					stringBuilder.append("(blocked)");
				}
			}
		}
		return stringBuilder.toString();
	}
}
