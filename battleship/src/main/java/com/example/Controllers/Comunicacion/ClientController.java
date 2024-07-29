package com.example.Controllers.Comunicacion;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import com.example.Models.ClientModel;
import com.example.Views.Board;
import com.example.Views.ClientView;
import com.example.Views.PointXY;
import com.example.Views.Ship;
import com.example.Views.Board.Cell;

//sender
public class ClientController {
  private JFrame frame;
  private ClientModel clientModel;
  private ClientView clientView;
  private String playerName;
  private int port;

  public ClientController() {
    this.clientModel = new ClientModel();
    this.clientView = new ClientView(clientModel);

    // add the clientView as observer to the Client model
    clientModel.addObserver(clientView);
    // Add action listeners
    clientView.getCreateGame().addActionListener(e -> initGame(1));
    clientView.getJoinGame().addActionListener(e -> initGame(2));
    clientView.getPorTextField().addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(java.awt.event.FocusEvent evt) {
        clientView.getPorTextField().setText("");
      }
    });
    SwingUtilities.invokeLater(() -> {
      frame = new JFrame();
      frame.add(clientView.getClientPanel());
      frame.setTitle("BattleShip Educativo");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setResizable(false);
      frame.setBackground(new Color(238, 255, 255));
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
      System.out.println("- - - Muestra main panel - - -");
    });
  }

  // Create a new game and connect to the server
  private void initGame(int op) {
    playerName = clientView.getPlayerName();
    switch (op) {
      case 1:
        if (isValidPlayerName(playerName)) {
          randomPort();
          new ServerController(port);
          connectToServer("localhost");
        } else {
          clientView.showError("Ingrese su nombre");
        }
        break;
      case 2:
        if (isValidPlayerName(playerName) && isValidPort()) {
          System.out.println();
          String[] parts = clientView.getServerPort().split(":");
          String serverIp = parts[0];
          port = Integer.parseInt(parts[1]);
          connectToServer(serverIp);
        } else {
          clientView.showError("Ingrese su nombre y el Puerto válido (cuatro dígitos)");
        }
        break;
    }
  }

  private boolean isValidPlayerName(String playerName) {
    return playerName != null && !playerName.isEmpty();
  }

  private void connectToServer(String serverIp) {
    try {
      clientModel.connect(playerName, port, serverIp);
      clientModel.listenForMessages();
      SwingUtilities.invokeLater(() -> {
        clientView.showBoard();
        frame.setTitle("BattleShip Educativo - Inicio de Partida");
        frame.addWindowListener(new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            clientModel.disconnect();
          }
        });
      });

      // SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
      // @Override
      // protected Void doInBackground() {
      // // Wait for the second player to connect
      // while (clientModel.getOnlineUsers().size() != 2) {
      // try {
      // Thread.sleep(200); // Sleep for a short time to avoid busy-waiting
      // } catch (InterruptedException e) {
      // Thread.currentThread().interrupt();
      // }
      // }
      // // ddListenerBoard();
      // return null;
      // }
      // };

      // worker.execute();
      // addListenerBoard();
    } catch (IOException e) {
      String errorMessage = e.getMessage();
      if (errorMessage == null || errorMessage.contains("connection was aborted")) {
        clientView.showError("La sala está llena, no puedes unirte.");
      } else {
        clientView.showError("Error al conectar con el servidor");
        e.printStackTrace();
      }
    }
    System.out.println("- - - connected - - -");
  }

  private boolean isValidPort() {
    String[] parts = clientView.getServerPort().split(":");
    int serverPort = Integer.parseInt(parts[1]);
    try {
      if (serverPort >= 3001 && serverPort <= 8999) {
        System.out.println("port: " + serverPort);
        return true;
      } else {
        System.out.println("El puerto debe tener exactamente cuatro dígitos.");
        return false;
      }
    } catch (NumberFormatException e) {
      System.out.println("La cadena no representa un número válido.");
      return false;
    }
  }

  private void randomPort() {
    Random random = new Random();
    this.port = random.nextInt(6000) + 3001;
  }

  // private void addListenerBoard() {
  // Board board = clientModel.getBoard();
  // for (int i = 0; i < 10; i++) {
  // for (int j = 0; j < 10; j++) {
  // Cell cell = board.cells[i][j];
  // cell.addMouseListener(new ButtonClickListener(cell.coord));
  // System.out.println("Listener added to cell: " + i + ", " + j);
  // }
  // }
  // }

  // public class ButtonClickListener implements ActionListener, MouseListener {
  // Cell[][] cells = clientModel.getBoard().getCells();
  // private final int row;
  // private final int col;
  // private int currentShipIndex = clientModel.getCurrentShip();
  // private ArrayList<Ship> ships = clientModel.getShips();
  // private int size = ships.size();
  // private int[][] logicMatrix = clientModel.getLogicMatrix();

  // public ButtonClickListener(PointXY coord) {
  // this.row = coord.x;
  // this.col = coord.y;
  // }

  // @Override
  // public void actionPerformed(ActionEvent e) {
  // Cell cell = cells[row][col];
  // int currentShipIndex = clientModel.getCurrentShip();
  // cell.setBackground(Color.BLACK);

  // if (currentShipIndex < size) {
  // Ship currentShip = ships.get(currentShipIndex);
  // int length = currentShip.getLength();

  // if (canPlaceShip(row, col, length)) {
  // System.out.println("Ship placed at: " + row + ", " + col);
  // placeShip(row, col, length);
  // currentShipIndex++;
  // }

  // if (currentShipIndex >= size) {

  // }
  // }
  // }

  // @Override
  // public void mouseEntered(java.awt.event.MouseEvent e) {
  // if (currentShipIndex < size) {
  // Ship currentShip = ships.get(currentShipIndex);
  // int length = currentShip.getLength();

  // if (canPlaceShip(row, col, length)) {
  // SwingUtilities.invokeLater(() -> highlightShip(row, col, length, true));
  // }
  // }
  // }

  // @Override
  // public void mouseExited(java.awt.event.MouseEvent e) {
  // if (currentShipIndex < size) {
  // Ship currentShip = ships.get(currentShipIndex);
  // int length = currentShip.getLength();

  // if (canPlaceShip(row, col, length)) {
  // SwingUtilities.invokeLater(() -> highlightShip(row, col, length, false));
  // }
  // }
  // }

  // private boolean canPlaceShip(int row, int col, int length) {
  // if (col + length > 10)
  // return false;
  // for (int i = 0; i < length; i++) {
  // if (logicMatrix[row][col + i] != 0)
  // return false;
  // }
  // return true;
  // }

  // private void highlightShip(int row, int col, int length, boolean highlight) {
  // for (int i = 0; i < length; i++) {
  // Cell cell = cells[row][col + i];
  // SwingUtilities.invokeLater(() -> cell.setBorder(
  // highlight ? BorderFactory.createLineBorder(Color.MAGENTA, 3, true) :
  // UIManager.getBorder("Button.border")));

  // }
  // }

  // private void placeShip(int row, int col, int length) {
  // for (int i = 0; i < length; i++) {
  // Cell cell = cells[row][col + i];
  // cell.setBackground(Color.BLACK);
  // cell.setEnabled(false);
  // logicMatrix[row][col + i] = 1;
  // }
  // }

  // @Override
  // public void mouseClicked(java.awt.event.MouseEvent e) {
  // }

  // @Override
  // public void mousePressed(java.awt.event.MouseEvent e) {
  // }

  // @Override
  // public void mouseReleased(java.awt.event.MouseEvent e) {
  // }
  // }
}
