package ch.bbw.zork;

import java.util.ArrayList;
import java.util.List;

/**
 * Befehle rund um Welt und Navigation, die über einfaches Gehen hinausgehen:
 * Gegenstände benutzen, Karte anzeigen und Starter wählen.
 * {@link Game} ruft diese Methoden nur auf.
 *
 * Besitzer: Engel.
 */
public class WorldActions {

	/** Alle Richtungen in der Reihenfolge, in der die Karte gesperrte Ausgänge zeigt. */
	private static final String[] DIRECTIONS = {"north", "east", "south", "west"};

	/** Liest Eingaben, z. B. für die Starter-Auswahl. */
	private final Parser parser;

	/** Die Spielwelt mit allen Räumen. */
	private final WorldBuilder world;

	/** Der Spieler mit aktuellem Raum, Inventar und Pokémon. */
	private final Player player;

	/**
	 * Erstellt die Welt-Befehle für ein Spiel.
	 *
	 * @param parser liest Eingaben des Spielers
	 * @param world  die Spielwelt
	 * @param player der Spieler
	 */
	public WorldActions(Parser parser, WorldBuilder world, Player player) {
		this.parser = parser;
		this.world = world;
		this.player = player;
	}

	/**
	 * Benutzt einen Gegenstand aus dem Inventar, z. B. die Flöte oder den Schlüssel.
	 * Ein {@link KeyItem} öffnet im passenden Raum seinen Ausgang und wird dabei
	 * aus dem Inventar entfernt. In jedem anderen Raum passiert nichts.
	 *
	 * @param command Befehl mit dem Namen des Gegenstands als zweitem Wort
	 */
	public void useItem(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("Use what?");
			return;
		}

		Item item = player.getInventory().get(command.getSecondWord());
		if (item == null) {
			System.out.println("You don't have that.");
			return;
		}
		if (!(item instanceof KeyItem keyItem)) {
			System.out.println("You can't use that here.");
			return;
		}

		Room room = player.getCurrentRoom();
		if (!room.getName().equals(keyItem.getTargetRoom())) {
			System.out.println("Nothing happens here.");
			return;
		}

		room.unlockExit(keyItem.getDirection());
		player.getInventory().remove(keyItem.getName());
		System.out.println(unlockMessage(keyItem));
	}

	/**
	 * Text, der erscheint, wenn ein {@link KeyItem} seinen Ausgang öffnet.
	 *
	 * @param keyItem das benutzte Item
	 * @return passender Text für Flöte oder Schlüssel
	 */
	private String unlockMessage(KeyItem keyItem) {
		if (keyItem.getName().equalsIgnoreCase("flute")) {
			return "You play the flute. The Snorlax wakes up, yawns and walks away. The way east is free.";
		}
		return "The key turns in the lock. The heavy door swings open. The way down is free.";
	}

	/**
	 * Zeigt alle Räume mit Items und gesperrten Ausgängen, der aktuelle Raum ist
	 * mit {@code >} markiert.
	 */
	public void showMap() {
		System.out.println("Map of the Lost Dungeons (> = you are here):");
		for (String line : mapLines()) {
			System.out.println(line);
		}
	}

	/**
	 * Baut die Zeilen der Karte, eine pro Raum aus {@code world.getRooms()}.
	 * Getrennt von {@link #showMap()}, damit man die Karte ohne Konsole testen kann.
	 *
	 * @return eine Zeile pro Raum, z. B. {@code "> Greenpath | Items: - | Blocked: east"}
	 */
	List<String> mapLines() {
		List<String> lines = new ArrayList<>();
		for (Room room : world.getRooms()) {
			String marker = room == player.getCurrentRoom() ? ">" : " ";
			lines.add(String.format("%s %-21s | Items: %s | Blocked: %s",
					marker, room.getName(), itemNames(room), blockedExits(room)));
		}
		return lines;
	}

	/**
	 * Namen aller Items, die offen im Raum liegen.
	 *
	 * @param room der Raum
	 * @return z. B. {@code "flute, key"} oder {@code "-"}, wenn keine da sind
	 */
	private String itemNames(Room room) {
		List<String> names = new ArrayList<>();
		for (Item item : room.getItems()) {
			names.add(item.getName());
		}
		return names.isEmpty() ? "-" : String.join(", ", names);
	}

	/**
	 * Alle gesperrten Ausgänge eines Raums.
	 *
	 * @param room der Raum
	 * @return z. B. {@code "south, north"} oder {@code "-"}, wenn keiner gesperrt ist
	 */
	private String blockedExits(Room room) {
		List<String> blocked = new ArrayList<>();
		for (String direction : DIRECTIONS) {
			if (room.isLocked(direction)) {
				blocked.add(direction);
			}
		}
		return blocked.isEmpty() ? "-" : String.join(", ", blocked);
	}

	/**
	 * Lässt den Spieler zu Beginn ein Starter-Pokémon wählen.
	 * TODO (Engel, Block 3): Auswahl über parser.readLine(), Pokémon über Pokedex.create.
	 */
	public void chooseStarter() {
	}
}
