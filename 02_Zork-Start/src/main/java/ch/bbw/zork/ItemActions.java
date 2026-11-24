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
	 * Ist er zu schwer (Inventar + Pokémon über {@link Player#MAX_WEIGHT}), bleibt er liegen.
	 *
	 * @param command Befehl mit dem Namen des Gegenstands als zweitem Wort
	 */
	public void takeItem(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("Take what?");
			return;
		}
		String name = command.getSecondWord();
		Room room = player.getCurrentRoom();
		Item item = room.getItem(name);
		if (item == null) {
			System.out.println("There is no " + name + " here.");
			return;
		}
		if (!player.canCarry(item.getWeight())) {
			System.out.println("That is too heavy. Drop something or release a Pokémon first.");
			return;
		}
		room.removeItem(item.getName());
		player.getInventory().add(item);
		System.out.println("You take the " + item.getName() + ".");
	}

	/**
	 * Legt einen Gegenstand aus dem Inventar in den aktuellen Raum.
	 *
	 * @param command Befehl mit dem Namen des Gegenstands als zweitem Wort
	 */
	public void dropItem(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("Drop what?");
			return;
		}
		Item item = player.getInventory().remove(command.getSecondWord());
		if (item == null) {
			System.out.println("You don't have that.");
			return;
		}
		player.getCurrentRoom().addItem(item);
		System.out.println("You drop the " + item.getName() + ".");
	}

	/**
	 * Zeigt alle getragenen Gegenstände und das Gesamtgewicht an.
	 * TODO (Castelberg, Block 3): umsetzen.
	 */
	public void showInventory() {
		System.out.println("'inventory' is not implemented yet.");
	}
}
