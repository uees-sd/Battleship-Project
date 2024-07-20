package Views;

import javax.swing.*;

import Models.ClientModel;
import java.awt.*;

public class ClientView {
  private JPanel clientPanel, connectionPanel, infoPanel;
  private GameView gamePanel;
  private JTextField playerName, serverPort;
  private JButton createGameBtn, joinGameBtn;
  private JTextArea infoArea;
  private ClientModel clientModel;

  public ClientView(ClientModel clientModel) {
    this.clientModel = clientModel;
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
    infoPanel = new JPanel();
    infoPanel.setLayout(new GridBagLayout());
    infoArea = new JTextArea(10, 20);
    infoArea.setEditable(false);
    infoPanel.add(infoArea);
  }

  private void initGamePanel() {
    gamePanel = new GameView();
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

  public void updateInfoPort() {
    infoArea.append("Puerto usado: " + clientModel.getPort() + "\n\n");
  }

  public void updateInfoUsers() {
    infoArea.append("Usuarios conectados: \n");
    for (String user : clientModel.getOnlineUsers()) {
      infoArea.append(user);
    }
  }

  public void updateBoards() {
    for (Board board : clientModel.getBoards()) {
      gamePanel.add(board);
    }
    gamePanel.revalidate();
    gamePanel.repaint();

    Container parent = clientPanel;
    while (parent != null && !(parent instanceof JFrame)) {
      parent = parent.getParent();
    }

    if (parent instanceof JFrame) {
      ((JFrame) parent).pack();
    } else {
      System.out.println("JFrame no encontrado");
    }
  }
}
