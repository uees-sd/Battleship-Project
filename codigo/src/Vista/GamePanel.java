package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class GamePanel extends JPanel {
    private final int BOARD_SIZE = 10;
    private final int[][] playerBoardMatrix = new int[BOARD_SIZE][BOARD_SIZE];
    private final int[][] enemyBoardMatrix = new int[BOARD_SIZE][BOARD_SIZE];

    public GamePanel(Board playerBoard, Board enemyBoard) {
        setLayout(new GridLayout(2, 1, 10, 10));
        setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(20, 60, 20, 60), new LineBorder(Color.BLACK, 2)));

        add(playerBoard);
        add(enemyBoard);

        setVisible(true);
    }
}
