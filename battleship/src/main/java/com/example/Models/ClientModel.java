package com.example.Models;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import com.example.Views.Ship;
import com.example.Views.ClientView;
import com.example.Views.LogicBoard;
import com.example.Views.PointXY;
import com.example.Interfaces.ModelObserver;
import com.example.Views.Board;
import com.example.Controllers.Preguntas.QuestionManager;

public class ClientModel {
  private ArrayList<String> onlineUsers = new ArrayList<>();
  private ArrayList<Board> boards = new ArrayList<>();
  private ModelObserver observer;
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
  private int sunkenShips = 0;
  private LogicBoard enemyLogicBoard;
  private Boolean running = true;

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
    sendMessage(playerName);
    SwingUtilities.invokeLater(() -> clientView.updateInfoPort());
    sendBoard();
  }

  // Send the attack to the server
  public void sendAttack(PointXY pointXY) throws IOException {
    out.writeObject(pointXY);
  }

  public void sendMessage(String message) throws IOException {
    out.writeObject(message);
  }

  public void sendBoard() throws IOException {
    out.writeObject(playerBoard);
  }

  public void sendLogicBoard() throws IOException {
    out.writeObject(logicBoard);
  }

  public void sendSunkShip(String message) throws IOException {
    out.writeObject(message);
  }

  @SuppressWarnings("unchecked")
  public void receiveMessage() throws IOException, ClassNotFoundException {
    Object obj = in.readObject();

    if (obj instanceof String) {
      if (obj.toString().contains("ha abandonado")) {
        this.status = Status.FINISHED;
        notifyGameFinished(obj.toString() + "\nTú Ganas");
      }
    }
    switch (this.status) {
      case WAITPLAYER:
        if (obj instanceof ArrayList) {
          onlineUsers = (ArrayList<String>) obj;
          SwingUtilities.invokeLater(() -> clientView.updateInfoUsers());
          this.status = Status.WAITBOARD;
        }
        break;

      case WAITBOARD:
        if (obj instanceof ArrayList) {
          boards = (ArrayList<Board>) obj;
          this.status = Status.WAITSHIPS;
          notifyBoardObserver();
        }
        break;

      case WAITSHIPS:
        if (obj instanceof String) {
          String message = (String) obj;
          clientView.setLblStatus(message);
          if (message.equals("Comienza el juego")) {
            this.status = Status.PLAYING;
            notifyBoardObserver();
            notifyChangeTurn();
          }
        } else if (obj instanceof LogicBoard) {
          enemyLogicBoard = (LogicBoard) obj;
        } else if (obj instanceof ArrayList) {
          notifyShipSunk((ArrayList<String>) obj);
        }
        break;

      case PLAYING:
        if (obj instanceof String) {
          String message = (String) obj;
          if (message.equals("CHANGE_TURN")) {
            turn = !turn;
            clientView.setLblStatus(turn ? "Es tu turno..." : "Es el turno del oponente...");
            notifyChangeTurn();
          } else if (message.contains("ha ganado")) {
            this.status = Status.FINISHED;
            notifyGameFinished(message);
          }
        } else if (obj instanceof PointXY) {
          playerBoard.repaintCell((PointXY) obj);
        } else if (obj instanceof ArrayList) {
          notifyShipSunk((ArrayList<String>) obj);
        }
        break;

      default:
        // Manejar casos inesperados si es necesario
        break;
    }
  }

  // Listen for messages from the server
  public void listenForMessages() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        while (running) {
          try {
            receiveMessage();
          } catch (EOFException e) {
            disconnect();
          } catch (ClassNotFoundException e) {
            e.printStackTrace();
          } catch (IOException e) {
            disconnect();
            e.printStackTrace();
          }
        }
      }
    }).start();
  }

  // Disconnect the client from the server
  public void disconnect() {
    running = false;
    try {
      if (out != null)
        out.close();
      if (in != null)
        in.close();
      if (clientSocket != null && !clientSocket.isClosed()) {
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
      System.out.println("changeTurn");
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

  public int getSunkenInt() {
    return sunkenShips;
  }

  public void addSunkenShip() throws IOException {
    sunkenShips++;
    sendSunkShip("<b>Barcos Hundidos de " + playerName + ": </b>" + sunkenShips +
        "<br>");
  }

  public LogicBoard getEnemyLogicBoard() {
    return enemyLogicBoard;
  }

  private void notifyBoardObserver() {
    observer.boardUpdated();
  }

  private void notifyChangeTurn() {
    observer.turnChanged();
  }

  private void notifyGameFinished(String winner) {
    observer.gameFinished(winner);
  }

  private void notifyShipSunk(ArrayList<String> sunkShips) {
    observer.shipSunkUpdated(sunkShips);
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

  public void setModelObserver(ModelObserver observer) {
    this.observer = observer;
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

  public String getPlayerName() {
    return playerName;
  }
}
