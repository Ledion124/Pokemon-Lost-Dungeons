package ch.bbw.zork;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Befehle rund um Pokémon: wilde Pokémon spawnen, kämpfen, verscheuchen,
 * Team anzeigen und freilassen. {@link Game} ruft diese Methoden nur auf.
 *
 * Besitzer: Sakaj.
 */
public class PokemonActions {

	/** Höchstens so viele Pokémon dürfen in einem Kampf mitmachen. */
	private static final int MAX_TEAM_SIZE = 3;

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
	 * Setzt zu Spielbeginn in jeden Spawn-Raum ein zufälliges wildes Pokémon
	 * mit dem Level des Raums.
	 */
	public void spawnAll() {
		for (Room room : world.getRooms()) {
			if (room.isSpawnRoom()) {
				spawn(room);
			}
		}
	}

	/**
	 * Wird nach jedem Betreten eines Raums aufgerufen.
	 * In The Abyss wird der Boss gesetzt, solange er nicht besiegt ist.
	 * In anderen Spawn-Räumen spawnt ein neues Pokémon, wenn der Raum leer ist.
	 *
	 * @param room der Raum, der gerade betreten wurde
	 */
	public void onEnterRoom(Room room) {
		if (room.hasWildPokemon()) {
			return;
		}
		if (room.getName().equals(WorldBuilder.THE_ABYSS)) {
			if (!isBossDefeated()) {
				room.setWildPokemon(Pokedex.createBoss());
			}
		} else if (room.isSpawnRoom()) {
			spawn(room);
		}
	}

	/**
	 * Kämpft gegen das wilde Pokémon im aktuellen Raum.
	 * Der Spieler wählt bis zu 3 Pokémon per Nummer, dann läuft der Kampf.
	 * Bei einem Sieg gegen den Boss öffnet sich der Ausgang im White Palace,
	 * bei einem anderen Sieg wird das Pokémon gefangen, wenn das Gewicht reicht.
	 *
	 * @return true, wenn der Spieler verloren hat und das Spiel neu starten muss
	 */
	public boolean fight() {
		Room room = player.getCurrentRoom();
		Pokemon wild = room.getWildPokemon();
		if (wild == null) {
			System.out.println("There is nothing to fight here.");
			return false;
		}

		showTeam();
		System.out.println("Choose up to 3 Pokémon by number (e.g. 1 3 2):");
		List<Pokemon> team = parseTeam(parser.readLine());
		if (team == null) {
			System.out.println("Invalid team. The fight is cancelled.");
			return false;
		}

		Battle battle = new Battle(team, wild);
		boolean won = battle.fight();
		for (String line : battle.getLog()) {
			System.out.println(line);
		}

		if (!won) {
			return true;
		}

		room.removeWildPokemon();
		if (wild instanceof BossPokemon boss) {
			System.out.println(boss.getDefeatMessage());
			world.getRoom(WorldBuilder.WHITE_PALACE).unlockExit("north");
		} else {
			System.out.println("You defeated " + wild.getName() + "!");
			catchPokemon(wild);
		}
		return false;
	}

	/**
	 * Verscheucht das wilde Pokémon im aktuellen Raum.
	 * Der Boss lässt sich nicht verscheuchen.
	 */
	public void scare() {
		Room room = player.getCurrentRoom();
		Pokemon wild = room.getWildPokemon();
		if (wild == null) {
			System.out.println("There is nothing to scare away.");
		} else if (!wild.canBeScared()) {
			System.out.println(wild.getName() + " does not move an inch.");
		} else {
			room.removeWildPokemon();
			System.out.println(wild.getName() + " runs away.");
		}
	}

	/**
	 * Zeigt alle Pokémon des Spielers nummeriert an (1-basiert)
	 * und am Ende ihr Gesamtgewicht im Pokéball.
	 */
	public void showTeam() {
		PokemonCollection pokemons = player.getPokemons();
		if (pokemons.size() == 0) {
			System.out.println("You have no Pokémon.");
			return;
		}
		System.out.println("Your Pokémon:");
		for (int number = 1; number <= pokemons.size(); number++) {
			System.out.println(number + ") " + pokemons.get(number));
		}
		System.out.println(String.format(Locale.ROOT, "Total weight: %.2f kg", pokemons.getTotalWeight()));
	}

