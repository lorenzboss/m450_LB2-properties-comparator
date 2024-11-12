package org.example.properties.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

/**
 * Deserializer for the price, which is represented as a string in the json file.
 *
 * @author Lorenz Boss
 * @version 1.0
 */
public class PriceDeserializer implements JsonDeserializer<Integer> {

  /**
   * Deserializes the price.
   *
   * @param json the json element
   * @param typeOfT the type of the object
   * @param context the context
   * @return the number of rooms
   * @throws JsonParseException if the number of rooms is unknown
   */
  @Override
  public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    String jsonString = json.getAsString().trim();
    if (jsonString.equals("( )")) {
      return null;
    }
    try {
      return Integer.parseInt(jsonString);
    } catch (NumberFormatException e) {
      throw new JsonParseException("Invalid integer format: " + jsonString, e);
    }
  }
}
