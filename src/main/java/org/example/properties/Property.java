package org.example.properties;

/**
 * This class represents a property.
 *
 * @author Lorenz Boss
 * @version 1.0
 */
public record Property(int year, int districtNumber, Rooms rooms, Integer price) {}
