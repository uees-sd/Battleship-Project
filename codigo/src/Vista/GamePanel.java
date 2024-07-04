package Vista;

import javax.swing.*;
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
    private Ship[] ships = new Ship[] { new Ship(5), new Ship(4), new Ship(3), new Ship(3), new Ship(2) };
    private int currentShipIndex = 0;

    public GamePanel(String playerName) {
        this.playerName = playerName;
        setLayout(new GridLayout(2, 1, 10, 10));
        setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(20, 60, 20, 60), new LineBorder(Color.BLACK, 2)));

        attackBoard = createBoardPanel("Enemy", enemyButtons);
        playerBoard = createBoardPanel(playerName, playerButtons);

        add(attackBoard);
        add(playerBoard);

        setVisible(true);
    }

    private JPanel createBoardPanel(String title, JButton[][] buttons) {
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new BorderLayout());
        boardPanel.setBorder(new LineBorder(Color.cyan, 2));

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
                    button.addActionListener(new ButtonClickListener(row - 1, col - 1, buttons));
                    button.addMouseListener(new ButtonClickListener(row - 1, col - 1, buttons));
                    buttons[row - 1][col - 1] = button;
                    gridPanel.add(button);
                }
            }
        }

        boardPanel.add(gridPanel, BorderLayout.CENTER);
        JLabel boardTitle = new JLabel(title, SwingConstants.CENTER);
        boardPanel.add(boardTitle, BorderLayout.NORTH);

        return boardPanel;
    }

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
            // Asegurarse de que los barcos solo se coloquen en el tablero del jugador
            if (buttons == playerButtons) {
                JButton button = buttons[row][col];
                button.setEnabled(false);
                button.setBackground(Color.BLACK);

                if (currentShipIndex < ships.length) {
                    Ship currentShip = ships[currentShipIndex];
                    int length = currentShip.getLength();

                    if (canPlaceShip(row, col, length, playerBoardMatrix)) {
                        placeShip(row, col, length, playerBoardMatrix);
                        currentShipIndex++;
                    }

                    if (currentShipIndex >= ships.length) {
                        // Una vez que se hayan colocado todos los barcos, mostrar la ventana de preguntas
                        if (Preguntas.askQuestion((JFrame) SwingUtilities.getWindowAncestor(GamePanel.this))) {
                            // Mensaje de éxito
                            JOptionPane.showMessageDialog(GamePanel.this, 
                                "Pregunta respondida correctamente, ahora puedes atacar al enemigo.", 
                                "Correcto", JOptionPane.INFORMATION_MESSAGE);
                            // Aquí se puede añadir la lógica para atacar al enemigo
                        } else {
                            // Mensaje de error
                            JOptionPane.showMessageDialog(GamePanel.this, 
                                "Respuesta incorrecta, no puedes atacar al enemigo.", 
                                "Incorrecto", JOptionPane.ERROR_MESSAGE);
                        }
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
            // Asegurarse de que el resaltado del barco solo ocurra en el tablero del jugador
            if (buttons == playerButtons && currentShipIndex < ships.length) {
                Ship currentShip = ships[currentShipIndex];
                int length = currentShip.getLength();

                if (canPlaceShip(row, col, length, playerBoardMatrix)) {
                    highlightShip(row, col, length, true, playerBoardMatrix);
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // Asegurarse de que el resaltado del barco solo ocurra en el tablero del jugador
            if (buttons == playerButtons && currentShipIndex < ships.length) {
                Ship currentShip = ships[currentShipIndex];
                int length = currentShip.getLength();

                if (canPlaceShip(row, col, length, playerBoardMatrix)) {
                    highlightShip(row, col, length, false, playerBoardMatrix);
                }
            }
        }

        // Modificar el método canPlaceShip para trabajar con el tablero del jugador
        private boolean canPlaceShip(int row, int col, int length, int[][] boardMatrix) {
            if (col + length > BOARD_SIZE)
                return false;
            for (int i = 0; i < length; i++) {
                if (boardMatrix[row][col + i] != 0)
                    return false;
            }
            return true;
        }

        // Modificar el método highlightShip para trabajar con el tablero del jugador
        private void highlightShip(int row, int col, int length, boolean highlight, int[][] boardMatrix) {
            for (int i = 0; i < length; i++) {
                JButton button = buttons[row][col + i];
                button.setBorder(
                        highlight ? BorderFactory.createLineBorder(Color.MAGENTA, 3, true) : UIManager.getBorder("Button.border"));
            }
        }

        // Modificar el método placeShip para trabajar con el tablero del jugador
        private void placeShip(int row, int col, int length, int[][] boardMatrix) {
            for (int i = 0; i < length; i++) {
                JButton button = buttons[row][col + i];
                button.setBackground(Color.BLACK);
                button.setEnabled(false);
                boardMatrix[row][col + i] = 1;
            }
        }
    }
}
