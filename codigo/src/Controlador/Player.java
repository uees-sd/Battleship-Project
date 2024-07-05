package Controlador;

import java.io.*;
import java.net.*;

public class Player {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ipAddress, int port) throws IOException {
        socket = new Socket(ipAddress, port);
        System.out.println("Conectado al servidor en " + ipAddress + ":" + port);

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Procesar mensajes del servidor (en este caso, no se recibirán hasta iniciar el juego)
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public static void main(String[] args) {
        Player player = new Player();
        String serverAddress = "127.0.0.1"; // Dirección IP del servidor
        int portNumber = 2020; // Puerto del servidor

        try {
            player.startConnection(serverAddress, portNumber);

            // Ejemplo: Enviar un disparo al servidor
            player.sendMessage("A1"); // Ejemplo de disparo en la coordenada A1

            player.stopConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
