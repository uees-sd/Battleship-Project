package Models;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import Views.Ship;
import Views.ClientView;

public class ClientModel {
  private ArrayList<Ship> ships = new ArrayList<>();
  private ArrayList<String> onlineUser = new ArrayList<>();
  private String playerName;
  private ObjectOutputStream out;
  private ObjectInputStream in;
  private Socket clientSocket;
  private ClientView clientView;

  // Create a new client model and the conecction to the server
  public void connect(String playerName, int serverPort) throws IOException {
    this.playerName = playerName;
    clientSocket = new Socket("localhost", serverPort);
    out = new ObjectOutputStream(clientSocket.getOutputStream());
    in = new ObjectInputStream(clientSocket.getInputStream());
    out.writeObject(playerName);
  }

  // Send the attack to the server
  public void sendAttack(int x, int y) throws IOException {
    out.writeObject(new int[] { x, y });
  }

  // Receive the attack from the server
  public void receiveAttack() throws IOException, ClassNotFoundException {
    // int[] attack = (int[]) in.readObject();
    System.out.println("Attack received: " + attack[0] + " " + attack[1]);
  }

  // Verifiy if I don't have ships alive
  /*
   * public void destroyedShips() {
   * for (Ship ship : ships) {
   * if (ship.getShipStatus() == Ship.ShipStatus.ALIVE) {
   * break;
   * }
   * }
   * }
   */

  // Listen for messages from the server
  public void listenForMessages() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          try {
            receiveAttack();
          } catch (IOException e) {
            e.printStackTrace();
          } catch (ClassNotFoundException e) {
            e.printStackTrace();
          }
        }
      }
    }).start();
  }

  // Disconnect the client from the server
  public void disconnect() {
    try {
      if (clientSocket.isConnected()) {
        clientSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Obersver pattern to update the view
  public void addObserver(ClientView clientView) {
    this.clientView = clientView;
  }

}
