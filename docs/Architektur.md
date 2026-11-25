# Architektur

Dieses Dokument beschreibt, wie der Code von Pokémon Lost Dungeons aufgebaut ist: welche Klasse wofür zuständig ist, wie die Klassen zusammenarbeiten und wie einzelne Spielregeln (z. B. `back` oder die gesperrten Wege) umgesetzt sind. Das Klassendiagramm dazu steht in [Idee.md](Idee.md#21-b-klassendiagramm).

## Grundidee

Wir haben die Vorlage von Zork so umgebaut, dass `Game` möglichst wenig selbst macht. `Game` liest nur Befehle ein und gibt sie an die passende Klasse weiter. Die eigentliche Arbeit passiert in drei "Actions"-Klassen, eine pro Bereich:

- `WorldActions` – Welt und Navigation (Engel)
- `ItemActions` – Gegenstände (Castelberg)
- `PokemonActions` – Pokémon und Kampf (Sakaj)

Darunter liegen die Fachklassen (`Room`, `Player`, `Item`, `Pokemon`, `Battle` usw.). Sie geben nichts auf der Konsole aus und lesen keine Eingaben. Dadurch kann man sie mit JUnit testen, ohne das Spiel zu starten.

Grob sieht der Ablauf so aus:

```
Zork2 (main)
  └─ Game
       ├─ Parser ── CommandWords, Command
       ├─ WorldBuilder ── 13 × Room
       ├─ Player ── Inventory, PokemonCollection
       ├─ WorldActions
       ├─ ItemActions
       └─ PokemonActions ── Battle, Pokedex
```

## Verantwortung der Klassen

### Steuerung

| Klasse | Aufgabe |
|---|---|
| `Zork2` | Enthält nur `main` und startet ein `Game`. |
| `Game` | Hauptschleife. Baut beim Start (und nach einer Niederlage) Welt, Spieler und Actions-Klassen neu auf, verteilt die Befehle in `processCommand` und kümmert sich selbst nur um `go` und `back`. |
| `Parser` | Liest eine Zeile von der Konsole und macht daraus ein `Command`. Mit `readLine()` kann man auch eine freie Eingabe lesen, z. B. die Teamauswahl im Kampf. |
| `CommandWords` | Liste der gültigen Befehlswörter. |
| `Command` | Ein eingegebener Befehl: Befehlswort und optional ein zweites Wort (`go north`, `take key`). |

### Welt und Spieler

| Klasse | Aufgabe |
|---|---|
| `WorldBuilder` | Erstellt alle 13 Räume mit Beschreibung und Pokémon-Level, verbindet die Ausgänge, setzt die drei Sperren, legt die Verstecke an, versteckt fünf Fundsachen (`stone`, `tablet`, `ring`, `honey`, `skull`) an festen Orten und die Flöte zufällig. Die Fundsachen liegen nur in Räumen, in denen die Flöte nie liegt, weil ein Versteck nur einen Gegenstand halten kann. Die Raumnamen sind als Konstanten abgelegt (`WorldBuilder.WHITE_PALACE` usw.), damit sich niemand vertippt. |
| `Room` | Ein Raum. Kennt seine Ausgänge (inkl. gesperrter), die Gegenstände am Boden, die Verstecke und das wilde Pokémon. `longDescription()` baut daraus den Text, den der Spieler sieht. |
| `Player` | Der Spieler: aktueller Raum, Verlauf für `back`, Inventar und Pokémon-Sammlung. Prüft auch das Maximalgewicht von 20 kg. |
| `WorldActions` | Befehle `use` und `map` sowie die Wahl des Starter-Pokémon. |

### Gegenstände

| Klasse | Aufgabe |
|---|---|
| `Item` | Gegenstand mit Name, Beschreibung und Gewicht. |
| `KeyItem` | Erbt von `Item`. Ein Gegenstand, der in einem bestimmten Raum einen bestimmten Ausgang öffnet. Die Flöte und der Schlüssel werden über `createFlute()` und `createKey()` erzeugt. |
| `Inventory` | Die Gegenstände, die der Spieler trägt. |
| `HidingSpot` | Eine Stelle in einem Raum, die man mit `search` untersuchen kann. Kann einen Gegenstand verstecken, gibt ihn aber nur einmal her. |
| `ItemActions` | Befehle `look`, `search`, `take`, `drop` und `inventory`. |

### Pokémon und Kampf

| Klasse | Aufgabe |
|---|---|
| `PokemonType` | Enum mit den Typen Fire, Grass, Ice, Ground, Rock und Normal. `getMultiplier()` liefert den Schadensfaktor (2.0, 1.0 oder 0.5). |
| `Pokemon` | Ein Pokémon mit Typ, Level, HP und Angriff. HP und Angriff werden aus den Grundwerten und dem Level berechnet (+10 % pro Level). |
| `BossPokemon` | Erbt von `Pokemon`. Arceus lässt sich nicht verscheuchen (`canBeScared()` gibt `false` zurück) und hat einen eigenen Text, wenn er besiegt wird. |
| `Pokedex` | Kennt die Grundwerte aller 20 Pokémon und erzeugt neue Exemplare, zufällig oder nach Name. Erzeugt auch den Boss. |
| `PokemonCollection` | Die Pokémon des Spielers. Zugriff über Nummern ab 1, so wie `team` sie anzeigt. |
| `Battle` | Führt einen Kampf zwischen einem Team (1–3 Pokémon) und einem Gegner durch und schreibt den Verlauf in ein Log. Gibt selbst nichts aus. |
| `PokemonActions` | Befehle `fight`, `scare`, `team` und `release`, dazu das Spawnen der wilden Pokémon. |

## Wie `back` umgesetzt ist

`Player` speichert die besuchten Räume in einem `Stack<Room>`. Bei jedem `go` legt `moveTo()` den alten Raum oben auf den Stack.

Bei `back` prüft `canGoBack()` zwei Dinge:

1. Der Stack ist nicht leer.
2. Vom aktuellen Raum führt ein **offener** Ausgang zum obersten Raum auf dem Stack. Das prüft `Room.hasExitTo()`.

Der zweite Punkt sorgt dafür, dass man über Einbahnwege nicht zurückkommt. Von Forgotten Crossroads geht es zum Beispiel nach Süden in den Fog Canyon, aber vom Fog Canyon führt kein Ausgang zurück. `hasExitTo()` findet deshalb nichts und das Spiel meldet "The way back is blocked." Wir mussten Einbahnwege also nirgends extra markieren, die Regel ergibt sich aus den Ausgängen.

Wenn `back` klappt, wird der Raum vom Stack genommen und nicht neu draufgelegt. So kann man mit mehreren `back` hintereinander den ganzen Weg zurückgehen.

## Gesperrte Wege

Ein Ausgang kann gesperrt sein, ohne dass er verschwindet. `Room` hat dafür neben `exits` eine zweite Map `lockMessages` (Richtung → Text). Solange eine Richtung dort drinsteht, ist sie gesperrt, und `go` zeigt den Text an.

| Weg | Sperre | geöffnet durch |
|---|---|---|
| Greenpath → Resting Grounds (Osten) | schlafendes Snorlax | `use flute` im Greenpath |
| White Palace → The Abyss (Süden) | verschlossene Tür | `use key` im White Palace |
| White Palace → Exit (Norden) | verschütteter Ausgang | Arceus in The Abyss besiegen |

Flöte und Schlüssel sind `KeyItem`s. Sie wissen selbst, in welchem Raum und in welche Richtung sie etwas öffnen. `WorldActions.useItem()` muss deshalb nur vergleichen, ob der Spieler im richtigen Raum steht, und ruft dann `unlockExit()` auf.

Ob Arceus schon besiegt ist, speichern wir nicht in einem eigenen Feld. Es gilt einfach: Ist der Ausgang im Norden des White Palace offen, ist der Boss besiegt. So kann der Zustand nicht auseinanderlaufen.

## Gewicht

Gegenstände und Pokémon teilen sich ein gemeinsames Maximalgewicht von 20 kg. Pokémon zählen nur mit einem Zehntel ihres Gewichts, weil sie im Pokéball getragen werden. `Player.canCarry()` rechnet beides zusammen und wird von `take` und beim Fangen eines Pokémon benutzt. Weil `double` nicht ganz genau rechnet, vergleichen wir mit einer kleinen Toleranz.

Damit das Maximalgewicht im Spiel auch wirklich eine Rolle spielt, gibt es neben Flöte und Schlüssel noch die Fundsachen in den Verstecken. Die schwerste ist die `tablet` mit 12 kg. Wer schon den `stone` (5 kg) und ein paar Pokémon dabei hat, kann sie nicht mehr aufnehmen und muss zuerst etwas ablegen oder ein Pokémon freilassen.

## Zufall und Tests

Alles, was zufällig ist (Versteck der Flöte, welche Pokémon spawnen), bekommt ein `Random` als Parameter und benutzt nie `Math.random()`. In den Tests übergeben wir `new Random(42)`, dann kommt jedes Mal dasselbe heraus und die Tests sind wiederholbar.

Die Actions-Klassen lesen Eingaben über den `Parser`. In den Tests geben wir dem `Parser` statt `System.in` einen `ByteArrayInputStream` mit den gewünschten Eingaben, z. B. `"1 2\n"` für eine Teamauswahl.

Pro Fachklasse gibt es mindestens fünf Tests. Alle laufen mit `mvn clean test`.

## Neustart nach einer Niederlage

Verliert der Spieler einen Kampf, gibt `PokemonActions.fight()` `true` zurück. `Game` ruft dann `restartGame()` auf und baut mit `initGame()` alles neu: neue Welt, neuer Spieler, neue Actions-Klassen. Dadurch gibt es keine Reste aus dem alten Spiel, und wir brauchen keine eigene Reset-Logik in den einzelnen Klassen.
