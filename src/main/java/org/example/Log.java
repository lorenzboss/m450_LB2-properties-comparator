package org.example;

/** This class is responsible for logging messages to the console. */
public class Log {
  private static final String RESET = "\u001B[0m";
  private static final String RED = "\u001B[31m";
  private static final String ORANGE = "\u001B[38;5;214m";
  private static final String LIGHT_BLUE = "\u001B[34m";
  private static final String GREEN = "\u001B[32m";

  /**
   * This method logs an info message to the console.
   *
   * @param message The message to log.
   */
  public static void info(String message) {
    System.out.println(LIGHT_BLUE + "[INFO] " + message + RESET);
  }

  /**
   * This method logs a success message to the console.
   *
   * @param message The message to log.
   */
  public static void success(String message) {
    System.out.println(GREEN + "[SUCCESS] " + message + RESET);
  }

  /**
   * This method logs a warning message to the console.
   *
   * @param message The message to log.
   */
  public static void warn(String message) {
    System.out.println(ORANGE + "[WARN] " + message + RESET);
  }

  /**
   * This method logs an error message to the console.
   *
   * @param message The message to log.
   */
  public static void error(String message) {
    System.err.println(RED + "[ERROR] " + message + RESET);
  }
}
