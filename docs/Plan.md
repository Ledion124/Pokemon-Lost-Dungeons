# Plan – Arbeitsteilung Pokémon Lost Dungeons

Spielidee und Regeln: siehe [Idee.md](Idee.md). Bewertung: `Bewertung.md`.

## Context
Die Spielidee ist gewachsen (Relaxo/Flöte, Schlüssel → Abyss, Boss, Untersuchen, Pokémon-Kampfsystem). Der alte Stufen-Plan passt nicht mehr. Ziel: Jede Person arbeitet pro Stufe **unabhängig**, am Ende der Stufe wird nach `dev` gemergt, dann geht es mit der nächsten Stufe weiter.

## Verantwortungsbereiche
| Person | Bereich |
|---|---|
| **Engel** | Welt & Navigation: Räume, Ausgänge, Sperren, Spieler-Bewegung, `back`, `map`, `use`, Gewonnen |
| **Sakaj** | Gegenstände: Items, Inventar (Gewicht), Untersuchen, Flöte/Schlüssel platzieren, `take`/`drop`/`inventory`/`look`/`search` |
| **Castelberg** | Pokémon & Kampf: Typen, Pokémon, Pokédex, Sammlung, Kampf, Boss, Spawnen, Starter, `fight`/`scare`/`team`, Neustart |

## Ablauf pro Stufe
1. `git pull origin dev`
2. Nur in den eigenen Dateien der Stufe arbeiten. Was ein anderer in **derselben** Stufe baut, braucht man nicht.
3. Fertig + eigene Tests grün → commit, push, PR **eigener Branch → `dev`**
4. Wer als 2./3. mergt: vorher `git pull origin dev` + `mvn clean test`
5. Nächste Stufe erst, wenn alle drei gemergt haben

In `Game.java` ändert jeder **nur seine eigenen Methoden** (Stubs) → Git mergt ohne Konflikte.

---

## Stufe 0 – Fundament ✅
Compile-Fehler, Zeilenenden, JUnit 5, erste Befehls-Stubs, leere `Item`/`Player`/`Inventory`.

## Stufe 1 – Grundklassen (je ~5 h)
| Person | Aufgabe | Dateien |
|---|---|---|
| **Engel** | `Room`: `name`, `pokemonLevel`, `isWinRoom`, `exitString()` nur echte Ausgänge, **gesperrte Ausgänge** (`lockExit(richtung, meldung)`, `unlockExit(richtung)`, `isLocked(richtung)`). `WorldBuilder`: 13 Räume, Ausgänge, Level, Sperren (Relaxo, Abyss-Tür, Exit), `getRooms()`, `getRoom(name)`, `getStartRoom()`. `Game`-Konstruktor nutzt `WorldBuilder` | `Room`, `WorldBuilder` (neu), `Game` (Konstruktor), `RoomTest`, `WorldBuilderTest` |
| **Sakaj** | `Item` (name, description, weight), `KeyItem extends Item`, `Inventory` (`ArrayList<Item>`, `add`, `remove(name)`, `contains(name)`, `getTotalWeight()`). Item-Gewichte festlegen. (Das Maximalgewicht prüft der `Player`, nicht das Inventory) | `Item`, `KeyItem` (neu), `Inventory`, `ItemTest`, `KeyItemTest`, `InventoryTest` |
| **Castelberg** | `PokemonType` (enum, 5 Typen + `NORMAL` für Arceus, `getMultiplier(gegner)` → 2 / 1 / 0.5, Normal immer 1). `Pokemon` (name, type, baseHp, baseAttack, **weight**, `getCarryWeight()` = weight / 10 (Pokéball), level, currentHp, `getMaxHp()`, `getAttack()`, `takeDamage()`, `isFainted()`, `heal()`, `levelUp()` +2). `Pokedex`: Pokémon-Liste aus `Idee.md` übernehmen, `createRandom(level, Random)`. `PokemonCollection` (`add`, `remove(name)`, `get(name)`, `size()`, `healAll()`, `getTotalWeight()` mit Pokéball-Gewicht) | `PokemonType`, `Pokemon`, `Pokedex`, `PokemonCollection` (neu), `PokemonTypeTest`, `PokemonTest`, `PokedexTest`, `PokemonCollectionTest` |

## Stufe 2 – Systeme (je ~6 h)
| Person | Aufgabe | Dateien |
|---|---|---|
| **Engel** | `Player`: currentRoom, `Stack<Room>` history, `Inventory`, `PokemonCollection`, `moveTo(room)`, `goBack()` (nicht über Einbahnwege), `canGoBack()`, **Tragegewicht**: `MAX_WEIGHT` = 20 kg, `getTotalWeight()` (Items + Pokémon im Pokéball), `canCarry(weight)`. `Game`: `go` (beachtet Sperren) + `back` über Player, **Gewonnen** im Exit. `Game`-Init in `initGame()` auslagern (für Neustart). **Stubs v2**: neue Befehle `use`, `search`, `fight`, `scare`, `team`, `release` in `CommandWords` + leere Methoden, dazu leere Hooks `chooseStarter()` und `onEnterRoom(room)` | `Player`, `Game`, `CommandWords`, `PlayerTest` |
| **Sakaj** | `Room`: `ArrayList<Item>` (`addItem`, `removeItem`, `getItem`, `hasItem`), Items in der Beschreibung. `HidingSpot` (Stein, Spalte …; kann ein Item verstecken, `search()` gibt es heraus). `Room`: Liste von HidingSpots | `Room` (Item-/HidingSpot-Teil), `HidingSpot` (neu), `RoomItemTest`, `HidingSpotTest` |
| **Castelberg** | `BossPokemon extends Pokemon`. `Battle`: Team (bis 3, Reihenfolge) gegen Gegner, abwechselnd angreifen, nächstes Pokémon wenn eins fällt, Ergebnis gewonnen/verloren, Sieger +2 Level, danach alle heilen | `BossPokemon`, `Battle` (neu), `BossPokemonTest`, `BattleTest` |

