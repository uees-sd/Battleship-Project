package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel {
  JPanel playerBoard, attackBoard, mainPanel;
  String playerName;
  private final int BOARD_SIZE = 10;
  private final JButton[][] playerButtons = new JButton[BOARD_SIZE][BOARD_SIZE];
  private final JButton[][] enemyButtons = new JButton[BOARD_SIZE][BOARD_SIZE];
  private final int[][] playerBoardMatrix = new int[BOARD_SIZE][BOARD_SIZE];
  private final int[][] enemyBoardMatrix = new int[BOARD_SIZE][BOARD_SIZE];

  public GamePanel(String playerName) {
    // Panel que contiene los tableros (Border Temporal para visualizarlo mejor)
    this.playerName = playerName;
    setLayout(new GridLayout(2, 1, 10, 10));
    setBorder(BorderFactory.createCompoundBorder(
        new EmptyBorder(20, 60, 20, 60), new LineBorder(Color.BLACK, 2)));

    // Crear los tableros
    attackBoard = createBoardPanel("Enemy", enemyButtons);
    playerBoard = createBoardPanel(playerName, playerButtons);

    // Añadir los tableros al panel principal
    add(attackBoard);
    add(playerBoard);

    setVisible(true);
  }

  public void initComponents() {

  }

  private JPanel createBoardPanel(String Title, JButton[][] buttons) {
    // Crear el panel del tablero
    JPanel boardPanel = new JPanel();
    boardPanel.setLayout(new BorderLayout());
    boardPanel.setBorder(new LineBorder(Color.cyan, 2));

    // Crear el panel para la cuadrícula
    JPanel gridPanel = new JPanel();
    gridPanel.setLayout(new GridLayout(BOARD_SIZE + 1, BOARD_SIZE + 1));

    // Llenar las casillas de los tableros
    for (int row = 0; row < 11; row++) {
      for (int col = 0; col < 11; col++) {
        if (row == 0 && col == 0) {
          gridPanel.add(new JLabel(""));
        } else if (row == 0) {
          gridPanel.add(new JLabel(String.valueOf(col), SwingConstants.CENTER));
        } else if (col == 0) {
          gridPanel.add(new JLabel(String.valueOf(row), SwingConstants.CENTER));
        } else {
          JButton button = new JButton();
          button.setBackground(getForeground());
          button.setForeground(Color.BLACK);
          // button.setOpaque(true);
          button.addActionListener(new ButtonClickListener(row - 1, col - 1, buttons));
          buttons[row - 1][col - 1] = button;
          // button.addActionListener(new ButtonClickListener());
          gridPanel.add(button);
        }
      }
    }

    // Añadir el panel de la cuadrícula al panel del tablero
    boardPanel.add(gridPanel, BorderLayout.CENTER);

    // Añadir el título al panel del tablero
    JLabel boardTitle = new JLabel(Title, SwingConstants.CENTER);
    boardPanel.add(boardTitle, BorderLayout.NORTH);

    return boardPanel;
  }

  // Clase interna para manejar los eventos de clic en los botones
  private class ButtonClickListener implements ActionListener {
    private final int row;
    private final int col;
    private final JButton[][] buttons;

    public ButtonClickListener(int row, int col, JButton[][] buttons) {
      this.row = row;
      this.col = col;
      this.buttons = buttons;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      JButton button = buttons[row][col];
      System.out.println("Button clicked: [" + row + ", " + col + "]");
      button.setEnabled(false);
      button.setBackground(Color.GRAY);

      // Actualizar la matriz lógica correspondiente
      if (buttons == playerButtons) {
        playerBoardMatrix[row][col] = 1; // 1 representa un golpe
      } else if (buttons == enemyButtons) {
        enemyBoardMatrix[row][col] = 1; // 1 representa un golpe
      }
    }
  }
}
