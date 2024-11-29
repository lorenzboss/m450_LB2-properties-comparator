package org.example.database;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/** This class is responsible for creating a connection to the database. */
public class DatabaseConnector {
  private static final Dotenv dotenv = Dotenv.load();

  /**
   * This method creates a connection to the database.
   *
   * @return Connection to the database.
   * @throws SQLException If a database access error occurs.
   */
  public static Connection getConnection() throws SQLException {
    Dotenv dotenv = Dotenv.load(); // Dotenv wird zur Laufzeit geladen
    return DriverManager.getConnection(
        Objects.requireNonNull(dotenv.get("DB_URL")),
        dotenv.get("DB_USER"),
        dotenv.get("DB_PASSWORD"));
  }
}
