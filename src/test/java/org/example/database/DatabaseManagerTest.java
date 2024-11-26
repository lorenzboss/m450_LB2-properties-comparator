package org.example.database;

import static org.junit.jupiter.api.Assertions.*;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class DatabaseManagerTest {

  private MockedStatic<Dotenv> mockedDotenv;

  @AfterEach
  void tearDown() {
    if (mockedDotenv != null) {
      mockedDotenv.close();
    }
  }

  @Test
  void testDatabaseConnectionSuccess() {
    // Kein Mocking für diesen Test
    assertDoesNotThrow(
        () -> {
          try (Connection connection = DatabaseManager.getConnection()) {
            assertNotNull(connection, "The database connection should not be null.");
          }
        });
  }

  @Test
  void testDatabaseConnectionFailure() {
    // Mock Dotenv für fehlerhafte Verbindung
    mockedDotenv = Mockito.mockStatic(Dotenv.class);
    Dotenv mockDotenv = Mockito.mock(Dotenv.class);
    mockedDotenv.when(Dotenv::load).thenReturn(mockDotenv);

    Mockito.when(mockDotenv.get("DB_URL"))
        .thenReturn("jdbc:postgresql://invalid-url:5432/other-db");
    Mockito.when(mockDotenv.get("DB_USER")).thenReturn("invalid_user");
    Mockito.when(mockDotenv.get("DB_PASSWORD")).thenReturn("invalid_password");

    SQLException exception =
        assertThrows(
            SQLException.class,
            () -> {
              try (Connection connection = DatabaseManager.getConnection()) {
                connection.isValid(2); // Verbindung explizit validieren
              }
            });

    String expectedMessage = "invalid-url";
    assertEquals(
        exception.getCause().getMessage(),
        expectedMessage,
        "Expected message to contain: " + expectedMessage + ", but got: " + exception.getMessage());
  }

  @Test
  void testDatabaseConnectionWrongCredentials() {
    // Mock Dotenv für falsche Anmeldedaten
    mockedDotenv = Mockito.mockStatic(Dotenv.class);
    Dotenv mockDotenv = Mockito.mock(Dotenv.class);
    mockedDotenv.when(Dotenv::load).thenReturn(mockDotenv);

    Mockito.when(mockDotenv.get("DB_URL"))
        .thenReturn("jdbc:postgresql://localhost:5432/properties_db");
    Mockito.when(mockDotenv.get("DB_USER")).thenReturn("invalid_user");
    Mockito.when(mockDotenv.get("DB_PASSWORD")).thenReturn("invalid_password");

    SQLException exception =
        assertThrows(
            SQLException.class,
            () -> {
              try (Connection connection = DatabaseManager.getConnection()) {
                connection.isValid(2); // Verbindung explizit validieren
              }
            });

    String expectedMessage = "FATAL: role \"invalid_user\" does not exist";
    assertEquals(
        exception.getMessage(),
        expectedMessage,
        "Expected message to contain: " + expectedMessage + ", but got: " + exception.getMessage());
  }
}
