package org.example.properties.logic;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.example.properties.Property;
import org.example.properties.PropertyService;
import org.example.properties.Rooms;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AveragePriceTest {

  @Mock private PropertyService propertyServiceMock;
  @InjectMocks private AveragePrice averagePrice;

  @Test
  void averagePricePerYear_WithMultipleProperties() {
    List<Property> properties =
        List.of(
            new Property(2021, 1, Rooms.ONE, 1000),
            new Property(2021, 2, Rooms.TWO, 2000),
            new Property(2022, 1, Rooms.THREE, 1500),
            new Property(2022, 3, Rooms.FOUR, 2570),
            new Property(2022, 1, Rooms.TWO, 6250));

    Mockito.when(propertyServiceMock.getProperties()).thenReturn(properties);

    List<Map.Entry<Integer, Double>> result = averagePrice.averagePricePerYear();

    assertEquals(2, result.size());
    assertEquals(1500.0, result.get(0).getValue());
    assertEquals(3440, result.get(1).getValue());
  }

  @Test
  void averagePricePerYear_WithNoProperties() {
    Mockito.when(propertyServiceMock.getProperties()).thenReturn(Collections.emptyList());

    List<Map.Entry<Integer, Double>> result = averagePrice.averagePricePerYear();

    assertTrue(result.isEmpty());
  }

  @Test
  void averagePricePerYear_WithNullPrices() {
    List<Property> properties =
        List.of(new Property(2021, 1, Rooms.ONE, null), new Property(2022, 2, Rooms.TWO, null));

    Mockito.when(propertyServiceMock.getProperties()).thenReturn(properties);

    List<Map.Entry<Integer, Double>> result = averagePrice.averagePricePerYear();

    assertTrue(result.isEmpty());
  }
}
