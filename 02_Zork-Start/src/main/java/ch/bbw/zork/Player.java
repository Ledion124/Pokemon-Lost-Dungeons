package ch.bbw.zork;

import java.util.Stack;

/**
 * Der Spieler: weiss, in welchem Raum er gerade ist und woher er gekommen ist.
 * Die besuchten Räume liegen auf einem Stack, damit {@code back} Schritt für
 * Schritt zurückgehen kann.
 */
public class Player {

	/** Raum, in dem der Spieler gerade steht. */
	private Room currentRoom;

	/** Bisher besuchte Räume, der zuletzt verlassene liegt oben. */
	private final Stack<Room> history = new Stack<>();

	/**
	 * Erstellt einen Spieler im Startraum mit leerem Verlauf.
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
}
