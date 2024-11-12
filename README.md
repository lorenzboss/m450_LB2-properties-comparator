# Properties Comparator - Modul 450

## Beschreibung

In diesem Projekt werden die Daten von 335 verkauften Immobilien aus dem Kanton Basel-Landschaft zwischen den Jahren 2011 und 2023 analysiert und
verglichen.

Es ist ein Java-Projekt, erstellt mit Maven, OpenJDK 22 und der Gson-Library zur JSON-Verarbeitung von Google.

## Voraussetzungen

* Apache Maven 3.9.9
* Java Development Kit (JDK) 22
* IntelliJ IDEA (zum Ausführen des Projekts erforderlich)

## Ausführen des Projekts

**TODO**

1. Laden Sie das Projekt herunter oder klonen Sie das Projekt von GitHub.
2. Öffnen Sie das Projekt in IntelliJ IDEA.
4. Die Gson-Library sollte bereits über IntelliJ als Projekt-Library erkannt werden.
5. Wenn nicht, fügen Sie die Library hinzu, indem Sie auf `Project Structure` gehen und dann auf `Libraries`. Klicken Sie auf das "+"-Symbol und
   wählen Sie die Library in dem `/lib` Ordner aus.
6. Wählen Sie die Main-Klasse `Main.java` in dem `/src` Ordner aus.
7. Klicken Sie auf das grüne Play-Symbol neben der Main-Klasse, um das Projekt auszuführen.

**Hinweis: Dieses Projekt ist speziell für IntelliJ konfiguriert. Die Abhängigkeiten werden möglicherweise nicht korrekt erkannt, wenn das Projekt in
einer anderen Entwicklungsumgebung ausgeführt wird.**

# Tests

## White-Box Testing

### Unit Test 1: Mock statt DB-Verbindung

Für den ersten Unit-Test teste ich die `averagePricePerYear` Methode in der `AveragePrice` Klasse.
Diese Methode errechnet den durchschnittlichen Verkaufspreis der Immobilien pro Jahr.

### Unit Test 2: Mutation-Testing (Eigene Idee)

Für den zweiten Unit-Test teste ich die `mostExpensivePropertiesPrice` Methode in der `HighestPrice` Klasse.
Diese Methode gibt die Preise der teuersten Immobilien zurück.
Die Idee ist dass ich Mutation-Testing verwende, um zu überprüfen, ob die Methode korrekt funktioniert.
Für die manuelle Mutation kann man zum Beispiel:

- den AtomicInteger auf 0 setzen
- das Filtern der Immobilien entfernen
- das absteigende Sortieren der Immobilien ändern

## Test-Driven-Development (TDD)

Für das Test-Driven-Development erstelle ich eine neue Methode, welche die Districts aggregiert.

## Test ausführen

### Normale Tests ausführen:

```
mvn test
```

### Mutation-Tests ausführen:

```
mvn org.pitest:pitest-maven:mutationCoverage
```

