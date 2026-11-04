package ch.bbw.zork;

import java.util.Random;

/**
 * Class Game - the main class of the "Zork" game.
 *
 * Author:  Michael Kolling, 1.1, March 2000
 * refactoring: Rinaldo Lanza, September 2020
 */

public class Game {

	private Parser parser;
	private WorldBuilder world;
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
			case "take":
				takeItem(command);
				break;
			case "drop":
				dropItem(command);
				break;
			case "inventory":
				showInventory();
				break;
			case "map":
				showMap();
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

	/**
	 * Zeigt den aktuellen Raum mit allen Gegenständen an.
	 * TODO (Sakaj): Gegenstände im Raum ausgeben.
	 */
	private void look() {
		System.out.println(currentRoom.longDescription());
	}

	/**
	 * Nimmt einen Gegenstand aus dem Raum ins Inventar.
	 * TODO (Castelberg): mit Gewichtsprüfung umsetzen.
	 *
	 * @param command Befehl mit dem Namen des Gegenstands als zweitem Wort
	 */
	private void takeItem(Command command) {
		System.out.println("'take' ist noch nicht implementiert.");
	}

	/**
	 * Legt einen Gegenstand aus dem Inventar in den Raum.
	 * TODO (Castelberg): umsetzen.
	 *
	 * @param command Befehl mit dem Namen des Gegenstands als zweitem Wort
	 */
	private void dropItem(Command command) {
		System.out.println("'drop' ist noch nicht implementiert.");
	}

	/**
	 * Zeigt alle getragenen Gegenstände und das Gesamtgewicht an.
	 * TODO (Castelberg): umsetzen.
	 */
	private void showInventory() {
		System.out.println("'inventory' ist noch nicht implementiert.");
	}

	/**
	 * Zeigt alle Räume mit ihren Gegenständen an, der aktuelle Raum ist markiert.
	 * TODO (Engel): über ArrayList&lt;Room&gt; umsetzen.
	 */
	private void showMap() {
		System.out.println("'map' ist noch nicht implementiert.");
	}
}
