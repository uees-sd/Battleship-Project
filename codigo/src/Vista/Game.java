package Vista;

import java.awt.BorderLayout;
import javax.swing.*;

public class Game extends JFrame {
    private GamePanel gamePanel;

    public Game(Board playerBoard, Board enemyBoard) {

        setTitle("BattleShip Educativo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        gamePanel = new GamePanel(playerBoard, enemyBoard);
        getContentPane().add(gamePanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
