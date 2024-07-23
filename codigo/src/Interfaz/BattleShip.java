package Interfaz;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BattleShip {

    private ArrayList<Jugador> jugadores;
    private Jugador jugador1;
    private Jugador jugador2;
    private TabLero tablero1;
    private TabLero tablero2;
    private DialogoCrearBarcos crearBarcos;

    public BattleShip() {
        jugador1 = null;
        jugador2 = null;
        tablero1 = new TabLero();
        tablero2 = new TabLero();
    }

    /*
     * Abre el diálogo para colocar barcos.
     * El tablero mostrado dependerá del jugador que va a colocar barcos.
     * @param numPlayer Valor int entre 1 y 2 para indicar que Player coloca barcos.
     */

    public void colocarBarcos(int numPlayer) {
        if (numPlayer == 1) {
            crearBarcos = new DialogoCrearBarcos(null, true, tablero1);
            crearBarcos.botonTerminar.addActionListener(new AccionColocarBarcoJugador1());
        } else {
            crearBarcos = new DialogoCrearBarcos(null, true, tablero2);
            crearBarcos.botonTerminar.addActionListener(new AccionColocarBarcoJugador2());
        }

        crearBarcos.setVisible(true);
    }


    private class AccionColocarBarcoJugador1 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Cerramos el diálogo que ha usado Player1
            crearBarcos.dispose();
            // Mostramos un mensaje y preparamos el diálogo para Player2
            JOptionPane.showMessageDialog(null, "A continuación colocará Barcos el jugador: "
                    + jugador1.getNombre(), "Colocar Barcos", JOptionPane.INFORMATION_MESSAGE);
            // Llamamos al método para colocar barcos para el player 2
            colocarBarcos(1);
        }
    }


    private class AccionColocarBarcoJugador2 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            //Cerramos el Dialogo que ha usado Player1
            crearBarcos.dispose();
            //Abrimos uno nuevo para Player2
            JOptionPane.showMessageDialog(null, "A continuación colocará Barcos el jugador: "
                    + jugador2.getNombre(), "Colocar Barcos", JOptionPane.INFORMATION_MESSAGE);
            colocarBarcos(2);

        }
    }

    private class AccionComenzarJuego implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            crearBarcos.dispose();
            JOptionPane.showMessageDialog(null, "Comienza Battleship");
        }
    }
}
