package ch.bbw.zork;

import java.util.Locale;

/**
 * Ein Pokémon im Spiel. Es hat einen Typ, ein Level und Basiswerte,
 * aus denen HP und Angriff berechnet werden. Es kann kämpfen,
 * Schaden nehmen, geheilt werden und aufleveln.
 */
public class Pokemon {

    /** Englischer Name, z. B. "Charmander". */
    private final String name;

    /** Typ des Pokémon, bestimmt die Stärken und Schwächen im Kampf. */
    private final PokemonType type;

    /** HP auf Level 0, daraus werden die maximalen HP berechnet. */
    private final int baseHp;

    /** Angriff auf Level 0, daraus wird der Angriff berechnet. */
    private final int baseAttack;

    /** Echtes Gewicht in kg. */
    private final double weight;

    /** Aktuelles Level. */
    private int level;

    /** Aktuelle HP, liegen immer zwischen 0 und den maximalen HP. */
    private int currentHp;

    /**
     * Erstellt ein neues Pokémon mit vollen HP.
     *
     * @param name       englischer Name, z. B. "Charmander"
     * @param type       Typ des Pokémon
     * @param baseHp     HP auf Level 0
     * @param baseAttack Angriff auf Level 0
     * @param weight     echtes Gewicht in kg
     * @param level      Start-Level
     */
    public Pokemon(String name, PokemonType type, int baseHp, int baseAttack, double weight, int level) {
        this.name = name;
        this.type = type;
        this.baseHp = baseHp;
        this.baseAttack = baseAttack;
        this.weight = weight;
        this.level = level;
        currentHp = getMaxHp();
    }

    /**
     * Gibt den Namen zurück.
     *
     * @return englischer Name
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt den Typ zurück.
     *
     * @return Typ des Pokémon
     */
    public PokemonType getType() {
        return type;
    }

    /**
     * Gibt das aktuelle Level zurück.
     *
     * @return aktuelles Level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gibt das echte Gewicht zurück.
     *
     * @return Gewicht in kg
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Gibt die aktuellen HP zurück.
     *
     * @return aktuelle HP
     */
    public int getCurrentHp() {
        return currentHp;
    }

    /**
     * Gibt das Gewicht im Pokéball zurück (ein Zehntel des echten Gewichts).
     * Dieser Wert zählt zum Tragegewicht des Spielers.
     *
     * @return Tragegewicht in kg
     */
    public double getCarryWeight() {
        return weight / 10;
    }

    /**
     * Berechnet die maximalen HP: abgerundet baseHp × (1 + level × 0.1).
     *
     * @return maximale HP
     */
    public int getMaxHp() {
        return (int) Math.floor(baseHp * (1 + level * 0.1));
    }

    /**
     * Berechnet den Angriff: abgerundet baseAttack × (1 + level × 0.1).
     *
     * @return Angriffswert
     */
    public int getAttack() {
        return (int) Math.floor(baseAttack * (1 + level * 0.1));
    }

    /**
     * Berechnet den Schaden gegen ein anderes Pokémon:
     * abgerundet Angriff × Typ-Faktor, mindestens 1.
     *
     * @param defender das angegriffene Pokémon
     * @return Schaden, mindestens 1
     */
    public int getDamageAgainst(Pokemon defender) {
        double factor = type.getMultiplier(defender.getType());
        int damage = (int) Math.floor(getAttack() * factor);
        if (damage < 1) {
            return 1;
        }
        return damage;
    }

    /**
     * Zieht Schaden von den aktuellen HP ab. Die HP fallen nie unter 0.
     *
     * @param damage Schaden, der abgezogen wird
     */
    public void takeDamage(int damage) {
        currentHp = currentHp - damage;
        if (currentHp < 0) {
            currentHp = 0;
        }
    }

    /**
     * Prüft, ob das Pokémon besiegt ist.
     *
     * @return true, wenn die aktuellen HP 0 sind
     */
    public boolean isFainted() {
        return currentHp == 0;
    }

    /**
     * Heilt das Pokémon vollständig auf die maximalen HP.
     */
    public void heal() {
        currentHp = getMaxHp();
    }

    /**
     * Erhöht das Level um 2 und heilt das Pokémon danach vollständig.
     */
    public void levelUp() {
        level += 2;
        heal();
    }

    /**
     * Gibt an, ob dieses Pokémon verscheucht werden kann.
     * Wird in BossPokemon überschrieben.
     *
     * @return true, normale Pokémon können immer verscheucht werden
     */
    public boolean canBeScared() {
        return true;
    }

    /**
     * Gibt das Pokémon als Text zurück,
     * z. B. "Charmander (Fire) Lv 5 - HP 30/30, ATK 9, 0.85 kg".
     *
     * @return Beschreibung mit Name, Typ, Level, HP, Angriff und Tragegewicht
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "%s (%s) Lv %d - HP %d/%d, ATK %d, %.2f kg",
                name, type.getDisplayName(), level, currentHp, getMaxHp(), getAttack(), getCarryWeight());
    }
}