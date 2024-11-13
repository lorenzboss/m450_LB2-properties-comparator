package org.example.properties.logic;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import org.example.properties.Property;
import org.example.properties.PropertyService;
import org.example.properties.Rooms;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HighestPriceTest {

  @Mock private PropertyService propertyServiceMock;
  @InjectMocks private HighestPrice highestPrice;

  private List<Property> sampleProperties;

  @BeforeEach
  void setUp() {
    sampleProperties =
        List.of(
            new Property(2021, 1, Rooms.ONE, 500000),
            new Property(2021, 2, Rooms.TWO, 750000),
            new Property(2021, 3, Rooms.THREE, 250000));
  }

  @Test
  void testMostExpensiveProperties_Limit2() {
    Mockito.when(propertyServiceMock.getProperties()).thenReturn(sampleProperties);

    List<Map.Entry<Integer, Integer>> result = highestPrice.mostExpensiveProperties(2);

    assertEquals(2, result.size());
    assertEquals(Map.entry(1, 750000), result.get(0));
    assertEquals(Map.entry(2, 500000), result.get(1));
  }

  @Test
  void mostExpensiveProperties_EmptyList() {
    Mockito.when(propertyServiceMock.getProperties()).thenReturn(List.of());

    List<Map.Entry<Integer, Integer>> result = highestPrice.mostExpensiveProperties(2);

    assertTrue(result.isEmpty());
  }

  @Test
  void mostExpensiveProperties_NullPriceIgnored() {
    sampleProperties =
        List.of(new Property(2021, 1, Rooms.ONE, null), new Property(2021, 2, Rooms.TWO, 750000));
    Mockito.when(propertyServiceMock.getProperties()).thenReturn(sampleProperties);

    List<Map.Entry<Integer, Integer>> result = highestPrice.mostExpensiveProperties(3);

    assertEquals(1, result.size());
    assertEquals(Map.entry(1, 750000), result.get(0));
  }

  @Test
  void mostExpensiveProperties_LimitExceedsSize() {
    Mockito.when(propertyServiceMock.getProperties()).thenReturn(sampleProperties);

    List<Map.Entry<Integer, Integer>> result = highestPrice.mostExpensiveProperties(5);

    assertEquals(3, result.size());
    assertEquals(Map.entry(1, 750000), result.get(0));
    assertEquals(Map.entry(2, 500000), result.get(1));
    assertEquals(Map.entry(3, 250000), result.get(2));
  }
}
