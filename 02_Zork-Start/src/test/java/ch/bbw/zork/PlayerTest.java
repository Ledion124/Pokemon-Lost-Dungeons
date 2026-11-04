package ch.bbw.zork;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für {@link Player}: Bewegung, Zurückgehen mit {@code back} und Gewicht.
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

	/** Ein neuer Spieler hat leeres Inventar, keine Pokémon und wiegt 0 kg. */
	@Test
	@DisplayName("Neuer Spieler ist leer und wiegt 0 kg")
	void newPlayerIsEmpty() {
		assertTrue(player.getInventory().isEmpty());
		assertEquals(0, player.getPokemons().size());
		assertEquals(0.0, player.getTotalWeight(), 0.0001);
	}

	/** Das Gesamtgewicht zählt Items und das Tragegewicht der Pokémon zusammen. */
	@Test
	@DisplayName("Gesamtgewicht zählt Items und Pokémon im Pokéball")
	void totalWeightCountsItemsAndPokemons() {
		player.getInventory().add(new Item("rock", "A rock.", 2.5));
		player.getPokemons().add(new Pokemon("Testmon", PokemonType.NORMAL, 10, 5, 30.0, 5));
		assertEquals(2.5 + 3.0, player.getTotalWeight(), 0.0001);
	}

	/** Genau bis 20 kg kann der Spieler tragen. */
	@Test
	@DisplayName("canCarry ist an der 20-kg-Grenze true")
	void canCarryUpToMaxWeight() {
		player.getInventory().add(new Item("rock", "A rock.", 19.5));
		assertTrue(player.canCarry(0.5));
	}

	/** Schon knapp über 20 kg kann der Spieler nicht mehr tragen. */
	@Test
	@DisplayName("canCarry ist knapp über 20 kg false")
	void cannotCarryOverMaxWeight() {
		player.getInventory().add(new Item("rock", "A rock.", 19.5));
		assertFalse(player.canCarry(0.51));
	}

	/** Rundungsfehler bei Kommazahlen führen nicht zu einem falschen false. */
	@Test
	@DisplayName("canCarry verträgt Rundungsfehler")
	void canCarryToleratesRounding() {
		for (int i = 0; i < 199; i++) {
			player.getInventory().add(new Item("pebble" + i, "A pebble.", 0.1));
		}
		assertTrue(player.canCarry(0.1));
	}
}
