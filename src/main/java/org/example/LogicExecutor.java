package org.example;

import org.example.districts.DistrictService;
import org.example.districts.logic.AverageArea;
import org.example.districts.logic.HighestPopulation;
import org.example.properties.PropertyService;
import org.example.properties.logic.AveragePrice;
import org.example.properties.logic.HighestPrice;
import org.example.properties.logic.NumberOfSales;

/** This class is responsible for executing the logic of the application. */
public class LogicExecutor {
  private final PropertyService propertyService = new PropertyService();
  private final DistrictService districtService = new DistrictService();
  private final NumberOfSales numberOfSales = new NumberOfSales(propertyService);
  private final HighestPrice highestPrice = new HighestPrice(propertyService);
  private final AveragePrice averagePrice = new AveragePrice(propertyService);
  private final AverageArea averageArea = new AverageArea(districtService);
  private final HighestPopulation highestPopulation = new HighestPopulation(districtService);

  /** This method executes the logic of the application. */
  public void executeLogic() {
    executePropertyLogic();
    executeDistrictLogic();
  }

  private void executePropertyLogic() {
    System.out.println("\n\nNumber of properties sold: " + numberOfSales.numberOfSales());

    System.out.println("\n\nNumber of sales per year:");
    numberOfSales
        .numberOfSalesPerYear()
        .forEach(
            entry ->
                System.out.printf("%d: Sold properties: %d%n", entry.getKey(), entry.getValue()));

    System.out.println("\n\nThe price of the most expensive properties:");
    highestPrice
        .mostExpensiveProperties(5)
        .forEach(
            entry ->
                System.out.printf(
                    "%3d: selling price CHF: %d%n", entry.getKey(), entry.getValue()));

    System.out.println("\n\nAverage price per year for properties:");
    averagePrice
        .averagePricePerYear()
        .forEach(
            entry ->
                System.out.printf(
                    "year: %d, average price: %.2f%n", entry.getKey(), entry.getValue()));
  }

  private void executeDistrictLogic() {
    System.out.println(
        "\n\nThe district with the highest population: " + highestPopulation.highestPopulation());

    System.out.println("\n\nAverage area of districts: " + averageArea.averageDistrictArea());
  }
}
