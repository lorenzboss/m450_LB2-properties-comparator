package org.example;

/**
 * This class is responsible for executing the logic methods.
 *
 * @author Lorenz Boss
 * @version 1.0
 */
public class App {

  public static void main(String[] args) {
    LogicExecutor logicExecutor = new LogicExecutor();
    logicExecutor.executePropertyLogic();
    logicExecutor.executeDistrictLogic();
  }
}
