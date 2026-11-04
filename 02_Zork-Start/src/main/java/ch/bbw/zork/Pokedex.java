package ch.bbw.zork;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Der Katalog aller 20 Pokémon, die im Spiel vorkommen können.
 * Er erstellt neue Pokémon mit ihren Grundwerten aus docs/Idee.md.
 */
public class Pokedex {

    /** Namen aller 20 Pokémon im Katalog, sortiert nach Typ. */
    private static final List<String> NAMES = List.of(
            "Charmander", "Growlithe", "Ponyta", "Magmar",
            "Bulbasaur", "Bellsprout", "Chikorita", "Tangela",
            "Cubchoo", "Vanillite", "Snorunt", "Sneasel",
            "Diglett", "Cubone", "Sandshrew", "Phanpy",
            "Omanyte", "Kabuto", "Geodude", "Larvitar");

    /**
     * Erstellt ein bestimmtes Pokémon mit dem angegebenen Level.
     *
     * @param name  englischer Name, z. B. "Charmander"
     * @param level Level des neuen Pokémon
     * @return neues Pokémon mit vollen HP oder null, wenn der Name unbekannt ist
     */
    public static Pokemon create(String name, int level) {
        switch (name) {
            // Feuer
            case "Charmander": return new Pokemon("Charmander", PokemonType.FIRE, 20, 6, 8.5, level);
            case "Growlithe":  return new Pokemon("Growlithe", PokemonType.FIRE, 26, 7, 19.0, level);
            case "Ponyta":     return new Pokemon("Ponyta", PokemonType.FIRE, 30, 8, 30.0, level);
            case "Magmar":     return new Pokemon("Magmar", PokemonType.FIRE, 36, 9, 44.5, level);
            // Pflanze
            case "Bulbasaur":  return new Pokemon("Bulbasaur", PokemonType.GRASS, 22, 5, 6.9, level);
            case "Bellsprout": return new Pokemon("Bellsprout", PokemonType.GRASS, 16, 7, 4.0, level);
            case "Chikorita":  return new Pokemon("Chikorita", PokemonType.GRASS, 20, 5, 6.4, level);
            case "Tangela":    return new Pokemon("Tangela", PokemonType.GRASS, 34, 7, 35.0, level);
            // Eis
            case "Cubchoo":    return new Pokemon("Cubchoo", PokemonType.ICE, 21, 6, 8.5, level);
            case "Vanillite":  return new Pokemon("Vanillite", PokemonType.ICE, 16, 6, 5.7, level);
            case "Snorunt":    return new Pokemon("Snorunt", PokemonType.ICE, 24, 6, 16.8, level);
            case "Sneasel":    return new Pokemon("Sneasel", PokemonType.ICE, 26, 9, 28.0, level);
            // Boden
            case "Diglett":    return new Pokemon("Diglett", PokemonType.GROUND, 12, 8, 0.8, level);
            case "Cubone":     return new Pokemon("Cubone", PokemonType.GROUND, 22, 6, 6.5, level);
            case "Sandshrew":  return new Pokemon("Sandshrew", PokemonType.GROUND, 24, 7, 12.0, level);
            case "Phanpy":     return new Pokemon("Phanpy", PokemonType.GROUND, 32, 8, 33.5, level);
            // Gestein
            case "Omanyte":    return new Pokemon("Omanyte", PokemonType.ROCK, 18, 6, 7.5, level);
            case "Kabuto":     return new Pokemon("Kabuto", PokemonType.ROCK, 20, 7, 11.5, level);
            case "Geodude":    return new Pokemon("Geodude", PokemonType.ROCK, 28, 6, 20.0, level);
            case "Larvitar":   return new Pokemon("Larvitar", PokemonType.ROCK, 38, 9, 72.0, level);
            default:           return null;
        }
    }

    /**
     * Erstellt ein zufälliges der 20 Pokémon mit dem angegebenen Level.
     *
     * @param level  Level des neuen Pokémon
     * @param random Zufallsgenerator, im Test mit festem Startwert für gleiche Ergebnisse
     * @return neues, zufällig gewähltes Pokémon mit vollen HP
     */
    public static Pokemon createRandom(int level, Random random) {
        String name = NAMES.get(random.nextInt(NAMES.size()));
        return create(name, level);
    }

    /**
     * Gibt die Namen aller 20 Pokémon im Katalog zurück.
     *
     * @return neue Liste mit allen Namen
     */
    public static List<String> getAllNames() {
        return new ArrayList<>(NAMES);
    }
}