	/**
	 * Lässt ein Pokémon frei. Das letzte Pokémon kann nicht freigelassen werden.
	 *
	 * @param command Befehl mit der Nummer des Pokémon als zweitem Wort
	 */
	public void release(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("Release which Pokémon? Use the number from 'team'.");
			return;
		}
		int number = parseNumber(command.getSecondWord());
		if (number == -1) {
			System.out.println("There is no Pokémon with that number.");
			return;
		}
		if (player.getPokemons().size() == 1) {
			System.out.println("You can't release your last Pokémon.");
			return;
		}
		Pokemon released = player.getPokemons().remove(number);
		System.out.println(released.getName() + " was released.");
	}

	/**
	 * Setzt ein neues zufälliges Pokémon mit dem Level des Raums in den Raum.
	 *
	 * @param room der Raum
	 */
	private void spawn(Room room) {
		room.setWildPokemon(Pokedex.createRandom(room.getPokemonLevel(), random));
	}

	/**
	 * Der Boss gilt als besiegt, sobald der Ausgang im Norden des White Palace offen ist.
	 *
	 * @return true, wenn Arceus schon besiegt wurde
	 */
	private boolean isBossDefeated() {
		return !world.getRoom(WorldBuilder.WHITE_PALACE).isLocked("north");
	}

	/**
	 * Nimmt ein besiegtes Pokémon in die Sammlung auf, wenn das Tragegewicht reicht.
	 * Sonst darf der Spieler ein Pokémon freilassen, um Platz zu machen.
	 * Passt es danach immer noch nicht, läuft das Pokémon weg.
	 *
	 * @param wild das besiegte Pokémon
	 */
	private void catchPokemon(Pokemon wild) {
		if (!player.canCarry(wild.getCarryWeight())) {
			showTeam();
			System.out.println("Release a Pokémon by number to make room, or press Enter to let "
					+ wild.getName() + " go:");
			int number = parseNumber(parser.readLine());
			if (number != -1) {
				Pokemon released = player.getPokemons().remove(number);
				System.out.println(released.getName() + " was released.");
			}
		}
		if (player.canCarry(wild.getCarryWeight())) {
			player.getPokemons().add(wild);
			System.out.println(wild.getName() + " joins your team!");
		} else {
			System.out.println(wild.getName() + " runs away.");
		}
	}

	/**
	 * Liest die Teamauswahl, z. B. {@code "1 3 2"}.
	 * Ungültig sind: leere Eingabe, mehr als 3 Nummern, keine Zahl,
	 * Nummern, die es nicht gibt, und doppelte Nummern.
	 *
	 * @param input Eingabe des Spielers
	 * @return die gewählten Pokémon in Kampfreihenfolge oder null, wenn die Eingabe ungültig ist
	 */
	private List<Pokemon> parseTeam(String input) {
		if (input == null || input.isBlank()) {
			return null;
		}
		String[] parts = input.trim().split("\\s+");
		if (parts.length > MAX_TEAM_SIZE) {
			return null;
		}
		List<Pokemon> team = new ArrayList<>();
		for (String part : parts) {
			int number = parseNumber(part);
			if (number == -1) {
				return null;
			}
			Pokemon pokemon = player.getPokemons().get(number);
			if (team.contains(pokemon)) {
				return null;
			}
			team.add(pokemon);
		}
		return team;
	}

	/**
	 * Wandelt eine Eingabe in eine gültige Pokémon-Nummer um (1-basiert, wie in team).
	 *
	 * @param text die Eingabe
	 * @return die Nummer oder -1, wenn es keine Zahl ist oder es die Nummer nicht gibt
	 */
	private int parseNumber(String text) {
		try {
			int number = Integer.parseInt(text.trim());
			if (number >= 1 && number <= player.getPokemons().size()) {
				return number;
			}
		} catch (NumberFormatException exc) {
			// keine Zahl, siehe unten
		}
		return -1;
	}
}
