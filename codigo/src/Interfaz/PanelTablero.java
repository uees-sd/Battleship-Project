package Interfaz;

import javax.swing.*;
import java.awt.*;


public class PanelTablero extends JPanel {
    public Celda[][] celdas; // Celdas jugables donde habrá agua, barcos, etc.
    private TabLero tablero;

    public PanelTablero(TabLero tablero) {
        this.tablero = tablero;

        // Construimos celdas para las "aguas"
        celdas = new Celda[10][10];
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                celdas[i][j] = new Celda(new PuntoXY(i, j), null);

        // Celdas con número
        Celda[] celdasX = new Celda[10];
        Celda[] celdasY = new Celda[10];
        for (int i = 0; i < 10; i++) {
            celdasX[i] = new Celda(new PuntoXY(i, 0), new JLabel(Integer.toString(i)));
            celdasY[i] = new Celda(new PuntoXY(0, i), new JLabel(Integer.toString(i)));
        }

        // Maquetamos tablero
        setLayout(new GridLayout(11, 11, 4, 4)); // Esta grilla tendrá ambos tipos de celdas
        // La grilla se rellena fila a fila.
        // 1ª fila
        add(new JPanel()); // Primera celda de la grilla no será nada, solo un panel vacío.
        // A continuación, las Celdas con número para las Columnas, o sea, el eje Y de coordenadas
        for (Celda valorEjeY : celdasY)
            add(valorEjeY);

        // Las siguientes filas las automatizamos con bucles anidados
        for (int i = 0; i < 10; i++) {
            add(celdasX[i]); // Comienza con número de Fila, o sea, eje X
            // A continuación, una tanda de Celdas "agua" de las que tenemos en la matriz
            for (Celda agua : celdas[i])
                add(agua);
        }
    }

    /*
     * Cambia el color de las Celdas según la información del Tablero modelo
     */
    public void setTableroModelo() {
        // Recorremos celdas y modificamos colores según valores del modelo
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++) {
                switch (tablero.tablero[i][j]) { // Consultamos matriz del Tablero
                    case 0: // Casilla sin desvelar
                        celdas[i][j].setBackground(Color.WHITE);
                        break;
                    case 1: // Barco
                        celdas[i][j].setBackground(Color.DARK_GRAY);
                        break;
                    case 2: // Barco Tocado
                        celdas[i][j].setBackground(Color.RED);
                        break;
                    case 3: // Agua/Disparo fallido
                        celdas[i][j].setBackground(Color.CYAN);
                        break;
                }
            }
    }

    /*
     * Habrán dos tipos de Celdas.
     * Una tendrá una etiqueta para mostrar el número de fila
     * o columna que representa, con valores de 0 a 9. Serán las
     * Celdas que identifican las coordenadas.
     *
     * Las otras no tendrán etiqueta y representarán las "aguas" donde
     * se lleva a cabo la batalla.
     *
     * Tendremos un constructor, que acepta coordenadas y opcionalmente una etiqueta.
     */

    public class Celda extends JPanel {
        public PuntoXY coord; // Todas las casillas tendrán coordenadas
        private JLabel numCelda; // Solo algunas casillas mostrarán número

        public Celda(PuntoXY coord, JLabel numCelda) {
            this.coord = coord;
            this.numCelda = numCelda;

            setPreferredSize(new Dimension(50, 50));
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 4, true));

            if (numCelda != null) {
                numCelda.setFont(new Font("Verdana", Font.BOLD, 22));
                numCelda.setForeground(Color.WHITE);
                add(numCelda);
                setBackground(new Color(120, 118, 118));
            } else {
                setBackground(Color.WHITE);
            }
        }
    }

    /*
     * Elimina los MouseListener de cada Celda.<br>
     * Esto sirve para cuando queremos que el usuario
     * no pueda clickar en las Celdas, por ejemplo
     * cuando ya ha colocado todos los barcos
     */

    public void desactivarListener() {
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                celdas[i][j].removeMouseListener(celdas[i][j].getMouseListeners()[0]);
    }
}
