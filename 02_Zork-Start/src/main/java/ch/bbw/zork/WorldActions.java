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
	 * TODO (Engel, Block 3): KeyItem im richtigen Raum öffnet den Ausgang.
	 *
	 * @param command Befehl mit dem Namen des Gegenstands als zweitem Wort
	 */
	public void useItem(Command command) {
		System.out.println("'use' is not implemented yet.");
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
