package Controllers.Comunicacion;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFrame;
import Models.ClientModel;
import Views.ClientView;

//sender
public class ClientController {
  private JFrame frame;
  private ClientModel clientModel;
  private ClientView clientView;
  private String playerName;
  private int port;
  private Boolean serverListening;

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
          // Iniciado el servidor, se conecta el cliente
          connectToServer();
        } else {
          clientView.showError("Ingrese su nombre");
        }
        break;
      case 2:
        if (isValidPlayerName(playerName) && isValidPort()) {
          port = Integer.parseInt(clientView.getServerPort());
          connectToServer();
        } else {
          clientView.showError("Ingrese su nombre y el Puerto válido (cuatro dígitos)");
        }
        break;
    }
  }

  private boolean isValidPlayerName(String playerName) {
    return playerName != null && !playerName.isEmpty();
  }

  private void connectToServer() {
    try {
      clientModel.connect(playerName, port);
      clientModel.listenForMessages();
      clientView.showBoard();
      frame.setTitle("BattleShip Educativo - Inicio de Partida");
      frame.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
          clientModel.disconnect();
        }
      });
    } catch (IOException e) {
      clientView.showError("Error al conectar con el servidor");
      e.printStackTrace();
    }
    System.out.println("- - - connected - - -");
    // start();
    // startListening();
  }

  private boolean isValidPort() {
    int serverPort = Integer.parseInt(clientView.getServerPort());
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

  public void startListening() {
    Thread listenerThread = new Thread(new Runnable() {
      @Override
      public void run() {
        while (serverListening) {
          // receiveData();

        }
      }
    });
    listenerThread.start();
  }

  // Generate a random port where create the Game
  private void randomPort() {
    Random random = new Random();
    this.port = random.nextInt(6000) + 3001;
  }

  /*
   * @Override
   * public Ship receiveData() {
   * try {
   *
   * } catch (IOException | ClassNotFoundException e) {
   * e.printStackTrace();
   * return null;
   * }
   * }
   * 
   * @Override
   * public DatagramPacket sendData(Ship content) {
   * 
   * }
   * 
   * }
   * 
   * @Override
   * public boolean getAttackComponent() {
   * return attackComponent;
   * }
   * 
   * public void setServerListening(boolean open) {
   * this.serverListening = open;
   * }
   * 
   * @Override
   * public String getPort() {
   * return "" + this.host + ":" + this.port;
   * }
   * 
   * @Override
   * public void start() {
   * this.serverListening = true;
   * }
   * 
   * @Override
   * public void end() {
   * this.serverListening = false;
   * 
   * }
   * 
   * @Override
   * public boolean getServerListening() {
   * return this.serverListening;
   * }
   * 
   * public Board getBoard() {
   * return board;
   * }
   */
  public static void main(String[] args) {
    new ClientController();
  }
}
