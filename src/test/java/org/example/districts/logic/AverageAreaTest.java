package org.example.districts.logic;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.example.districts.District;
import org.example.districts.DistrictService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AverageAreaTest {

  @Mock private DistrictService districtServiceMock;
  @InjectMocks private AverageArea averageArea;

  @Test
  void averageArea_CalculatesCorrectly() {
    List<District> districts =
        List.of(
            new District(1, "Arlesheim", 1, 200, 1, 1),
            new District(2, "Laufen", 1, 100, 1, 1),
            new District(3, "Liestal", 1, 80, 1, 1),
            new District(4, "Sissach", 1, 140, 1, 1));
    Mockito.when(districtServiceMock.getDistricts()).thenReturn(districts);

    double result = averageArea.averageDistrictArea();

    // Durchschnitt: (200 + 100 + 80 + 140) / 4 = 130
    assertEquals(130, result, 0.01);
  }

  @Test
  void averageArea_WithEmptyDistrictList() {
    Mockito.when(districtServiceMock.getDistricts()).thenReturn(Collections.emptyList());

    double result = averageArea.averageDistrictArea();

    assertEquals(0.0, result);
  }

  @Test
  void averageArea_WithIdenticalAreas() {
    List<District> districts =
        List.of(
            new District(1, "Arlesheim", 1, 100, 1, 1),
            new District(2, "Laufen", 1, 100, 1, 1),
            new District(3, "Liestal", 1, 100, 1, 1));
    Mockito.when(districtServiceMock.getDistricts()).thenReturn(districts);

    double result = averageArea.averageDistrictArea();

    assertEquals(100.0, result, 0.01);
  }
}
