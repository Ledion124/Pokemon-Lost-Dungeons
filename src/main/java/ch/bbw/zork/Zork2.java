package ch.bbw.zork;

/**
 * Startklasse von Pokémon Lost Dungeons. Enthält nur die {@code main}-Methode,
 * die ein neues {@link Game} erstellt und startet.
 *
 * @author Michael Kolling, Rinaldo Lanza (Vorlage)
 */
public class Zork2 {

	/**
	 * Startet das Spiel.
	 *
	 * @param args Kommandozeilen-Argumente (werden nicht verwendet)
	 */
	public static void main(String[] args) {
		Game zorkgame = new Game();
		zorkgame.play();
	}

}
