package ch.bbw.zork;

/**
 * Befehle rund um Gegenstände: umsehen, Verstecke untersuchen, aufnehmen,
 * ablegen und Inventar anzeigen. {@link Game} ruft diese Methoden nur auf.
 *
 * Besitzer: Castelberg.
 */
public class ItemActions {

	/** Der Spieler mit aktuellem Raum und Inventar. */
	private final Player player;

	/**
	 * Erstellt die Item-Befehle für einen Spieler.
	 *
	 * @param player der Spieler
	 */
	public ItemActions(Player player) {
		this.player = player;
	}

	/**
	 * Zeigt den aktuellen Raum mit Items, Verstecken und wildem Pokémon an.
	 * TODO (Castelberg, Block 3): prüfen, ob noch etwas ergänzt werden muss.
	 */
	public void look() {
		System.out.println(player.getCurrentRoom().longDescription());
	}

	/**
	 * Untersucht ein Versteck im aktuellen Raum.
	 * TODO (Castelberg, Block 3): verstecktes Item in den Raum legen.
	 *
	 * @param command Befehl mit dem Namen des Verstecks als zweitem Wort
	 */
	public void search(Command command) {
		System.out.println("'search' is not implemented yet.");
	}

	/**
	 * Nimmt einen Gegenstand aus dem Raum ins Inventar.
	 * TODO (Castelberg, Block 3): mit Gewichtsprüfung über player.canCarry umsetzen.
	 *
	 * @param command Befehl mit dem Namen des Gegenstands als zweitem Wort
	 */
	public void takeItem(Command command) {
		System.out.println("'take' is not implemented yet.");
	}

	/**
	 * Legt einen Gegenstand aus dem Inventar in den Raum.
	 * TODO (Castelberg, Block 3): umsetzen.
	 *
	 * @param command Befehl mit dem Namen des Gegenstands als zweitem Wort
	 */
	public void dropItem(Command command) {
		System.out.println("'drop' is not implemented yet.");
	}

	/**
	 * Zeigt alle getragenen Gegenstände und das Gesamtgewicht an.
	 * TODO (Castelberg, Block 3): umsetzen.
	 */
	public void showInventory() {
		System.out.println("'inventory' is not implemented yet.");
	}
}
