package org.example.properties;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonParseException;
import org.junit.jupiter.api.Test;

class PropertyValidationTest {

  @Test
  void testInvalidJsonData() {
    JsonToProperties jsonToProperties = new JsonToProperties();
    Exception exception =
        assertThrows(
            JsonParseException.class,
            () ->
                jsonToProperties.convertJsonToProperties(
                    "src/test/resources/invalid_properties.json"));
    assertTrue(
        exception.getMessage().contains("Unknown number of rooms:"),
        "Expected an error related to invalid JSON data.");
  }

  @Test
  void testValidJsonData() {
    JsonToProperties jsonToProperties = new JsonToProperties();
    assertDoesNotThrow(
        () -> {
          var properties =
              jsonToProperties.convertJsonToProperties("src/test/resources/valid_properties.json");
          assertNotNull(properties, "Properties list should not be null for valid JSON.");
          assertFalse(properties.isEmpty(), "Properties list should not be empty for valid JSON.");
        });
  }
}
