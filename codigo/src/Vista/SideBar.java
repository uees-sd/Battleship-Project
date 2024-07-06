package Vista;

import javax.swing.*;

import Controllers.Comunicacion.ConnectGame;
import Controllers.Comunicacion.CreateGame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SideBar extends JFrame implements ActionListener {
  private JTextField nombreJugador, puertoservidor;
  private JButton crearJuego, unirseJuego;

  public SideBar() {
    setTitle("BattleShip Educativo");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(300, 800);
    setResizable(false);
    getContentPane().setBackground(new Color(238, 255, 255));
    setLayout(new GridBagLayout());

    nombreJugador = new JTextField(15);
    puertoservidor = new JTextField(15);
    crearJuego = new JButton("Crear Juego");
    unirseJuego = new JButton("Unirse Juego");

    addComponents();
    addListener();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private void addComponents() {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;

    add(new JLabel("Nombre del Jugador"), gbc);
    gbc.gridy++;
    add(nombreJugador, gbc);

    gbc.gridy++;
    add(new JLabel("Puerto del Servidor"), gbc);
    gbc.gridy++;
    add(puertoservidor, gbc);

    gbc.gridy++;
    add(crearJuego, gbc);

    gbc.gridy++;
    add(unirseJuego, gbc);
  }

  private void addListener() {
    crearJuego.addActionListener(this);
    unirseJuego.addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == crearJuego) {
      if (nombreJugador.getText().equals("")) {
        JOptionPane.showMessageDialog(null, "Ingrese su nombre.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
      System.out.println("Crear Juego");
      CreateGame server;
      try {
        server = new CreateGame(nombreJugador.getText());
        Board board = server.getBoard();
        JFrame boardFrame = new JFrame("Tablero de Barcos - " + nombreJugador.getText());
        boardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        boardFrame.add(board);
        boardFrame.pack();
        boardFrame.setLocationRelativeTo(null);
        boardFrame.setVisible(true);
        this.dispose();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    } else if (e.getSource() == unirseJuego) {
      if (nombreJugador.getText().equals("") || puertoservidor.getText().equals("")) {
        JOptionPane.showMessageDialog(null, "Ingrese su nombre y puerto.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
      ConnectGame connectGame;
      try {
        connectGame = new ConnectGame(puertoservidor.getText(), nombreJugador.getText());
        Board board = connectGame.getBoard();
        JFrame boardFrame = new JFrame("Tablero de Barcos - " + nombreJugador.getText());
        boardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        boardFrame.add(board);
        boardFrame.pack();
        boardFrame.setLocationRelativeTo(null);
        boardFrame.setVisible(true);
        this.dispose();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
      System.out.println("Unirse Juego");
    }
  }
}
