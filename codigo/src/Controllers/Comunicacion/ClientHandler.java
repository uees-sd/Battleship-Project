package Controllers.Comunicacion;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import Models.ServerModel;

public class ClientHandler implements Runnable {

  private ObjectInputStream objectInputStream;
  private ObjectOutputStream objectOutputStream;
  private Socket clientSocket;
  private String playerName;
  private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
  private ServerModel serverModel;

  public ClientHandler(Socket clientSocket, ServerModel serverModel) throws IOException, ClassNotFoundException {
    this.clientSocket = clientSocket;
    this.serverModel = serverModel;
    clientHandlers.add(this);

    this.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
    this.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

    this.playerName = (String) objectInputStream.readObject();

    serverModel.addOnlineUser(playerName);
    // serverModel.addShipDestroyed();

    broadCastMessage(playerName + " se ha unido al juego");
    broadCastUsers(serverModel.getOnlineUsers());
  }

  private void broadCastUsers(ArrayList<String> onlineUsers) throws IOException {
    for (ClientHandler clientHandler : clientHandlers) {
      clientHandler.sendUsers(onlineUsers);
    }
  }

  private void sendUsers(ArrayList<String> onlineUsers) throws IOException {
    objectOutputStream.writeObject(onlineUsers);
    objectOutputStream.reset();
  }

  private void broadCastMessage(String message) throws IOException {
    for (ClientHandler clientHandler : clientHandlers) {
      clientHandler.sendMessage(message);
    }
  }

  public void sendMessage(String message) throws IOException {
    objectOutputStream.writeObject(message);
  }

  public void readMessage() throws IOException, ClassNotFoundException {
    while (true) {
      String message = (String) objectInputStream.readObject();
      broadCastMessage(message);
    }
  }

  @Override
  public void run() {
    try {
      readMessage();
    } catch (IOException | ClassNotFoundException e) {
      closeConnection();
      e.printStackTrace();
    }
  }

  private void closeConnection() {
    try {
      if (clientSocket.isConnected())
        clientSocket.close();
      clientHandlers.remove(this);
      serverModel.getOnlineUsers().remove(playerName);
      broadCastMessage(playerName + " se ha desconectado tú ganas!");
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
