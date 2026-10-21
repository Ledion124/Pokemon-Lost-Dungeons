# Plan: Pokémon Lost Dungeons – Spielidee & Arbeitsteilung

## Context
Zork-2-Projekt (Modul 320), 3 Personen: **Engel, Sakaj, Castelberg**. Bewertet wird nach `Bewertung.md` (20 Punkte, 12 automatisch via `repo_check.py`, 8 an der Präsentation) und dem Auftrag `02_Zork2GameProjekt.pdf` (Aufgaben 2.1–3.4).
Stand heute: Code kompiliert nicht (`cityOfTears` fehlt, `lab`-Rest), 13 Räume (bleiben alle), Hollow-Knight-Namen statt Pokémon, keine Tests, kaum JavaDoc, kein README, CR-Zeilenenden. Branch-Workflow: eigener Branch → PR nach `dev` → am Schluss 1× `dev` → `main`.

---

## 1. Spielidee (ausgearbeitet)

**Story:** Du bist ein junger Pokémon-Trainer. Bei einer Expedition stürzt du durch einen Felsspalt in die *Lost Dungeons* – ein uraltes Höhlensystem. Dein Ziel: den **Kristallschlüssel** finden und durch das **Kristalltor** zurück ans Tageslicht.

**Gewonnen (2.2):** Man gewinnt, sobald man den Raum **Tageslicht** betritt (`Room.isWinRoom() == true`). Das Kristalltor (Kristallhalle → Norden) ist verschlossen, solange der Spieler den **Kristallschlüssel** nicht im Inventar trägt → Gewicht/Inventar beeinflussen das Spiel (Raster 16/18 Stufe 3).

### Räume (13) – alle bleiben, nur Pokémon-Namen; Ausgänge exakt wie in `docs/Idee.md`
| # | Raum | bisher | Beschreibung |
|---|---|---|---|
| 1 | **Absturzstelle** (Start) | Ashfall Valley | Asche und Geröll, hier bist du gelandet. |
| 2 | Regenruinen | City of Tears | Zerfallene Stadt, Wasser tropft von der Decke. |
| 3 | Vergessene Kreuzung | Forgotten Crossroads | Alte Wegweiser; steiler Abhang nach Süden. |
| 4 | Nebelschlucht | Fog Canyon | Dichter Nebel, wilde Zubat flattern. |
| 5 | Kristallhalle | White Palace | Leuchtende Kristalle, im Norden das verschlossene Tor. |
| 6 | Dunkler Abgrund | The Abyss | Still und kalt, Augen leuchten im Dunkeln. |
| 7 | **Tageslicht** (Ziel) | Exit | Frische Luft – gewonnen! |
| 8 | Schatzkammer | Ancient Treasures | Truhen voller alter Münzen. |
| 9 | Felspass | King's Pass | Schmaler Pass, ein Felsabbruch nach Westen. |
| 10 | Wilder Garten | Queen's Gardens | Dornen, Blumen und ein schlafendes Bisasam. |
| 11 | Pokémon-Friedhof | Resting Grounds | Ruhige Grabsteine, Nebulak schweben umher. |
| 12 | Moorpfad | Greenpath | Moos und Farn, der Boden schmatzt. |
| 13 | Unterirdischer See | Royale Waterways | Trübes Wasser, Karpador planschen. |

### Ausgänge (N / O / S / W), (E) = Einbahn – identisch zu Idee.md
| Raum | N | O | S | W |
|---|---|---|---|---|
| Absturzstelle | Regenruinen | Schatzkammer | See | – |
| Regenruinen | – | – | Absturzstelle | Kreuzung |
| Kreuzung | – | Regenruinen | Nebelschlucht (E) | – |
| Nebelschlucht | – | Absturzstelle (E) | – | Kristallhalle |
| Kristallhalle | Tageslicht (E, braucht Schlüssel) | Nebelschlucht | Abgrund | – |
| Abgrund | Kristallhalle | – | – | – |
| Schatzkammer | – | – | – | Absturzstelle |
| Felspass | Wilder Garten | – | Friedhof | Schatzkammer (E) |
| Wilder Garten | – | – | Felspass | – |
| Friedhof | Felspass | – | – | Moorpfad |
| Moorpfad | – | Friedhof | – | See |
| See | Absturzstelle | Moorpfad | – | – |
| Tageslicht | – | – | – | – |

