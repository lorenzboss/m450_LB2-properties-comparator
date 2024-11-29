package org.example.districts;

/**
 * This class represents a district.
 *
 * @version 1.0
 * @author Lorenz Boss
 */
public record District(
    int districtNumber,
    String name,
    int population,
    int area,
    int averageAge,
    int numberOfHouseholds) {}
