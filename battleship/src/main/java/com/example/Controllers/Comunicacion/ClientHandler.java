package com.example.Controllers.Comunicacion;

import java.io.ObjectOutputStream;
import java.util.ArrayList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.example.Models.ServerModel;
import com.example.Views.Board;
import com.example.Views.LogicBoard;
import com.example.Views.PointXY;

public class ClientHandler implements Runnable {

  private ObjectInputStream objectInputStream;
  private ObjectOutputStream objectOutputStream;
  private Socket clientSocket;
  private String playerName;
  private LogicBoard logicBoard = new LogicBoard();
  private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
  private static ArrayList<Board> boardUsers = new ArrayList<>();
  private static ArrayList<String> sunkShipsText = new ArrayList<>();
  private ServerModel serverModel;
  private static int count = 0;
  private Boolean running = true;

  public ClientHandler(Socket clientSocket, ServerModel serverModel) throws IOException, ClassNotFoundException {
    this.clientSocket = clientSocket;
    this.serverModel = serverModel;
    clientHandlers.add(this);

    this.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
    this.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

    this.playerName = (String) objectInputStream.readObject();

    serverModel.addOnlineUser(playerName);
    boardUsers.add((Board) objectInputStream.readObject());
    sunkShipsText.add("<b>Barcos Hundidos de " + playerName + ": </b> 0 <br>");
    if (clientHandlers.size() == 2) {
      broadCastUsers(serverModel.getOnlineUsers());
      broadCastBoards();
    }

  }

  private void broadCastUsers(ArrayList<String> onlineUsers) throws IOException {
    for (ClientHandler clientHandler : clientHandlers) {
      clientHandler.sendUsers(onlineUsers);
    }
  }

  private void broadCastBoards() throws IOException {
    for (ClientHandler clientHandler : clientHandlers) {
      clientHandler.sendBoards(boardUsers);
    }
  }

  private void broadCastLogicBoards() throws IOException {
    for (ClientHandler clientHandler : clientHandlers) {
      if (clientHandler != this) {
        clientHandler.sendLogicBoard(logicBoard);
      }
    }
  }

  private void sendUsers(ArrayList<String> onlineUsers) throws IOException {
    objectOutputStream.writeObject(onlineUsers);
    objectOutputStream.reset();
  }

  private void sendBoards(ArrayList<Board> boards) throws IOException {
    objectOutputStream.writeObject(boards);
    objectOutputStream.reset();
  }

  private void sendLogicBoard(LogicBoard logicBoard) throws IOException {
    objectOutputStream.writeObject(logicBoard);
    objectOutputStream.reset();
  }

  private void sendSunkShips(ArrayList<String> message) throws IOException {
    objectOutputStream.writeObject(message);
    objectOutputStream.reset();
  }

  private void closeAllConnections() {
    for (ClientHandler clientHandler : clientHandlers) {
      clientHandler.closeConnection();
    }
  }

  @SuppressWarnings("unchecked")
  private void broadCastMessage(Object message) throws IOException {
    if (message instanceof LogicBoard) {
      changeLogicBoard((LogicBoard) message);
      if (count == 0) {
        sendMessage("En espera del otro jugador");
        broadCastLogicBoards();
        count++;
      } else if (count == 1) {
        broadCastMessage(sunkShipsText);
        broadCastLogicBoards();
        broadCastMessage("Comienza el juego");
      }
      return;
    }
    for (ClientHandler clientHandler : clientHandlers) {
      if (message instanceof String) {
        String obj = (String) message;
        if (obj.equals("CHANGE_TURN") || obj.equals("Comienza el juego") || obj.contains("ha ganado")
            || obj.contains("ha abandonado")) {
          clientHandler.sendMessage(obj);
        } else if (obj.toString().contains("Barcos Hundidos de")) {
          sunkShipsText.set(serverModel.getOnlineUsers().indexOf(playerName), obj);
          broadCastMessage(sunkShipsText);
        }
      } else if (message instanceof PointXY && clientHandler != this) {
        clientHandler.sendAttack((PointXY) message);
        return;
      } else if (message instanceof ArrayList) {
        clientHandler.sendSunkShips((ArrayList<String>) message);
        return;
      }
    }
    if (message instanceof String && (((String) message).contains("ha ganado"))
        || ((String) message).contains("ha abandonado")) {
      running = false;
      closeAllConnections(); // Cierra todas las conexiones después de enviar el mensaje
    }
  }

  public void sendAttack(PointXY pointXY) throws IOException {
    objectOutputStream.writeObject(pointXY);
    objectOutputStream.reset();
  }

  public void sendMessage(String message) throws IOException {
    objectOutputStream.writeObject(message);
    objectOutputStream.reset();
  }

  public void readMessage() throws IOException, ClassNotFoundException {
    while (running) {
      Object object = objectInputStream.readObject();
      broadCastMessage(object);
    }
  }

  public void changeLogicBoard(LogicBoard logicBoard) {
    this.logicBoard = logicBoard;
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
      if (objectOutputStream != null)
        objectOutputStream.close();
      if (objectInputStream != null)
        objectInputStream.close();
      if (clientSocket != null && !clientSocket.isClosed()) {
        clientSocket.close();
      }
      clientHandlers.remove(this);
      serverModel.getOnlineUsers().remove(playerName);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
