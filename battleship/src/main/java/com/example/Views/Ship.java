package com.example.Views;

import java.io.Serializable;
import java.util.ArrayList;

public class Ship implements Serializable {
    private final int length;
    public ArrayList<PointXY> coords;

    public Ship(int length) {
        this.length = length;
        coords = new ArrayList<>();
    }

    public void addCoords(PointXY coord) {
        coords.add(coord);
    }

    public boolean evaluateShot(PointXY disparo) {
        for (PointXY coord : coords) {
            if (coord.equals(disparo)) {
                coord.setTouched(true);
                return true;
            }
        }
        // Ninguna coords ha coincidido con el disparo
        return false;
    }

    public int getLength() {
        return length;
    }

    public boolean esHundido() {
        for (PointXY coord : coords) {
            if (!coord.isTouched()) { // Al menos una coordenada no ha sido tocada
                return false; // Barco no está hundido
            }
        }
        // Si durante el bucle for no se ha devuelto false, es que todas las coord. han
        // sido tocadas
        return true; // Barco hundido
    }
}