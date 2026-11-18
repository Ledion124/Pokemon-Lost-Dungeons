package ch.bbw.zork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Liest die Eingaben des Spielers. {@link #getCommand()} macht aus einer Zeile
 * einen {@link Command}, {@link #readLine()} liefert eine Zeile unverändert
 * (z. B. für die Starter- oder Teamauswahl).
 *
 * author:  Michael Kolling, Version: 1.0, Date: July 1999
 * refactoring: Rinaldo Lanza, September 2020
 */
public class Parser {

	/** Alle gültigen Befehlswörter. */
	private CommandWords validCommandWords;

	/** Liest die Eingabe zeilenweise. */
	private BufferedReader bufferedReader;

	/**
	 * Erstellt einen Parser, der aus dem angegebenen Stream liest.
	 *
	 * @param inputStream Quelle der Eingaben, im Spiel {@code System.in}
	 */
	public Parser(InputStream inputStream) {
		this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		this.validCommandWords = new CommandWords();
	}

	/**
	 * Gibt {@code "> "} aus und liest den nächsten Befehl.
	 * Ein unbekanntes Befehlswort ergibt einen unbekannten Befehl,
	 * am Ende der Eingabe wird {@code quit} zurückgegeben.
	 *
	 * @return der eingelesene Befehl
	 */
	public Command getCommand() {
		String inputLine;

		System.out.print("> ");

		try {
			inputLine = bufferedReader.readLine();
			if (inputLine == null) {
				return new Command("quit"); // Ende der Eingabe
			}

			String[] tokens = inputLine.split(" ");
			switch(tokens.length) {
				case 2:
					if (validCommandWords.isCommand(tokens[0])) {
						return new Command(tokens[0], tokens[1]);
					} else {
						// TODO: refactor this
						return new Command(null, tokens[1]);
					}
				case 1:
					if (validCommandWords.isCommand(tokens[0])) {
						return new Command(tokens[0]);
					} else {
						// TODO: refactor this and that
						return new Command(null);
					}
				default:
					// TODO: handle this error with an exception and non
					break;
			}
		} catch (IOException exc) {
			System.out.println("There was an error during reading: " + exc.getMessage());
		}
		// TODO: handle error
		return new Command(null);
	}

	/**
	 * Gibt {@code "> "} aus und liest die nächste Eingabezeile, ohne sie als
	 * Befehl zu deuten. Leerzeichen am Anfang und Ende werden entfernt.
	 *
	 * @return die getrimmte Zeile, oder {@code ""} am Ende der Eingabe oder bei einem Lesefehler
	 */
	public String readLine() {
		System.out.print("> ");
		try {
			String line = bufferedReader.readLine();
			return line == null ? "" : line.trim();
		} catch (IOException exc) {
			System.out.println("There was an error during reading: " + exc.getMessage());
			return "";
		}
	}

	/**
	 * Gibt alle gültigen Befehlswörter als einen String zurück.
	 *
	 * @return Befehlswörter, durch Leerzeichen getrennt
	 */
	public String showCommands() {
		return validCommandWords.showAll();
	}
}
