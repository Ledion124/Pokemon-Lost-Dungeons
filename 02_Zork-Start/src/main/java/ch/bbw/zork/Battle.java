package ch.bbw.zork;

import java.util.ArrayList;
import java.util.List;

/**
 * Ein Kampf zwischen dem Team des Spielers (1 bis 3 Pokémon) und einem gegnerischen Pokémon.
 * Der Verlauf wird nicht ausgegeben, sondern in einem Log gesammelt.
 */
public class Battle {

    /** Die Pokémon des Spielers in Kampfreihenfolge. */
    private final List<Pokemon> team;

    /** Das gegnerische Pokémon. */
    private final Pokemon enemy;

    /** Der Kampfverlauf, eine Zeile pro Ereignis. */
    private final List<String> log = new ArrayList<>();

    /** Die Pokémon des Teams, die im Kampf mindestens einmal angegriffen haben. */
    private final List<Pokemon> fighters = new ArrayList<>();

    /**
     * Bereitet einen Kampf vor.
     *
     * @param team  1 bis 3 Pokémon des Spielers in Kampfreihenfolge
     * @param enemy das gegnerische Pokémon
     */
    public Battle(List<Pokemon> team, Pokemon enemy) {
        this.team = new ArrayList<>(team);
        this.enemy = enemy;
    }

    /**
     * Führt den Kampf durch. Der Spieler greift zuerst an, dann abwechselnd.
     * Fällt ein Pokémon des Teams, kämpft das nächste.
     * Bei Sieg steigen alle eingesetzten Pokémon um 2 Level, danach wird das ganze Team geheilt.
     *
     * @return true, wenn der Spieler gewonnen hat
     */
    public boolean fight() {
        boolean won = false;

        while (true) {
            Pokemon current = null;
            for (Pokemon poke : team) {
                if (!poke.isFainted()) {
                    current = poke;
                    break;
                }
            }
            if (current == null) {
                break;
            }
            if (!fighters.contains(current)) {
                fighters.add(current);
            }
            attack(current, enemy);
            if (enemy.isFainted()) {
                won = true;
                break;
            }
            attack(enemy, current);
            if (current.isFainted()) {
                log.add(current.getName() + " fainted!");
            }
        }

        if (won) {
            for (Pokemon fighter : fighters) {
                fighter.levelUp();
            }
            enemy.heal();
        }
        for (Pokemon pokemon : team) {
            pokemon.heal();
        }

        return won;
    }

    /**
     * Ein Angriff: berechnet den Schaden, zieht ihn beim Verteidiger ab und schreibt eine Zeile ins Log.
     *
     * @param attacker das angreifende Pokémon
     * @param defender das verteidigende Pokémon
     */
    private void attack(Pokemon attacker, Pokemon defender) {
        int damage = attacker.getDamageAgainst(defender);
        defender.takeDamage(damage);

        log.add(attacker.getName() + " hits " + defender.getName() + " for " + damage + " damage ("
                + defender.getName() + ": " + defender.getCurrentHp() + "/" + defender.getMaxHp() + " HP)");
    }

    /**
     * Gibt den Kampfverlauf zurück, eine Zeile pro Angriff oder Ereignis.
     *
     * @return Kopie der Liste mit allen Log-Zeilen
     */
    public List<String> getLog() {
        return new ArrayList<>(log);
    }
}