Achtung: Der aktuelle Code in `Game.java` weicht davon ab (Fog Canyon N, Resting Grounds N/O) – im `WorldBuilder` gilt diese Tabelle.

Lösungsweg: Absturzstelle → See → Moorpfad → Friedhof → Felspass → Wilder Garten (**Schlüssel**) → Felspass → Schatzkammer → Absturzstelle → Regenruinen → Kreuzung → Nebelschlucht → Kristallhalle → Tageslicht.

### Items (Gewicht in kg, Maximalgewicht Spieler: **10 kg**)
| Item | Raum | kg | Klasse |
|---|---|---|---|
| Pokéball | Absturzstelle | 0.5 | Item |
| Trank | Regenruinen | 0.3 | Item |
| Taschenlampe | Kreuzung | 1.0 | Item |
| Seil | Moorpfad | 3.0 | Item |
| Fahrrad | See | 12.0 | Item (zu schwer → Fehlermeldung, Demo fürs Maximalgewicht) |
| Beere | Felspass | 0.2 | Item |
| Goldnugget | Schatzkammer | 1.0 | `TreasureItem` (Wert in Pokédollar) |
| Mondstein | Nebelschlucht | 2.0 | `TreasureItem` |
| Schattenstein | Dunkler Abgrund | 4.0 | `TreasureItem` |
| Altes Amulett | Pokémon-Friedhof | 0.5 | `TreasureItem` |
| **Kristallschlüssel** | Wilder Garten | 0.5 | `KeyItem` (öffnet Kristalltor) |

**Vererbung (3.4):** `Item` → `KeyItem`, `Item` → `TreasureItem`.

### Befehle
`go <richtung>`, `back`, `look`, `take <item>`, `drop <item>`, `inventory`, `map`, `help`, `quit`

---

## 2. Architektur (Single Responsibility)
| Klasse | Verantwortung |
|---|---|
| `Zork2` | `main` – startet `Game` |
| `Game` | Spielschleife + Befehle verteilen (bleibt < 150 Zeilen) |
| `WorldBuilder` *(neu)* | Erstellt alle Räume, Ausgänge, Items; liefert `List<Room>` + Startraum |
| `Room` | Beschreibung, Ausgänge, `ArrayList<Item>`, `isWinRoom` |
| `Item` *(neu)* | Name, Beschreibung, Gewicht |
| `KeyItem`, `TreasureItem` *(neu)* | Spezial-Items (Vererbung) |
| `Player` *(neu)* | aktueller Raum, `Stack<Room>` für `back`, `Inventory` |
| `Inventory` *(neu)* | Items tragen, Maximalgewicht prüfen, ablegen |
| `Parser`, `Command`, `CommandWords` | bestehend, `CommandWords` um neue Befehle erweitern |
| `TestStack` (`ch.bbw.diverses`) | löschen (gehört nicht zum Spiel) |

---

## 3. Arbeitsteilung

### Phase 0 – Fundament (Engel, **zuerst**, alle anderen warten auf diesen PR)
- CR-Zeilenenden → normal, Compile-Fehler beheben, `TestStack` entfernen
- `pom.xml`: Java 17, JUnit 5 (`junit-jupiter`), `maven-surefire-plugin`
- `CommandWords` um alle Befehle oben erweitern; in `Game.processCommand` je Befehl einen **leeren Methoden-Stub** (`takeItem`, `dropItem`, `showInventory`, `look`, `goBack`, `showMap`) → jeder füllt danach nur seine eigenen Methoden, keine Merge-Konflikte
- leere Klassen `Item`, `Player`, `Inventory` anlegen
- PR → `dev`, danach `git pull origin dev` für alle

