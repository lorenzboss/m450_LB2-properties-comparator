package org.example.database;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.example.districts.JsonToDistricts;
import org.example.properties.JsonToProperties;
import org.junit.jupiter.api.*;

class DatabaseInitializerTest {

  private static Connection connection;

  @BeforeAll
  static void setUp() throws SQLException {
    connection = DatabaseManager.getConnection();
    DatabaseInitializer initializer =
        new DatabaseInitializer(new JsonToDistricts(), new JsonToProperties());
    initializer.initializeDatabase();
  }

  @AfterAll
  static void tearDown() throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement("TRUNCATE TABLE Property, District RESTART IDENTITY CASCADE")) {
      statement.execute();
    }
    connection.close();
  }

  @Test
  void testInsertInvalidProperty() {
    String sql = "INSERT INTO Property (year, district_number, rooms, price) VALUES (?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, 2023);
      statement.setInt(2, 9999); // Non-existent district_number
      statement.setString(3, "TWO");
      statement.setInt(4, 100000);

      SQLException exception = assertThrows(SQLException.class, statement::executeUpdate);
      assertTrue(
          exception.getMessage().contains("violates foreign key constraint"),
          "Expected a foreign key violation.");
    } catch (SQLException e) {
      fail("Test setup failed: " + e.getMessage());
    }
  }

  @Test
  void testInsertValidProperty() {
    String sqlDistrict =
        "INSERT INTO District (district_number, name, population, area, average_age, number_of_households) VALUES (?, ?, ?, ?, ?, ?)";
    String sqlProperty =
        "INSERT INTO Property (year, district_number, rooms, price) VALUES (?, ?, ?, ?)";
    try (PreparedStatement stmt1 = connection.prepareStatement(sqlDistrict);
        PreparedStatement stmt2 = connection.prepareStatement(sqlProperty)) {

      stmt1.setInt(1, 1); // District number
      stmt1.setString(2, "Test District"); // District name
      stmt1.setInt(3, 10000); // District Population
      stmt1.setInt(4, 50); // District Area
      stmt1.setInt(5, 35); // District Average age
      stmt1.setInt(6, 3000); // District Number of households
      stmt1.executeUpdate();

      stmt2.setInt(1, 2023); // Property Year
      stmt2.setInt(2, 1); // District number
      stmt2.setString(3, "TWO"); // Property Rooms
      stmt2.setInt(4, 100000); // Property Price
      int rows = stmt2.executeUpdate();

      assertEquals(1, rows, "Exactly one row should be inserted into the Property table.");
    } catch (SQLException e) {
      fail("Test failed: " + e.getMessage());
    }
  }
}
