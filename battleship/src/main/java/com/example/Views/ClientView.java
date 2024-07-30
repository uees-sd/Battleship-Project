package com.example.Views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.example.Models.ClientModel;

import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ClientView {
  private JPanel clientPanel, connectionPanel, infoPanel, gamePanel;
  private JTextField playerName, serverPort;
  private JButton createGameBtn, joinGameBtn;
  private JTextPane infoArea;
  private ClientModel clientModel;
  private Font font = new Font("Arial", Font.PLAIN, 12);
  private ArrayList<String> strings = new ArrayList<>();
  // private ArrayList<Board> boards = new ArrayList<>();
  private Board myBoard, enemyBoard;
  private JLabel lblStatus;
  public int flag = 0;

  public ClientView(ClientModel clientModel) {
    this.clientModel = clientModel;
    strings.add("<html>");
    strings.add("</html>");
    initConnectionPanel();
    initInfoPanel();
    initGamePanel();
    initClientPanel();
  }

  private void initClientPanel() {
    clientPanel = new JPanel();
    clientPanel.setLayout(new CardLayout());
    clientPanel.add(connectionPanel, "connectionPanel");
    clientPanel.add(gamePanel, "gamePanel");
  }

  private void initConnectionPanel() {
    connectionPanel = new JPanel();
    connectionPanel.setLayout(new GridBagLayout());

    playerName = new JTextField(15);
    serverPort = new JTextField(15);
    serverPort.setText("ip:puerto");
    createGameBtn = new JButton("Crear Juego");
    joinGameBtn = new JButton("Unirse Juego");

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;

    connectionPanel.add(new JLabel("Nombre del Jugador"), gbc);
    gbc.gridy++;
    connectionPanel.add(playerName, gbc);

    gbc.gridy++;
    connectionPanel.add(new JLabel("Puerto del Servidor"), gbc);
    gbc.gridy++;
    connectionPanel.add(serverPort, gbc);

    gbc.gridy++;
    connectionPanel.add(createGameBtn, gbc);

    gbc.gridy++;
    connectionPanel.add(joinGameBtn, gbc);
  }

  private void initInfoPanel() {
    lblStatus = new JLabel("Iniciando Partida");
    infoPanel = new JPanel();
    infoPanel.setBorder(
        BorderFactory.createCompoundBorder(new EmptyBorder(20, 60, 20, 60), new LineBorder(Color.MAGENTA, 2)));
    infoPanel.setLayout(new BorderLayout());
    infoArea = new JTextPane();
    infoArea.setFont(font);
    infoArea.setEditable(false);
    infoArea.setContentType("text/html");
    infoArea.setPreferredSize(new Dimension(300, 75));
    infoPanel.setPreferredSize(new Dimension(320, 120));

    infoPanel.add(infoArea);
    infoPanel.add(lblStatus, BorderLayout.SOUTH);
  }

  private void initGamePanel() {
    gamePanel = new JPanel();
    gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.X_AXIS));
    gamePanel
        .setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(20, 60, 20, 60), new LineBorder(Color.BLACK, 2)));
    gamePanel.add(infoPanel);
  }

  public void showBoard() {
    CardLayout cardLayout = (CardLayout) clientPanel.getLayout();
    cardLayout.show(clientPanel, "gamePanel");
  }

  public void showHome() {
    CardLayout cardLayout = (CardLayout) clientPanel.getLayout();
    cardLayout.show(clientPanel, "connectionPanel");
  }

  public void showWaitingForPlayers() {
    JOptionPane.showMessageDialog(null, "Esperando a que se unan los jugadores", "Esperando",
        JOptionPane.INFORMATION_MESSAGE);
  }

  public void showError(String message) {
    JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  public JPanel getClientPanel() {
    return clientPanel;
  }

  public JPanel getGamePanel() {
    return gamePanel;
  }

  public String getPlayerName() {
    return playerName.getText();
  }

  public String getServerPort() {
    return serverPort.getText();
  }

  public JButton getCreateGame() {
    return createGameBtn;
  }

  public JButton getJoinGame() {
    return joinGameBtn;
  }

  public JTextField getPorTextField() {
    return serverPort;
  }

  public ArrayList<Board> getBoards() {
    return null;
  }

  public Board getMyBoard() {
    return myBoard;
  }

  public Board getEnemyBoard() {
    return enemyBoard;
  }

  public void setLblStatus(String status) {
    lblStatus.setText(status);
  }

  public void updateInfoPort() {
    try {
      strings.add(1, "<b>Puerto:</b> " + clientModel.getPort() + "<br><b>IP Servidor:</b> "
          + InetAddress.getLocalHost().getHostAddress() + "<br><br>");
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    infoArea.setText(strings.get(0) + strings.get(1) + strings.get(2));
  }

  public void updateInfoUsers() {
    StringBuilder sb = new StringBuilder();
    for (String user : clientModel.getOnlineUsers()) {
      sb.append(user).append("<br>");
    }
    strings.add(2, sb.toString());
    infoArea.setText(strings.get(0) + strings.get(1) + strings.get(2) + strings.get(3));
  }

  public void updateBoards(String name) {
    for (Board board : clientModel.getBoards()) {
      if (board.getBoardTitle().equals(name)) {
        myBoard = board;
      } else {
        enemyBoard = board;
      }
      gamePanel.add(board);
    }
    setLblStatus("Coloca los barcos!!");
    gamePanel.revalidate();
    gamePanel.repaint();

    Container parent = clientPanel;
    while (parent != null && !(parent instanceof JFrame)) {
      parent = parent.getParent();
    }

    if (parent instanceof JFrame) {
      ((JFrame) parent).pack();
      ((JFrame) parent).setLocationRelativeTo(null);
    } else {
      System.out.println("JFrame no encontrado");
    }
    flag = 2;
  }
}
