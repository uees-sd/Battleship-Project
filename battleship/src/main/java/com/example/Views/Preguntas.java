package com.example.Views;

import java.util.Random;

import javax.swing.*;

// Aquí se ponen las preguntas y el índice de la respuesta correcta.
public class Preguntas {
        public static final String[] preguntas = {
                        "¿Cuál es el río más largo del mundo?",
                        "¿Cuál es el océano más grande del mundo?",
        };

        private static final String[][] opciones = {
                        { "Río Amazonas", "Río Nilo", "Río Danubio" },
                        { "Océano Pacífico", "Océano Índico", "Océano Atlántico" }
        };

        private static final int[] respuestaCorrecta = {
                        1,
                        0

        };

        public static boolean askQuestion(JFrame parent) {
                Random rand = new Random();
                int questionIndex = rand.nextInt(preguntas.length);

                int answer = JOptionPane.showOptionDialog(parent,
                                preguntas[questionIndex], "Question",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                                null, opciones[questionIndex], opciones[questionIndex][0]);

                return answer == respuestaCorrecta[questionIndex];
        }
}
