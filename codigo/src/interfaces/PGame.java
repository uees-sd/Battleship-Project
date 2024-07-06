package interfaces;

import java.net.DatagramPacket;

import Vista.Ship;

public interface PGame {
    void start();

    void end();

    String getPort();

    Ship receiveData();

    DatagramPacket sendData(Ship content);

    boolean getServerListening();

    boolean getAttackComponent();

}
