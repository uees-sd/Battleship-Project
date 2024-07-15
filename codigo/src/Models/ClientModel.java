package Models;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Views.Ship;
import Views.ClientView;
import Views.Board;

@SuppressWarnings("unused")

public class ClientModel {
  private ArrayList<Ship> ships = new ArrayList<>();
  private ArrayList<String> onlineUsers = new ArrayList<>();
  private ArrayList<Board> boards = new ArrayList<>();
  private String serverPort;
  private String playerName;
  private ObjectOutputStream out;
  private ObjectInputStream in;
  private Socket clientSocket;
  private Board playerBoard = new Board();
  private ClientView clientView;
  private int flag = 0;

  // Create a new client model and the conecction to the server
  public void connect(String playerName, int serverPort, String serverIp) throws IOException {
    this.playerName = playerName;
    this.serverPort = String.valueOf(serverPort);
    playerBoard.setBoardTitle(playerName);
    clientSocket = new Socket(serverIp, serverPort);
    out = new ObjectOutputStream(clientSocket.getOutputStream());
    in = new ObjectInputStream(clientSocket.getInputStream());
    sendName(playerName);
    SwingUtilities.invokeLater(() -> clientView.updateInfoPort());
    sendBoards(playerBoard);
  }

  // Send the attack to the server
  public void sendAttack(int x, int y) throws IOException {
    out.writeObject(new int[] { x, y });
  }

  public void sendName(String message) throws IOException {
    out.writeObject(message);
  }

  public void sendBoards(JPanel playerBoard) throws IOException {
    out.writeObject(playerBoard);
  }

  @SuppressWarnings("unchecked")
  public void receiveMessage() throws IOException, ClassNotFoundException {
    if (this.flag == 0) {
      onlineUsers = (ArrayList<String>) in.readObject();
      SwingUtilities.invokeLater(() -> clientView.updateInfoUsers());
      this.flag = 1;
    } else if (this.flag == 1) {
      boards = (ArrayList<Board>) in.readObject();
      SwingUtilities.invokeLater(() -> clientView.updateBoards());
      this.flag = 2;
    }
  }

  // Listen for messages from the server
  public void listenForMessages() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          try {
            receiveMessage();
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

  public JPanel getBoard() {
    return playerBoard;
  }

  public String getPort() {
    return serverPort;
  }

  public ArrayList<String> getOnlineUsers() {
    return onlineUsers;
  }

  public ArrayList<Board> getBoards() {
    return boards;
  }
}
