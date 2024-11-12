package org.example.districts;

/**
 * This class represents a district.
 *
 * @version 1.0
 * @author Lorenz Boss
 */
public record District(
    int district_number,
    String name,
    int population,
    int area,
    int average_age,
    int number_of_households) {}
