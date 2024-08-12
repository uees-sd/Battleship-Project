package com.example.Views;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Board extends JPanel {
    private JLabel boardTitle;
    public Cell[][] cells;
    public LogicBoard logicBoard;

    public Board(LogicBoard logicBoard) {
        this.logicBoard = logicBoard;
        cells = new Cell[10][10];

        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(11, 11));
        gridPanel.setOpaque(false); // Hacer el panel de la cuadrícula transparente

        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {
                if (row == 0 && col == 0) {
                    gridPanel.add(new JLabel(""));
                } else if (row == 0) {
                    gridPanel.add(new Cell(null, new JLabel(String.valueOf(col), SwingConstants.CENTER), true, Color.BLACK));
                } else if (col == 0) {
                    gridPanel.add(new Cell(null, new JLabel(String.valueOf(row), SwingConstants.CENTER), true, Color.BLACK));
                } else {
                    cells[row - 1][col - 1] = new Cell(new PointXY(row - 1, col - 1), null, false, new Color(135, 206, 235)); // Azul claro para las celdas normales
                    gridPanel.add(cells[row - 1][col - 1]);
                }
            }
        }

        add(gridPanel, BorderLayout.CENTER);
        boardTitle = new JLabel("", SwingConstants.CENTER);
        add(boardTitle, BorderLayout.NORTH);
        // Configurar el título del tablero
        setBoardTitle("Nombre del Jugador");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Crear un gradiente de color para el fondo
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(70, 130, 180), // Azul marino en la esquina superior izquierda
            0, getHeight(), new Color(70, 130, 180)  // Azul claro en la esquina inferior derecha
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Dibujar ondas para simular el movimiento del agua
        g2d.setColor(new Color(255, 255, 255, 100)); // Blanco semi-transparente para las olas
        g2d.setStroke(new BasicStroke(2));
        int waveHeight = 15;
        int waveLength = 40;

        for (int y = 0; y < getHeight(); y += waveHeight) {
            for (int x = 0; x < getWidth(); x += waveLength) {
                g2d.drawLine(x, y, x + waveLength / 2, y + waveHeight);
                g2d.drawLine(x + waveLength / 2, y + waveHeight, x + waveLength, y);
            }
        }

        // Agregar un efecto de texturización sutil
        g2d.setColor(new Color(0, 0, 128, 80)); // Azul oscuro con mayor opacidad para la textura
        for (int y = 0; y < getHeight(); y += 15) {
            for (int x = 0; x < getWidth(); x += 15) {
                g2d.fillOval(x, y, 8, 8); // Pequeños círculos para crear una textura de agua
            }
        }
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setBoardTitle(String title) {
        boardTitle.setText(title);
        boardTitle.setForeground(Color.WHITE); // Establecer el color del texto en blanco
        boardTitle.setFont(new Font("Arial", Font.BOLD, 20)); // Cambiar la fuente a Arial, negrita, tamaño 20
    }

    public String getBoardTitle() {
        return boardTitle.getText();
    }

    public class Cell extends JPanel {
        public PointXY coord; // Todas las casillas tendrán coordenadas
        public Border originalBorder;
        private boolean isSelected = false; // Estado de selección
        private boolean isHit = false; // Estado de ataque

        public Cell(PointXY coord, JLabel numCell, boolean isBordered, Color bgColor) {
            this.coord = coord;
            originalBorder = BorderFactory.createLineBorder(isBordered ? Color.BLACK : Color.BLACK, 4, true);
            setPreferredSize(new Dimension(30, 30));
            setBorder(originalBorder);
            setBackground(bgColor); // Color de fondo según el parámetro

            if (numCell != null) {
                numCell.setFont(new Font("Verdana", Font.BOLD, 12));
                numCell.setForeground(Color.WHITE);
                add(numCell);
            }

            setOpaque(true); // Asegurarse de que la celda es opaca para que el color sea visible
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
            updateBackground();
        }

        public void setHit(boolean hit) {
            isHit = hit;
            updateBackground();
        }

        private void updateBackground() {
            if (isHit) {
                setBackground(Color.RED); // Color cuando ha sido atacado
            } else if (isSelected) {
                setBackground(Color.BLUE); // Color cuando está seleccionado
            } else {
                setBackground(new Color(139, 69, 19)); // Café madera por defecto
            }
        }

        public void highlight(boolean highlight) {
            setBorder(highlight ? BorderFactory.createLineBorder(new Color(139, 69, 19), 3, true) // Café madera
                    : originalBorder);
        }
    }

    public void desactivarListener() {
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                cells[i][j].removeMouseListener(cells[i][j].getMouseListeners()[0]);
    }

    public void repaintCell(PointXY pointXY) {
        Cell cell = cells[pointXY.x][pointXY.y];
        if (logicBoard.logicMatrix[pointXY.x][pointXY.y] == 1) {
            cell.setHit(true); // Marca como atacado si el valor en la lógica es 1
        } else if (logicBoard.logicMatrix[pointXY.x][pointXY.y] == 0) {
            cell.setHit(false); // Marca como no atacado si el valor en la lógica es 0
            cell.setBackground(Color.BLUE); // Cambia el color a azul si no hubo impacto
        }
    }
    
}