package ch.bbw.zork;

import java.util.Random;

/**
 * Class Game - the main class of the "Zork" game.
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
	 * lässt die Pokémon spawnen und den Starter wählen.
	 */
	private void initGame() {
		world = new WorldBuilder(random);
		player = new Player(world.getStartRoom());
		spawnAll();
		chooseStarter();
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

		// Enter the main command loop.  Here we repeatedly read commands and
		// execute them until the game is over.
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
	 * Führt einen Befehl aus.
	 *
	 * @param command der eingegebene Befehl
	 * @return true, wenn das Spiel beendet werden soll
	 */
	private boolean processCommand(Command command) {
		if (command.isUnknown()) {
			System.out.println("I don't understand that.");
			return false;
		}

		switch (command.getCommandWord()) {
			case "help":
				printHelp();
				break;
			case "go":
				return goRoom(command);
			case "back":
				goBack();
				break;
			case "look":
				look();
				break;
			case "search":
				search(command);
				break;
			case "take":
				takeItem(command);
				break;
			case "drop":
				dropItem(command);
				break;
			case "inventory":
				showInventory();
				break;
			case "use":
				useItem(command);
				break;
			case "map":
				showMap();
				break;
			case "fight":
				fight();
				break;
			case "scare":
				scare();
				break;
			case "team":
				showTeam();
				break;
			case "release":
				release(command);
				break;
			case "quit":
				if (command.hasSecondWord()) {
					System.out.println("Quit what?");
				} else {
					return true; // signal that we want to quit
				}
				break;
			default:
				break;
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
		onEnterRoom(nextRoom);

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
		onEnterRoom(room);
	}

	// ===================== Engel (Block 3) =====================

	/**
	 * Benutzt einen Gegenstand aus dem Inventar, z. B. die Flöte oder den Schlüssel.
	 * TODO (Engel, Block 3): KeyItem im richtigen Raum öffnet den Ausgang.
	 *
	 * @param command Befehl mit dem Namen des Gegenstands als zweitem Wort
	 */
	private void useItem(Command command) {
		System.out.println("'use' is not implemented yet.");
	}

	/**
	 * Zeigt alle Räume mit Items und gesperrten Ausgängen, der aktuelle Raum ist markiert.
	 * TODO (Engel, Block 3): über world.getRooms() umsetzen.
	 */
	private void showMap() {
		System.out.println("'map' is not implemented yet.");
	}

	/**
	 * Lässt den Spieler zu Beginn ein Starter-Pokémon wählen.
	 * TODO (Engel, Block 3): Auswahl über parser.readLine(), Pokémon über Pokedex.create.
	 */
	private void chooseStarter() {
	}

	// ===================== Castelberg (Block 3) =====================

	/**
	 * Zeigt den aktuellen Raum mit Items, Verstecken und wildem Pokémon an.
	 * TODO (Castelberg, Block 3): longDescription des aktuellen Raums ausgeben.
	 */
	private void look() {
		System.out.println(player.getCurrentRoom().longDescription());
	}

	/**
	 * Untersucht ein Versteck im aktuellen Raum.
	 * TODO (Castelberg, Block 3): verstecktes Item in den Raum legen.
	 *
	 * @param command Befehl mit dem Namen des Verstecks als zweitem Wort
	 */
	private void search(Command command) {
		System.out.println("'search' is not implemented yet.");
	}

	/**
	 * Nimmt einen Gegenstand aus dem Raum ins Inventar.
	 * TODO (Castelberg, Block 3): mit Gewichtsprüfung über player.canCarry umsetzen.
	 *
	 * @param command Befehl mit dem Namen des Gegenstands als zweitem Wort
	 */
	private void takeItem(Command command) {
		System.out.println("'take' is not implemented yet.");
	}

	/**
	 * Legt einen Gegenstand aus dem Inventar in den Raum.
	 * TODO (Castelberg, Block 3): umsetzen.
	 *
	 * @param command Befehl mit dem Namen des Gegenstands als zweitem Wort
	 */
	private void dropItem(Command command) {
		System.out.println("'drop' is not implemented yet.");
	}

	/**
	 * Zeigt alle getragenen Gegenstände und das Gesamtgewicht an.
	 * TODO (Castelberg, Block 3): umsetzen.
	 */
	private void showInventory() {
		System.out.println("'inventory' is not implemented yet.");
	}

	// ================== Sakaj (Block 3) ===================

	/**
	 * Setzt zu Spielbeginn in jeden Spawn-Raum ein wildes Pokémon.
	 * TODO (Sakaj, Block 3): Pokedex.createRandom pro Raum mit isSpawnRoom().
	 */
	private void spawnAll() {
	}

	/**
	 * Wird nach jedem Betreten eines Raums aufgerufen (Pokémon nachspawnen, Boss setzen).
	 * TODO (Sakaj, Block 3): umsetzen.
	 *
	 * @param room der Raum, der gerade betreten wurde
	 */
	private void onEnterRoom(Room room) {
	}

	/**
	 * Kämpft gegen das wilde Pokémon im aktuellen Raum.
	 * TODO (Sakaj, Block 3): Team wählen, Battle ausführen, fangen oder Neustart.
	 */
	private void fight() {
		System.out.println("'fight' is not implemented yet.");
	}

	/**
	 * Verscheucht das wilde Pokémon im aktuellen Raum.
	 * TODO (Sakaj, Block 3): Boss lässt sich nicht verscheuchen.
	 */
	private void scare() {
		System.out.println("'scare' is not implemented yet.");
	}

	/**
	 * Zeigt alle Pokémon des Spielers nummeriert an.
	 * TODO (Sakaj, Block 3): umsetzen.
	 */
	private void showTeam() {
		System.out.println("'team' is not implemented yet.");
	}

	/**
	 * Lässt ein Pokémon frei.
	 * TODO (Sakaj, Block 3): Nummer prüfen, letztes Pokémon nicht freilassen.
	 *
	 * @param command Befehl mit der Nummer des Pokémon als zweitem Wort
	 */
	private void release(Command command) {
		System.out.println("'release' is not implemented yet.");
	}
}
