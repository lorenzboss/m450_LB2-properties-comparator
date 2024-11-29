package org.example;

import org.example.database.DatabaseInitializer;
import org.example.districts.JsonToDistricts;
import org.example.properties.JsonToProperties;

/**
 * This class is responsible for executing the logic methods.
 *
 * @author Lorenz Boss
 * @version 1.0
 */
public class App {

  public static void main(String[] args) {
    JsonToDistricts jsonToDistricts = new JsonToDistricts();
    JsonToProperties jsonToProperties = new JsonToProperties();
    LogicExecutor logicExecutor = new LogicExecutor();

    DatabaseInitializer initializer = new DatabaseInitializer(jsonToDistricts, jsonToProperties);
    initializer.initializeDatabase();

    logicExecutor.executeLogic();
  }
}
