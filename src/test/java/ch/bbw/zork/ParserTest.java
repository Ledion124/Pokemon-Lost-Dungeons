package ch.bbw.zork;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für {@link Parser}: Befehle einlesen mit {@code getCommand} und
 * freie Eingaben mit {@code readLine}. Die Eingabe kommt aus einem String
 * statt von der Konsole.
 */
class ParserTest {

	/**
	 * Erstellt einen Parser, der den angegebenen Text als Eingabe liest.
	 *
	 * @param input simulierte Eingabe, Zeilen mit {@code \n} getrennt
	 * @return Parser für diese Eingabe
	 */
	private Parser parserFor(String input) {
		return new Parser(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
	}

	/** readLine entfernt Leerzeichen am Anfang und Ende. */
	@Test
	@DisplayName("readLine trimmt die Eingabe")
	void readLineTrims() {
		assertEquals("1 3 2", parserFor("   1 3 2  \n").readLine());
	}

	/** Am Ende der Eingabe gibt readLine einen leeren String zurück. */
	@Test
	@DisplayName("readLine gibt am Ende der Eingabe \"\" zurück")
	void readLineReturnsEmptyAtEnd() {
		assertEquals("", parserFor("").readLine());
	}

	/** readLine liest Zeile für Zeile. */
	@Test
	@DisplayName("readLine liest Zeile für Zeile")
	void readLineReadsLineByLine() {
		Parser parser = parserFor("first\nsecond\n");
		assertEquals("first", parser.readLine());
		assertEquals("second", parser.readLine());
		assertEquals("", parser.readLine());
	}

	/** Ein Befehl mit zweitem Wort wird erkannt. */
	@Test
	@DisplayName("getCommand erkennt Befehl mit zweitem Wort")
	void getCommandWithSecondWord() {
		Command command = parserFor("go north\n").getCommand();
		assertFalse(command.isUnknown());
		assertEquals("go", command.getCommandWord());
		assertEquals("north", command.getSecondWord());
	}

	/** Ein Befehl ohne zweites Wort wird erkannt. */
	@Test
	@DisplayName("getCommand erkennt Befehl ohne zweites Wort")
	void getCommandWithoutSecondWord() {
		Command command = parserFor("back\n").getCommand();
		assertEquals("back", command.getCommandWord());
		assertFalse(command.hasSecondWord());
	}

	/** Ein unbekanntes Wort ergibt einen unbekannten Befehl. */
	@Test
	@DisplayName("Unbekanntes Wort ergibt unbekannten Befehl")
	void unknownWordGivesUnknownCommand() {
		assertTrue(parserFor("dance\n").getCommand().isUnknown());
	}

	/** Am Ende der Eingabe gibt getCommand quit zurück, damit das Spiel endet. */
	@Test
	@DisplayName("getCommand gibt am Ende der Eingabe quit zurück")
	void getCommandReturnsQuitAtEnd() {
		assertEquals("quit", parserFor("").getCommand().getCommandWord());
	}

	/** getCommand und readLine lesen aus derselben Eingabe nacheinander. */
	@Test
	@DisplayName("getCommand und readLine lesen nacheinander")
	void getCommandAndReadLineShareInput() {
		Parser parser = parserFor("look\n2\n");
		assertEquals("look", parser.getCommand().getCommandWord());
		assertEquals("2", parser.readLine());
	}
}