### Engel – Welt, Räume, Navigation
- `WorldBuilder` mit den 13 Räumen + Ausgängen (Tabellen oben)
- `Room`: `isWinRoom`, Gewonnen-Logik, verschlossenes Kristalltor
- `back` (über `Stack<Room>` in `Player`), `map` (`ArrayList<Room>` in Game, aktueller Raum markiert)
- Tests: `RoomTest`, `WorldBuilderTest` (je 5–8)
- Doku: `docs/Idee.md` + Raumplan (`Map.png`) auf die Pokémon-Namen aktualisieren, **README.md** (≥ 150 Wörter: Story, Start mit `mvn`, Befehle, Lösungsweg-Hinweis)

### Sakaj – Items
- `Item` (name, description, weight, Getter, `toString`)
- `KeyItem`, `TreasureItem` (Vererbung)
- `Room`: `ArrayList<Item>`, `addItem`, `removeItem(name)`, `getItem(name)`, `hasItem`, Items in `longDescription()`
- `look` Befehl
- Items im `WorldBuilder` platzieren (nach Engels PR)
- Tests: `ItemTest`, `KeyItemTest`, `TreasureItemTest`, Item-Tests in `RoomTest`
- Doku: `docs/Items.md` (Aufgaben 2.4, 2.5, 2.6, 3.4)

### Castelberg – Spieler & Inventar
- `Inventory` (`ArrayList<Item>`, `maxWeight`, `add` mit Gewichtsprüfung, `remove`, `getTotalWeight`)
- `Player` (currentRoom, history-Stack, Inventory)
- Befehle `take`, `drop`, `inventory` mit Fehlermeldungen (zu schwer / nicht da / nicht im Inventar)
- `Parser`-TODOs aufräumen (leere Eingabe, > 2 Wörter)
- Tests: `InventoryTest`, `PlayerTest`, `CommandTest`, `CommandWordsTest`
- Doku: `docs/Architektur.md`, **Klassendiagramm** (Mermaid in `Idee.md` + PNG) am Schluss mit allen Klassen, Attributen, Methoden, Vererbung

### Für alle
- **JavaDoc** auf jeder eigenen Klasse und Methode (Ziel > 90 %)
- Klein und oft committen mit sinnvollen Nachrichten (Raster 1 bewertet gleichmässige Beteiligung)
- Vor dem Arbeiten `git pull origin dev`, fertige Teile per PR → `dev`
- `docs/Fragen.md` fertig machen (Code-Blöcke ohne `\` escapen)

### Präsentation (10 Min.)
1. Engel: Spielidee, Raumplan, Gewonnen, `back`, `map` (~3 Min.)
2. Sakaj: Item, ArrayList im Raum, Gewicht, Vererbung (~3 Min.)
3. Castelberg: Spieler/Inventar, Architektur, Klassendiagramm, Tests (~3 Min.)
4. Live-Demo des Lösungswegs (~1 Min.)

---

## 4. Verifikation
- `mvn clean test` im Ordner `02_Zork-Start` → alles grün
- Jede Fachklasse (Room, Item, KeyItem, TreasureItem, Player, Inventory, WorldBuilder, Command, CommandWords) hat ≥ 5 Tests mit `assert`
- Manueller Durchlauf: Lösungsweg oben spielen, dazu: Tor ohne Schlüssel (verschlossen), Fahrrad nehmen (zu schwer), `back` nach Einbahnweg, `map`, `drop`
- Checkliste gegen `Bewertung.md` Punkt 1–20 vor dem finalen PR `dev` → `main`
- Repo: keine `target/`, `.idea/`, `.DS_Store` (`.gitignore` ist schon da)
