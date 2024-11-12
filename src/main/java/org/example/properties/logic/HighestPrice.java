package org.example.properties.logic;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import org.example.properties.Property;
import org.example.properties.PropertyService;

/**
 * This class is responsible for calculating the highest price of properties.
 *
 * @author Lorenz Boss
 * @version 2.0
 */
public class HighestPrice {
  private final PropertyService propertyService;

  public HighestPrice(PropertyService propertyService) {
    this.propertyService = propertyService;
  }

  /**
   * Prints the price of the most expensive properties.
   *
   * @param limit the number of properties to print
   * @return the list of the most expensive properties
   */
  public List<Map.Entry<Integer, Integer>> mostExpensiveProperties(int limit) {
    List<Property> propertyList = propertyService.getProperties();
    AtomicInteger index = new AtomicInteger(1);

    return propertyList.stream()
        .map(Property::price)
        .filter(Objects::nonNull)
        .sorted(Comparator.reverseOrder())
        .limit(limit)
        .map(price -> Map.entry(index.getAndIncrement(), price))
        .toList();
  }
}
