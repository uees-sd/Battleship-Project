package Vista;

import java.awt.BorderLayout;

import javax.swing.*;

/*
 * Clase que se inicia para pedir el nombre al usuario
 
 */
public class Game extends JFrame {
  private String name;
  private GamePanel gamePanel;

  // Instacio la clase Game
  public static void main(String[] args) {
    new Game();
  }

  public Game() {
    // Pregunto por el nombre del usuario
    name = askName();

    // Si el nombre está vacio no puede continuar
    while (name.isEmpty()) {
      JOptionPane.showMessageDialog(Game.this, "Por favor, ingrese un nombre.", "Error", JOptionPane.ERROR_MESSAGE);
      name = askName();
    }

    // Creo el panel del juego y pongo el GamePanel en el JFrame
    setTitle("BattleShip Educativo");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);

    gamePanel = new GamePanel(name);
    getContentPane().add(gamePanel, BorderLayout.CENTER);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private String askName() {
    return (JOptionPane
        .showInputDialog(null, "Ingrese su nombre:", "", JOptionPane.QUESTION_MESSAGE, null, null, null).toString());
  }
}