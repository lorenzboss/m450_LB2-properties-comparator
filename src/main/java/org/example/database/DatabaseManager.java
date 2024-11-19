package org.example.database;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
  private static final Dotenv dotenv = Dotenv.load();

  private static final String DB_URL = dotenv.get("DB_URL");
  private static final String DB_USER = dotenv.get("DB_USER");
  private static final String DB_PASSWORD = dotenv.get("DB_PASSWORD");

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
  }
}
