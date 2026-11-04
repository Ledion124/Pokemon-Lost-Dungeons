package ch.bbw.zork;

import java.util.Stack;

/**
 * Der Spieler: weiss, in welchem Raum er gerade ist, woher er gekommen ist,
 * welche Gegenstände er trägt und welche Pokémon er dabei hat.
 * Die besuchten Räume liegen auf einem Stack, damit {@code back} Schritt für
 * Schritt zurückgehen kann.
 */
public class Player {

	/** Maximales Gesamtgewicht in kg (Gegenstände + Pokémon im Pokéball). */
	public static final double MAX_WEIGHT = 20.0;

	/** Toleranz für Rundungsfehler beim Vergleich von Gewichten. */
	private static final double WEIGHT_TOLERANCE = 0.0001;

	/** Raum, in dem der Spieler gerade steht. */
	private Room currentRoom;

	/** Bisher besuchte Räume, der zuletzt verlassene liegt oben. */
	private final Stack<Room> history = new Stack<>();

	/** Gegenstände, die der Spieler trägt. */
	private final Inventory inventory = new Inventory();

	/** Pokémon, die der Spieler gefangen hat. */
	private final PokemonCollection pokemons = new PokemonCollection();

	/**
	 * Erstellt einen Spieler im Startraum mit leerem Verlauf, leerem Inventar
	 * und ohne Pokémon.
	 *
	 * @param startRoom Raum, in dem das Spiel beginnt
	 */
	public Player(Room startRoom) {
		this.currentRoom = startRoom;
	}

	/**
	 * Gibt den Raum zurück, in dem der Spieler gerade steht.
	 *
	 * @return aktueller Raum
	 */
	public Room getCurrentRoom() {
		return currentRoom;
	}

	/**
	 * Geht in einen neuen Raum. Der bisherige Raum wird auf den Stack gelegt.
	 *
	 * @param room Raum, in den der Spieler geht
	 */
	public void moveTo(Room room) {
		history.push(currentRoom);
		currentRoom = room;
	}

	/**
	 * Prüft, ob der Spieler zurückgehen kann. Das geht nur, wenn es einen
	 * vorherigen Raum gibt und ein offener Ausgang dorthin führt. So ist
	 * {@code back} über Einbahnwege und gesperrte Ausgänge automatisch blockiert.
	 *
	 * @return true, wenn {@link #goBack()} möglich ist
	 */
	public boolean canGoBack() {
		return !history.isEmpty() && currentRoom.hasExitTo(history.peek());
	}

	/**
	 * Geht zurück in den zuletzt verlassenen Raum. Der Raum wird vom Stack
	 * genommen und nicht wieder darauf gelegt.
	 *
	 * @return der neue aktuelle Raum, oder {@code null}, wenn {@link #canGoBack()} false ist
	 */
	public Room goBack() {
		if (!canGoBack()) {
			return null;
		}
		currentRoom = history.pop();
		return currentRoom;
	}

	/**
	 * Gibt das Inventar mit allen getragenen Gegenständen zurück.
	 *
	 * @return Inventar des Spielers
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * Gibt die Sammlung aller gefangenen Pokémon zurück.
	 *
	 * @return Pokémon des Spielers
	 */
	public PokemonCollection getPokemons() {
		return pokemons;
	}

	/**
	 * Berechnet das Gesamtgewicht: Gegenstände im Inventar plus das
	 * Tragegewicht der Pokémon im Pokéball.
	 *
	 * @return Gesamtgewicht in kg
	 */
	public double getTotalWeight() {
		return inventory.getTotalWeight() + pokemons.getTotalWeight();
	}

	/**
	 * Prüft, ob der Spieler zusätzliches Gewicht tragen kann, ohne
	 * {@link #MAX_WEIGHT} zu überschreiten.
	 *
	 * @param extraWeight zusätzliches Gewicht in kg
	 * @return true, wenn das neue Gesamtgewicht höchstens {@link #MAX_WEIGHT} ist
	 */
	public boolean canCarry(double extraWeight) {
		return getTotalWeight() + extraWeight <= MAX_WEIGHT + WEIGHT_TOLERANCE;
	}
}
