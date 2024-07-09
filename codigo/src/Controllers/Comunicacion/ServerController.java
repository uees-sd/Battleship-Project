package Controllers.Comunicacion;

import Views.Board;
import Views.ServerView;
import Models.ServerModel;
import java.awt.Frame;
import java.io.IOException;

import javax.swing.JFrame;

public class ServerController {
    private int port;
    private ServerModel serverModel;
    private ServerView serverView;
    private Board board;
    private JFrame frame;

    protected int counter = 0;

    public ServerController(int port) {
        this.serverModel = new ServerModel();
        this.serverView = new ServerView(serverModel);
        this.serverModel.addObserver(serverView);
        this.port = port;

        startListening();

        serverView.getPortUsed().setText("Puerto: " + this.port);

        frame = new JFrame();
        frame.add(serverView.getServerPanel());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setTitle("BattleShip Educativo - Servidor");
        frame.setVisible(true);
    }

    public void startListening() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverModel.startServer(port);
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /*
     * public Ship receiveData() {
     * 
     * }
     * public DatagramPacket sendData(Ship content) {
     * }
     */

    /*
     * public boolean getAttackComponent() {
     * }
     */

    /*
     * public void end() {
     * this.serverListening = false;
     * 
     * }
     * 
     * public boolean getServerListening() {
     * return this.serverListening;
     * }
     * 
     * public Board getBoard() {
     * return board;
     * }
     */
    public static void main(String[] args) {
    }
}
