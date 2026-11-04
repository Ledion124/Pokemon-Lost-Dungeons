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

	/** Die Spielwelt mit allen Räumen. */
	private WorldBuilder world;

	/** Raum, in dem der Spieler gerade steht. */
	private Room currentRoom;

	/**
	 * Erstellt das Spiel: baut die Welt über den {@link WorldBuilder} und setzt den Startraum.
	 */
	public Game() {
		parser = new Parser(System.in);
		world = new WorldBuilder(new Random());
		currentRoom = world.getStartRoom();
	}

	/**
	 *  Main play routine.  Loops until end of play.
	 */
	public void play() {
		printWelcome();

		// Enter the main command loop.  Here we repeatedly read commands and
		// execute them until the game is over.
		boolean finished = false;
		while (!finished) {
			Command command = parser.getCommand();
			finished = processCommand(command);
		}
		System.out.println("Danke fürs Spielen. Auf Wiedersehen!");
	}

	/**
	 * Gibt die Begrüssung und die Beschreibung des Startraums aus.
	 */
	private void printWelcome() {
		System.out.println();
		System.out.println("Willkommen bei Pokémon Lost Dungeons!");
		System.out.println("Finde den Weg zurück ans Tageslicht.");
		System.out.println("Tippe 'help', wenn du Hilfe brauchst.");
		System.out.println();
		System.out.println(currentRoom.longDescription());
	}

	/**
	 * Führt einen Befehl aus.
	 *
	 * @param command der eingegebene Befehl
	 * @return true, wenn das Spiel beendet werden soll
	 */
	private boolean processCommand(Command command) {
		if (command.isUnknown()) {
			System.out.println("Das verstehe ich nicht...");
			return false;
		}

		switch (command.getCommandWord()) {
			case "help":
				printHelp();
				break;
			case "go":
				goRoom(command);
				break;
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
	 * Geht in die angegebene Richtung, falls dort ein Ausgang ist.
	 *
	 * @param command Befehl mit der Richtung als zweitem Wort
	 */
	private void goRoom(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("Go where?");
		} else {

			String direction = command.getSecondWord();

			// Try to leave current room.
			Room nextRoom = currentRoom.nextRoom(direction);

			if (nextRoom == null)
				System.out.println("There is no door!");
			else {
				currentRoom = nextRoom;
				System.out.println(currentRoom.longDescription());
			}
		}
	}

	/**
	 * Geht zurück in den Raum, aus dem man gekommen ist.
	 * TODO (Engel): über Stack&lt;Room&gt; im Player umsetzen.
	 */
	private void goBack() {
		System.out.println("'back' ist noch nicht implementiert.");
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
		System.out.println(currentRoom.longDescription());
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
