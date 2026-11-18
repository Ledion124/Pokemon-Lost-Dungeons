package ch.bbw.zork;

import java.util.Random;

/**
 * Befehle rund um Pokémon: wilde Pokémon spawnen, kämpfen, verscheuchen,
 * Team anzeigen und freilassen. {@link Game} ruft diese Methoden nur auf.
 *
 * Besitzer: Sakaj.
 */
public class PokemonActions {

	/** Liest Eingaben, z. B. für die Teamauswahl im Kampf. */
	private final Parser parser;

	/** Die Spielwelt mit allen Räumen. */
	private final WorldBuilder world;

	/** Der Spieler mit aktuellem Raum und Pokémon. */
	private final Player player;

	/** Zufallsgenerator für das Spawnen der Pokémon. */
	private final Random random;

	/**
	 * Erstellt die Pokémon-Befehle für ein Spiel.
	 *
	 * @param parser liest Eingaben des Spielers
	 * @param world  die Spielwelt
	 * @param player der Spieler
	 * @param random Zufallsgenerator für das Spawnen
	 */
	public PokemonActions(Parser parser, WorldBuilder world, Player player, Random random) {
		this.parser = parser;
		this.world = world;
		this.player = player;
		this.random = random;
	}

	/**
	 * Setzt zu Spielbeginn in jeden Spawn-Raum ein wildes Pokémon.
	 * TODO (Sakaj, Block 3): Pokedex.createRandom pro Raum mit isSpawnRoom().
	 */
	public void spawnAll() {
	}

	/**
	 * Wird nach jedem Betreten eines Raums aufgerufen (Pokémon nachspawnen, Boss setzen).
	 * TODO (Sakaj, Block 3): umsetzen.
	 *
	 * @param room der Raum, der gerade betreten wurde
	 */
	public void onEnterRoom(Room room) {
	}

	/**
	 * Kämpft gegen das wilde Pokémon im aktuellen Raum.
	 * TODO (Sakaj, Block 3): Team wählen, Battle ausführen, fangen.
	 *
	 * @return true, wenn der Spieler verloren hat und das Spiel neu starten muss
	 */
	public boolean fight() {
		System.out.println("'fight' is not implemented yet.");
		return false;
	}

	/**
	 * Verscheucht das wilde Pokémon im aktuellen Raum.
	 * TODO (Sakaj, Block 3): Boss lässt sich nicht verscheuchen.
	 */
	public void scare() {
		System.out.println("'scare' is not implemented yet.");
	}

	/**
	 * Zeigt alle Pokémon des Spielers nummeriert an.
	 * TODO (Sakaj, Block 3): umsetzen.
	 */
	public void showTeam() {
		System.out.println("'team' is not implemented yet.");
	}

	/**
	 * Lässt ein Pokémon frei.
	 * TODO (Sakaj, Block 3): Nummer prüfen, letztes Pokémon nicht freilassen.
	 *
	 * @param command Befehl mit der Nummer des Pokémon als zweitem Wort
	 */
	public void release(Command command) {
		System.out.println("'release' is not implemented yet.");
	}
}
