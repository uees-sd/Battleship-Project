package Interfaz;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DialogoCrearBarcos extends JDialog {

    // Modelo
    private TabLero tablero;
    private int limiteBarcos;

    // Vista
    public PanelTablero panelTablero;
    private JComboBox<String> listaBarcos;
    public JButton botonTerminar;
    private JButton botonCancelar;

    public DialogoCrearBarcos(Frame parent, boolean modal, TabLero tablero) {
        super(parent, modal);
        this.tablero = tablero;

        setLayout(new BorderLayout());
        add(new PanelNorte(), BorderLayout.NORTH);
        add(new PanelCentro(), BorderLayout.CENTER);
        add(new PanelSur(), BorderLayout.SOUTH);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private class PanelNorte extends JPanel {

        private final int TIEMPO_MAXIMO = 60; // Tiempo máximo en segundos
        private int tiempoRestante;

        public PanelNorte() {
            tiempoRestante = TIEMPO_MAXIMO;

            // Crear y configurar la JProgressBar
            JProgressBar progressBar = new JProgressBar(0, TIEMPO_MAXIMO);
            progressBar.setValue(tiempoRestante);
            progressBar.setStringPainted(true);

            // Configurar el temporizador
            Timer timer = new Timer(1000, new ActionListener() { // Actualiza cada segundo
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (tiempoRestante > 0) {
                        tiempoRestante--;
                        progressBar.setValue(tiempoRestante);
                    } else {
                        ((Timer)e.getSource()).stop(); // Detener el temporizador
                        JOptionPane.showMessageDialog(DialogoCrearBarcos.this,
                                "Se agotó tu tiempo", "Tiempo agotado", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
            timer.start();

            // Configuración del panel
            setLayout(new BorderLayout());
            add(progressBar, BorderLayout.CENTER);
            setBorder(BorderFactory.createTitledBorder("Temporizador"));
        }
    }

    private class PanelCentro extends JPanel {

        public PanelCentro() {
            panelTablero = new PanelTablero(tablero);
            panelTablero.setTableroModelo();
            // Añadimos listener a las Celdas
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    panelTablero.celdas[i][j].addMouseListener(new ClickCelda());
                }
            }
            add(panelTablero);

            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(20, 20, 20, 20),
                    BorderFactory.createBevelBorder(BevelBorder.RAISED)));
        }
    }

    private class PanelSur extends JPanel {

        public PanelSur() {
            botonTerminar = new JButton("Terminar");
            botonTerminar.setEnabled(false);
            botonCancelar = new JButton("Cancelar");
            botonCancelar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cerrarDialogo();
                }
            });

            JPanel izq = new JPanel();
            izq.add(botonTerminar);
            JPanel der = new JPanel();
            der.add(botonCancelar);

            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(izq);
            add(der);
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        }
    }

    /*
     * Cierra este JDialog, previa confirmación del usuario.
     */

    private void cerrarDialogo() {
        int respuesta = JOptionPane.showConfirmDialog(null, "¿Seguro que desea Cancelar?",
                "Colocar Barcos", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION)
            dispose();
    }

    // Clase MouseListener para las Celdas
    class ClickCelda implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            //Consultamos código barco seleccionado
            String codigo = ((String) listaBarcos.getSelectedItem()).substring(0, 2);
            //Celda en la que se ha hecho click
            PanelTablero.Celda cel = (PanelTablero.Celda) e.getSource();
            ///Sus coordenadas
            int x = cel.coord.x;
            int y = cel.coord.y;
            //Consultamos a la matriz del tablero modelo si está libre o no esta Celda
            if (tablero.tablero[x][y] == 1)
                JOptionPane.showMessageDialog(null, "Ya hay un Barco en esta casilla",
                        "Colocar Barcos", JOptionPane.WARNING_MESSAGE);
            else {
                //Pedimos orientacion
                String[] opciones = new String[]{"Vertical", "Horizontal"};
                int orientacion = JOptionPane.showOptionDialog(null, "Elija orientación",
                        "Colocar Barcos", 0, JOptionPane.QUESTION_MESSAGE, null, opciones, 0);

                colocarBarco(codigo, orientacion, cel);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    /*
     * Intentará colocar el Barco correspondiente al código recibido,
     * en la orientación indicada, a partir de la Celda especificada.
     * Se apoya en los métodos colocarVertical() y colocarHorizontal().
     * @param codigo String que identifica el tipo de Barco
     * @param orientacion Si el valor es 0 será vertical y 1 será horizontal.
     * @param celda Celda a partir de la cuál se intentará colocar el Barco
     */

    private void colocarBarco(String codigo, int orientacion, PanelTablero.Celda celda) {
        boolean colocado;

        if (orientacion == 0) // Vertical
            colocado = colocarVertical(codigo, celda);
        else
            colocado = colocarHorizontal(codigo, celda);

        // Si se ha colocado el Barco, comprobamos si se permite poner más
        if (colocado) {
            limiteBarcos--;

            if (limiteBarcos == 0) {
                listaBarcos.setEnabled(false);
                panelTablero.desactivarListener();
                botonTerminar.setEnabled(true);
            } else {
                // Se permiten más barcos, pero no el mismo que acabamos de poner.
                if (codigo.equals("DT")) {
                    // Eliminar el Destructor de la lista solo si ya hay dos colocados (en el caso de dificultad EASY, anteriormente)
                    if (tablero.hayDosDestructores()) {
                        listaBarcos.removeItem("DT-Destructor");
                    }
                } else {
                    // No es destructor, eliminamos el Barco correspondiente al código
                    listaBarcos.removeItem(listaBarcos.getSelectedItem());
                }
            }
        }
    }


    /*
     * Intentará colocar el barco indicado en la columna vertical de la Celda
     * donde el usuario ha clickado. Dicha Celda será el origen y se intentará
     * colocar primero desde ese origen hacia abajo. Si no fuera posible se
     * intentará hacia arriba. Si consigue colocar el Barco, quedará registrado
     * en la Lista de Barcos del Tablero(modelo).
     * Este método se apoya en los métodos comprobarVerticalAbajo() y
     * comprobarVerticalArriba()
     * @param codigo String para indicar tipo de Barco
     * @param origen Celda a partir de la cuál se intentará colocar el Barco.
     * @return True si se colocó con éxito, False en caso contrario
     */

    private boolean colocarVertical(String codigo, PanelTablero.Celda origen) {
        //Calculamos casillas restantes necesarias según tipo de barco
        int casillas = 0;
        switch(codigo) {
            case "PA": //PortaAviones 5 casillas, necesitamos 4 más la Celda origen
                casillas = 4;
                break;
            case "AZ": //Acorazado 4 casillas, 3 más queremos
                casillas = 3;
                break;
            case "SM": //Submarino 3 casillas, 2 más
                casillas = 2;
                break;
            case "DT": //Destructor 2 casillas, 1 más
                casillas = 1;
                break;
        }

        //Intentamos colocar hacia abajo primero, y si no, hacia arriba
        if (comprobarVerticalAbajo(origen, casillas)) {
            //Colocamos Barco desde la casilla indicada, hacia abajo
            Barco barco = new Barco(codigo);
            int y = origen.coord.y;
            int x = origen.coord.x;
            for (int i = x; i <= (x + casillas); i++)
                barco.addCoordenada(new PuntoXY(i, y)); //Añadimos coordenada al Barco
            /*
             * Barco construido, lo añadimos a la lista de Barcos del Tablero.
             * Este método para añadir, se encarga también de actualizar
             * la matriz del Tablero poniendo valor 1 ahí donde ocupa el Barco.
             */
            tablero.agregarBarco(barco);
            panelTablero.setTableroModelo(); //Actualizamos la Vista para mostrar nuevo Barco
            return true;
        }
        else if (comprobarVerticalArriba(origen, casillas)) {
            Barco barco = new Barco(codigo);
            int y = origen.coord.y;
            int x = origen.coord.x;
            for (int i = x; i >= (x - casillas); i--)
                barco.addCoordenada(new PuntoXY(i, y));

            tablero.agregarBarco(barco);
            panelTablero.setTableroModelo(); //Actualizamos la Vista para mostrar nuevo Barco
            return true;
        }
        else {
            JOptionPane.showMessageDialog(null, "No hay espacio disponible en esta vertical"
                    + "\nPruebe otra Celda como origen","Colocar Barco", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    /*
     * Comprueba si hay espacio en la vertical inferior a partir
     * de la Celda considerada como origen.
     * @param origen Celda en la que se ha clickado
     * @param casillas Cantidad de casillas necesarias además de la
     * ocupada por la Celda origen.
     * @return True si hay espacio libre, False si no hay espacio.
     */

    private boolean comprobarVerticalAbajo(PanelTablero.Celda origen, int casillas) {
        int y = origen.coord.y;
        int x = origen.coord.x;
        if (7 - x >= casillas) {
            //Hay espacio hacia abajo, pero hay que comprobar si está libre.
            for (int i = x; i <= (x + casillas); i++)
                if (tablero.tablero[i][y] != 0)
                    return false; //Hay otro barco, no se puede colocar
            //Si bucle for no retorna false, es que hay hueco libre
            return true;
        }
        else
            return false; //No hay tablero suficiente
    }

    /*
     * Comprueba si hay espacio en la vertical superior a partir
     * de la Celda considerada como origen.
     * @param origen Celda en la que se ha clickado
     * @param casillas Cantidad de casillas necesarias además de la
     * ocupada por la Celda origen.
     * @return True si hay espacio libre, False si no hay espacio.
     */

    private boolean comprobarVerticalArriba(PanelTablero.Celda origen, int casillas) {
        int y = origen.coord.y;
        int x = origen.coord.x;
        if (x - 0 >= casillas) {
            for (int i = x; i >= (x - casillas); i--)
                if (tablero.tablero[i][y] != 0)
                    return false;

            return true;
        }
        else
            return false;
    }

    /*
     * Intentará colocar el barco indicado en la columna horizontal de la Celda
     * donde el usuario ha clickado. Dicha Celda será el origen y se intentará
     * colocar primero desde ese origen hacia derecha. Si no fuera posible se
     * intentará hacia izquierda. Si consigue colocar el Barco, quedará registrado
     * en la Lista de Barcos del Tablero(modelo).
     * Este método se apoya en los métodos comprobarHorizontalDerecha() y
     * comprobarHorizontalIzquierda()
     * @param codigo String para indicar tipo de Barco
     * @param origen Celda a partir de la cuál se intentará colocar el Barco.
     * @return True si se colocó con éxito, False en caso contrario
     */

    private boolean colocarHorizontal(String codigo, PanelTablero.Celda origen) {
        //Calculamos casillas restantes necesarias según tipo de barco
        int casillas = 0;
        switch(codigo) {
            case "PA": //PortaAviones 5 casillas, necesitamos 4 más la Celda origen
                casillas = 4;
                break;
            case "AZ": //Acorazado 4 casillas, 3 más queremos
                casillas = 3;
                break;
            case "SM": //Submarino 3 casillas, 2 más
                casillas = 2;
                break;
            case "DT": //Destructor 2 casillas, 1 más
                casillas = 1;
                break;
        }

        if (comprobarHorizontalDerecha(origen, casillas)) {
            //Colocamos Barco desde la casilla indicada, hacia derecha
            Barco barco = new Barco(codigo);
            int y = origen.coord.y;
            int x = origen.coord.x;
            for (int i = y; i <= (y + casillas); i++)
                barco.addCoordenada(new PuntoXY(x, i)); //Añadimos coordenada al Barco
            /*
             * Barco construido, lo añadimos a la lista de Barcos del Tablero.
             * Este método para añadir, se encarga también de actualizar
             * la matriz del Tablero poniendo valor 1 ahí donde ocupa el Barco.
             */
            tablero.agregarBarco(barco);
            panelTablero.setTableroModelo(); //Actualizamos la Vista para mostrar nuevo Barco
            return true;
        }
        else if (comprobarHorizontalIzquierda(origen, casillas)) {
            Barco barco = new Barco(codigo);
            int y = origen.coord.y;
            int x = origen.coord.x;
            for (int i = y; i >= (y - casillas); i--)
                barco.addCoordenada(new PuntoXY(x, i));

            tablero.agregarBarco(barco);
            panelTablero.setTableroModelo(); //Actualizamos la Vista para mostrar nuevo Barco
            return true;
        }
        else {
            JOptionPane.showMessageDialog(null, "No hay espacio disponible en esta horizontal"
                    + "\nPruebe otra Celda como origen","Colocar Barco", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    /*
     * Comprueba si hay espacio en la horizontal derecha a partir
     * de la Celda considerada como origen.
     * @param origen Celda en la que se ha clickado
     * @param casillas Cantidad de casillas necesarias además de la
     * ocupada por la Celda origen.
     * @return True si hay espacio libre, False si no hay espacio.
     */

    private boolean comprobarHorizontalDerecha(PanelTablero.Celda origen, int casillas) {
        int y = origen.coord.y;
        int x = origen.coord.x;
        if ((7 - y) >= casillas) {
            for (int i = y; i <= (y + casillas); i++)
                if (tablero.tablero[x][i] != 0)
                    return false;

            return true;
        }
        else
            return false;
    }

    /*
     * Comprueba si hay espacio en la horizontal izquierda a partir
     * de la Celda considerada como origen.
     * @param origen Celda en la que se ha clickado
     * @param casillas Cantidad de casillas necesarias además de la
     * ocupada por la Celda origen.
     * @return True si hay espacio libre, False si no hay espacio.
     */

    private boolean comprobarHorizontalIzquierda(PanelTablero.Celda origen, int casillas) {
        int y = origen.coord.y;
        int x = origen.coord.x;
        if ((y - 0) >= casillas) {
            for (int i = y; i >= (y - casillas); i--)
                if (tablero.tablero[x][i] != 0)
                    return false;

            return true;
        }
        else
            return false;
    }
}
