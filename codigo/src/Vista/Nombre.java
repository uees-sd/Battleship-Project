package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Nombre extends JFrame {
  private JTextField nameField;
  private JLabel nameLabel;
  private JButton submitButton;

  public Nombre() {
    setTitle("BattleShip - Ingrese su Nombre");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(300, 150);
    setLocationRelativeTo(null);
    setResizable(false);
    setLayout(new GridLayout(3, 1));

    nameLabel = new JLabel("Ingrese su nombre:");
    nameField = new JTextField();

    submitButton = new JButton("Submit");
    submitButton.addActionListener(new SubmitButtonListener());

    add(nameLabel);
    add(nameField);
    add(submitButton);

    setVisible(true);
  }

  private class SubmitButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      String playerName = nameField.getText();
      if (!playerName.isEmpty()) {
        SwingUtilities.invokeLater(() -> new GameFrame(playerName));
        dispose();
      } else {
        JOptionPane.showMessageDialog(Nombre.this, "Por favor, ingrese un nombre.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }
}