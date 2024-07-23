package Interfaz;

import java.util.ArrayList;

public class Barco {

    private String codigo;
    public ArrayList<PuntoXY> coordenadas;

    public Barco(String codigo) {
        this.codigo = codigo;
        coordenadas = new ArrayList<PuntoXY>();
    }

    public String getCodigo() {
        return codigo;
    }

    public void addCoordenada(PuntoXY coord) {
        coordenadas.add(coord);
    }

    //Evalúa si un disparo ha tocado a este Barco.
    //@param disparo, Objeto PuntoXY con las coordenadas del disparo
    //@return True si ha sido tocado, False en caso contrario.

    public boolean evaluaDisparo(PuntoXY disparo) {
        for (PuntoXY coordenada: coordenadas) {
            if (coordenada.equals(disparo)) {
                coordenada.setTocado(true);
                return true;
            }
        }
        //Ninguna coordenada ha coincidido con el disparo
        return false;
    }

    /* Indica si este Barco ha sido hundido
     * Un Barco está hundido cuando todos sus PuntosXY han sido "tocados".
     * @return True si está hundido, false en caso contrario.
     */

    public boolean esHundido() {
        for (PuntoXY coordenada: coordenadas) {
            if (!coordenada.esTocado()) { //Al menos una coordenada no ha sido tocada
                return false; //Barco no está hundido
            }
        }
        //Si durante el bucle for no se ha devuelto false, es que todas las coord. han sido tocadas
        return true; //Barco hundido
    }

}
