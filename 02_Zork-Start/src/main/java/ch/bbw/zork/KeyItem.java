package ch.bbw.zork;

/**
 * Ein Gegenstand, der in einem bestimmten Raum einen gesperrten Ausgang öffnet.
 * Die Flöte weckt Relaxo im Greenpath, der Schlüssel öffnet die Tür im White Palace.
 */
public class KeyItem extends Item {

    /** Name des Raums, in dem der Gegenstand benutzt werden kann. */
    private final String targetRoom;

    /** Richtung des Ausgangs, der geöffnet wird ("north", "east", "south" oder "west"). */
    private final String direction;

    /**
     * Erstellt einen neuen Schlüssel-Gegenstand.
     *
     * @param name        Name des Gegenstands (Befehlswort)
     * @param description kurze englische Beschreibung
     * @param weight      Gewicht in kg
     * @param targetRoom  Name des Raums, in dem er wirkt
     * @param direction   Richtung des Ausgangs, der geöffnet wird
     */
    public KeyItem(String name, String description, double weight, String targetRoom, String direction) {
        super(name, description, weight);
        this.targetRoom = targetRoom;
        this.direction = direction;
    }

    /**
     * Gibt den Namen des Raums zurück, in dem der Gegenstand wirkt.
     *
     * @return der Raumname
     */
    public String getTargetRoom() {
        return targetRoom;
    }

    /**
     * Gibt die Richtung des Ausgangs zurück, der geöffnet wird.
     *
     * @return die Richtung
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Erstellt die Flöte, die Relaxo im Greenpath aufweckt (Ausgang east).
     *
     * @return die Flöte (0.5 kg)
     */
    public static KeyItem createFlute() {
        return new KeyItem("flute", "an old wooden flute", 0.5, "Greenpath", "east");
    }

    /**
     * Erstellt den Schlüssel für die Tür im White Palace (Ausgang south).
     *
     * @return der Schlüssel (0.2 kg)
     */
    public static KeyItem createKey() {
        return new KeyItem("key", "a heavy iron key", 0.2, "White Palace", "south");
    }
}
