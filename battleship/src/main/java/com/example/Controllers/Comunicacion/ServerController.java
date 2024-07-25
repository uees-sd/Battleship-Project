package com.example.Controllers.Comunicacion;

import com.example.Views.ServerView;
import com.example.Models.ServerModel;
import java.io.IOException;
import javax.swing.JFrame;

public class ServerController {
    private int port;
    private ServerModel serverModel;
    private ServerView serverView;
    private JFrame frame;

    public ServerController(int port) {
        this.serverModel = new ServerModel();
        this.serverView = new ServerView(serverModel);
        this.serverModel.addObserver(serverView);
        this.port = port;

        startListening();

        serverView.getPortUsed().setText("Puerto: " + this.port);

        frame = new JFrame();
        frame.add(serverView.getServerPanel());
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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

    public boolean readyToStart() {
        return serverModel.getOnlineUsers().size() == 2;
    }
}
