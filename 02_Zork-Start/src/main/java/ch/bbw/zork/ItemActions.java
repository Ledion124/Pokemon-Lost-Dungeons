package ch.bbw.zork;

import java.util.Locale;

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
	 * Untersucht ein Versteck im aktuellen Raum. Ein gefundener Gegenstand
	 * liegt danach offen im Raum und kann mit {@code take} aufgenommen werden.
	 *
	 * @param command Befehl mit dem Namen des Verstecks als zweitem Wort
	 */
	public void search(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("Search what?");
			return;
		}
		String name = command.getSecondWord();
		Room room = player.getCurrentRoom();
		HidingSpot hidingSpot = room.getHidingSpot(name);
		if (hidingSpot == null) {
			System.out.println("There is no " + name + " here.");
			return;
		}
		Item found = hidingSpot.search();
		if (found == null) {
			System.out.println("You search " + hidingSpot.getDescription() + ". You find nothing.");
			return;
		}
		room.addItem(found);
		System.out.println("You search " + hidingSpot.getDescription() + ". You found " + found.getName() + "!");
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
	 * Zeigt alle getragenen Gegenstände mit Gewicht, danach das Gewicht der Pokémon
	 * und das Gesamtgewicht im Vergleich zum Maximum.
	 */
	public void showInventory() {
		Inventory inventory = player.getInventory();
		if (inventory.isEmpty()) {
			System.out.println("You are not carrying any items.");
		} else {
			System.out.println("You are carrying:");
			for (Item item : inventory.getItems()) {
				System.out.println("  " + item);
			}
		}
		System.out.println("Pokémon: " + formatWeight(player.getPokemons().getTotalWeight()));
		System.out.println("Total: " + formatWeight(player.getTotalWeight())
				+ " / " + formatWeight(Player.MAX_WEIGHT));
	}

	/**
	 * Formatiert ein Gewicht mit 2 Nachkommastellen und Punkt als Dezimaltrenner.
	 *
	 * @param weight Gewicht in kg
	 * @return z. B. "1.50 kg"
	 */
	private static String formatWeight(double weight) {
		return String.format(Locale.ROOT, "%.2f kg", weight);
	}
}
