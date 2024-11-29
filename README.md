# Properties Comparator - Modul 450 LB2

## Beschreibung

In diesem Projekt werden die Daten von 335 verkauften Immobilien aus dem Kanton Basel-Landschaft zwischen den Jahren 2011 und 2023 analysiert und
verglichen.

Es ist eine Java-Maven-Konsolenapplikation, mit einer Anbindung an eine Postgres-Datenbank.

In der LB2 wurde das Projekt um Mutation-Tests erweitert.

## Voraussetzungen

* Apache Maven 3.9.9
* Java Development Kit (JDK) 17
* PostgreSQL 13.4
* IntelliJ IDEA (zum Ausführen des Projekts erforderlich)

## Ausführen des Projekts

1. Laden Sie das Projekt herunter oder klonen Sie das Projekt von GitHub.
2. Öffnen Sie das Projekt in IntelliJ IDEA.
3. Installieren sie alle Abhängigkeiten mit Maven-IntelliJ Plugin.
4. Wählen Sie die App-Klasse `App.java` in dem `/src` Ordner aus.
5. Klicken Sie auf das grüne Play-Symbol neben der App-Klasse, um das Projekt auszuführen.

**Hinweis: Dieses Projekt ist speziell für IntelliJ konfiguriert. Die Abhängigkeiten werden möglicherweise nicht korrekt erkannt, wenn das Projekt in
einer anderen Entwicklungsumgebung ausgeführt wird.**

# Tests

## Tests ausführen

Die Tests Unit und Integrations können mit dem folgenden Befehl ausgeführt werden:

```
mvn test
```

Die Mutation-Tests können mit dem folgenden Befehl ausgeführt werden

```
mvn org.pitest:pitest-maven:mutationCoverage
```

Das Linting kann mit dem folgenden Befehl ausgeführt werden:

```
mvn checkstyle:check
```

## Mutation-Tests

Wir haben uns bei den Schwerpunkten für Datenbankverbindung, Datenintegrität und Datenvalidierung entschieden.
Die Datenbankverbindung wird im `DatabaseConnectionTest` getestet.
Die Datenintegrität wird im `DatabaseInitializerTest` getestet.
Die Datenvalidierung wird im `PropertyValidationTest` und `DistrictValidationTest` getestet.