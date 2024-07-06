package Controllers.Comunicacion;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import Vista.Board;
import Vista.GamePanel;
import Vista.Ship;
import interfaces.PGame;

/*import com.iac.shipwar.UI.layout.UiDashboard;
import com.iac.shipwar.controllers.ShipDeployed;
import com.iac.shipwar.controllers.Singleton;
import com.iac.shipwar.interfaces.IGame;
import com.iac.shipwar.models.enums.Dashboard;*/

//Receiver
public class CreateGame implements PGame {
    private int port;
    private int senderPort;
    private final int bufferSize = 10 * 1024 * 1024;// 10mb
    private DatagramSocket dtSocket;
    private byte[] buffer;
    private DatagramPacket dtPacket;
    private InetAddress address;
    private Boolean serverListening;
    private Boolean attackComponent = false;
    private String playerName;
    private Board board;
    // protected final Singleton singleton = Singleton.getInstance();
    // private UiDashboard dashboard;
    protected int counter = 0;

    public CreateGame(String playerName) throws Exception {
        // this.dashboard = dashboard;
        this.playerName = playerName;
        start();
        randomPort();
        this.dtSocket = new DatagramSocket(this.port);
        System.out.println("- - - create game - - -");
        System.out.println("- - - connected - - - \nPORT: " + this.port);
        board = new Board(playerName);
        startListening();
    }

    public void startListening() {
        Thread listenerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (serverListening) {
                    receiveData();
                }
            }
        });

        listenerThread.start();
    }

    @Override
    public Ship receiveData() {
        try {
            this.buffer = new byte[this.bufferSize];
            this.dtPacket = new DatagramPacket(this.buffer, this.bufferSize);
            this.dtSocket.receive(this.dtPacket);
            this.address = this.dtPacket.getAddress();
            this.senderPort = this.dtPacket.getPort();
            ByteArrayInputStream bis = new ByteArrayInputStream(this.buffer);
            ObjectInputStream in = new ObjectInputStream(bis);
            Ship receivedData = (Ship) in.readObject();
            this.attackComponent = true;

            return receivedData;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public DatagramPacket sendData(Ship content) {
        /*
         * try {
         * if (this.address == null || this.senderPort == 0) {
         * System.out.println("Error: Dirección o puerto no configurados.");
         * return null;
         * }
         * ByteArrayOutputStream bos = new ByteArrayOutputStream();
         * ObjectOutputStream out = new ObjectOutputStream(bos);
         * out.writeObject(content);
         * this.buffer = bos.toByteArray();
         * this.dtPacket = new DatagramPacket(this.buffer, this.buffer.length,
         * this.address, this.senderPort);
         * content.printDetails("MIO");
         * this.dtSocket.send(dtPacket);
         * } catch (Exception e) {
         * e.printStackTrace();
         * return null;
         * }
         */
        return null;
    }

    private void randomPort() {
        Random random = new Random();
        this.port = random.nextInt(6000) + 3001;
    }

    @Override
    public boolean getAttackComponent() {
        return attackComponent;
    }

    @Override
    public String getPort() {
        return "" + this.port;
    }

    @Override
    public void start() {
        this.serverListening = true;
    }

    @Override
    public void end() {
        this.serverListening = false;

    }

    @Override
    public boolean getServerListening() {
        return this.serverListening;
    }

    public Board getBoard() {
        return board;
    }
}
