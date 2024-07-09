package Views;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Board extends JPanel {
  private final int BOARD_SIZE = 10;
  private final JButton[][] buttons = new JButton[BOARD_SIZE][BOARD_SIZE];
  private final int[][] boardMatrix = new int[BOARD_SIZE][BOARD_SIZE];
  private Ship[] ships = new Ship[] { new Ship(5), new Ship(4), new Ship(3), new Ship(3), new Ship(2) };
  private int currentShipIndex = 0;

  public Board(String title) {
    setLayout(new BorderLayout());
    setBorder(new LineBorder(Color.cyan, 2));

    JPanel gridPanel = new JPanel();
    gridPanel.setLayout(new GridLayout(BOARD_SIZE + 1, BOARD_SIZE + 1));

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
          button.addActionListener(new ButtonClickListener(row - 1, col - 1));
          button.addMouseListener(new ButtonClickListener(row - 1, col - 1));
          buttons[row - 1][col - 1] = button;
          gridPanel.add(button);
        }
      }
    }

    add(gridPanel, BorderLayout.CENTER);
    JLabel boardTitle = new JLabel(title, SwingConstants.CENTER);
    add(boardTitle, BorderLayout.NORTH);
  }

  private class ButtonClickListener implements ActionListener, MouseListener {
    private final int row;
    private final int col;

    public ButtonClickListener(int row, int col) {
      this.row = row;
      this.col = col;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      JButton button = buttons[row][col];
      button.setEnabled(false);
      button.setBackground(Color.BLACK);

      if (currentShipIndex < ships.length) {
        Ship currentShip = ships[currentShipIndex];
        int length = currentShip.getLength();

        if (canPlaceShip(row, col, length)) {
          placeShip(row, col, length);
          currentShipIndex++;
        }

        if (currentShipIndex >= ships.length) {
          // Una vez que se hayan colocado todos los barcos, mostrar la ventana de
          // preguntas
          if (Preguntas.askQuestion((JFrame) SwingUtilities.getWindowAncestor(Board.this))) {
            // Mensaje de éxito
            JOptionPane.showMessageDialog(Board.this,
                "Pregunta respondida correctamente, ahora puedes atacar al enemigo.",
                "Correcto", JOptionPane.INFORMATION_MESSAGE);
            // Aquí se puede añadir la lógica para atacar al enemigo
          } else {
            // Mensaje de error
            JOptionPane.showMessageDialog(Board.this,
                "Respuesta incorrecta, no puedes atacar al enemigo.",
                "Incorrecto", JOptionPane.ERROR_MESSAGE);
          }
        }
      }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      // No implementado
    }

    @Override
    public void mousePressed(MouseEvent e) {
      // No implementado
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      // No implementado
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      if (currentShipIndex < ships.length) {
        Ship currentShip = ships[currentShipIndex];
        int length = currentShip.getLength();

        if (canPlaceShip(row, col, length)) {
          highlightShip(row, col, length, true);
        }
      }
    }

    @Override
    public void mouseExited(MouseEvent e) {
      if (currentShipIndex < ships.length) {
        Ship currentShip = ships[currentShipIndex];
        int length = currentShip.getLength();

        if (canPlaceShip(row, col, length)) {
          highlightShip(row, col, length, false);
        }
      }
    }

    private boolean canPlaceShip(int row, int col, int length) {
      if (col + length > BOARD_SIZE)
        return false;
      for (int i = 0; i < length; i++) {
        if (boardMatrix[row][col + i] != 0)
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
        boardMatrix[row][col + i] = 1;
      }
    }
  }
}
