package org.example.districts;

import com.google.gson.JsonParseException;
import org.example.districts.JsonToDistricts;
import org.example.properties.JsonToProperties;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DistrictValidationTest {

  @Test
  void testInvalidJsonData() {
    JsonToDistricts jsonToDistricts = new JsonToDistricts();
    Exception exception =
        assertThrows(
            JsonParseException.class,
            () ->
                jsonToDistricts.convertJsonToDistricts(
                    "src/test/resources/invalid_districts.json"));
    assertTrue(
        exception
            .getMessage()
            .contains("java.lang.NumberFormatException: For input string: \"holok\""),
        "Expected an error related to invalid JSON data.");
  }

  @Test
  void testValidJsonData() {
    JsonToDistricts jsonToDistricts = new JsonToDistricts();
    assertDoesNotThrow(
        () -> {
          var districts =
              jsonToDistricts.convertJsonToDistricts("src/test/resources/valid_districts.json");
          assertNotNull(districts, "District list should not be null for valid JSON.");
          assertFalse(districts.isEmpty(), "District list should not be empty for valid JSON.");
        });
  }
}
