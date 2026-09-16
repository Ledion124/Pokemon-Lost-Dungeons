# Fragen zum Programm "Zork"

## 1. Konstruktor der Klasse `Game`

**Frage:** Suchen Sie den Konstruktor der Klasse `Game` und schreiben Sie hier den Code, der ein Objekt der Klasse erzeugt (instanziiert). Wo wird dies im Programm gemacht?

**Antwort:** Die Objekte werden im Konstruktor von `Game` erzeugt:

\`\`\`java
outside = new Room("outside G block on Peninsula campus");
lab     = new Room("lab, a lecture theatre in A block");
tavern  = new Room("the Seahorse Tavern (the campus pub)");
gblock  = new Room("the G Block");
office  = new Room("the computing admin office");
\`\`\`

---

## 2. Konstruktor der Klasse `Room`

**Frage:** Suchen Sie den Konstruktor der Klasse `Room` und schreiben Sie die Signatur hier hin.

**Antwort:**

\`\`\`java
/**
* Erstellt einen neuen Raum mit der angegebenen Beschreibung.
* Die Ausgänge (exits) werden zu Beginn leer initialisiert.
*
* @param description Die Beschreibung des Raums
  */
  public Room(String description)
  \`\`\`

---

## 3. Konstruktor der Klasse `Command`

**Frage:** Suchen Sie den Konstruktor der Klasse `Command` und schreiben Sie die Signatur hier hin.

**Antwort:**

\`\`\`java
/**
* Erstellt ein Command-Objekt ohne zweites Wort.
* Ruft den Konstruktor mit zwei Parametern auf und setzt secondWord auf null.
*
* @param commandWord Das erste Wort des Befehls
  */
  public Command(String commandWord)

/**
* Erstellt ein Command-Objekt mit einem ersten und einem zweiten Wort.
*
* @param commandWord Das erste Wort des Befehls
* @param secondWord Das zweite Wort des Befehls, oder null falls keines vorhanden ist
  */
  public Command(String commandWord, String secondWord)
  \`\`\`

---

## 4. Deklaration der Objektvariable für die Ausgänge im Raum

**Frage:** Suchen Sie die Stelle, wo die Objektvariable für die Ausgänge im Raum deklariert wird. Schreiben Sie den Code hier hin.

**Antwort:**

\`\`\`java
public void setExits(Room north, Room east, Room south, Room west) {
exits.put("north", north);
exits.put("east", east);
exits.put("south", south);
exits.put("west", west);
}
\`\`\`

---

## 5. Instanziierung des Ausgänge-Objekts

**Frage:** Suchen Sie die Stelle, wo das entsprechende Objekt instanziiert wird. Wo ist das und wie sieht der Code aus?

**Antwort:** Das befindet sich in der Klasse `Game`:

\`\`\`java
outside.setExits(null, lab, gblock, tavern);
lab.setExits(null, null, null, outside);
tavern.setExits(null, outside, null, null);
gblock.setExits(outside, office, null, null);
office.setExits(null, null, null, gblock);
\`\`\`

---

## 6. Meldung "Das Fenster ist offen, Brrr"

**Frage:** Für einen bestimmten Raum soll eine Meldung "Das Fenster ist offen, brrrrrrr" programmiert werden. Diese wird ausgegeben, wenn der Raum betreten wird. Programmieren Sie diese Logik in der Klasse `Game`.

**Antwort:** Die Logik befindet sich in der Methode `goRoom` in der Klasse `Game`:

\`\`\`java
private void goRoom(Command command) {
if (!command.hasSecondWord()) {
System.out.println("Go where?");
} else {
String direction = command.getSecondWord();
Room nextRoom = currentRoom.nextRoom(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        } else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.longDescription());

            if (currentRoom == lab) {
                System.out.println("Das Fenster ist offen, Brrr");
            }
        }
    }
}
\`\`\`
