package Interfaz;

import javax.swing.*;

public class VentanaTablero extends JFrame {
    public VentanaTablero(TabLero modelo) {
        setTitle("Tablero de Battleship");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        PanelTablero panelTablero1 = new PanelTablero(modelo);
        add(panelTablero1);

        setVisible(true);
    }
}

/*
 * public class Main {
    public static void main(String[] args) {
        // Suponiendo que tienes una clase Tablero que inicializa el modelo del tablero
        TabLero modelo = new TabLero(); // Inicializa tu modelo aquí
        VentanaTablero ventana = new VentanaTablero(modelo);
        ventana.setVisible(true);
    }
}

*/