package ch.bbw.zork;

/**
 * Befehle rund um Welt und Navigation, die über einfaches Gehen hinausgehen:
 * Gegenstände benutzen, Karte anzeigen und Starter wählen.
 * {@link Game} ruft diese Methoden nur auf.
 *
 * Besitzer: Engel.
 */
public class WorldActions {

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
	 * Zeigt alle Räume mit Items und gesperrten Ausgängen, der aktuelle Raum ist markiert.
	 * TODO (Engel, Block 3): über world.getRooms() umsetzen.
	 */
	public void showMap() {
		System.out.println("'map' is not implemented yet.");
	}

	/**
	 * Lässt den Spieler zu Beginn ein Starter-Pokémon wählen.
	 * TODO (Engel, Block 3): Auswahl über parser.readLine(), Pokémon über Pokedex.create.
	 */
	public void chooseStarter() {
	}
}
