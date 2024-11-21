package org.example.database;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import org.example.Log;
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
    try (Connection connection =
        DriverManager.getConnection(
            Objects.requireNonNull(dotenv.get("DB_URL")),
            dotenv.get("DB_USER"),
            dotenv.get("DB_PASSWORD"))) {
      Log.info("Connected to the database 'properties_db'.");
    } catch (SQLException e) {
      Log.warn("Database 'properties_db' does not exist. Attempting to create it...");

      try (Connection adminConnection =
              DriverManager.getConnection(
                  Objects.requireNonNull(dotenv.get("ADMIN_DB_URL")),
                  dotenv.get("DB_USER"),
                  dotenv.get("DB_PASSWORD"));
          Statement statement = adminConnection.createStatement()) {

        statement.execute("CREATE DATABASE properties_db");
        Log.success("Database 'properties_db' created successfully.");
      } catch (SQLException createException) {
        Log.error("Failed to create the database: " + createException.getMessage());
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
    } catch (SQLException e) {
      Log.error("Failed to create tables: " + e.getMessage());
    }
  }

  private void populateTablesIfEmpty() {
    try (Connection connection = DatabaseManager.getConnection()) {
      // Check if the District table contains data
      if (!hasData(connection, "District")) {
        Log.warn("The 'District' table is empty. Importing data...");
        List<District> districts =
            jsonToDistricts.convertJsonToDistricts("src/main/resources/districts.json");
        insertDistricts(connection, districts);
      } else {
        Log.info("The 'District' table already contains data.");
      }

      // Check if the Property table contains data
      if (!hasData(connection, "Property")) {
        Log.warn("The 'Property' table is empty. Importing data...");
        List<Property> properties =
            jsonToProperties.convertJsonToProperties("src/main/resources/properties.json");
        insertProperties(connection, properties);
      } else {
        Log.info("The 'Property' table already contains data.");
      }
    } catch (SQLException | IOException e) {
      Log.error("Failed to import data: " + e.getMessage());
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
      Log.success("Data successfully imported into the 'District' table.");
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
      Log.success("Data successfully imported into the 'Property' table.");
    }
  }
}
