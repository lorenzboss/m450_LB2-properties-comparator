package org.example.districts;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * This class is responsible for converting a json file to a list of districts.
 *
 * @version 1.0
 * @author Lorenz Boss
 */
public class JsonToDistricts {

  /**
   * Converts a json file to a list of districts.
   *
   * @param filePath the path to the json file
   * @return the list of districts
   * @throws IOException if the file is not found
   */
  public List<District> convertJsonToDistricts(String filePath) throws IOException {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
    Gson gson = gsonBuilder.create();

    Type listType = new TypeToken<List<District>>() {}.getType();

    try (FileReader reader = new FileReader(filePath)) {
      return gson.fromJson(reader, listType);
    }
  }
}
