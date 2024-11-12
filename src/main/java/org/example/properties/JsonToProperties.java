package org.example.properties;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import org.example.properties.deserializers.PriceDeserializer;
import org.example.properties.deserializers.RoomsDeserializer;

/**
 * This class is responsible for converting a json file to a list of properties.
 *
 * @author Lorenz Boss
 * @version 1.0
 */
public class JsonToProperties {

  /**
   * Converts a json file to a list of properties.
   *
   * @param filePath the path to the json file
   * @return the list of properties
   * @throws IOException if the file is not found
   */
  public List<Property> convertJsonToProperties(String filePath) throws IOException {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(Rooms.class, new RoomsDeserializer());
    gsonBuilder.registerTypeAdapter(Integer.class, new PriceDeserializer());
    Gson gson = gsonBuilder.create();

    Type listType = new TypeToken<List<Property>>() {}.getType();

    try (FileReader reader = new FileReader(filePath)) {
      return gson.fromJson(reader, listType);
    }
  }
}
