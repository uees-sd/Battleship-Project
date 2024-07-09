package Views;

import javax.swing.*;

import Models.ClientModel;
import java.awt.*;

public class ClientView {
  private JPanel clientPanel, connectionPanel;
  private Board board;
  private JTextField playerName, serverPort;
  private JButton createGameBtn, joinGameBtn;
  private ClientModel clientModel;

  public ClientView(ClientModel clientModel) {
    this.clientModel = clientModel;
    initConnectionPanel();
    initGamePanel();
    initClientPanel();

  }

  private void initClientPanel() {
    clientPanel = new JPanel();
    clientPanel.setLayout(new CardLayout());
    clientPanel.add(connectionPanel, "connectionPanel");
    clientPanel.add(board, "gamePanel");
  }

  private void initConnectionPanel() {
    connectionPanel = new JPanel();
    connectionPanel.setLayout(new GridBagLayout());

    playerName = new JTextField(15);
    serverPort = new JTextField(15);
    serverPort.setText("Just for joining a game");
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

  private void initGamePanel() {
    board = new Board(playerName.getText());
  }

  public void showBoard() {
    CardLayout cardLayout = (CardLayout) clientPanel.getLayout();
    cardLayout.show(clientPanel, "gamePanel");
  }

  public void showHome() {
    CardLayout cardLayout = (CardLayout) clientPanel.getLayout();
    cardLayout.show(clientPanel, "connectionPanel");
  }

  public void showError(String message) {
    JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  public JPanel getClientPanel() {
    return clientPanel;
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
}
