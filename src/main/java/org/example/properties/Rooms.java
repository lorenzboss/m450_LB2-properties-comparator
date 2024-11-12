package org.example.properties;

/**
 * Enum for the number of rooms.
 *
 * @author Lorenz Boss
 * @version 1.0
 */
public enum Rooms {
  ONE,
  TWO,
  THREE,
  FOUR,
  TOTAL,
  FIVE_PLUS;

  /**
   * Returns the sort order of the enum.
   *
   * @return the sort order
   */
  public int getSortOrder() {
    return switch (this) {
      case ONE -> 1;
      case TWO -> 2;
      case THREE -> 3;
      case FOUR -> 4;
      case TOTAL -> 5;
      case FIVE_PLUS -> 6;
    };
  }
}
