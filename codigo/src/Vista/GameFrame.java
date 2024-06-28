package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;

public class GameFrame extends JFrame {
  JPanel playerBoard, attackBoard, mainPanel;
  String playerName;

  public GameFrame(String playerName) {
    // Frame del Juego, donde se visualizan los tableros de juego
    this.playerName = playerName;
    setTitle("BattleShip Educativo");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(600, 800);
    setLayout(new BorderLayout());
    setLocationRelativeTo(null);

    initComponents();
    add(mainPanel, BorderLayout.CENTER);
    addListeners();

    setVisible(true);
  }

  public void initComponents() {
    // Panel que contiene los tableros (Border Temporal para visualizarlo mejor)
    mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
    mainPanel.setBorder(BorderFactory.createCompoundBorder(
        new EmptyBorder(20, 60, 20, 60), new LineBorder(Color.BLACK, 2)));

    // Crear panales para tableros
    attackBoard = createBoardPanel("Enemy");
    playerBoard = createBoardPanel(playerName);

    // añadir los tableros al panel principal
    mainPanel.add(attackBoard);
    mainPanel.add(playerBoard);
  }

  private JPanel createBoardPanel(String Title) {
    // Crear el panel del tablero
    JPanel boardPanel = new JPanel();
    boardPanel.setLayout(new BorderLayout());
    boardPanel.setBorder(new LineBorder(Color.cyan, 2));

    // Crear el panel para la cuadrícula
    JPanel gridPanel = new JPanel();
    gridPanel.setLayout(new GridLayout(11, 11));

    // Llenar las casillas de los tableros
    for (int row = 0; row < 11; row++) {
      for (int col = 0; col < 11; col++) {
        if (row == 0 && col == 0) {
          gridPanel.add(new JLabel(""));
        } else if (row == 0) {
          gridPanel.add(new JLabel(String.valueOf(col)));
        } else if (col == 0) {
          gridPanel.add(new JLabel(String.valueOf(row)));
        } else {
          JButton button = new JButton();
          button.setBackground(getForeground());
          // button.addActionListener(new ButtonClickListener());
          gridPanel.add(new JButton());
        }
      }
    }

    // Añadir el panel de la cuadrícula al panel del tablero
    boardPanel.add(gridPanel, BorderLayout.CENTER);

    JLabel boardTitle = new JLabel(Title, SwingConstants.CENTER);
    boardPanel.add(boardTitle, BorderLayout.NORTH);

    return boardPanel;
  }

  private void addListeners() {
    // Agregar Listeners
  }
  /*
   * private class ButtonClickListener implements ActionListener {
   * 
   * @Override
   * public void actionPerformed(ActionEvent e) {
   * JButton button = (JButton) e.getSource();
   * System.out.println("Button clicked: " + button.getText() + "1");
   * button.setEnabled(false);
   * button.setBackground(Color.GRAY);
   * }
   * 
   * }
   */
}
