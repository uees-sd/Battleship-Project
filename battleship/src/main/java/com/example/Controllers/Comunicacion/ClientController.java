package com.example.Controllers.Comunicacion;

import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import com.example.Models.ClientModel;
import com.example.Models.ClientModel.Status;
import com.example.Views.Board;
import com.example.Views.Board.Cell;
import com.example.Views.ClientView;
import com.example.Views.LogicBoard;
import com.example.Views.PointXY;
import com.example.Views.Ship;
import com.example.Interfaces.ModelObserver;

//sender
public class ClientController implements ModelObserver {
  private JFrame frame;
  private ClientModel clientModel;
  private ClientView clientView;
  private String playerName;
  private int port;
  private int[] length = { 5, 4, 3, 2, 2 };
  private int currentShipIndex = 0;

  public ClientController() {
    this.clientModel = new ClientModel();
    this.clientView = new ClientView(clientModel);

    // add the clientView as observer to the Client model
    clientModel.addObserver(clientView);
    clientModel.setModelObserver(this);
    // Add action listeners
    clientView.getCreateGame().addActionListener(e -> initGame(1));
    clientView.getJoinGame().addActionListener(e -> initGame(2));
    clientView.getPorTextField().addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(java.awt.event.FocusEvent evt) {
        clientView.getPorTextField().setText("");
      }

    });
    SwingUtilities.invokeLater(() -> {
      frame = new JFrame();
      frame.add(clientView.getClientPanel());
      frame.setTitle("BattleShip Educativo");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setResizable(false);
      frame.setBackground(new Color(238, 255, 255));
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
    });
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
          clientModel.setTurn(true);
        } else {
          clientView.showError("Ingrese su nombre");
        }
        break;
      case 2:
        if (isValidPlayerName(playerName) && isValidPort()) {
          String[] parts = clientView.getServerPort().split(":");
          String serverIp = parts[0];
          port = Integer.parseInt(parts[1]);
          connectToServer(serverIp);
          clientModel.setTurn(false);
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
            try {
              clientModel.sendMessage(playerName + " ha abandonado la partida");
            } catch (IOException e1) {
              e1.printStackTrace();
            }
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
  }

  private boolean isValidPort() {
    String[] parts = clientView.getServerPort().split(":");
    int serverPort = Integer.parseInt(parts[1]);
    try {
      if (serverPort >= 3001 && serverPort <= 8999) {
        return true;
      } else {
        return false;
      }
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private void randomPort() {
    Random random = new Random();
    this.port = random.nextInt(6000) + 3001;
  }

  @Override
  public void boardUpdated() {
    try {
      if (clientModel.getStatus() == ClientModel.Status.WAITSHIPS) {
        SwingUtilities.invokeAndWait(() -> clientView.updateBoards(playerName));
        clientModel.setPlayerBoard(clientView.getMyBoard());
        clientModel.setLogicBoard();
        addListenerBoard(clientView.getMyBoard());
      } else if (clientModel.getStatus() == ClientModel.Status.PLAYING) {
        clientView.getMyBoard().desactivarListener();
        clientView.getEnemyBoard().logicBoard = clientModel.getEnemyLogicBoard();
        addListenerBoard(clientView.getEnemyBoard());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void turnChanged() {
    if (clientModel.isTurn() && clientModel.getStatus() == ClientModel.Status.PLAYING) {
      clientModel.genQuestion();
      int index = clientView.showQuestion(clientModel.getQuestion());
      clientModel.checkAnswer(index);
    }
  }

  @Override
  public void shipSunkUpdated(ArrayList<String> sunkShips) {
    clientView.updateInfoShips(sunkShips);
  }

  @Override
  public void gameFinished(String message) {
    clientView.showWinner(message);
    clientModel.disconnect();
    restartApplication();
  }

  private void restartApplication() {
    try {
      // Obtener el nombre del JAR
      String jarPath = "target/battleship-1.0-SNAPSHOT-jar-with-dependecies.jar";
      String command = String.format("java -jar %s", jarPath);

      // Ejecutar el comando para reiniciar la aplicación
      Process process = Runtime.getRuntime().exec(command);

      // Leer la salida y errores del proceso
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }
      while ((line = errorReader.readLine()) != null) {
        System.err.println(line);
      }

      // Esperar a que el proceso termine
      process.waitFor();

      // Salir de la aplicación actual
      System.exit(0);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addListenerBoard(Board board) {
    Cell cells[][] = board.getCells();
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        Cell cell = cells[i][j];
        cell.addMouseListener(new ButtonClickListener(cell.coord, board));
      }
    }
  }

  public class ButtonClickListener implements MouseListener {
    private Status status = clientModel.getStatus();
    private LogicBoard logicBoard;
    private Cell[][] cells;
    private final int row;
    private final int col;
    private int size = 5;
    private int[][] logicMatrix;

    public ButtonClickListener(PointXY coord, Board board) {
      this.row = coord.x;
      this.col = coord.y;
      this.logicBoard = board.logicBoard;
      this.cells = board.cells;
      this.logicMatrix = logicBoard.logicMatrix;
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
      if (status == Status.WAITSHIPS) {
        if (currentShipIndex < size) {
          if (canPlaceShip(row, col, length[currentShipIndex])) {
            highlightShip(row, col, length[currentShipIndex], true);
          }
        }
      } else if (status == Status.PLAYING && clientModel.isTurn() && logicMatrix[row][col] < 2) {
        cells[row][col].highlight(true);
      }
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
      if (status == Status.WAITSHIPS) {
        if (currentShipIndex < size) {
          if (canPlaceShip(row, col, length[currentShipIndex])) {
            highlightShip(row, col, length[currentShipIndex], false);
          }
        }
      } else if (status == Status.PLAYING && clientModel.isTurn()) {
        cells[row][col].highlight(false);
      }
    }

    private boolean canPlaceShip(int row, int col, int length) {
      if (col + length > 10)
        return false;
      for (int i = 0; i < length; i++) {
        if (logicMatrix[row][col + i] != 0)
          return false;
      }
      return true;
    }

    private void highlightShip(int row, int col, int length, boolean highlight) {
      for (int i = 0; i < length; i++) {
        Cell cell = cells[row][col + i];
        cell.highlight(highlight);
      }
    }

    private void placeShip(int row, int col, int length) {
      Ship ship = new Ship(length);
      for (int i = 0; i < length; i++) {
        Cell cell = cells[row][col + i];
        cell.setBackground(Color.GRAY);
        cell.setEnabled(false);
        ship.addCoords(cell.coord);
      }
      logicBoard.addShip(ship);
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
      if (status == Status.WAITSHIPS) {
        if (currentShipIndex < size) {
          if (canPlaceShip(row, col, length[currentShipIndex])) {
            placeShip(row, col, length[currentShipIndex]);
            currentShipIndex++;
            clientModel.setCurrentShip(currentShipIndex);
            if (currentShipIndex == size) {
              try {
                clientModel.sendLogicBoard();
              } catch (IOException e1) {
                e1.printStackTrace();
              }
            }
          }
        }
      } else if (status == Status.PLAYING && clientModel.isTurn() && logicMatrix[row][col] < 2) {
        PointXY pointXY = new PointXY(row, col);
        try {
          clientModel.sendAttack(pointXY);
        } catch (IOException e1) {
          e1.printStackTrace();
        }
        if (logicBoard.evaluateShot(pointXY)) {
          try {
            clientModel.addSunkenShip();
          } catch (IOException e1) {
            e1.printStackTrace();
          }
          if (clientModel.getSunkenInt() == 5) {
            try {
              clientModel.sendMessage(playerName + " ha ganado la partida");
            } catch (IOException e1) {
              e1.printStackTrace();
            }
          }
        }
        if (logicMatrix[row][col] == 2) {
          cells[row][col].setBackground(Color.RED);
        } else if (logicMatrix[row][col] == 3) {
          cells[row][col].setBackground(Color.BLUE);
        }
        clientModel.incrementAttackCount();
      }

    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
    }
  }

}