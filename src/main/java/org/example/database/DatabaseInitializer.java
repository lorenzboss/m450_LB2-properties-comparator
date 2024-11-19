package org.example.database;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import org.example.districts.District;
import org.example.districts.JsonToDistricts;
import org.example.properties.JsonToProperties;
import org.example.properties.Property;

public class DatabaseInitializer {
  private static final Dotenv dotenv = Dotenv.load();

  private final JsonToDistricts jsonToDistricts;
  private final JsonToProperties jsonToProperties;

  public DatabaseInitializer(JsonToDistricts jsonToDistricts, JsonToProperties jsonToProperties) {
    this.jsonToDistricts = jsonToDistricts;
    this.jsonToProperties = jsonToProperties;
  }

  public void initializeDatabase() {
    createDatabaseIfNotExists();
    createTablesIfNotExist();
    populateTablesIfEmpty();
  }

  private void createDatabaseIfNotExists() {
    String postgresUrl = "jdbc:postgresql://localhost:5432/postgres";

    try (Connection connection = DriverManager.getConnection(dotenv.get("DB_URL"), dotenv.get("DB_USER"), dotenv.get("DB_PASSWORD"));
        Statement statement = connection.createStatement()) {

      statement.execute("CREATE DATABASE properties_db");
      System.out.println("Datenbank 'properties_db' erfolgreich erstellt.");
    } catch (SQLException e) {
      if (e.getMessage().contains("already exists")) {
        System.out.println("Datenbank 'properties_db' existiert bereits.");
      } else {
        System.err.println("Fehler beim Erstellen der Datenbank: " + e.getMessage());
      }
    }
  }

  private void createTablesIfNotExist() {
    String sqlDistrictTable =
        "CREATE TABLE IF NOT EXISTS District ("
            + "district_number INT PRIMARY KEY, "
            + "name VARCHAR(100) NOT NULL, "
            + "population INT NOT NULL, "
            + "area INT NOT NULL, "
            + "average_age INT NOT NULL, "
            + "number_of_households INT NOT NULL"
            + ")";

    String sqlPropertyTable =
        "CREATE TABLE IF NOT EXISTS Property ("
            + "id SERIAL PRIMARY KEY, "
            + "year INT NOT NULL, "
            + "district_number INT NOT NULL REFERENCES District(district_number), "
            + "rooms VARCHAR(50) NOT NULL, "
            + "price INT"
            + ")";

    try (Connection connection = DatabaseManager.getConnection();
        PreparedStatement stmt1 = connection.prepareStatement(sqlDistrictTable);
        PreparedStatement stmt2 = connection.prepareStatement(sqlPropertyTable)) {

      stmt1.execute();
      stmt2.execute();
      System.out.println("Datenbank und Tabellen erfolgreich initialisiert.");
    } catch (SQLException e) {
      System.err.println("Fehler bei der Initialisierung der Datenbank: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void populateTablesIfEmpty() {
    try (Connection connection = DatabaseManager.getConnection()) {
      // Prüfen, ob die District-Tabelle Daten enthält
      if (!hasData(connection, "District")) {
        System.out.println("District-Tabelle ist leer. Füge Daten hinzu...");
        List<District> districts =
            jsonToDistricts.convertJsonToDistricts("src/main/resources/districts.json");
        insertDistricts(connection, districts);
      } else {
        System.out.println("District-Tabelle hat bereits Daten.");
      }

      // Prüfen, ob die Property-Tabelle Daten enthält
      if (!hasData(connection, "Property")) {
        System.out.println("Property-Tabelle ist leer. Füge Daten hinzu...");
        List<Property> properties =
            jsonToProperties.convertJsonToProperties("src/main/resources/properties.json");
        insertProperties(connection, properties);
      } else {
        System.out.println("Property-Tabelle hat bereits Daten.");
      }
    } catch (SQLException | IOException e) {
      System.err.println("Fehler beim Überprüfen oder Einfügen von Daten: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private boolean hasData(Connection connection, String tableName) throws SQLException {
    String query = "SELECT EXISTS (SELECT 1 FROM " + tableName + ")";
    try (PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery()) {
      return resultSet.next() && resultSet.getBoolean(1);
    }
  }

  private void insertDistricts(Connection connection, List<District> districts)
      throws SQLException {
    String sql =
        "INSERT INTO District (district_number, name, population, area, average_age, number_of_households) "
            + "VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      for (District district : districts) {
        statement.setInt(1, district.district_number());
        statement.setString(2, district.name());
        statement.setInt(3, district.population());
        statement.setInt(4, district.area());
        statement.setInt(5, district.average_age());
        statement.setInt(6, district.number_of_households());
        statement.addBatch();
      }
      statement.executeBatch();
      System.out.println("Daten in District-Tabelle erfolgreich eingefügt.");
    }
  }

  private void insertProperties(Connection connection, List<Property> properties)
      throws SQLException {
    String sql = "INSERT INTO Property (year, district_number, rooms, price) VALUES (?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      for (Property property : properties) {
        statement.setInt(1, property.year());
        statement.setInt(2, property.district_number());
        statement.setString(3, property.rooms().name());
        if (property.price() != null) {
          statement.setInt(4, property.price());
        } else {
          statement.setNull(4, java.sql.Types.INTEGER);
        }
        statement.addBatch();
      }
      statement.executeBatch();
      System.out.println("Daten in Property-Tabelle erfolgreich eingefügt.");
    }
  }
}
