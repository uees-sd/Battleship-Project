package com.example.Views;

import java.io.Serializable;
import java.util.ArrayList;

public class Ship implements Serializable {
    private final int length;
    public ArrayList<PointXY> coords;
    private int touchedCount;

    public Ship(int length) {
        this.length = length;
        coords = new ArrayList<>();
        touchedCount = 0;
    }

    public void addCoords(PointXY coord) {
        coords.add(coord);
    }

    public boolean evaluateShot(PointXY disparo) {
        for (PointXY coord : coords) {
            if (coord.equals(disparo)) {
                coord.setTouched(true);
                System.out.println("Barco tocado de length " + length + "tocado en " + coord);
                touchedCount++;
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
        System.out.println("Touched count: " + touchedCount + " length: " + length);
        return touchedCount == length;
    }

    @Override
    public String toString() {
        // Devuelve la longitud del barco y sus coordenadas con sus valors x y y
        return "Ship [length=" + length + ", coords=" + coords + "]";
    }
}