package ch.bbw.zork;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für {@link CommandWords}: welche Wörter gültige Befehle sind.
 */
class CommandWordsTest {

	/** Die Befehle aus dem Startprojekt. */
	private static final String[] OLD_COMMANDS = {
			"go", "back", "look", "take", "drop", "inventory", "map", "help", "quit"};

	/** Die Befehle, die in Block 2 dazukommen. */
	private static final String[] NEW_COMMANDS = {
			"use", "search", "fight", "scare", "team", "release"};

	/** Getestete Befehlswörter. */
	private CommandWords commandWords;

	/** Erstellt vor jedem Test neue Befehlswörter. */
	@BeforeEach
	void setUp() {
		commandWords = new CommandWords();
	}

	/** Alle alten Befehle sind weiterhin gültig. */
	@Test
	@DisplayName("Alte Befehle sind gültig")
	void oldCommandsAreValid() {
		for (String command : OLD_COMMANDS) {
			assertTrue(commandWords.isCommand(command), command);
		}
	}

	/** Alle neuen Befehle aus Block 2 sind gültig. */
	@Test
	@DisplayName("Neue Befehle aus Block 2 sind gültig")
	void newCommandsAreValid() {
		for (String command : NEW_COMMANDS) {
			assertTrue(commandWords.isCommand(command), command);
		}
	}

	/** Ein unbekanntes Wort ist kein Befehl. */
	@Test
	@DisplayName("Unbekanntes Wort ist kein Befehl")
	void unknownWordIsNotCommand() {
		assertFalse(commandWords.isCommand("dance"));
	}

	/** null und ein leerer String sind keine Befehle. */
	@Test
	@DisplayName("null und leerer String sind keine Befehle")
	void nullAndEmptyAreNotCommands() {
		assertFalse(commandWords.isCommand(null));
		assertFalse(commandWords.isCommand(""));
	}

	/** showAll enthält alle 15 Befehle. */
	@Test
	@DisplayName("showAll enthält alle 15 Befehle")
	void showAllContainsAllCommands() {
		String all = " " + commandWords.showAll() + " ";
		for (String command : OLD_COMMANDS) {
			assertTrue(all.contains(" " + command + " "), command);
		}
		for (String command : NEW_COMMANDS) {
			assertTrue(all.contains(" " + command + " "), command);
		}
		assertEquals(15, commandWords.showAll().split(" ").length);
	}
}
