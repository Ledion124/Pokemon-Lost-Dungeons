# Gegenstände (Items)

In diesem Dokument erkläre ich, wie die Gegenstände in Pokémon Lost Dungeons umgesetzt sind. Das ist mein Teil im Projekt. Dazu gehören die Klassen `Item`, `KeyItem`, `Inventory`, `HidingSpot` und `ItemActions`, der Item- und Versteck-Teil von `Room` und die Methode `placeItems()` in `WorldBuilder`.

Abgedeckt sind die Aufgaben 2.4–2.6 und 3.1–3.3 aus dem Auftrag. Im Bewertungsraster (`docs/Bewertung.md`) gehören dazu die Punkte 14 (Item-Klasse), 15 (mehrere Gegenstände), 16 (Gewicht), 18 (Spieler trägt Gegenstände) und 20 (Vererbung).

Alle Code-Ausschnitte sind aus dem echten Code kopiert, JavaDoc habe ich zum Teil weggelassen, damit es kürzer wird.

## Übersicht

| Klasse | Aufgabe |
|---|---|
| `Item` | ein Gegenstand mit Name, Beschreibung und Gewicht |
| `KeyItem` | erbt von `Item`, öffnet in einem bestimmten Raum einen gesperrten Ausgang |
| `Inventory` | die Gegenstände, die der Spieler trägt |
| `HidingSpot` | eine Stelle im Raum, die man mit `search` untersuchen kann |
| `Room` | speichert die Gegenstände und Verstecke, die im Raum sind |
| `ItemActions` | die Befehle `look`, `search`, `take`, `drop` und `inventory` |
| `WorldBuilder` | verteilt mit `placeItems()` die Gegenstände in der Welt |

## Die Klasse Item

Ein Gegenstand hat drei Attribute: einen Namen, eine Beschreibung und ein Gewicht in kg. Der Name ist gleichzeitig das Wort, das man im Spiel eintippt, zum Beispiel `take flute`. Alle Attribute sind `final`, weil sich ein Gegenstand während dem Spiel nicht verändert.

```java
public class Item {

    private final String name;
    private final String description;
    private final double weight;

    public Item(String name, String description, double weight) {
        this.name = name;
        this.description = description;
        this.weight = weight;
    }
```

Dazu gibt es die Getter `getName()`, `getDescription()` und `getWeight()`.

`toString()` gibt den Namen und das Gewicht mit zwei Nachkommastellen zurück, also zum Beispiel `flute (0.50 kg)`. Ich habe `Locale.ROOT` verwendet, damit immer ein Punkt als Dezimaltrenner kommt. Mit der Schweizer Spracheinstellung von Windows käme sonst unter Umständen `0,50` heraus.

```java
@Override
public String toString() {
    return name + " (" + String.format(Locale.ROOT, "%.2f kg", weight) + ")";
}
```

## KeyItem erbt von Item

Im Spiel gibt es zwei besondere Gegenstände: die Flöte und den Schlüssel. Beide sind ganz normale Gegenstände, die man aufnehmen, ablegen und tragen kann. Zusätzlich öffnen sie aber in einem bestimmten Raum einen gesperrten Ausgang. Darum habe ich `KeyItem` als Unterklasse von `Item` gemacht. So muss ich Name, Beschreibung und Gewicht nicht nochmals schreiben, und ein `KeyItem` kann überall verwendet werden, wo ein `Item` erwartet wird, zum Beispiel im `Inventory` oder in einem Raum.

`KeyItem` hat zwei zusätzliche Attribute:

- `targetRoom` ist der Name des Raums, in dem der Gegenstand wirkt.
- `direction` ist die Richtung des Ausgangs, der dort geöffnet wird.

```java
public class KeyItem extends Item {

    private final String targetRoom;
    private final String direction;

    public KeyItem(String name, String description, double weight, String targetRoom, String direction) {
        super(name, description, weight);
        this.targetRoom = targetRoom;
        this.direction = direction;
    }
```

Damit die Werte für Flöte und Schlüssel nur an einer Stelle stehen, gibt es zwei statische Methoden, die den Gegenstand erstellen:

```java
public static KeyItem createFlute() {
    return new KeyItem("flute", "an old wooden flute", 0.5, "Greenpath", "east");
}

public static KeyItem createKey() {
    return new KeyItem("key", "a heavy iron key", 0.2, "White Palace", "south");
}
```

Benutzt werden die beiden mit `use` (das hat Louis in `WorldActions` gemacht). Dort wird geprüft, ob der Gegenstand ein `KeyItem` ist und ob der Spieler im richtigen Raum steht. Wenn ja, wird der Ausgang mit `unlockExit(getDirection())` geöffnet und der Gegenstand aus dem Inventar entfernt:

