package com.example.Views;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Board extends JPanel {
  private JLabel boardTitle;
  public Cell[][] cells;
  public LogicBoard logicBoard;

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

  public String getBoardTitle() {
    return boardTitle.getText();
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
