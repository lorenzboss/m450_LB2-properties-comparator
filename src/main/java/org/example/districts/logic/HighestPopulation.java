package org.example.districts.logic;

import java.util.List;
import org.example.districts.District;
import org.example.districts.DistrictService;

/**
 * This class is responsible for calculating the district with the highest population.
 *
 * @author Leandro Aebi
 * @version 1.0
 */
public class HighestPopulation {

  private final DistrictService districtService;

  public HighestPopulation(DistrictService districtService) {
    this.districtService = districtService;
  }

  /**
   * Finds the highest population among all districts.
   *
   * @return the highest population value
   */
  public int highestPopulation() {
    List<District> districts = districtService.getDistricts();

    return districts.stream().mapToInt(District::population).max().orElse(0);
  }
}
