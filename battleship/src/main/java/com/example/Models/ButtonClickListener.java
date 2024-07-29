// package com.example.Models;

// import java.awt.event.ActionListener;
// import java.awt.event.MouseListener;

// public class ButtonClickListener implements ActionListener, MouseListener {
// private final int row;
// private final int col;

// public ButtonClickListener(int row, int col) {
// this.row = row;
// this.col = col;
// }

// @Override
// public void actionPerformed(ActionEvent e) {
// JButton button = buttons[row][col];
// button.setEnabled(false);
// button.setBackground(Color.BLACK);

// if (currentShipIndex < ships.length) {
// Ship currentShip = ships[currentShipIndex];
// int length = currentShip.getLength();

// if (canPlaceShip(row, col, length)) {
// placeShip(row, col, length);
// currentShipIndex++;
// }

// if (currentShipIndex >= ships.length) {
// // Una vez que se hayan colocado todos los barcos, mostrar la ventana de
// // preguntas
// if (Preguntas.askQuestion((JFrame)
// SwingUtilities.getWindowAncestor(Board.this))) {
// // Mensaje de éxito
// JOptionPane.showMessageDialog(Board.this,
// "Pregunta respondida correctamente, ahora puedes atacar al enemigo.",
// "Correcto", JOptionPane.INFORMATION_MESSAGE);
// // Aquí se puede añadir la lógica para atacar al enemigo
// } else {
// // Mensaje de error
// JOptionPane.showMessageDialog(Board.this,
// "Respuesta incorrecta, no puedes atacar al enemigo.",
// "Incorrecto", JOptionPane.ERROR_MESSAGE);
// }
// }
// }
// }

// @Override
// public void mouseClicked(MouseEvent e) {
// // No implementado
// }

// @Override
// public void mousePressed(MouseEvent e) {
// // No implementado
// }

// @Override
// public void mouseReleased(MouseEvent e) {
// // No implementado
// }

// @Override
// public void mouseEntered(MouseEvent e) {
// if (currentShipIndex < ships.length) {
// Ship currentShip = ships[currentShipIndex];
// int length = currentShip.getLength();

// if (canPlaceShip(row, col, length)) {
// highlightShip(row, col, length, true);
// }
// }
// }

// @Override
// public void mouseExited(MouseEvent e) {
// if (currentShipIndex < ships.length) {
// Ship currentShip = ships[currentShipIndex];
// int length = currentShip.getLength();

// if (canPlaceShip(row, col, length)) {
// highlightShip(row, col, length, false);
// }
// }
// }

// private boolean canPlaceShip(int row, int col, int length) {
// if (col + length > BOARD_SIZE)
// return false;
// for (int i = 0; i < length; i++) {
// if (boardMatrix[row][col + i] != 0)
// return false;
// }
// return true;
// }

// private void highlightShip(int row, int col, int length, boolean highlight) {
// for (int i = 0; i < length; i++) {
// JButton button = buttons[row][col + i];
// button.setBorder(
// highlight ? BorderFactory.createLineBorder(Color.MAGENTA, 3, true) :
// UIManager.getBorder("Button.border"));
// }
// }

// private void placeShip(int row, int col, int length) {
// for (int i = 0; i < length; i++) {
// JButton button = buttons[row][col + i];
// button.setBackground(Color.BLACK);
// button.setEnabled(false);
// boardMatrix[row][col + i] = 1;
// }
// }
// }