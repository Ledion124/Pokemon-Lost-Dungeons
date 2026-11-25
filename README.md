# Pokémon Lost Dungeons

Ein Text-Adventure für die Konsole, entstanden im Modul 320 an der BBW. Grundlage war das Zork-Projekt aus dem Unterricht, das wir zu einem eigenen Spiel mit Pokémon, Gegenständen und Kämpfen ausgebaut haben.

## Worum geht es?

Du wachst tief in einem Höhlensystem auf. Der einzige Ausgang liegt nördlich vom White Palace, ist aber verschüttet. Um hinauszukommen, musst du:

1. die Zauberflöte finden, die irgendwo in einem der Räume versteckt ist,
2. damit das schlafende Snorlax im Greenpath wecken, das den Weg blockiert,
3. dahinter in den Queen's Gardens den Schlüssel holen,
4. mit dem Schlüssel im White Palace die Tür nach The Abyss öffnen,
5. dort Arceus besiegen, der dann den verschütteten Ausgang freiräumt.

Sobald du den Raum **Exit** betrittst, hast du gewonnen.

Unterwegs triffst du auf wilde Pokémon. Du kannst gegen sie kämpfen und sie fangen, sie wegscheuchen oder einfach weitergehen. Für Arceus brauchst du ein Team aus zwei, drei starken Pokémon, also lohnt es sich, unterwegs zu kämpfen.

Wenn alle Pokémon in einem Kampf besiegt werden, beginnt das Spiel von vorne.

## Starten

Voraussetzung ist Java 17 oder neuer und Maven.

Im Hauptordner des Projekts:

```
mvn compile
java -cp target/classes ch.bbw.zork.Zork2
```

In IntelliJ geht es auch direkt: `Zork2.java` öffnen und `main` starten.

Die Tests laufen mit:

```
mvn clean test
```

## Befehle

| Befehl | Was er macht |
|---|---|
| `go <richtung>` | in einen Nachbarraum gehen (`north`, `east`, `south`, `west`) |
| `back` | zurück in den vorherigen Raum (geht nicht über Einbahnwege) |
| `look` | den Raum nochmals anschauen |
| `search <stelle>` | eine Stelle im Raum untersuchen, z. B. `search ash` |
| `take <item>` | einen Gegenstand aufnehmen |
| `drop <item>` | einen Gegenstand ablegen |
| `inventory` | zeigt, was du trägst und wie schwer es ist |
| `use <item>` | einen Gegenstand benutzen, z. B. `use flute` |
| `map` | alle Räume mit Gegenständen und gesperrten Wegen |
| `fight` | gegen das wilde Pokémon im Raum kämpfen |
| `scare` | das wilde Pokémon verscheuchen |
| `team` | deine Pokémon mit Nummer, Level und HP |
| `release <nummer>` | ein Pokémon freilassen, z. B. `release 2` |
| `help` | Hilfe anzeigen |
| `quit` | Spiel beenden |

Bei `fight` wählst du bis zu drei Pokémon über ihre Nummer aus `team`, z. B. `1 3 2`. Sie kämpfen in dieser Reihenfolge.

## Ein paar Hinweise

- Zu Beginn wählst du ein Starter-Pokémon: Cubchoo (Eis), Charmander (Feuer) oder Bulbasaur (Pflanze).
- Jedes Pokémon hat einen Typ. Ein Typ-Vorteil macht doppelten Schaden, ein Nachteil nur halben. Feuer ist zum Beispiel stark gegen Pflanze und Eis, aber schwach gegen Boden und Gestein. Arceus ist Normal und hat weder Stärken noch Schwächen.
- Pokémon, die in einem gewonnenen Kampf mitgemacht haben, steigen um 2 Level. Nach jedem Kampf werden alle Pokémon geheilt.
- Du kannst insgesamt 20 kg tragen. Gegenstände und Pokémon zählen zusammen, Pokémon allerdings nur mit einem Zehntel ihres Gewichts, weil sie im Pokéball sind.
- Manche Wege sind Einbahnstrassen. Wer dort durchgeht, kommt mit `back` nicht mehr zurück und muss einen anderen Weg finden.
- Ist ein Gegner zu stark, kannst du `scare` benutzen. Beim nächsten Betreten des Raums taucht ein neues Pokémon auf.

## Projektstruktur

```
pom.xml                        Maven-Konfiguration
src/main/java/ch/bbw/zork/     Spielcode
src/test/java/ch/bbw/zork/     JUnit-Tests
docs/
  Idee.md          Spielidee, Räume, Pokémon-Liste, Klassendiagramm
  Architektur.md   Aufbau des Codes und wichtige Umsetzungen
  Items.md         Gegenstände, Gewicht, Inventar und Verstecke
  Plan.md          Arbeitsplan und Aufteilung im Team
  Map.png          Karte der Räume
```

## Team

| Person | Bereich |
|---|---|
| Louis Engel | Welt und Navigation: Räume, Sperren, `back`, `map`, `use` |
| Silvan Castelberg | Gegenstände: Items, Inventar, Verstecke |
| Ledion Sakaj | Pokémon und Kampf: Typen, Pokédex, Kampf, Boss |
