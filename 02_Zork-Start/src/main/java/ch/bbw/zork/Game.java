package ch.bbw.zork;

import java.util.Random;

/**
 * Hauptklasse des Spiels: baut Welt und Spieler auf, liest Befehle und gibt
 * sie weiter. Gehen ({@code go}, {@code back}), Hilfe, Gewinnen und Neustart
 * erledigt {@code Game} selbst; alle anderen Befehle übernehmen
 * {@link WorldActions}, {@link ItemActions} und {@link PokemonActions}.
 *
 * Author:  Michael Kolling, 1.1, March 2000
 * refactoring: Rinaldo Lanza, September 2020
 */
public class Game {

	/** Liest die Befehle des Spielers. */
	private Parser parser;

	/** Zufallsgenerator für die ganze Spielwelt (Pokémon, Flöten-Versteck). */
	private Random random;

	/** Die Spielwelt mit allen Räumen, wird bei jedem (Neu-)Start neu gebaut. */
	private WorldBuilder world;

	/** Der Spieler mit aktuellem Raum, Inventar und Pokémon. */
	private Player player;

	/** Befehle use, map und die Starter-Auswahl. */
	private WorldActions worldActions;

	/** Befehle look, search, take, drop und inventory. */
	private ItemActions itemActions;

	/** Befehle fight, scare, team, release und das Spawnen der Pokémon. */
	private PokemonActions pokemonActions;

	/**
	 * Erstellt das Spiel. Die Welt und der Spieler werden erst in
	 * {@link #initGame()} angelegt, damit ein Neustart dasselbe tun kann.
	 */
	public Game() {
		parser = new Parser(System.in);
		random = new Random();
	}

	/**
	 * Baut eine neue Welt, setzt einen neuen Spieler in den Startraum,
	 * erstellt die Befehls-Klassen, lässt die Pokémon spawnen und den Starter wählen.
	 */
	private void initGame() {
		world = new WorldBuilder(random);
		player = new Player(world.getStartRoom());
		worldActions = new WorldActions(parser, world, player);
		itemActions = new ItemActions(player);
		pokemonActions = new PokemonActions(parser, world, player, random);
		pokemonActions.spawnAll();
		worldActions.chooseStarter();
	}

	/**
	 * Startet das Spiel nach einer Niederlage neu: gibt den Niederlage-Text aus,
	 * baut alles neu auf und zeigt den Startraum.
	 */
	private void restartGame() {
		System.out.println("All your Pokémon have fainted. Everything goes dark... The game starts over.");
		initGame();
		System.out.println(player.getCurrentRoom().longDescription());
	}

	/**
	 * Hauptschleife: Begrüssung, Spiel aufbauen, Startraum zeigen und dann
	 * Befehle ausführen, bis das Spiel endet.
	 */
	public void play() {
		printWelcome();
		initGame();
		System.out.println(player.getCurrentRoom().longDescription());

		boolean finished = false;
		while (!finished) {
			Command command = parser.getCommand();
			finished = processCommand(command);
		}
		System.out.println("Thanks for playing. Goodbye!");
	}

	/**
	 * Gibt die Begrüssung mit der Story aus (ohne Raumbeschreibung).
	 */
	private void printWelcome() {
		System.out.println();
		System.out.println("Willkommen bei Pokémon Lost Dungeons!");
		System.out.println("Finde den Weg zurück ans Tageslicht.");
		System.out.println("Tippe 'help', wenn du Hilfe brauchst.");
		System.out.println();
	}

	/**
	 * Führt einen Befehl aus, indem er an die zuständige Methode oder Klasse
	 * weitergegeben wird.
	 *
	 * @param command der eingegebene Befehl
	 * @return true, wenn das Spiel beendet werden soll (gewonnen oder quit)
	 */
	private boolean processCommand(Command command) {
		if (command.isUnknown()) {
			System.out.println("I don't understand that.");
			return false;
		}

		switch (command.getCommandWord()) {
			case "help" -> printHelp();
			case "go" -> {
				return goRoom(command);
			}
			case "back" -> goBack();
			case "look" -> itemActions.look();
			case "search" -> itemActions.search(command);
			case "take" -> itemActions.takeItem(command);
			case "drop" -> itemActions.dropItem(command);
			case "inventory" -> itemActions.showInventory();
			case "use" -> worldActions.useItem(command);
			case "map" -> worldActions.showMap();
			case "fight" -> {
				if (pokemonActions.fight()) {
					restartGame();
				}
			}
			case "scare" -> pokemonActions.scare();
			case "team" -> pokemonActions.showTeam();
			case "release" -> pokemonActions.release(command);
			case "quit" -> {
				if (!command.hasSecondWord()) {
					return true;
				}
				System.out.println("Quit what?");
			}
			default -> { }
		}
		return false;
	}

	/**
	 * Gibt einen Hilfetext und alle gültigen Befehle aus.
	 */
	private void printHelp() {
		System.out.println("Du bist in den Lost Dungeons gefangen.");
		System.out.println("Finde den Kristallschlüssel und entkomme ans Tageslicht.");
		System.out.println();
		System.out.println("Deine Befehle sind:");
		System.out.println(parser.showCommands());
	}

	/**
	 * Geht in die angegebene Richtung. Gibt es dort keinen Ausgang oder ist er
	 * gesperrt, bleibt der Spieler stehen und bekommt eine Meldung. Wer den
	 * Gewinnraum betritt, hat gewonnen.
	 *
	 * @param command Befehl mit der Richtung als zweitem Wort
	 * @return true, wenn der Spieler gewonnen hat und das Spiel endet
	 */
	private boolean goRoom(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("Go where?");
			return false;
		}

		String direction = command.getSecondWord();
		Room currentRoom = player.getCurrentRoom();
		Room nextRoom = currentRoom.nextRoom(direction);

		if (nextRoom == null) {
			System.out.println("There is no way in that direction.");
			return false;
		}
		if (currentRoom.isLocked(direction)) {
			System.out.println(currentRoom.getLockMessage(direction));
			return false;
		}

		player.moveTo(nextRoom);
		System.out.println(nextRoom.longDescription());
		pokemonActions.onEnterRoom(nextRoom);

		if (nextRoom.isWinRoom()) {
			System.out.println("You climb out into the daylight. You escaped the Lost Dungeons! You win!");
			return true;
		}
		return false;
	}

	/**
	 * Geht zurück in den Raum, aus dem man gekommen ist. Über Einbahnwege und
	 * gesperrte Ausgänge geht das nicht (prüft {@link Player#canGoBack()}).
	 */
	private void goBack() {
		Room room = player.goBack();
		if (room == null) {
			System.out.println("The way back is blocked.");
			return;
		}
		System.out.println(room.longDescription());
		pokemonActions.onEnterRoom(room);
	}
}