```java
if (!(item instanceof KeyItem keyItem)) {
```

Die Flöte weckt im Greenpath das Snorlax (Relaxo) auf, das den Weg nach Osten blockiert. Der Schlüssel öffnet im White Palace die Tür nach Süden in The Abyss.

## Gegenstände im Raum

Jeder Raum hat eine Liste mit den Gegenständen, die dort offen herumliegen. Ich habe eine `ArrayList` verwendet, damit beliebig viele Gegenstände in einem Raum liegen können:

```java
private final List<Item> items = new ArrayList<>();
```

Dafür gibt es in `Room` diese Methoden:

```java
public void addItem(Item item) {
    items.add(item);
}

public Item removeItem(String name) {
    Item item = getItem(name);
    if (item != null) {
        items.remove(item);
    }
    return item;
}

public Item getItem(String name) {
    if (name == null) {
        return null;
    }
    for (Item item : items) {
        if (item.getName().equalsIgnoreCase(name)) {
            return item;
        }
    }
    return null;
}
```

Mit `getItem` kann man den Raum also nach einem Gegenstand fragen. Gross- und Kleinschreibung spielt keine Rolle (`equalsIgnoreCase`), `take FLUTE` funktioniert also auch. Wenn es den Gegenstand nicht gibt, kommt `null` zurück. `getItems()` gibt eine Kopie der Liste zurück, damit niemand von aussen die Liste im Raum verändern kann.

Damit man im Spiel sieht, was im Raum liegt, hängt `longDescription()` die Zeile `Items: ...` an. Die Zeile kommt nur, wenn auch wirklich etwas da liegt:

```java
if (!items.isEmpty()) {
    stringBuilder.append("\n").append(itemString());
}
```

```java
private String itemString() {
    List<String> names = new ArrayList<>();
    for (Item item : items) {
        names.add(item.getName());
    }
    return "Items: " + String.join(", ", names);
}
```

So sieht das im Spiel aus, wenn man in die Queen's Gardens kommt:

```
You are in Queen's Gardens. An overgrown garden full of thorns and flowers.
Exits: south
Items: key
You could search: bush, fountain, bench, flowerbed
A wild Charmander (Fire) Lv 30 - HP 80/80, ATK 24, 0.85 kg appears!
```

### Mehrere Gegenstände pro Raum

Weil es eine Liste ist, können auch mehrere Gegenstände im gleichen Raum liegen. Was man mit `drop` ablegt, kommt einfach hinten in die Liste und bleibt dort liegen, bis man es wieder aufnimmt. Die Gegenstände werden in der Reihenfolge angezeigt, in der sie in den Raum gekommen sind, mit Komma getrennt:

```
Items: flute, key
```

Diese Ausgabe ist aus dem Test `RoomItemTest`. Im normalen Spielablauf liegen selten zwei Gegenstände im gleichen Raum, weil es nur die Flöte und den Schlüssel gibt und die Flöte beim Benutzen verbraucht wird, bevor man zum Schlüssel kommt. Die Liste funktioniert aber für beliebig viele Gegenstände, das ist in den Tests geprüft.

## Gewicht

Jeder Gegenstand hat ein Gewicht. Der Spieler kann höchstens 20 kg tragen. Die Konstante dafür steht in `Player`:

```java
public static final double MAX_WEIGHT = 20.0;
```

Zum Gewicht des Spielers zählen die Gegenstände im Inventar und die Pokémon, die er dabei hat. Die Pokémon sind in ihren Pokébällen und zählen deshalb nur mit einem Zehntel von ihrem echten Gewicht:

```java
public double getTotalWeight() {
    return inventory.getTotalWeight() + pokemons.getTotalWeight();
}
```

```java
public double getCarryWeight() {
    return weight / 10;
}
```

Ein Charmander wiegt zum Beispiel 8.5 kg, zählt aber nur mit 0.85 kg.

Ob noch etwas dazu passt, prüft `canCarry()`. Weil man mit `double` rechnet, gibt es kleine Rundungsfehler (zum Beispiel 0.1 + 0.2 ist nicht genau 0.3). Darum gibt es eine kleine Toleranz, sonst könnte man einen Gegenstand, der genau bis 20 kg reicht, nicht mehr aufnehmen:

```java
private static final double WEIGHT_TOLERANCE = 0.0001;

public boolean canCarry(double extraWeight) {
    return getTotalWeight() + extraWeight <= MAX_WEIGHT + WEIGHT_TOLERANCE;
}
```

