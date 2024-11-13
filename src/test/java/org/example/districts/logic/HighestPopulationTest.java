package org.example.districts.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import org.example.districts.District;
import org.example.districts.DistrictService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HighestPopulationTest {

  @Mock private DistrictService districtServiceMock;
  @InjectMocks private HighestPopulation highestPopulation;

  @BeforeEach
  void setUp() {
    districtServiceMock = Mockito.mock(DistrictService.class);
    highestPopulation = new HighestPopulation(districtServiceMock);
  }

  @Test
  void highestPopulation_CalculatesCorrectly() {
    List<District> districts =
        List.of(
            new District(1, "Arlesheim", 100_000, 1, 1, 1),
            new District(2, "Laufen", 50_000, 1, 1, 1),
            new District(3, "Liestal", 18_000, 1, 1, 1),
            new District(4, "Sissach", 40, 1, 1, 1));

    Mockito.when(districtServiceMock.getDistricts()).thenReturn(districts);

    int highestPopulationResult = highestPopulation.highestPopulation();

    assertEquals(100_000, highestPopulationResult);
  }

  @Test
  void highestPopulation_WithEmptyDistrictList() {
    Mockito.when(districtServiceMock.getDistricts()).thenReturn(Collections.emptyList());

    int result = highestPopulation.highestPopulation();

    assertEquals(0, result);
  }

  @Test
  void highestPopulation_WithSingleDistrict() {
    List<District> districts = List.of(new District(1, "Arlesheim", 150_000, 1, 1, 1));
    Mockito.when(districtServiceMock.getDistricts()).thenReturn(districts);

    int result = highestPopulation.highestPopulation();

    assertEquals(150_000, result);
  }

  @Test
  void highestPopulation_WithNegativePopulation() {
    List<District> districts =
        List.of(
            new District(1, "Arlesheim", -1000, 1, 1, 1),
            new District(2, "Laufen", 30000, 1, 1, 1),
            new District(3, "Liestal", 20000, 1, 1, 1));
    Mockito.when(districtServiceMock.getDistricts()).thenReturn(districts);

    int result = highestPopulation.highestPopulation();

    assertEquals(30000, result);
  }
}
