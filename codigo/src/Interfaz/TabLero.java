package Interfaz;

import java.util.ArrayList;

public class TabLero {
    /*
     * El tablero será una matriz 10x10 de tipo int.
     * 0 -> Casilla sin desvelar
     * 1 -> Barco
     * 2 -> Barco Tocado
     * 3 -> Agua/Disparo fallido
     */

    int[][] tablero;
    private ArrayList<Barco> barcos;

    public TabLero() {
        tablero = new int[10][10]; // Inicializa la matriz del tablero
        barcos = new ArrayList<>();
    }

    /*
     * Agrega un Barco a la lista de Barcos.
     * Además actualiza los valores del tablero para
     * representar la posición del Barco.
     * @param barco, Objeto Barco que añadimos al tablero.
     */
    public void agregarBarco(Barco barco) {
        barcos.add(barco);

        // Modificamos el tablero según coordenadas con el nuevo barco
        for (PuntoXY coord : barco.coordenadas) {
            if (coord.x >= 0 && coord.x < tablero.length && coord.y >= 0 && coord.y < tablero[0].length) {
                tablero[coord.x][coord.y] = 1;
            }
        }
    }

    /*
     * Recibe un disparo (un objeto PuntoXY) y comprueba
     * si coincide con las coordenadas de algún barco de este tablero.
     * Si coincide, es que un Barco ha sido tocado, de lo contrario, el disparo ha fallado.
     * Los valores del tablero se actualizan según lo que haya ocurrido.
     * @param disparo, Objeto PuntoXY con las coordenadas donde se ha disparado.
     * @return True si se ha tocado un Barco, False en caso contrario.
     */

    public boolean evaluarDisparo(PuntoXY disparo) {
        if (disparo.x >= 0 && disparo.x < tablero.length && disparo.y >= 0 && disparo.y < tablero[0].length) {
            // De entrada consideramos el disparo como fallido
            tablero[disparo.x][disparo.y] = 3;

            /*
             * Ahora recorremos los barcos y comprobamos si alguno tiene
             * coordenada coincidente con el disparo.
             * Si coincide, corregimos el valor del tablero
             * para la coordenada del disparo
             */
            for (Barco barco : barcos) {
                if (barco.evaluaDisparo(disparo)) {
                    tablero[disparo.x][disparo.y] = 2; // El disparo ha tocado barco
                    return true;
                }
            }
        }
        // Si el bucle no ha retornado true, el disparo se ha confirmado como fallido
        return false;
    }

    /*
     * Comprueba si ya hay dos Destructores en el Tablero.
     * El Destructor es el único Barco que se puede repetir y solo
     * cuando el nivel de dificultad es EASY. Con este método comprobamos
     * si ya se ha alcanzado este límite permitido.
     * @return True si ya hay dos destructores, False en caso contrario.
     */

    public boolean hayDosDestructores() {
        int dt = 0;
        for (Barco b : barcos) {
            if (b.getCodigo().equals("DT")) {
                dt++;
            }
        }
        return dt == 2;
    }
}
