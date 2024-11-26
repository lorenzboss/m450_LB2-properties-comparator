package org.example.database;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class DatabaseManager {
  private static final Dotenv dotenv = Dotenv.load();

  public static Connection getConnection() throws SQLException {
    Dotenv dotenv = Dotenv.load();
    String url = Objects.requireNonNull(dotenv.get("DB_URL"));
    String user = dotenv.get("DB_USER");
    String password = dotenv.get("DB_PASSWORD");

    for (int i = 0; i < 5; i++) {
      try {
        return DriverManager.getConnection(url, user, password);
      } catch (SQLException e) {
        try {
          Thread.sleep(2000); // 2 Sekunden warten
        } catch (InterruptedException ex) {
          Thread.currentThread().interrupt();
          throw new SQLException("Interrupted while waiting for database connection", ex);
        }
      }
    }
    throw new SQLException("Unable to connect to database after multiple attempts");
  }
}
