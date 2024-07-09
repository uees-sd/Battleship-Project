package Views;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import Models.ServerModel;

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
    portUsed = new JTextArea(10, 20);
    usersPlaying = new JTextArea(10, 20);
    shipsDestroyed = new JTextArea(10, 20);
    serverPanel.add(portUsed);
    serverPanel.add(usersPlaying);
    serverPanel.add(shipsDestroyed);
  }

  public void updateUsers() {
    usersPlaying.setText(serverModel.getOnlineUsers().toString());
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
