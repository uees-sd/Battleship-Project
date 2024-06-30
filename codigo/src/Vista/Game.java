package Vista;

import java.awt.BorderLayout;
import javax.swing.*;

public class Game extends JFrame {
    private String name;
    private GamePanel gamePanel;

    public static void main(String[] args) {
        new Game();
    }

    public Game() {
        name = askName();

        while (name.isEmpty()) {
            JOptionPane.showMessageDialog(Game.this, "Por favor, ingrese un nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            name = askName();
        }

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
        return (JOptionPane.showInputDialog(null, "Ingrese su nombre:", "", JOptionPane.QUESTION_MESSAGE, null, null, null).toString());
    }
}
