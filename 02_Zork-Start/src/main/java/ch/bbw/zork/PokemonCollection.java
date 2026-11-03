package ch.bbw.zork;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Pokémon-Sammlung des Spielers. Die Pokémon werden im Spiel
 * über Nummern angesprochen, die bei 1 beginnen.
 */
public class PokemonCollection {

    /** Alle Pokémon der Sammlung, in der Reihenfolge, in der sie hinzugefügt wurden. */
    private final List<Pokemon> pokemons = new ArrayList<>();

    /**
     * Fügt ein Pokémon am Ende der Sammlung hinzu.
     *
     * @param pokemon das Pokémon, das hinzugefügt wird
     */
    public void add(Pokemon pokemon) {
        pokemons.add(pokemon);
    }

    /**
     * Gibt das Pokémon mit der angegebenen Nummer zurück (die Nummerierung beginnt bei 1).
     *
     * @param number Nummer des Pokémon, beginnend bei 1
     * @return das Pokémon oder null, wenn es die Nummer nicht gibt
     */
    public Pokemon get(int number) {
        if (number < 1 || number > pokemons.size()) {
            return null;
        }
        return pokemons.get(number - 1);
    }

    /**
     * Entfernt das Pokémon mit der angegebenen Nummer (die Nummerierung beginnt bei 1).
     *
     * @param number Nummer des Pokémon, beginnend bei 1
     * @return das entfernte Pokémon oder null, wenn es die Nummer nicht gibt
     */
    public Pokemon remove(int number) {
        if (number < 1 || number > pokemons.size()) {
            return null;
        }
        return pokemons.remove(number - 1);
    }

    /**
     * Gibt die Anzahl der Pokémon in der Sammlung zurück.
     *
     * @return Anzahl der Pokémon
     */
    public int size() {
        return pokemons.size();
    }

    /**
     * Prüft, ob die Sammlung leer ist.
     *
     * @return true, wenn keine Pokémon in der Sammlung sind
     */
    public boolean isEmpty() {
        return pokemons.isEmpty();
    }

    /**
     * Gibt eine Kopie aller Pokémon der Sammlung zurück.
     * Änderungen an der Kopie verändern die Sammlung nicht.
     *
     * @return neue Liste mit allen Pokémon
     */
    public List<Pokemon> getAll() {
        return new ArrayList<>(pokemons);
    }

    /**
     * Heilt alle Pokémon der Sammlung vollständig.
     */
    public void healAll() {
        for (Pokemon pokemon : pokemons) {
            pokemon.heal();
        }
    }

    /**
     * Berechnet das Gesamtgewicht aller Pokémon im Pokéball.
     * Dieser Wert zählt zum Tragegewicht des Spielers.
     *
     * @return Summe der Tragegewichte in kg
     */
    public double getTotalWeight() {
        double total = 0;
        for (Pokemon pokemon : pokemons) {
            total += pokemon.getCarryWeight();
        }
        return total;
    }
}
