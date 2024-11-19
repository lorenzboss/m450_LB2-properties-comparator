package org.example.properties;

import static org.example.database.DatabaseManager.getConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class gets the properties and executes the property logic.
 *
 * @version 1.0
 * @author Lorenz Boss
 */
public class PropertyService {
  /**
   * Gets the properties.
   *
   * @return the list of properties
   */
  public List<Property> getProperties() {
    List<Property> properties = new ArrayList<>();
    String sql = "SELECT year, district_number, rooms, price FROM Property";

    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery()) {

      while (resultSet.next()) {
        int year = resultSet.getInt("year");
        int districtNumber = resultSet.getInt("district_number");
        String rooms = resultSet.getString("rooms");
        Integer price = resultSet.getObject("price", Integer.class);

        Property property = new Property(year, districtNumber, Rooms.valueOf(rooms), price);
        properties.add(property);
      }

    } catch (SQLException e) {
      System.err.println("Fehler beim Auslesen der Property-Daten: " + e.getMessage());
      e.printStackTrace();
    }

    System.out.println("The properties were successfully extracted from the database.");
    return properties;
  }
}
