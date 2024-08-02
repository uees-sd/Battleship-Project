package com.example.Controllers.Comunicacion;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import com.example.Models.ServerModel;
import com.example.Views.Board;
import com.example.Views.LogicBoard;

public class ClientHandler implements Runnable {

  private ObjectInputStream objectInputStream;
  private ObjectOutputStream objectOutputStream;
  private Socket clientSocket;
  private String playerName;
  private LogicBoard logicBoard = new LogicBoard();
  private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
  private static ArrayList<Board> boardUsers = new ArrayList<>();
  private ServerModel serverModel;
  private static int count = 0;

  public ClientHandler(Socket clientSocket, ServerModel serverModel) throws IOException, ClassNotFoundException {
    this.clientSocket = clientSocket;
    this.serverModel = serverModel;
    clientHandlers.add(this);

    this.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
    this.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

    this.playerName = (String) objectInputStream.readObject();

    serverModel.addOnlineUser(playerName);
    boardUsers.add((Board) objectInputStream.readObject());

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

  private void broadCastMessage(Object message) throws IOException {
    if (message instanceof LogicBoard) {
      changeLogicBoard((LogicBoard) message);
      if (count == 0) {
        sendMessage("En espera del otro jugador");
        broadCastLogicBoards();
        count++;
      } else if (count == 1) {
        System.out.println(count);
        broadCastLogicBoards();
        System.out.println("Se enviaron los tableros");
        broadCastMessage("Comienza el juego");
      }
    }
    for (ClientHandler clientHandler : clientHandlers) {
      if (message instanceof String) {
        clientHandler.sendMessage((String) message);
      }
    }
  }

  public void sendMessage(String message) throws IOException {
    objectOutputStream.writeObject(message);
    objectOutputStream.reset();
  }

  public void readMessage() throws IOException, ClassNotFoundException {
    while (true) {
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
