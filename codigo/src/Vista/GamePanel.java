package Vista;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GamePanel extends JPanel {
  JPanel playerBoard, attackBoard, mainPanel;
  String playerName;
  private final int BOARD_SIZE = 10;
  private final JButton[][] playerButtons = new JButton[BOARD_SIZE][BOARD_SIZE];
  private final JButton[][] enemyButtons = new JButton[BOARD_SIZE][BOARD_SIZE];
  private final int[][] playerBoardMatrix = new int[BOARD_SIZE][BOARD_SIZE];
  private final int[][] enemyBoardMatrix = new int[BOARD_SIZE][BOARD_SIZE];
  // Ships con su medida (botones)
  private Ship[] ships = new Ship[] { new Ship(5), new Ship(4), new Ship(3), new Ship(3), new Ship(2) };
  private int currentShipIndex = 0;

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
          button.setFocusable(false);
          // button.setOpaque(true);
          button.addActionListener(new ButtonClickListener(row - 1, col - 1, buttons));
          // borde
          button.addMouseListener(new ButtonClickListener(row - 1, col - 1, buttons));

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
  private class ButtonClickListener implements ActionListener, MouseListener {
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
      button.setBackground(Color.BLACK);
      // Colorcar barco en la matriz
      if (currentShipIndex >= ships.length) {
        return; // Todos los barcos han sido colocados
      }

      Ship currentShip = ships[currentShipIndex];
      int length = currentShip.getLength();

      // Verificar si el barco puede ser colocado en la posición actual
      if (canPlaceShip(row, col, length)) {
        placeShip(row, col, length);
        currentShipIndex++;
      }
      /*
       * Actualizar la matriz lógica correspondiente
       * if (buttons == playerButtons) {
       * playerBoardMatrix[row][col] = 1; // 1 representa un golpe
       * } else if (buttons == enemyButtons) {
       * enemyBoardMatrix[row][col] = 1; // 1 representa un golpe
       * }
       */
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'mouseClicked'");
    }

    @Override
    public void mousePressed(MouseEvent e) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'mousePressed'");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'mouseReleased'");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      // TODO si entra el mouse se muestran directamente los barcos con su
      // medida(5,4,3,3,2)
      if (currentShipIndex >= ships.length) {
        return;
      }

      Ship currentShip = ships[currentShipIndex];
      int length = currentShip.getLength();

      if (canPlaceShip(row, col, length)) {
        highlightShip(row, col, length, true);
      }

    }

    @Override
    public void mouseExited(MouseEvent e) {
      JButton button = buttons[row][col];
      if (currentShipIndex >= ships.length) {
        // button.setBorder(UIManager.getBorder("Button.border"));
        return;
      }

      Ship currentShip = ships[currentShipIndex];
      int length = currentShip.getLength();

      if (canPlaceShip(row, col, length)) {
        highlightShip(row, col, length, false);
      }
    }

    private boolean canPlaceShip(int row, int col, int length) {
      // Verifica si el barco entraa en el tablero en horizontal sin toparse con otro
      // barco
      if (col + length > BOARD_SIZE)
        return false;
      for (int i = 0; i < length; i++) {
        if (playerBoardMatrix[row][col + i] != 0)
          return false;
      }
      return true;
    }

    private void highlightShip(int row, int col, int length, boolean highlight) {
      for (int i = 0; i < length; i++) {
        JButton button = buttons[row][col + i];
        button.setBorder(
            highlight ? BorderFactory.createLineBorder(Color.MAGENTA, 3, true) : UIManager.getBorder("Button.border"));
      }
    }

    private void placeShip(int row, int col, int length) {
      for (int i = 0; i < length; i++) {
        JButton button = buttons[row][col + i];
        button.setBackground(Color.BLACK);
        button.setEnabled(false);
        playerBoardMatrix[row][col + i] = 1; // Marcado como ocupado
      }
    }
  }
}
