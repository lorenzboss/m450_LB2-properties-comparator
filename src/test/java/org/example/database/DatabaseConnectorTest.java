package org.example.database;

import static org.junit.jupiter.api.Assertions.*;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class DatabaseConnectorTest {

  private MockedStatic<Dotenv> mockedDotenv;

  @AfterEach
  void tearDown() {
    if (mockedDotenv != null) {
      mockedDotenv.close();
    }
  }

  @Test
  void testDatabaseConnectionSuccess() {
    assertDoesNotThrow(
        () -> {
          try (Connection connection = DatabaseConnector.getConnection()) {
            assertNotNull(connection, "The database connection should not be null.");
          }
        });
  }

  @Test
  void testDatabaseConnectionFailure() {
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
              try (Connection connection = DatabaseConnector.getConnection()) {
                connection.isValid(2);
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
              try (Connection connection = DatabaseConnector.getConnection()) {
                connection.isValid(2);
              }
            });

    String expectedMessage1 = "FATAL: role \"invalid_user\" does not exist";
    String expectedMessage2 = "FATAL: password authentication failed for user \"invalid_user\"";

    assertTrue(
        exception.getMessage().contains(expectedMessage1)
            || exception.getMessage().contains(expectedMessage2),
        "Expected message to contain one of the following: "
            + expectedMessage1
            + " or "
            + expectedMessage2
            + ", but got: "
            + exception.getMessage());
  }
}
