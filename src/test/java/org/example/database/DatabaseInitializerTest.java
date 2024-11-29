package org.example.database;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.districts.JsonToDistricts;
import org.example.properties.JsonToProperties;
import org.junit.jupiter.api.*;

class DatabaseInitializerTest {

  private static Connection connection;

  @BeforeAll
  static void setUp() throws SQLException {
    connection = DatabaseConnector.getConnection();
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

  @BeforeEach
  void cleanDatabaseBeforeEach() throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement("TRUNCATE TABLE Property, District RESTART IDENTITY CASCADE")) {
      statement.execute();
    }
  }

  @Test
  void shouldCreateTables() {
    String sqlDistrictExists =
        "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'district')";
    String sqlPropertyExists =
        "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'property')";

    try (PreparedStatement stmt1 = connection.prepareStatement(sqlDistrictExists);
        PreparedStatement stmt2 = connection.prepareStatement(sqlPropertyExists);
        ResultSet rs1 = stmt1.executeQuery();
        ResultSet rs2 = stmt2.executeQuery()) {

      assertTrue(rs1.next() && rs1.getBoolean(1), "District table should exist.");
      assertTrue(rs2.next() && rs2.getBoolean(1), "Property table should exist.");
    } catch (SQLException e) {
      fail("Failed to verify table existence: " + e.getMessage());
    }
  }

  @Test
  void shouldPopulateDistrictsIfEmpty() {
    try {
      try (PreparedStatement truncateStmt =
          connection.prepareStatement("TRUNCATE TABLE District RESTART IDENTITY CASCADE")) {
        truncateStmt.execute();
      }

      DatabaseInitializer initializer =
          new DatabaseInitializer(new JsonToDistricts(), new JsonToProperties());
      initializer.initializeDatabase();

      String sql = "SELECT COUNT(*) FROM District";
      try (PreparedStatement stmt = connection.prepareStatement(sql);
          ResultSet rs = stmt.executeQuery()) {
        assertTrue(rs.next() && rs.getInt(1) > 0, "District table should be populated.");
      }
    } catch (SQLException e) {
      fail("Test failed: " + e.getMessage());
    }
  }

  @Test
  void shouldPopulatePropertiesIfEmpty() {
    try {
      try (PreparedStatement truncateStmt =
          connection.prepareStatement("TRUNCATE TABLE Property RESTART IDENTITY CASCADE")) {
        truncateStmt.execute();
      }

      DatabaseInitializer initializer =
          new DatabaseInitializer(new JsonToDistricts(), new JsonToProperties());
      initializer.initializeDatabase();

      String sql = "SELECT COUNT(*) FROM Property";
      try (PreparedStatement stmt = connection.prepareStatement(sql);
          ResultSet rs = stmt.executeQuery()) {
        assertTrue(rs.next() && rs.getInt(1) > 0, "Property table should be populated.");
      }
    } catch (SQLException e) {
      fail("Test failed: " + e.getMessage());
    }
  }

  @Test
  void shouldInsertValidDistrict() {
    String sqlDistrict =
        "INSERT INTO District (district_number, name, population, area, average_age, number_of_households) VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(sqlDistrict)) {
      stmt.setInt(1, 1); // District number
      stmt.setString(2, "Test District"); // District name
      stmt.setInt(3, 10000); // District Population
      stmt.setInt(4, 50); // District Area
      stmt.setInt(5, 35); // District Average age
      stmt.setInt(6, 3000); // District Number of households
      int rows = stmt.executeUpdate();

      assertEquals(1, rows, "Exactly one row should be inserted into the District table.");
    } catch (SQLException e) {
      fail("Test failed: " + e.getMessage());
    }
  }

  @Test
  void shouldInsertValidProperty() {
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

  @Test
  void shouldHandleNoPrice() {
    String sqlDistrict =
        "INSERT INTO District (district_number, name, population, area, average_age, number_of_households) VALUES (?, ?, ?, ?, ?, ?)";
    String sqlProperty =
        "INSERT INTO Property (year, district_number, rooms) VALUES (?, ?, ?) RETURNING price";

    try (PreparedStatement stmt1 = connection.prepareStatement(sqlDistrict);
        PreparedStatement stmt2 = connection.prepareStatement(sqlProperty)) {

      stmt1.setInt(1, 1); // District number
      stmt1.setString(2, "Test District"); // District name
      stmt1.setInt(3, 5000); // District Population
      stmt1.setInt(4, 25); // District Area
      stmt1.setInt(5, 30); // District Average age
      stmt1.setInt(6, 1200); // District Number of households
      stmt1.executeUpdate();

      stmt2.setInt(1, 2023); // Property Year
      stmt2.setInt(2, 1); // District number
      stmt2.setString(3, "THREE"); // Property Rooms
      try (ResultSet rs = stmt2.executeQuery()) {
        assertTrue(rs.next(), "Insert should return a result.");
        assertNull(rs.getObject("price"), "Default value for 'price' should be NULL.");
      }
    } catch (SQLException e) {
      fail("Test failed: " + e.getMessage());
    }
  }

  @Test
  void shouldEnforceNotNullConstraint() {
    String sql =
        "INSERT INTO District (district_number, name, population, area, average_age, number_of_households) VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setNull(1, java.sql.Types.INTEGER); // district_number (NOT NULL)
      stmt.setString(2, "Test District"); // name
      stmt.setInt(3, 10000); // population
      stmt.setInt(4, 50); // area
      stmt.setInt(5, 35); // average_age
      stmt.setInt(6, 3000); // number_of_households

      SQLException exception = assertThrows(SQLException.class, stmt::executeUpdate);
      assertTrue(
          exception.getMessage().contains("violates not-null constraint"),
          "Expected a NOT NULL constraint violation for 'district_number'.");
    } catch (SQLException e) {
      fail("Test setup failed: " + e.getMessage());
    }

    String sqlProperty =
        "INSERT INTO Property (year, district_number, rooms, price) VALUES (?, ?, ?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(sqlProperty)) {
      stmt.setInt(1, 2023); // year
      stmt.setNull(2, java.sql.Types.INTEGER); // district_number (NOT NULL)
      stmt.setString(3, "TWO"); // rooms
      stmt.setInt(4, 100000); // price

      SQLException exception = assertThrows(SQLException.class, stmt::executeUpdate);
      assertTrue(
          exception.getMessage().contains("violates not-null constraint"),
          "Expected a NOT NULL constraint violation for 'district_number' in Property table.");
    } catch (SQLException e) {
      fail("Test setup failed: " + e.getMessage());
    }
  }

  @Test
  void shouldNotAllowInsertWithInvalidForeignKey() {
    String sql = "INSERT INTO Property (year, district_number, rooms, price) VALUES (?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, 2023); // Property Year
      statement.setInt(2, 9999); // Non-existent district_number
      statement.setString(3, "TWO"); // Property Rooms
      statement.setInt(4, 100000); // Property Price

      SQLException exception = assertThrows(SQLException.class, statement::executeUpdate);
      assertTrue(
          exception.getMessage().contains("violates foreign key constraint"),
          "Expected a foreign key violation.");
    } catch (SQLException e) {
      fail("Test setup failed: " + e.getMessage());
    }
  }
}