Wenn ein Gegenstand zu schwer ist, bleibt er im Raum liegen und es kommt diese Meldung:

```
That is too heavy. Drop something or release a Pokémon first.
```

Das Gewicht wird auch an anderen Stellen gebraucht: Wenn man nach einem Kampf ein Pokémon fangen will, prüft Ledions Code in `PokemonActions` ebenfalls mit `player.canCarry(wild.getCarryWeight())`, ob es noch Platz hat.

## Inventar

Das `Inventory` ist die Liste der Gegenstände, die der Spieler trägt. Es funktioniert ähnlich wie die Liste im Raum:

```java
private final ArrayList<Item> items = new ArrayList<>();
```

Die Methoden sind `add`, `remove`, `get`, `getItems`, `getTotalWeight` und `isEmpty`. Ein Maximalgewicht hat das `Inventory` selbst nicht, das prüft der `Player`, weil dort auch die Pokémon mitgezählt werden.

```java
public double getTotalWeight() {
    double total = 0.0;
    for (Item item : items) {
        total += item.getWeight();
    }
    return total;
}
```

Die Befehle dazu sind in `ItemActions`.

### take

`take` nimmt einen Gegenstand aus dem Raum und legt ihn ins Inventar. Vorher wird geprüft, ob überhaupt ein zweites Wort da ist, ob der Gegenstand im Raum liegt und ob er nicht zu schwer ist:

```java
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
```

### drop

`drop` macht das Gegenteil: Der Gegenstand kommt aus dem Inventar in den aktuellen Raum. Hat man ihn nicht, kommt `You don't have that.`, ohne zweites Wort `Drop what?`.

```java
Item item = player.getInventory().remove(command.getSecondWord());
if (item == null) {
    System.out.println("You don't have that.");
    return;
}
player.getCurrentRoom().addItem(item);
System.out.println("You drop the " + item.getName() + ".");
```

### inventory

`inventory` zeigt alle Gegenstände mit ihrem Gewicht, danach das Gewicht der Pokémon und am Schluss das Gesamtgewicht im Vergleich zum Maximum. Wenn man nichts trägt, steht `You are not carrying any items.`.

Hier ein Ausschnitt aus einem echten Spiel. Ich habe Charmander als Starter gewählt, die Flöte im Ashfall Valley gefunden und sie später im White Palace abgelegt und wieder aufgenommen:

```
> search ash
You found flute!
> take flute
You take the flute.
...
> inventory
You are carrying:
  flute (0.50 kg)
Pokémon: 0.85 kg
Total: 1.35 / 20.00 kg
> drop flute
You drop the flute.
> look
You are in White Palace. A shining white palace. To the north lies the buried exit.
Exits: north(blocked) east south(blocked)
Items: flute
You could search: throne, pillar, statue, niche, chandelier
A wild Sneasel (Ice) Lv 30 - HP 104/104, ATK 36, 2.80 kg appears!
> take flute
You take the flute.
```

(Die eingegebenen Befehle nach `>` habe ich zur besseren Lesbarkeit dazugeschrieben, im Spiel sieht man nur das `>`. Bei `...` bin ich vom Ashfall Valley bis in den White Palace gelaufen.)

## Verstecke und search

Die Flöte liegt nicht einfach offen herum, sondern ist versteckt. Dafür gibt es die Klasse `HidingSpot`. Ein Versteck hat ein Befehlswort (z. B. `ash`), eine englische Beschreibung (z. B. `a pile of ash`) und eventuell einen versteckten Gegenstand:

```java
public class HidingSpot {

    private final String name;
    private final String description;
    private Item hiddenItem;
```

Mit `search()` wird das Versteck untersucht. Der Gegenstand wird zurückgegeben und das Versteck ist danach leer. So kann jedes Versteck nur einmal etwas hergeben:

```java
public Item search() {
    Item found = hiddenItem;
    hiddenItem = null;
    return found;
}
```

Jeder Raum hat eine Liste mit seinen Verstecken (`addHidingSpot`, `getHidingSpot`, `getHidingSpots`). `longDescription()` zeigt sie in der Zeile `You could search: ...` an, damit der Spieler weiss, was er untersuchen kann.

Der Befehl `search` in `ItemActions` sucht das Versteck im aktuellen Raum. Findet man etwas, liegt es danach offen im Raum und kann mit `take` aufgenommen werden:

```java
Item found = hidingSpot.search();
if (found == null) {
    System.out.println("You find nothing.");
    return;
}
room.addItem(found);
System.out.println("You found " + found.getName() + "!");
```

