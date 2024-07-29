package com.example.Views;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.*;

public class Board extends JPanel {
  private JLabel boardTitle;
  public Cell[][] cells;
  public LogicBoard logicBoard;
  private int currentShipIndex = 0;
  private int length = 5;

  public Board(LogicBoard logicBoard) {
    this.logicBoard = logicBoard;
    cells = new Cell[10][10];

    setLayout(new BorderLayout());
    setBorder(new LineBorder(Color.cyan, 2));

    JPanel gridPanel = new JPanel();
    gridPanel.setLayout(new GridLayout(11, 11));

    for (int row = 0; row < 11; row++) {
      for (int col = 0; col < 11; col++) {
        if (row == 0 && col == 0) {
          gridPanel.add(new JLabel(""));
        } else if (row == 0) {
          gridPanel.add(new Cell(null, new JLabel(String.valueOf(col), SwingConstants.CENTER)));
        } else if (col == 0) {
          gridPanel.add(new Cell(null, new JLabel(String.valueOf(row), SwingConstants.CENTER)));
        } else {
          cells[row - 1][col - 1] = new Cell(new PointXY(row - 1, col - 1), null);
          gridPanel.add(cells[row - 1][col - 1]);
        }
      }
    }

    add(gridPanel, BorderLayout.CENTER);
    boardTitle = new JLabel("", SwingConstants.CENTER);
    add(boardTitle, BorderLayout.NORTH);
  }

  public Cell[][] getCells() {
    return cells;
  }

  public void setBoardTitle(String title) {
    boardTitle.setText(title);
  }

  public class Cell extends JPanel {
    public PointXY coord; // Todas las casillas tendrán coordenadas
    public Border originalBorder;

    public Cell(PointXY coord, JLabel numCell) {
      this.coord = coord;
      originalBorder = BorderFactory.createLineBorder(Color.BLACK, 4, true);
      setPreferredSize(new Dimension(30, 30));
      setBorder(originalBorder);

      if (numCell != null) {
        numCell.setFont(new Font("Verdana", Font.BOLD, 12));
        numCell.setForeground(Color.WHITE);
        add(numCell);
        setBackground(new Color(120, 118, 118));
      } else {
        setBackground(Color.WHITE);
      }
    }

    public void highlight(boolean highlight) {
      setBorder(highlight ? BorderFactory.createLineBorder(Color.MAGENTA, 3, true) : originalBorder);
    }
  }

  public void addListenerBoard() {
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        Cell cell = cells[i][j];
        cell.addMouseListener(new ButtonClickListener(cell.coord));
        System.out.println("Listener added to cell: " + i + ", " + j);
      }
    }
  }

  public class ButtonClickListener implements ActionListener, MouseListener {
    private final int row;
    private final int col;
    private int size = 5;
    private int[][] logicMatrix = logicBoard.logicMatrix;

    public ButtonClickListener(PointXY coord) {
      this.row = coord.x;
      this.col = coord.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
      if (currentShipIndex < size) {

        if (canPlaceShip(row, col, length)) {
          highlightShip(row, col, length, true);
        }
      }
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
      if (currentShipIndex < size) {
        if (canPlaceShip(row, col, length)) {
          highlightShip(row, col, length, false);
        }
      }
    }

    private boolean canPlaceShip(int row, int col, int length) {
      if (col + length > 10)
        return false;
      for (int i = 0; i < length; i++) {
        if (logicMatrix[row][col + i] != 0)
          return false;
      }
      return true;
    }

    private void highlightShip(int row, int col, int length, boolean highlight) {
      for (int i = 0; i < length; i++) {
        Cell cell = cells[row][col + i];
        cell.highlight(highlight);
        // cell.setBorder(
        // highlight ? BorderFactory.createLineBorder(Color.MAGENTA, 3, true) :
        // UIManager.getBorder("Panel.border"));

      }
    }

    private void placeShip(int row, int col, int length) {
      Ship ship = new Ship(length);
      for (int i = 0; i < length; i++) {
        Cell cell = cells[row][col + i];
        cell.setBackground(Color.BLACK);
        cell.setEnabled(false);
        ship.addCoords(cell.coord);
      }
      logicBoard.addShip(ship);
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
      if (currentShipIndex < size) {
        if (canPlaceShip(row, col, length)) {
          System.out.println("Ship placed at: " + row + ", " + col);
          placeShip(row, col, length);
          currentShipIndex++;
          length--;
        }

        if (currentShipIndex >= size) {

        }
      }
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
    }

  }

  /*
   * Elimina los MouseListener de cada Cell.<br>
   * Esto sirve para cuando queremos que el usuario
   * no pueda clickar en las Cells, por ejemplo
   * cuando ya ha colocado todos los barcos
   */

  public void desactivarListener() {
    for (int i = 0; i < 10; i++)
      for (int j = 0; j < 10; j++)
        cells[i][j].removeMouseListener(cells[i][j].getMouseListeners()[0]);
  }
}
