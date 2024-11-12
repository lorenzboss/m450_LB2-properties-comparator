package org.example.districts.logic;

import java.util.List;
import org.example.districts.District;
import org.example.districts.DistrictService;

/**
 * This class is responsible for calculating the average area of districts.
 *
 * @author Leandro Aebi
 * @version 1.0
 */
public class AverageArea {

  private final DistrictService districtService;

  public AverageArea(DistrictService districtService) {
    this.districtService = districtService;
  }

  /**
   * Calculates the average area of all districts.
   *
   * @return the average area of districts
   */
  public double averageDistrictArea() {
    List<District> districts = districtService.getDistricts();
    return districts.stream().mapToInt(District::area).average().orElse(0.0);
  }
}
