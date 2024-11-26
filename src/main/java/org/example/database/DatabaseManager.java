package org.example.database;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class DatabaseManager {
  private static final Dotenv dotenv = Dotenv.load();

  public static Connection getConnection() throws SQLException {
    Dotenv dotenv = Dotenv.load(); // Dotenv wird zur Laufzeit geladen
    return DriverManager.getConnection(
        Objects.requireNonNull(dotenv.get("DB_URL")),
        dotenv.get("DB_USER"),
        dotenv.get("DB_PASSWORD"));
  }
}
