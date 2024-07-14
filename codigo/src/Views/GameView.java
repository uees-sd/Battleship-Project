package Views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class GameView extends JPanel {
    private final int BOARD_SIZE = 10;
    private final int[][] playerBoardMatrix = new int[BOARD_SIZE][BOARD_SIZE];
    private final int[][] enemyBoardMatrix = new int[BOARD_SIZE][BOARD_SIZE];

    public GameView() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(20, 60, 20, 60), new LineBorder(Color.BLACK, 2)));
        setVisible(true);
    }
}
