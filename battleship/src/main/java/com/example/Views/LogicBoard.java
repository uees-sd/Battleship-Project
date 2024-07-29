package com.example.Views;

import java.io.Serializable;
import java.util.ArrayList;

public class LogicBoard implements Serializable {
  /*
   * El logicMatrix será una matriz 10x10 de tipo int.
   * 0 -> Casilla sin desvelar
   * 1 -> Ship
   * 2 -> Ship Tocado
   * 3 -> Agua/Disparo fallido
   */

  public int[][] logicMatrix;
  public ArrayList<Ship> ships;

  public LogicBoard() {
    logicMatrix = new int[10][10]; // Inicializa la matriz del logicMatrix
    ships = new ArrayList<>();
    /*
     * for (int i = 1; i < 6; i++) {
     * ships.add(new Ship(i));
     * }
     */
  }

  /*
   * Agrega un Ship a la lista de Ships.
   * Además actualiza los valores del logicMatrix para
   * representar la posición del Ship.
   * 
   * @param Ship, Objeto Ship que añadimos al logicMatrix.
   */
  public void addShip(Ship ship) {
    ships.add(ship);

    // Modificamos el logicMatrix según coordenadas con el nuevo Ship
    for (PointXY coord : ship.coords) {
      if (coord.x >= 0 && coord.x < logicMatrix.length && coord.y >= 0 && coord.y < logicMatrix[0].length) {
        logicMatrix[coord.x][coord.y] = 1;
      }
    }
  }

  /*
   * Recibe un disparo (un objeto PointXY) y comprueba
   * si coincide con las coordenadas de algún Ship de este logicMatrix.
   * Si coincide, es que un Ship ha sido tocado, de lo contrario, el disparo ha
   * fallado.
   * Los valores del logicMatrix se actualizan según lo que haya ocurrido.
   *
   * @param disparo, Objeto PointXY con las coordenadas donde se ha disparado.
   *
   * @return True si se ha tocado un Ship, False en caso contrario.
   */

  public boolean evaluateShot(PointXY disparo) {
    if (disparo.x >= 0 && disparo.x < logicMatrix.length && disparo.y >= 0 &&
        disparo.y < logicMatrix[0].length) {
      // De entrada consideramos el disparo como fallido
      logicMatrix[disparo.x][disparo.y] = 3;

      /*
       * Ahora recorremos los Ships y comprobamos si alguno tiene
       * coordenada coincidente con el disparo.
       * Si coincide, corregimos el valor del logicMatrix
       * para la coordenada del disparo
       */
      for (Ship ship : ships) {
        if (ship.evaluateShot(disparo)) {
          logicMatrix[disparo.x][disparo.y] = 2; // El disparo ha tocado Ship
          return true;
        }
      }
    }
    // Si el bucle no ha retornado true, el disparo se ha confirmado como fallido
    return false;
  }
}
