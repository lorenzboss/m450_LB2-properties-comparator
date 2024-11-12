package org.example.properties.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.example.properties.Rooms;

/**
 * Deserializer for the number of rooms, which is represented as a string in the json file.
 *
 * @author Lorenz Boss
 * @version 1.0
 */
public class RoomsDeserializer implements JsonDeserializer<Rooms> {

  /**
   * Deserializes the number of rooms.
   *
   * @param json the json element
   * @param typeOfT the type of the object
   * @param context the context
   * @return the number of rooms
   * @throws JsonParseException if the number of rooms is unknown
   */
  @Override
  public Rooms deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    String value = json.getAsString();
    return switch (value) {
      case "1" -> Rooms.ONE;
      case "2" -> Rooms.TWO;
      case "3" -> Rooms.THREE;
      case "4" -> Rooms.FOUR;
      case "5+" -> Rooms.FIVE_PLUS;
      case "Total" -> Rooms.TOTAL;
      default -> throw new JsonParseException("Unknown number of rooms: " + value);
    };
  }
}
