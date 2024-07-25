package com.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JOptionPane;

//CLASE PARA PROBAR LA BD 
public class Main {
    public static void main(String[] args) {
        // Configuración de la conexión a PostgreSQL usando la clase ConexionBD
        ConexionBD conexion = new ConexionBD("battleship");

        try {
            Connection con = conexion.getConnection(); // Obtener la conexión establecida

            // Declaración
            Statement stmt = con.createStatement();

            // Pregunta
            String sqlPregunta = "SELECT pregunta_id, texto FROM Preguntas ORDER BY RANDOM() LIMIT 1";
            ResultSet rsPregunta = stmt.executeQuery(sqlPregunta);
            String preguntaTexto = null;
            int preguntaId = 0;
            if (rsPregunta.next()) {
                preguntaId = rsPregunta.getInt("pregunta_id");
                preguntaTexto = rsPregunta.getString("texto");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron preguntas", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Posibles opciones para la pregunta
            String sqlOpciones = "SELECT opcion_id, texto, es_correcta FROM Opciones WHERE pregunta_id = " + preguntaId;
            ResultSet rsOpciones = stmt.executeQuery(sqlOpciones);

            String[] opciones = new String[4];
            boolean[] correctas = new boolean[4];
            int i = 0;

            // Procesar las opciones y almacenar los textos corrección
            while (rsOpciones.next() && i < 4) {
                opciones[i] = rsOpciones.getString("texto");
                correctas[i] = rsOpciones.getBoolean("es_correcta");
                i++;
            }

            // Mostrar el JOptionPane con la pregunta y las opciones de respuesta
            int respuesta = JOptionPane.showOptionDialog(
                    null,
                    preguntaTexto,
                    "Pregunta",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    null);

            // Verifica opcion correcta
            if (respuesta >= 0 && respuesta < correctas.length && correctas[respuesta]) {
                JOptionPane.showMessageDialog(null,
                        "¡Respuesta correcta! Pregunta respondida correctamente, ahora puedes atacar al enemigo.",
                        "Resultado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Respuesta incorrecta, NO puedes atacar al enemigo.", "Resultado",
                        JOptionPane.ERROR_MESSAGE);
            }

            // Se tiene que cerrar los recursos
            rsPregunta.close();
            rsOpciones.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al ejecutar la consulta: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            // Cerrar la conexión
            conexion.cerrarConexion();
        }
    }
}
