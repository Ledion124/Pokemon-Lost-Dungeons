# Plan – Arbeitsteilung Pokémon Lost Dungeons

Spielidee und Regeln: siehe [Idee.md](Idee.md). **Dieser Plan legt fest, wer was wann baut und wie die Klassen zusammenpassen.** Wo Plan und Idee sich widersprechen, gilt der Plan.

## Grundidee: Arbeiten in Blöcken

Wir arbeiten in **5 Blöcken** (0–4). In jedem Block arbeiten alle drei **gleichzeitig und unabhängig**, danach wird alles nach `dev` gemergt, und erst dann startet der nächste Block.

```
Block N:   Engel ──┐
      Castelberg ──┼──► alle 3 PRs in dev gemergt ──► mvn clean test grün ──► Block N+1
           Sakaj ──┘
```

Damit das ohne Warten und ohne Merge-Konflikte klappt:

1. **Man braucht nur, was in einem früheren Block gebaut wurde.** Was ein anderer im *gleichen* Block baut, braucht man nie.
2. **Jede Datei hat pro Block genau einen Besitzer** (siehe [Datei-Besitz](#datei-besitz)). Ausnahme `Game.java` in Block 3: dort hat jeder einen **eigenen Abschnitt** mit seinen Methoden.
3. **Die Schnittstellen in diesem Plan sind verbindlich.** Wer etwas ändern will, fragt zuerst im Gruppenchat und passt dann diesen Plan an.

## Verantwortungsbereiche

| Person | Bereich | Branch |
|---|---|---|
| **Engel** | Welt & Navigation: Räume, Ausgänge, Sperren, Spieler, `back`, `map`, `use`, Starter, Gewonnen, Neustart | `dev-engel` |
| **Castelberg** | Gegenstände: Items, Inventar, Verstecke, Flöte/Schlüssel platzieren, `take`/`drop`/`inventory`/`look`/`search` | `dev-castelberg` |
| **Sakaj** | Pokémon & Kampf: Typen, Pokémon, Pokédex, Sammlung, Kampf, Boss, Spawnen, `fight`/`scare`/`team`/`release` | `dev-sakaj` |

---

## Gemeinsame Festlegungen (gelten für alle Blöcke)

### Technik
- Java **17**, Maven, JUnit **5** (schon im `pom.xml`). Alles im Ordner `02_Zork-Start`.
- Code: `02_Zork-Start/src/main/java/ch/bbw/zork/`, Tests: `02_Zork-Start/src/test/java/ch/bbw/zork/` (Paket `ch.bbw.zork`).
- Testklasse heisst wie die Klasse + `Test`, z. B. `RoomTest`. Assertions mit `org.junit.jupiter.api.Assertions.*`.
- Zufall **immer** über einen `Random`-Parameter, in Tests `new Random(42)`. Nie `Math.random()`.
- Tests prüfen nur Fachklassen (nicht `Game`, da alles `private` und Konsole).

### Sprache
- **Im Spiel (Konsole) alles Englisch**: Ausgaben, Raumnamen, Beschreibungen, Items, Pokémon.
- **Klassen-, Methoden- und Variablennamen Englisch.**
- **JavaDoc und Kommentare Deutsch** (wie im bestehenden Code).
- Doku-Dateien (`docs/`, `README.md`) Deutsch.

### Raumnamen

Diese Strings werden von allen drei verwendet (`getRoom(name)`, `KeyItem`, Boss-Logik). Engel legt sie in Block 1 zusätzlich als Konstanten in `WorldBuilder` an (`WorldBuilder.WHITE_PALACE` usw.). In Block 1 benutzen Castelberg und Sakaj die String-Werte, ab Block 2 dürfen alle die Konstanten nehmen.

| Konstante | Name | `pokemonLevel` |
|---|---|---|
| `ASHFALL_VALLEY` | `Ashfall Valley` | 0 |
| `CITY_OF_TEARS` | `City of Tears` | 5 |
| `FORGOTTEN_CROSSROADS` | `Forgotten Crossroads` | 10 |
| `FOG_CANYON` | `Fog Canyon` | 20 |
| `WHITE_PALACE` | `White Palace` | 30 |
| `THE_ABYSS` | `The Abyss` | 50 |
| `ANCIENT_TREASURES` | `Ancient Treasures` | 5 |
| `KINGS_PASS` | `King's Pass` | 20 |
| `QUEENS_GARDENS` | `Queen's Gardens` | 30 |
| `RESTING_GROUNDS` | `Resting Grounds` | 10 |
| `GREENPATH` | `Greenpath` | 0 |
| `ROYALE_WATERWAYS` | `Royale Waterways` | 5 |
| `EXIT` | `Exit` | 0 |

`pokemonLevel` 0 heisst: hier spawnt nie ein Pokémon. The Abyss hat 50, dort steht aber nur der Boss.

### Eingaben
- Richtungen sind genau `north`, `east`, `south`, `west`.
- Item-, Versteck- und Befehlswörter werden **ohne Beachtung der Gross-/Kleinschreibung** verglichen (`equalsIgnoreCase`).
- Nummern (Team, `release`) sind **1-basiert**, so wie `team` sie anzeigt. Ungültige Eingabe (keine Zahl, zu klein, zu gross) → Meldung, nichts passiert.

### Rechnen
- HP und Schaden: `(int) Math.floor(...)`. Schaden ist **mindestens 1**.
- Gewichte sind `double` in kg. Vergleich mit Toleranz: `getTotalWeight() + extra <= MAX_WEIGHT + 0.0001`.
- Gewicht anzeigen mit 2 Nachkommastellen: `String.format("%.2f kg", w)`.

### Spieltexte

| Situation | Text |
|---|---|
| unbekannter Befehl | `I don't understand that.` |
| kein Ausgang | `There is no way in that direction.` |
| `back` geht nicht | `The way back is blocked.` |
| Relaxo-Sperre | `A huge Snorlax is sleeping in the way. Maybe some music would wake it up.` |
| Abyss-Tür | `A heavy locked door blocks the way down.` |
| Exit verschüttet | `The exit is buried under rubble. Only a great power could clear it.` |
| Gewonnen | `You climb out into the daylight. You escaped the Lost Dungeons! You win!` |
| Niederlage | `All your Pokémon have fainted. Everything goes dark... The game starts over.` |
| Spielende | `Thanks for playing. Goodbye!` |

Alle weiteren Texte formuliert jeder selbst, kurz und auf Englisch.

---

## Git-Ablauf pro Block

### Start des Blocks (alle)
```bash
git checkout dev-<name>
git pull origin dev
```

### Während des Blocks
- Nur die eigenen Dateien aus der Tabelle anfassen.
- Oft kleine Commits mit sinnvoller Nachricht, z. B. `Room: lockExit/unlockExit + Tests`.
- Regelmässig `git push origin dev-<name>` (Backup).

### Ende des Blocks
1. `mvn clean test` im Ordner `02_Zork-Start` → grün
2. Nochmals `git pull origin dev` (falls schon jemand gemergt hat), Konflikte lösen, nochmals `mvn clean test`
3. `git push origin dev-<name>`, PR **`dev-<name>` → `dev`**, im Chat melden
4. Ein anderes Teammitglied schaut kurz drüber und merged

### Block-Abschluss (gemeinsam, ~15 Min.)
- Alle drei PRs sind in `dev`
- Einer macht `git pull origin dev` + `mvn clean test` → grün?
- Kurz zeigen, was jeder gebaut hat, Fragen zu den Schnittstellen klären
- Erst dann startet der nächste Block

---

## Block 0 – Startklar (Engel, ~15 Min., vor Block 1)

- `docs/Idee.md` + `docs/Plan.md` committen, PR `dev-engel` → `dev`, mergen
- Den Tippfehler-Branch `dev-castleberg` auf GitHub löschen (wir nutzen nur `dev-castelberg`)
- Bewertungsraster vom Lehrer als `docs/Bewertung.md` ins Repo legen (wer es hat)
- Alle: `git pull origin dev` → alle starten Block 1 vom gleichen Stand

---

## Block 1 – Grundklassen (je ~5 h)

Alle Klassen sind reine Fachklassen ohne Abhängigkeit zu den anderen. `Game.java` ändert nur Engel (Konstruktor).

### Engel – Räume & Welt
**Dateien:** `Room.java`, `WorldBuilder.java` (neu), `Game.java` (nur Konstruktor + Raum-Felder), `RoomTest`, `WorldBuilderTest`

**`Room`**
- `Room(String name, String description, int pokemonLevel)` – der alte Konstruktor fällt weg
- `getName()`, `getDescription()`, `getPokemonLevel()`
- `isWinRoom()`, `setWinRoom(boolean winRoom)`
- `setExits(Room north, Room east, Room south, Room west)` – `null` heisst kein Ausgang, wird **nicht** in die Map eingetragen
- `setExit(String direction, Room room)`
- `nextRoom(String direction)` → Nachbarraum **auch wenn gesperrt**, `null` wenn kein Ausgang (die Sperre prüft `Game`)
- `getExits()` → `Map<String, Room>` mit allen echten Ausgängen (gesperrte inklusive)
- Sperren: `lockExit(String direction, String message)`, `unlockExit(String direction)`, `isLocked(String direction)`, `getLockMessage(String direction)` (`null` wenn offen)
- `hasExitTo(Room room)` → `true`, wenn ein **offener** Ausgang in diesen Raum führt
- `shortDescription()` → Name
- `longDescription()` → so aufgebaut (Castelberg und Sakaj hängen später Zeilen **hinten** an):
  ```
  You are in <name>. <description>
  Exits: north east south(blocked)
  ```
- `exitString()` → `Exits: ` + Ausgänge in der Reihenfolge north, east, south, west, gesperrte mit `(blocked)`

**`WorldBuilder`**
- Konstanten mit allen 13 Raumnamen (siehe [Raumnamen](#raumnamen))
- `WorldBuilder(Random random)` – speichert `random` in einem Feld `private final Random random` (braucht Castelberg in Block 3) und baut die Welt
- alle 13 Räume mit Name, **englischer** Beschreibung (aus der Tabelle in `Idee.md` übersetzt) und Level
- Ausgänge exakt wie Tabelle „Ausgänge“ in `Idee.md`; Einbahnwege = nur in eine Richtung eintragen
- Sperren mit den Texten aus [Spieltexte](#spieltexte): Greenpath `east`, White Palace `south`, White Palace `north`
- Exit: `setWinRoom(true)`
- `getRooms()` → `List<Room>` in der Reihenfolge der Raumnamen-Tabelle
- `getRoom(String name)` → Raum oder `null`
- `getStartRoom()` → Ashfall Valley
- `getRandom()` → das `Random`

**`Game`**: Konstruktor nutzt `new WorldBuilder(new Random())`, `currentRoom = world.getStartRoom()`. Die 13 Raum-Felder fallen weg, neues Feld `world`.

**Tests u. a.:** Sperre blockiert `hasExitTo`, `unlockExit` öffnet, Einbahn (Forgotten Crossroads → Fog Canyon, kein Weg zurück), 13 Räume, `getRoom("King's Pass")` nicht `null`, Exit ist `winRoom`.

### Castelberg – Gegenstände
**Dateien:** `Item.java`, `KeyItem.java` (neu), `Inventory.java`, `ItemTest`, `KeyItemTest`, `InventoryTest`

**`Item`**
- `Item(String name, String description, double weight)`, `getName()`, `getDescription()`, `getWeight()`
- `toString()` → `flute (0.50 kg)`
- Konstanten: `public static final String FLUTE = "flute"`, `KEY = "key"`

**`KeyItem extends Item`** – ein Item, das in einem bestimmten Raum einen Ausgang öffnet
- `KeyItem(String name, String description, double weight, String targetRoom, String direction)`
- `getTargetRoom()` (Raumname), `getDirection()`
- `static KeyItem createFlute()` → `flute`, 0.5 kg, `"Greenpath"`, `"east"`
- `static KeyItem createKey()` → `key`, 0.2 kg, `"White Palace"`, `"south"`

**`Inventory`**
- intern `ArrayList<Item>`
- `add(Item item)`, `remove(String name)` → entferntes `Item` oder `null`, `get(String name)` → `Item` oder `null`, `contains(String name)`
- `getItems()` → `List<Item>` (Kopie), `getTotalWeight()`, `isEmpty()`, `size()`
- **Kein** Maximalgewicht (prüft der `Player`)

### Sakaj – Pokémon
**Dateien:** `PokemonType.java`, `Pokemon.java`, `Pokedex.java`, `PokemonCollection.java` (alle neu) + je ein Test

**`PokemonType`** (enum)
- `FIRE("Fire"), GRASS("Grass"), ICE("Ice"), GROUND("Ground"), ROCK("Rock"), NORMAL("Normal")`, `getDisplayName()`
- `getMultiplier(PokemonType defender)` → `2.0` / `1.0` / `0.5` laut Tabelle in `Idee.md`, `NORMAL` als Angreifer oder Verteidiger immer `1.0`

**`Pokemon`**
- `Pokemon(String name, PokemonType type, int baseHp, int baseAttack, double weight, int level)` – startet mit vollen HP
- `getName()`, `getType()`, `getLevel()`, `getWeight()`, `getCarryWeight()` (= weight / 10)
- `getMaxHp()` = floor(baseHp × (1 + level × 0.1)), `getAttack()` = floor(baseAttack × (1 + level × 0.1)), `getCurrentHp()`
- `getDamageAgainst(Pokemon defender)` = max(1, floor(getAttack() × Typ-Faktor))
- `takeDamage(int damage)` (HP nie unter 0), `isFainted()`, `heal()` (volle HP), `levelUp()` (+2 Level, danach volle HP)
- `canBeScared()` → `true` (wird in Block 2 von `BossPokemon` überschrieben)
- `toString()` → `Charmander (Fire) Lv 5 - HP 30/30, ATK 9, 0.85 kg`

**`Pokedex`** – alle 20 Pokémon aus den Typ-Tabellen in `Idee.md` (ohne Arceus, ohne Snorlax)
- `createRandom(int level, Random random)` → zufälliges der 20 mit diesem Level
- `create(String name, int level)` → bestimmtes Pokémon (für Starter), `null` wenn unbekannt
- `getAllNames()` → `List<String>`

**`PokemonCollection`** – Zugriff per **Nummer**, 1-basiert
- `add(Pokemon)`, `get(int number)` → `Pokemon` oder `null`, `remove(int number)` → entferntes `Pokemon` oder `null`
- `size()`, `isEmpty()`, `getAll()` → `List<Pokemon>` (Kopie), `healAll()`, `getTotalWeight()` (Summe `getCarryWeight()`)

**Ende Block 1:** Alle Fachklassen da und getestet. Spiel läuft wie vorher, aber mit `WorldBuilder`.

---

## Block 2 – Systeme (je ~6 h)

Hier baut **nur Engel** an `Game.java`, `CommandWords.java` und `Parser.java` und legt dabei **alle Stubs für Block 3** an.

### Engel – Spieler, Bewegung, Stubs
**Dateien:** `Player.java`, `Game.java`, `CommandWords.java`, `Parser.java`, `PlayerTest`

**`Player`**
- `Player(Room startRoom)` – leeres `Inventory`, leere `PokemonCollection`, leerer `Stack<Room>`
- `getCurrentRoom()`, `moveTo(Room room)` (legt alten Raum auf den Stack)
- `canGoBack()` → Stack nicht leer **und** `getCurrentRoom().hasExitTo(stack.peek())` (so ist `back` über Einbahnwege automatisch gesperrt)
- `goBack()` → neuer Raum, oder `null` wenn `!canGoBack()` (Raum vom Stack nehmen, **nicht** neu auf den Stack legen)
- `getInventory()`, `getPokemons()`
- `public static final double MAX_WEIGHT = 20.0`
- `getTotalWeight()` = Inventar + `getPokemons().getTotalWeight()`
- `canCarry(double extraWeight)` (mit Toleranz, siehe [Rechnen](#rechnen))

**`Parser`**: neue Methode `public String readLine()` → nächste Eingabezeile, getrimmt, `""` bei Ende der Eingabe. Gibt vorher `"> "` aus.

**`CommandWords`**: + `use`, `search`, `fight`, `scare`, `team`, `release`

**`Game`**
- Felder: `parser`, `random` (`new Random()`), `world`, `player`. Das Feld `currentRoom` fällt weg → überall `player.getCurrentRoom()`
- Konstruktor: nur `parser = new Parser(System.in)` und `random = new Random()`
- `initGame()`: `world = new WorldBuilder(random)`, `player = new Player(world.getStartRoom())`, dann `spawnAll()`, dann `chooseStarter()`
- `play()`: `printWelcome()` (nur Story-Text) → `initGame()` → Beschreibung des Startraums → Befehlsschleife
- `restartGame()`: Niederlage-Text ausgeben, `initGame()`, Raumbeschreibung ausgeben
- `goRoom(Command)` → **gibt `boolean` zurück** (`true` = gewonnen, Spiel endet). In `processCommand`: `return goRoom(command);`
  - kein Ausgang → Text; gesperrt → `getLockMessage`; sonst `player.moveTo`, Beschreibung, `onEnterRoom(room)`, bei `isWinRoom()` Gewonnen-Text und `true`
- `goBack()`: über `player.goBack()`, sonst `The way back is blocked.`; danach Beschreibung und `onEnterRoom(room)`
- Texte aus [Spieltexte](#spieltexte) für unbekannten Befehl und Spielende übernehmen
- **Stubs für Block 3**: Methoden ohne Inhalt (nur `System.out.println("'<befehl>' is not implemented yet.");`, bei Hooks leer), mit JavaDoc und `TODO (<Name>, Block 3)`. Alle schon in `processCommand` verdrahtet. Die Methoden stehen **nach Besitzer gruppiert** in drei Abschnitten am Ende der Klasse:

```java
// ===================== Engel (Block 3) =====================
private void useItem(Command command) { }
private void showMap() { }
private void chooseStarter() { }

// ===================== Castelberg (Block 3) =====================
private void look() { }
private void search(Command command) { }
private void takeItem(Command command) { }
private void dropItem(Command command) { }
private void showInventory() { }

// ================== Sakaj (Block 3) ===================
private void spawnAll() { }
private void onEnterRoom(Room room) { }
private void fight() { }
private void scare() { }
private void showTeam() { }
private void release(Command command) { }
```

**Tests u. a.:** `back` nach normalem Weg geht, `back` nach Einbahn geht nicht, `back` bei leerem Stack geht nicht, `canCarry` an der 20-kg-Grenze, Gewicht zählt Items + Pokémon im Pokéball.

### Castelberg – Items in Räumen, Verstecke
**Dateien:** `Room.java` (nur Item-/Versteck-Teil, Engel fasst `Room` in Block 2 nicht an), `HidingSpot.java` (neu), `RoomItemTest`, `HidingSpotTest`

**`Room`** (neu dazu)
- `addItem(Item)`, `removeItem(String name)` → `Item` oder `null`, `getItem(String name)`, `hasItem(String name)`, `getItems()` → `List<Item>` (Kopie)
- `addHidingSpot(HidingSpot)`, `getHidingSpot(String name)` → `HidingSpot` oder `null`, `getHidingSpots()` → `List<HidingSpot>` (Kopie)
- `longDescription()` hängt **hinten** an (nur wenn vorhanden):
  ```
  Items: flute, key
  You could search: ash, stump, crack
  ```

**`HidingSpot`**
- `HidingSpot(String name, String description)` – `name` ist das Befehlswort (`ash`), `description` englisch (`a pile of ash`)
- `getName()`, `getDescription()`
- `hideItem(Item)`, `search()` → verstecktes `Item` oder `null`; danach ist es leer und `isSearched()` → `true`
- `hasHiddenItem()`

### Sakaj – Boss & Kampf
**Dateien:** `BossPokemon.java`, `Battle.java` (neu), `Pokedex.java` (nur `createBoss()`), `BossPokemonTest`, `BattleTest`

**`BossPokemon extends Pokemon`**
- `BossPokemon(String name, PokemonType type, int baseHp, int baseAttack, double weight, int level)`
- `canBeScared()` → `false`
- `getDefeatMessage()` → englischer Text: Arceus erkennt deine Stärke an und räumt den Ausgang frei

**`Pokedex.createBoss()`** → `BossPokemon` Arceus, `NORMAL`, 40 HP, 8 ATK, 320 kg, Level 50

**`Battle`**
- `Battle(List<Pokemon> team, Pokemon enemy)` – `team` hat 1–3 Pokémon in Kampfreihenfolge
- `fight()` → `boolean` (`true` = Spieler hat gewonnen)
  - das erste nicht besiegte Pokémon des Teams kämpft, **Spieler greift zuerst an**, dann abwechselnd
  - fällt es, kommt das nächste; Kampf endet, wenn der Gegner oder das ganze Team besiegt ist
  - bei Sieg: jedes Pokémon, das **wirklich gekämpft hat** (mindestens einmal dran war), bekommt `levelUp()`
  - danach **immer** alle Pokémon des Teams `heal()`; der Gegner wird bei Sieg auch geheilt (falls er gefangen wird)
- `getLog()` → `List<String>` mit dem Kampfverlauf, z. B. `Charmander hits Bulbasaur for 18 damage (Bulbasaur: 15/33 HP)` (`Game` gibt das nur aus → testbar ohne Konsole)

**Tests u. a.:** Typ-Vorteil verdoppelt Schaden, Spieler greift zuerst an (Gegner mit 1 HP fällt, ohne zuzuschlagen), Teamwechsel nach K.o., Level +2 nur für eingesetzte, alle danach geheilt, Niederlage.

**Ende Block 2:** Man kann herumlaufen, `back` funktioniert, Sperren halten, Gewonnen im Exit. Alle Befehle sind da (meist noch leer).

---

## Block 3 – Ins Spiel einbauen (je ~6 h)

Jeder füllt **nur die Methoden in seinem Abschnitt** von `Game.java`. Keine neuen Felder, kein Umbau von `processCommand`, nichts ausserhalb des eigenen Abschnitts. Hilfsmethoden kommen **in den eigenen Abschnitt**.

### Engel – Benutzen, Karte, Starter, Texte
**Dateien:** `Game.java` (Abschnitt Engel + `printWelcome`, `printHelp`)

- `useItem(command)`
  - ohne zweites Wort → `Use what?`; nicht im Inventar → `You don't have that.`; kein `KeyItem` → `You can't use that here.`
  - `KeyItem` und `player.getCurrentRoom().getName()` = `getTargetRoom()` → `unlockExit(getDirection())`, Item aus dem Inventar entfernen, passenden Text ausgeben (Relaxo wacht auf und geht weg / Tür geht auf)
  - sonst `Nothing happens here.`
- `showMap()`: für jeden Raum aus `world.getRooms()` eine Zeile: Name, Items im Raum, gesperrte Ausgänge; aktueller Raum mit `>` am Anfang markiert
- `chooseStarter()`: zeigt `1) Cubchoo (Ice)  2) Charmander (Fire)  3) Bulbasaur (Grass)`, liest mit `parser.readLine()`, bei ungültiger Eingabe nochmals fragen, bei leerer Eingabe (Ende) Nummer 1. Pokémon über `Pokedex.create(name, 5)` in `player.getPokemons()`
- `printWelcome()` und `printHelp()`: Story und Ziel **auf Englisch**, `printHelp` listet alle Befehle mit kurzer Erklärung
- alle übrigen deutschen Texte in `Game` ausserhalb der Abschnitte von Castelberg und Sakaj übersetzen

### Castelberg – Item-Befehle, Items in der Welt
**Dateien:** `Game.java` (Abschnitt Castelberg), `WorldBuilder.java` (nur neue Methode `placeItems()` + ihr Aufruf als **letzte Zeile** im Konstruktor), `WorldBuilderItemsTest` (neu, nicht `WorldBuilderTest` ändern)

- `takeItem(command)`: ohne zweites Wort → `Take what?`; nicht im Raum → `There is no <name> here.`; `!player.canCarry(weight)` → `That is too heavy. Drop something or release a Pokémon first.`; sonst aus dem Raum ins Inventar
- `dropItem(command)`: ohne zweites Wort → `Drop what?`; nicht im Inventar → `You don't have that.`; sonst in den Raum
- `showInventory()`: jedes Item mit Gewicht, dann `Pokémon: x.xx kg`, dann `Total: x.xx / 20.00 kg`
- `look()`: `player.getCurrentRoom().longDescription()` ausgeben (enthält Items, Verstecke und das wilde Pokémon)
- `search(command)`: ohne zweites Wort → `Search what?`; Versteck gibt es nicht → `There is no <name> here.`; schon untersucht oder leer → `You find nothing.`; sonst `You found <item>!` und das Item liegt jetzt im Raum
- `placeItems()` in `WorldBuilder`:
  - `KeyItem.createKey()` offen in Queen's Gardens (`addItem`)
  - Verstecke in jedem Raum laut Tabelle „Stellen pro Raum“ in `Idee.md` (Befehlswort + englische Beschreibung)
  - `KeyItem.createFlute()` in **ein zufälliges Versteck** aus allen Verstecken der 7 erreichbaren Räume (Ashfall Valley, City of Tears, Forgotten Crossroads, Fog Canyon, White Palace, Ancient Treasures, Royale Waterways), mit `random`
- **Tests:** Schlüssel liegt in Queen's Gardens, jeder Raum ausser Exit hat Verstecke, Flöte liegt genau einmal in einem der 7 Räume, mit `new Random(42)` immer am selben Ort

### Sakaj – Pokémon im Spiel
**Dateien:** `Room.java` (nur Pokémon-Teil, Castelberg fasst `Room` in Block 3 nicht an), `Game.java` (Abschnitt Sakaj), `RoomPokemonTest`

**`Room`** (neu dazu)
- `getWildPokemon()` → `Pokemon` oder `null`, `setWildPokemon(Pokemon)`, `removeWildPokemon()`, `hasWildPokemon()`
- `isSpawnRoom()` → `pokemonLevel > 0 && !name.equals("The Abyss")`
- `longDescription()` hängt **hinten** an (nur wenn vorhanden): `A wild <toString des Pokémon> appears!`

**`Game`** (Abschnitt Sakaj)
- `spawnAll()`: in jeden Raum mit `isSpawnRoom()` ein `Pokedex.createRandom(room.getPokemonLevel(), random)`
- `onEnterRoom(room)`:
  - The Abyss: Boss setzen, wenn kein Pokémon im Raum **und** der Boss noch nicht besiegt ist. „Boss besiegt“ heisst: `world.getRoom("White Palace").isLocked("north") == false` (kein neues Feld nötig)
  - sonst: wenn `isSpawnRoom()` und kein Pokémon da → neues spawnen
- `fight()`:
  - kein Pokémon im Raum → `There is nothing to fight here.`
  - `showTeam()` ausgeben, dann `Choose up to 3 Pokémon by number (e.g. 1 3 2):`, `parser.readLine()`
  - Eingabe ungültig (keine Zahl, Nummer nicht vorhanden, doppelt, mehr als 3, leer) → `Invalid team. The fight is cancelled.`, nichts passiert
  - `new Battle(team, wild).fight()`, danach jede Zeile aus `getLog()` ausgeben
  - **Niederlage** → `restartGame()`
  - **Sieg gegen Boss** → `getDefeatMessage()`, `removeWildPokemon()`, `world.getRoom("White Palace").unlockExit("north")`, nicht fangen
  - **Sieg sonst** → `removeWildPokemon()`; passt `wild.getCarryWeight()` (`player.canCarry`) → in die Sammlung; sonst `team` zeigen und fragen `Release a Pokémon by number to make room, or press Enter to let <name> go:`. Nach dem Freilassen nochmals prüfen; passt es immer noch nicht → `<name> runs away.`
- `scare()`: kein Pokémon → `There is nothing to scare away.`; `!canBeScared()` → `Arceus does not move an inch.`; sonst `removeWildPokemon()` und `<name> runs away.`
- `showTeam()`: nummerierte Liste `1) <toString>`, am Ende Gesamtgewicht der Pokémon
- `release(command)`: Nummer prüfen (siehe [Eingaben](#eingaben)); letztes Pokémon → `You can't release your last Pokémon.`; sonst entfernen und `<name> was released.`

**Ende Block 3:** Das Spiel ist komplett durchspielbar.

---

## Block 4 – Testen, Doku & Abschluss (je ~4 h)

**Erste Stunde gemeinsam:** Jeder spielt das Spiel einmal ganz durch (siehe [Verifikation](#verifikation)) und notiert Bugs als GitHub-Issue mit Label `engel` / `castelberg` / `sakaj`. Jeder fixt die Bugs **in seinem Bereich** (Datei-Besitz wie in Block 1–3).

| Person | Aufgabe | Dateien |
|---|---|---|
| **Engel** | **README.md** (≥ 150 Wörter: Story, Start mit `mvn`, alle Befehle, Gewonnen), `Map.png` mit den Sperren, Bugs Welt/Navigation | `README.md`, `docs/Map.png` |
| **Castelberg** | `docs/Items.md` (Aufgaben 2.4–2.6, 3.1–3.3), Item-Liste + Gewichte in `Idee.md`, `docs/Fragen.md` fertig, JavaDoc-Check **aller** Klassen (nur JavaDoc ergänzen, keinen Code ändern), Bugs Items | `docs/Items.md`, `docs/Idee.md` (Abschnitt Items), `docs/Fragen.md` |
| **Sakaj** | **Klassendiagramm** (Mermaid + PNG, alle Klassen, Vererbung `KeyItem`/`BossPokemon`), `docs/Architektur.md` (Verantwortung je Klasse, `back`-Umsetzung), Bugs Pokémon/Kampf | `docs/Idee.md` (Abschnitt Klassendiagramm), `docs/Zork_diagramm.png`, `docs/Architektur.md` |

`Idee.md` wird hier von zwei Personen geändert – aber in **verschiedenen Abschnitten**, das mergt Git ohne Probleme.

Danach gemeinsam: Bewertungsraster (`docs/Bewertung.md`) Punkt für Punkt prüfen, **einmal PR `dev` → `main`**, Präsentation üben.

---

## Datei-Besitz

| Datei | Block 1 | Block 2 | Block 3 | Block 4 |
|---|---|---|---|---|
| `Room` | Engel | Castelberg (Items, Verstecke) | Sakaj (Pokémon) | Bugfix im eigenen Teil |
| `WorldBuilder` | Engel | – | Castelberg (`placeItems`) | Bugfix im eigenen Teil |
| `Game` | Engel (Konstruktor) | Engel | **alle**, je eigener Abschnitt | Bugfix im eigenen Abschnitt |
| `Player`, `Parser`, `CommandWords` | – | Engel | – | Engel |
| `Item`, `KeyItem`, `Inventory`, `HidingSpot` | Castelberg | Castelberg | – | Castelberg |
| `PokemonType`, `Pokemon`, `Pokedex`, `PokemonCollection`, `Battle`, `BossPokemon` | Sakaj | Sakaj | – | Sakaj |
| `Command`, `Zork2` | – | – | – | – (nicht ändern) |

---

## Für alle, jeden Block
- **JavaDoc** (Deutsch) auf jeder eigenen Klasse und öffentlichen Methode (Ziel > 90 %)
- **5–8 Tests** pro eigener Fachklasse
- Mehrere kleine Commits pro Block, sinnvolle Nachrichten
- Nie direkt auf `dev` oder `main` pushen, immer über PR
- Keine Dateien aus `target/` oder `.idea/` committen

## Aufwand
| Block | pro Person |
|---|---|
| 0 | 15 Min. (Engel) |
| 1 | ~5 h |
| 2 | ~6 h |
| 3 | ~6 h |
| 4 | ~4 h |

Dazu Mergen/Bugfixing und Präsentation → **ca. 22–25 h pro Person**.

## Präsentation (10 Min.)
1. Engel: Story, Karte, Sperren, `back`, `map`, Gewonnen (~3 Min.)
2. Castelberg: Items, Gewicht, Inventar, Untersuchen, Vererbung `KeyItem` (~3 Min.)
3. Sakaj: Pokémon, Typen, Kampf, Vererbung `BossPokemon`, Klassendiagramm (~3 Min.)
4. Live-Demo (~1 Min.)

## Verifikation
- Nach jedem Merge: `mvn clean test` im Ordner `02_Zork-Start` grün
- Ende Block 3, einmal komplett durchspielen, aus der IDE (`Zork2.main`) oder im Ordner `02_Zork-Start` mit `mvn compile` und danach `java -cp target/classes ch.bbw.zork.Zork2`:
  1. Starter wählen, `look`, `team`
  2. Flöte suchen (`search …`), `take flute`, `inventory`
  3. Greenpath: `go east` blockiert → `use flute` → `go east` geht
  4. Queen's Gardens: `take key`; White Palace: `use key` → The Abyss offen
  5. Pokémon fangen, bis das Team stark genug ist; Arceus besiegen → `go north` aus dem White Palace → gewonnen
- Zusätzlich prüfen: zu schweres Item, `back` nach Einbahnweg, `scare` beim Boss, Niederlage → Neustart, `release` des letzten Pokémon, ungültige Teamauswahl
- Vor `dev` → `main`: Bewertungsraster durchgehen, keine `target/`/`.idea/` im Repo
