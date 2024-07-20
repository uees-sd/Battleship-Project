package Controllers.Comunicacion;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import Models.ClientModel;
import Views.ClientView;

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

    // Show the main panel
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

  public static void main(String[] args) {
    new ClientController();
  }
}
