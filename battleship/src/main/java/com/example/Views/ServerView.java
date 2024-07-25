package com.example.Views;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.example.Models.ServerModel;

public class ServerView {
  private JPanel serverPanel;
  private JTextArea portUsed, usersPlaying, shipsDestroyed;
  private ServerModel serverModel;

  public ServerView(ServerModel serverModel) {
    this.serverModel = serverModel;
    initServerPanel();
  }

  private void initServerPanel() {
    serverPanel = new JPanel();
    serverPanel.setLayout(new BoxLayout(serverPanel, BoxLayout.Y_AXIS));
    portUsed = new JTextArea();
    usersPlaying = new JTextArea();
    shipsDestroyed = new JTextArea();

    portUsed.setEditable(false);
    usersPlaying.setEditable(false);
    shipsDestroyed.setEditable(false);

    serverPanel.add(portUsed);
    serverPanel.add(usersPlaying);
    serverPanel.add(shipsDestroyed);
  }

  public void updateUsers() {
    usersPlaying.setText("Usuarios jugando: \n");
    for (String user : serverModel.getOnlineUsers()) {
      usersPlaying.append(user);
    }
  }

  public void showConnectionError() {
    JOptionPane.showMessageDialog(null, "Ya hay dos jugadores connectados", "Error", JOptionPane.ERROR_MESSAGE);
  }

  public JPanel getServerPanel() {
    return serverPanel;
  }

  public JTextArea getPortUsed() {
    return portUsed;
  }

  public JTextArea getUsersPlaying() {
    return usersPlaying;
  }

  public JTextArea getShipsDestroyed() {
    return shipsDestroyed;
  }
}
