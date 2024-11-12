package org.example.properties;

import java.io.IOException;
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
    JsonToProperties JsonToProperties = new JsonToProperties();
    try {
      return JsonToProperties.convertJsonToProperties("src/main/resources/properties.json");
    } catch (IOException e) {
      throw new RuntimeException("The file specified in the property service was not found!", e);
    }
  }
}
