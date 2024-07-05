package Controlador;

import java.io.*;
import java.net.*;

import Vista.Game;

public class GameServer {
    private ServerSocket serverSocket;
    private Socket player1Socket;
    private Socket player2Socket;
    private PrintWriter player1Out;
    private BufferedReader player1In;
    private PrintWriter player2Out;
    private BufferedReader player2In;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Servidor Battleship iniciado en el puerto " + port + "...");

        // Espera al primer jugador
        player1Socket = serverSocket.accept();
        System.out.println("Jugador 1 conectado: " + player1Socket.getInetAddress());
        player1Out = new PrintWriter(player1Socket.getOutputStream(), true);
        player1In = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));

        // Espera al segundo jugador
        player2Socket = serverSocket.accept();
        System.out.println("Jugador 2 conectado: " + player2Socket.getInetAddress());
        player2Out = new PrintWriter(player2Socket.getOutputStream(), true);
        player2In = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));

        // Inicia la lógica del juego
        iniciarJuego();
    }

    private void iniciarJuego() throws IOException {
        // Implementa aquí la lógica del juego Battleship
        // Por ejemplo, manejar los turnos, enviar y recibir disparos, etc.
        boolean juegoActivo = true;
        while (juegoActivo) {
            // Ejemplo: jugador 1 dispara
            String disparoJugador1 = player1In.readLine();
            // Procesar disparoJugador1 y enviar resultados a jugador 2
            player2Out.println(disparoJugador1);

            // Ejemplo: jugador 2 dispara
            String disparoJugador2 = player2In.readLine();
            // Procesar disparoJugador2 y enviar resultados a jugador 1
            player1Out.println(disparoJugador2);

            // Aquí puedes agregar la lógica para verificar impactos, finalizar el juego, etc.
            // juegoActivo = false; // Terminar juego cuando se cumplan las condiciones adecuadas
        }
    }

    public void stop() throws IOException {
        player1In.close();
        player1Out.close();
        player1Socket.close();

        player2In.close();
        player2Out.close();
        player2Socket.close();

        serverSocket.close();
    }

    public static void main(String[] args) {
        GameServer server = new GameServer();
        int portNumber = 2020; // Puerto en el que el servidor escucha

        try {
            server.start(portNumber);
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor en el puerto " + portNumber);
            e.printStackTrace();
        }
    }
}
