package org.example.districts;

import static org.example.database.DatabaseConnector.getConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.example.Log;

/**
 * This class gets the districts and executes the district logic.
 *
 * @version 1.0
 * @author Lorenz Boss
 */
public class DistrictService {
  /**
   * Gets the districts.
   *
   * @return the list of districts
   */
  public List<District> getDistricts() {
    List<District> districts = new ArrayList<>();
    String sql =
        "SELECT district_number, name, population, area, average_age, number_of_households FROM District";

    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery()) {

      while (resultSet.next()) {
        int districtNumber = resultSet.getInt("district_number");
        String name = resultSet.getString("name");
        int population = resultSet.getInt("population");
        int area = resultSet.getInt("area");
        int averageAge = resultSet.getInt("average_age");
        int numberOfHouseholds = resultSet.getInt("number_of_households");

        District district =
            new District(districtNumber, name, population, area, averageAge, numberOfHouseholds);
        districts.add(district);
      }

    } catch (SQLException e) {
      Log.error("An error occurred while reading the district data: " + e.getMessage());
      e.printStackTrace();
    }

    return districts;
  }
}
