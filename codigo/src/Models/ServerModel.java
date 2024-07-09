package Models;

import java.util.ArrayList;

import Controllers.Comunicacion.ClientHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import Views.ServerView;

public class ServerModel {
  private ArrayList<String> onlineUsers = new ArrayList<>();
  private ArrayList<String> shipsDestroyed = new ArrayList<>();
  private ServerSocket serverSocket;
  private ServerView serverView;

  public void startServer(int port) throws IOException, ClassNotFoundException {
    serverSocket = new ServerSocket(port);

    while (true && onlineUsers.size() < 3) {
      Socket clientSocket = serverSocket.accept();
      ClientHandler clientHandler = new ClientHandler(clientSocket, this);
      Thread thread = new Thread(clientHandler);
      thread.start();
    }
  }

  public void addOnlineUser(String playerName) {
    onlineUsers.add(playerName + "\n");
    serverView.updateUsers();
  }

  public void addShipDestroyed() {
  }

  public ArrayList<String> getOnlineUsers() {
    return onlineUsers;
  }

  public void addObserver(ServerView serverView) {
    this.serverView = serverView;
  }
}
