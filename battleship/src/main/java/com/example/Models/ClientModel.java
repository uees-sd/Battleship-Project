package com.example.Models;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import com.example.Views.Ship;
import com.example.Views.ClientView;
import com.example.Views.LogicBoard;
import com.example.Interfaces.ModelObserver;
import com.example.Views.Board;
import com.example.Controllers.Preguntas.QuestionManager;

public class ClientModel {
  private ArrayList<String> onlineUsers = new ArrayList<>();
  private ArrayList<Board> boards = new ArrayList<>();
  private ArrayList<ModelObserver> observers = new ArrayList<>();
  private String serverPort;
  private String playerName;
  private ObjectOutputStream out;
  private ObjectInputStream in;
  private Socket clientSocket;
  private LogicBoard logicBoard = new LogicBoard();
  private Board playerBoard = new Board(logicBoard), enemyBoard;
  private ClientView clientView;
  private int currentShipIndex = 0;
  private Status status = Status.WAITPLAYER;
  private Boolean turn;
  private int attackCount;
  private QuestionManager questionManager = new QuestionManager();
  private String[] currentQuestion;
  public int barcosHundidos = 0;
  private LogicBoard enemyLogicBoard;

  public enum Status {
    WAITPLAYER, WAITBOARD, WAITSHIPS, PLAYING, FINISHED
  }

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
    sendBoard();
  }

  // Send the attack to the server
  public void sendAttack(int x, int y) throws IOException {
    out.writeObject(new int[] { x, y });
  }

  public void sendName(String message) throws IOException {
    out.writeObject(message);
  }

  public void sendBoard() throws IOException {
    out.writeObject(playerBoard);
  }

  public void sendShips() throws IOException {
    out.writeObject(logicBoard.logicMatrix);
  }

  public void sendLogicBoard() throws IOException {
    out.writeObject(logicBoard);
  }

  @SuppressWarnings("unchecked")
  public void receiveMessage() throws IOException, ClassNotFoundException {
    if (this.status == Status.WAITPLAYER) {
      onlineUsers = (ArrayList<String>) in.readObject();
      SwingUtilities.invokeLater(() -> clientView.updateInfoUsers());
      this.status = Status.WAITBOARD;

    } else if (this.status == Status.WAITBOARD) {
      boards = (ArrayList<Board>) in.readObject();
      this.status = Status.WAITSHIPS;
      notifyBoardObservers();

    } else if (this.status == Status.WAITSHIPS) {
      Object obj = in.readObject();
      if (obj instanceof String) {
        String message = (String) obj;
        clientView.setLblStatus(message);
        if (message.equals("Comienza el juego")) {
          this.status = Status.PLAYING;
          notifyBoardObservers();
          notifyChangeTurn();
        }
      } else if (obj instanceof LogicBoard) {
        enemyLogicBoard = (LogicBoard) obj;
        System.out.println("Tamaño Logic Board" + enemyLogicBoard.ships.size());
      }
    } else if (this.status == Status.PLAYING) {
      Object obj = in.readObject();
      if (obj instanceof String) {
        turn = !turn;
        clientView.setLblStatus(turn ? "Es tu turno..." : "Es el turno del oponente...");
        notifyChangeTurn();
      }
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

  // Resetear el número de ataques al cambiar de turno.
  public void resetAttackCount() {
    attackCount = 0;
  }

  // Incrementar Ataque
  public void incrementAttackCount() {
    attackCount++;
    if (attackCount == 3) {
      changeTurn();
    }
  }

  // Cambiar de turno
  public void changeTurn() {
    resetAttackCount();
    try {
      out.writeObject("CHANGE_TURN");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Hacer pregunta
  public void genQuestion() {
    currentQuestion = questionManager.getRandomQuestion();
  }

  // Verificar respuesta
  public void checkAnswer(int answerIndex) {
    Boolean correct = questionManager.checkQuestion(currentQuestion, answerIndex);
    if (!correct) {
      changeTurn();
    } else {
      clientView.setLblStatus("Respuesta correcta, sigue atacando...");
    }
  }

  // Obersver pattern to update the view
  public void addObserver(ClientView clientView) {
    this.clientView = clientView;
  }

  public void addBoardObserver(ModelObserver observer) {
    observers.add(observer);
  }

  public void removeBoardObserver(ModelObserver observer) {
    observers.remove(observer);
  }

  public LogicBoard getEnemyLogicBoard() {
    return enemyLogicBoard;
  }

  private void notifyBoardObservers() {
    for (ModelObserver observer : observers) {
      observer.boardUpdated();
    }
  }

  private void notifyChangeTurn() {
    for (ModelObserver observer : observers) {
      observer.turnChanged();
    }
  }

  public Board getBoard() {
    return playerBoard;
  }

  public Board getEnemyBoard() {
    return enemyBoard;
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

  public int getCurrentShip() {
    return currentShipIndex;
  }

  public void setCurrentShip(int currentShipIndex) {
    this.currentShipIndex = currentShipIndex;
  }

  public ArrayList<Ship> getShips() {
    return logicBoard.ships;
  }

  public int[][] getLogicMatrix() {
    return logicBoard.logicMatrix;
  }

  public int getPlayerIndex() {
    return onlineUsers.indexOf(playerName);
  }

  public void setPlayerBoard(Board playerBoard) {
    this.playerBoard = playerBoard;
  }

  public void setLogicBoard() {
    this.logicBoard = playerBoard.logicBoard;
  }

  public Status getStatus() {
    return this.status;
  }

  public void setTurn(Boolean turn) {
    this.turn = turn;
  }

  public Boolean isTurn() {
    return this.turn;
  }

  public String[] getQuestion() {
    return currentQuestion;
  }
}