## Stufe 3 – Ins Spiel einbauen (je ~6 h)
| Person | Aufgabe | Dateien |
|---|---|---|
| **Engel** | `use <item>`: Flöte im Greenpath → Relaxo weg, Schlüssel im White Palace → Abyss offen. `map`: alle Räume mit Items, aktueller Raum markiert, gesperrte Wege sichtbar. Welcome-/Help-Texte zur Story | `Game` (`useItem`, `showMap`, `printWelcome`, `printHelp`) |
| **Sakaj** | `take` (prüft `player.canCarry`), `drop`, `inventory` (zeigt Items + Gesamtgewicht inkl. Pokémon; Fehlermeldungen: zu schwer / nicht da / nicht im Inventar), `look`, `search <ort>`. Im `WorldBuilder`: Schlüssel in Queen's Gardens, HidingSpots in jedem Raum, **Flöte zufällig** in einen der 7 erreichbaren Räume (`Random` als Parameter → testbar) | `Game` (`takeItem`, `dropItem`, `showInventory`, `look`, `search`), `WorldBuilder` (Items-Teil), `WorldBuilderTest` ergänzen |
| **Castelberg** | `Room`: Feld für wildes Pokémon. `chooseStarter()` (Eis / Feuer / Pflanze). `onEnterRoom()`: spawnen, wenn Raum leer. `fight` (Team bis 3 wählen, `Battle` starten, Sieg → Sammlung, wenn `player.canCarry` – sonst flieht es), `scare`, `team` (mit Gewichten), `release <pokemon>`. Boss in The Abyss, Sieg → `unlockExit` am White Palace. Niederlage → `initGame()` (Neustart) | `Room` (Pokémon-Teil), `Game` (`chooseStarter`, `onEnterRoom`, `fight`, `scare`, `showTeam`, `release`), `RoomPokemonTest` |

## Stufe 4 – Doku & Abschluss (je ~4 h)
| Person | Aufgabe | Dateien |
|---|---|---|
| **Engel** | **README.md** (≥ 150 Wörter: Story, Start mit `mvn`, alle Befehle, Gewonnen), `Map.png` mit Sperren, kompletter Testdurchlauf | `README.md`, `docs/Map.png` |
| **Sakaj** | `docs/Items.md` (Aufgaben 2.4–2.6, 3.1–3.3), Item-Liste + Gewichte in `Idee.md`, `docs/Fragen.md` fertig, JavaDoc-Check aller Klassen | `docs/Items.md`, `docs/Idee.md` (Items), `docs/Fragen.md` |
| **Castelberg** | **Klassendiagramm** (Mermaid + PNG, alle Klassen, Vererbung), `docs/Architektur.md` (Verantwortung je Klasse, `back`-Umsetzung), Pokémon-Liste in `Idee.md` | `docs/Idee.md` (Diagramm, Pokémon), `docs/Zork_diagramm.png`, `docs/Architektur.md` |

Danach gemeinsam: Checkliste `Bewertung.md` Punkt 1–20, **einmal PR `dev` → `main`**, Präsentation üben.

---

## Für alle, jede Stufe
- **JavaDoc** auf jeder eigenen Klasse und Methode (Ziel > 90 %)
- **5–8 Tests** pro eigener Fachklasse, mit `assert`. Zufall immer über `Random`-Parameter → in Tests `new Random(42)`
- Mehrere kleine Commits pro Stufe, sinnvolle Nachrichten (Raster 1)

## Aufwand
~21 h pro Person (Stufe 1–4), dazu Mergen/Bugfixing und Präsentation → **ca. 20–25 h pro Person**.

## Präsentation (10 Min.)
1. Engel: Story, Karte, Sperren, `back`, `map`, Gewonnen (~3 Min.)
2. Sakaj: Items, Gewicht, Inventar, Untersuchen, Vererbung `KeyItem` (~3 Min.)
3. Castelberg: Pokémon, Typen, Kampf, Vererbung `BossPokemon`, Klassendiagramm (~3 Min.)
4. Live-Demo (~1 Min.)

## Verifikation
- Nach jedem Merge: `mvn clean test` im Ordner `02_Zork-Start` grün
- Ende Stufe 3: Spiel einmal komplett durchspielen (Flöte suchen → Relaxo → Schlüssel → Boss → Exit), dazu: zu schweres Item, `back` nach Einbahnweg, Tod → Neustart
- Vor `dev` → `main`: Checkliste `Bewertung.md`, keine `target/`/`.idea/` im Repo
