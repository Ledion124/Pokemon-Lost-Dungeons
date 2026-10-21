package ch.bbw.zork;

/**
 * Class Game - the main class of the "Zork" game.
 *
 * Author:  Michael Kolling, 1.1, March 2000
 * refactoring: Rinaldo Lanza, September 2020
 */

public class Game {

	private Parser parser;
	private Room currentRoom;
	private Room ashfallValley;
	private Room cityOfTears;
	private Room forgottenCrossroads;
	private Room fogCanyon;
	private Room whitePalace;
	private Room theAbyss;
	private Room royalWaterways;
	private Room ancientTreasures;
	private Room greenpath;
	private Room restingGrounds;
	private Room kingsPass;
	private Room queensGarden;
	private Room exit;

	/**
	 * Erstellt das Spiel mit allen Räumen und setzt den Startraum.
	 */
	public Game() {

		parser = new Parser(System.in);

		// create rooms
		ashfallValley = new Room("Ein Tal voller grauer Asche. Von hier führen Wege in alle Teile der Welt.");
		cityOfTears = new Room("Eine verlassene Stadt, in der es ohne Pause regnet.");
		forgottenCrossroads = new Room("Alte, vergessene Wegkreuzungen. Ein steiler Abhang führt nach Süden in den Nebel.");
		fogCanyon = new Room("Eine Schlucht voller dichtem Nebel. Man sieht kaum die eigene Hand.");
		whitePalace = new Room("Ein strahlend weisser Palast. Im Norden liegt das Tor nach draussen.");
		theAbyss = new Room("Ein dunkler Abgrund unter dem Palast. Hier ist es still und kalt.");
		royalWaterways = new Room("Unterirdische Kanäle mit trübem Wasser.");
		ancientTreasures = new Room("Eine Schatzkammer mit uralten Truhen und Münzen.");
		greenpath = new Room("Ein grüner Pfad durch Moos und Farn.");
		restingGrounds = new Room("Ein ruhiger Friedhof, auf dem man sich ausruhen kann.");
		kingsPass = new Room("Ein schmaler Bergpass. Ein Felsabbruch führt nach Westen zu den Schätzen.");
		queensGarden = new Room("Ein verwilderter Garten voller Dornen und Blumen.");
		exit = new Room("Der Ausgang. Wer hier ankommt, hat das Spiel gewonnen.");

		// initialise room exits (north, east, south, west) - siehe docs/Idee.md
		ashfallValley.setExits(cityOfTears, ancientTreasures, royalWaterways, null);
		cityOfTears.setExits(null, null, ashfallValley, forgottenCrossroads);
		forgottenCrossroads.setExits(null, cityOfTears, fogCanyon, null);
		fogCanyon.setExits(null, ashfallValley, null, whitePalace);
		whitePalace.setExits(exit, fogCanyon, theAbyss, null);
		theAbyss.setExits(whitePalace, null, null, null);
		exit.setExits(null, null, null, null);
		royalWaterways.setExits(ashfallValley, greenpath, null, null);
		greenpath.setExits(null, restingGrounds, null, royalWaterways);
		restingGrounds.setExits(kingsPass, null, null, greenpath);
		kingsPass.setExits(queensGarden, null, restingGrounds, ancientTreasures);
		queensGarden.setExits(null, null, kingsPass, null);
		ancientTreasures.setExits(null, null, null, ashfallValley);

		currentRoom = ashfallValley; // start game in Ashfall Valley
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