Die anderen Meldungen sind `Search what?` (ohne zweites Wort) und `There is no <name> here.` (wenn es das Versteck im Raum nicht gibt).

### Die Flöte wird zufällig versteckt

Die Gegenstände werden in `WorldBuilder` mit `placeItems()` verteilt. Die Methode wird als letzte Zeile im Konstruktor aufgerufen, wenn alle Räume schon gebaut sind:

```java
private void placeItems() {
    getRoom(QUEENS_GARDENS).addItem(KeyItem.createKey());
    addHidingSpots();
    hideFlute();
}
```

Zuerst kommt der Schlüssel offen in die Queen's Gardens. Dann bekommt jeder Raum ausser Exit seine Verstecke, so wie in der Tabelle „Stellen pro Raum“ in `Idee.md`. Zum Schluss wird die Flöte versteckt.

Damit das Spiel nicht jedes Mal gleich ist, kommt die Flöte in ein zufälliges Versteck. Es kommen aber nur die 7 Räume in Frage, die man erreicht, bevor das Snorlax weg ist. Sonst könnte die Flöte hinter dem Snorlax liegen, und man käme nie dorthin. In diesen 7 Räumen gibt es zusammen 26 Verstecke:

```java
private void hideFlute() {
    String[] fluteRooms = {ASHFALL_VALLEY, CITY_OF_TEARS, FORGOTTEN_CROSSROADS, FOG_CANYON,
            WHITE_PALACE, ANCIENT_TREASURES, ROYALE_WATERWAYS};
    List<HidingSpot> candidates = new ArrayList<>();
    for (String roomName : fluteRooms) {
        candidates.addAll(getRoom(roomName).getHidingSpots());
    }
    candidates.get(random.nextInt(candidates.size())).hideItem(KeyItem.createFlute());
}
```

Der Zufall kommt über das `Random`-Objekt, das dem `WorldBuilder` im Konstruktor mitgegeben wird. Im Spiel ist das `new Random()`, in den Tests `new Random(42)`. Mit dem gleichen Seed liegt die Flöte immer am gleichen Ort, deshalb kann man es testen.

## Alle Gegenstände

| Gegenstand | Name im Spiel | Klasse | Gewicht | Fundort | Wirkung |
|---|---|---|---|---|---|
| Flöte | `flute` | `KeyItem` | 0.5 kg | zufälliges Versteck in Ashfall Valley, City of Tears, Forgotten Crossroads, Fog Canyon, White Palace, Ancient Treasures oder Royale Waterways | `use flute` im Greenpath weckt das Snorlax, Weg nach Osten ist frei |
| Schlüssel | `key` | `KeyItem` | 0.2 kg | offen in den Queen's Gardens | `use key` im White Palace öffnet die Tür nach Süden (The Abyss) |

Beide Gegenstände werden beim Benutzen verbraucht. Zum Tragegewicht zählen ausserdem die Pokémon im Pokéball mit einem Zehntel ihres Gewichts (siehe [Gewicht](#gewicht)).

## Tests

Für jede Klasse gibt es JUnit-Tests. Jeder Test hat JavaDoc und einen `@DisplayName`. Alle Tests laufen mit `mvn clean test`.

| Testklasse | Anzahl | Was wird getestet |
|---|---|---|
| `ItemTest` | 6 | Konstruktor, Getter, `toString` mit zwei Nachkommastellen und Rundung |
| `KeyItemTest` | 8 | Zielraum und Richtung, Vererbung von `Item`, `createFlute()` und `createKey()` |
| `InventoryTest` | 8 | hinzufügen, entfernen, suchen ohne Gross-/Kleinschreibung, Gesamtgewicht, Kopie von `getItems` |
| `HidingSpotTest` | 7 | verstecken, untersuchen, nach `search` ist das Versteck leer |
| `RoomItemTest` | 16 | Items und Verstecke im Raum, Zeilen `Items: ...` und `You could search: ...` in `longDescription()` |
| `ItemActionsTest` | 14 | `take`, `drop`, `search`, `inventory` und `look` mit allen Meldungen, zu schweres Item, Item genau an der 20-kg-Grenze |
| `WorldBuilderItemsTest` | 8 | Schlüssel in den Queen's Gardens, Verstecke in jedem Raum ausser Exit, Flöte genau einmal und nur in den 7 erreichbaren Räumen, gleicher Ort mit `Random(42)` |

Bei `ItemActionsTest` wird die Konsolenausgabe mit `System.setOut` abgefangen. So kann man auch prüfen, ob die richtige Meldung kommt, und nicht nur, ob der Gegenstand am richtigen Ort ist.
