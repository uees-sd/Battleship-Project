package com.example.Controllers.Preguntas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

//CLASE PARA PROBAR LA BD 
public class QuestionManager {
    public String[] getRandomQuestion() {
        ConexionBD conexion = new ConexionBD("battleship");
        String[] question = new String[6];

        try {
            Connection con = conexion.getConnection(); // Obtener la conexión establecida

            // Declaración
            Statement stmt = con.createStatement();

            // Pregunta
            String sqlPregunta = "SELECT pregunta_id, texto FROM Preguntas ORDER BY RANDOM() LIMIT 1";
            ResultSet rsPregunta = stmt.executeQuery(sqlPregunta);
            int preguntaId = 0;
            if (rsPregunta.next()) {
                preguntaId = rsPregunta.getInt("pregunta_id");
                question[0] = rsPregunta.getString("texto");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron preguntas", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            // Posibles opciones para la pregunta
            String sqlOpciones = "SELECT opcion_id, texto, es_correcta FROM Opciones WHERE pregunta_id = " + preguntaId
                    + " ORDER BY RANDOM()";
            ResultSet rsOpciones = stmt.executeQuery(sqlOpciones);

            int i = 1;
            // Procesar las opciones y almacenar los textos corrección
            while (rsOpciones.next() && i <= 4) {
                question[i] = rsOpciones.getString("texto");
                if (rsOpciones.getBoolean("es_correcta")) {
                    question[5] = String.valueOf(i - 1);
                }
                i++;
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
        return question;
    }

    public Boolean checkQuestion(String[] questionData, int answerIndex) {
        int correctIndex = Integer.parseInt(questionData[5]);
        return correctIndex == answerIndex;
    }
}