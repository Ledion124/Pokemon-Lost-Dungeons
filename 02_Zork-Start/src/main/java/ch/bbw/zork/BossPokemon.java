package ch.bbw.zork;

/**
 * Ein Boss-Pokémon. Es kämpft wie ein normales Pokémon,
 * kann aber nicht verscheucht werden und hat einen eigenen Text, wenn es besiegt wird.
 */
public class BossPokemon extends Pokemon {

    /**
     * Erstellt ein neues Boss-Pokémon mit vollen HP.
     *
     * @param name       englischer Name, z. B. "Arceus"
     * @param type       Typ des Boss-Pokémon
     * @param baseHp     HP auf Level 0
     * @param baseAttack Angriff auf Level 0
     * @param weight     echtes Gewicht in kg
     * @param level      Level des Boss-Pokémon
     */
    public BossPokemon(String name, PokemonType type, int baseHp, int baseAttack, double weight, int level) {
        super(name, type, baseHp, baseAttack, weight, level);
    }

    /**
     * Ein Boss-Pokémon lässt sich nicht verscheuchen.
     *
     * @return immer false
     */
    @Override
    public boolean canBeScared() {
        return false;
    }

    /**
     * Gibt den Text zurück, der erscheint, wenn der Boss besiegt wurde.
     *
     * @return englischer Text mit dem Namen des Bosses: er gibt den Weg zum Ausgang frei
     */
    public String getDefeatMessage() {
        return getName() + " bows its head. It recognizes your strength and clears the way to the exit.";
    }
}