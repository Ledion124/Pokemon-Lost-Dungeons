package ch.bbw.zork;

import java.util.Arrays;
import java.util.List;

/**
 * Kennt alle gültigen Befehlswörter des Spiels.
 *
 * author:  Michael Kolling, Version: 1.0, Date: July 1999
 * refactoring: Rinaldo Lanza, September 2020
 */
public class CommandWords {

	/** Alle Befehlswörter, die der Spieler eingeben kann. */
	private List<String> validCommands = Arrays.asList(
			"go", "back", "look", "search", "take", "drop", "inventory", "use",
			"map", "fight", "scare", "team", "release", "help", "quit");

	/**
	 * Prüft, ob ein Wort ein gültiger Befehl ist.
	 *
	 * @param commandWord das zu prüfende Wort
	 * @return true, wenn es ein gültiger Befehl ist
	 */
	public boolean isCommand(String commandWord) {
		return validCommands.stream()
				.filter( c -> c.equals(commandWord) )
				.count()>0;
	}

	/**
	 * Gibt alle gültigen Befehlswörter als einen String zurück.
	 *
	 * @return Befehlswörter, durch Leerzeichen getrennt
	 */
	public String showAll() {
		return String.join(" ", validCommands);
	}

}
