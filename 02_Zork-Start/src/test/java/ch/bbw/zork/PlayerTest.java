package ch.bbw.zork;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für {@link Player}: Bewegung und Zurückgehen mit {@code back}.
 */
class PlayerTest {

	/** Startraum, von hier geht es nach Osten in {@link #b}. */
	private Room a;

	/** Raum mit Ausgang zurück nach {@link #a} und Einbahnweg nach {@link #c}. */
	private Room b;

	/** Ende des Einbahnwegs, kein Ausgang zurück nach {@link #b}. */
	private Room c;

	/** Spieler, der in {@link #a} startet. */
	private Player player;

	/** Baut drei Testräume: a &lt;-&gt; b -&gt; c (b nach c ist ein Einbahnweg). */
	@BeforeEach
	void setUp() {
		a = new Room("A", "Room A.", 0);
		b = new Room("B", "Room B.", 0);
		c = new Room("C", "Room C.", 0);
		a.setExits(null, b, null, null);
		b.setExits(null, c, null, a);
		player = new Player(a);
	}

	/** Ein neuer Spieler steht im Startraum. */
	@Test
	@DisplayName("Neuer Spieler steht im Startraum")
	void startsInStartRoom() {
		assertSame(a, player.getCurrentRoom());
	}

	/** moveTo wechselt den aktuellen Raum. */
	@Test
	@DisplayName("moveTo wechselt den aktuellen Raum")
	void moveToChangesCurrentRoom() {
		player.moveTo(b);
		assertSame(b, player.getCurrentRoom());
	}

	/** Bei leerem Stack geht back nicht. */
	@Test
	@DisplayName("back geht bei leerem Stack nicht")
	void cannotGoBackWithEmptyHistory() {
		assertFalse(player.canGoBack());
		assertNull(player.goBack());
		assertSame(a, player.getCurrentRoom());
	}

	/** Nach einem normalen Weg führt back in den vorherigen Raum. */
	@Test
	@DisplayName("back geht nach normalem Weg")
	void goBackAfterNormalMove() {
		player.moveTo(b);
		assertTrue(player.canGoBack());
		assertSame(a, player.goBack());
		assertSame(a, player.getCurrentRoom());
	}

	/** Nach einem Einbahnweg geht back nicht. */
	@Test
	@DisplayName("back geht nach Einbahnweg nicht")
	void cannotGoBackAfterOneWay() {
		player.moveTo(b);
		player.moveTo(c);
		assertFalse(player.canGoBack());
		assertNull(player.goBack());
		assertSame(c, player.getCurrentRoom());
	}

	/** Ist der Ausgang zurück gesperrt, geht back nicht. */
	@Test
	@DisplayName("back geht über gesperrten Ausgang nicht")
	void cannotGoBackThroughLockedExit() {
		player.moveTo(b);
		b.lockExit("west", "Locked.");
		assertFalse(player.canGoBack());
		assertNull(player.goBack());
	}

	/** goBack legt den Raum nicht wieder auf den Stack, zweimal back geht also nicht hin und her. */
	@Test
	@DisplayName("goBack legt den Raum nicht wieder auf den Stack")
	void goBackDoesNotPushRoom() {
		player.moveTo(b);
		player.goBack();
		assertFalse(player.canGoBack());
		assertNull(player.goBack());
		assertSame(a, player.getCurrentRoom());
	}

	/** Mehrmals back geht Schritt für Schritt den Weg zurück. */
	@Test
	@DisplayName("Mehrmals back geht den Weg Schritt für Schritt zurück")
	void goBackStepByStep() {
		player.moveTo(b);
		player.moveTo(a);
		player.moveTo(b);
		assertSame(a, player.goBack());
		assertSame(b, player.goBack());
		assertSame(a, player.goBack());
		assertNull(player.goBack());
	}
}
