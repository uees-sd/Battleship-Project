package Interfaz;

import java.io.Serializable;
import java.util.ArrayList;

public class Jugador implements Serializable {
    private String nombre;
    private String contrasena;
    private int puntos;
    private ArrayList<String> historialPuntos;

    public Jugador(String nombre, String contrasena) {
        this.nombre = nombre;
        this.contrasena = contrasena;
        puntos = 0;
        historialPuntos = new ArrayList<String>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getPuntos() {
        return puntos;
    }

    //La puntuacion aumenta en 3 cuando se gana una partida
    public void aumentarPuntos() {
        puntos += 3;
    }

    //Existe un límite de 10 anotaciones, por lo que al alcanza, este límite
    // se eliminarán las anotaciones más antiguas para dar cabida a las más recientes.

    public void anotarHistorialPuntos (String anotacionPuntos) {
        if (historialPuntos.size() == 10) { //Alcanzado límite de 10 anotaciones
            //Se elimina la más antigua
            for (int i=1; i < 9; i++) {
                historialPuntos.set((i-1), historialPuntos.get(i));
            }
            historialPuntos.set(9,anotacionPuntos);
        }
        else { //Aun no tenemos 10 anotaciones, añadimos sin más.
            historialPuntos.add(anotacionPuntos);
        }
    }

}

