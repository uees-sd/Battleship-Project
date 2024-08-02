package com.example.Controllers.Preguntas;

import java.sql.*;

public class ConexionBD {

    // Conexion a bd
    private Connection con;

    public ConexionBD(String nombre) {
        try {
            // Carga driver de PostgreSQL
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró el driver de PostgreSQL.");
            e.printStackTrace();
        }

        try {
            // URL de conexión a la base de datos battleship asve
            String url = "jdbc:postgresql://localhost:5432/" + nombre;

            // Establecer la conexión utilizando DriverManager
            con = DriverManager.getConnection(url, "asve", "asdf");
            checkForWarning(con.getWarnings());

            // Obtener metadatos de la base de datos
            DatabaseMetaData dma = con.getMetaData();

            System.out.println("\nConectado a: " + dma.getURL());
            System.out.println("Driver: " + dma.getDriverName());
            System.out.println("Versión del Driver: " + dma.getDriverVersion());
            System.out.println("");

        } catch (SQLException ex) {
            System.out.println("\n*** SQLException capturada ***\n");
            while (ex != null) {
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("Mensaje: " + ex.getMessage());
                System.out.println("Código de Error: " + ex.getErrorCode());
                ex = ex.getNextException();
                System.out.println("");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Advertenciass
    private static boolean checkForWarning(SQLWarning warn) throws SQLException {
        boolean rc = false;

        if (warn != null) {
            System.out.println("\n *** Advertencia ***\n");
            rc = true;
            while (warn != null) {
                System.out.println("SQLState: " + warn.getSQLState());
                System.out.println("Mensaje: " + warn.getMessage());
                System.out.println("Código de Error: " + warn.getErrorCode());
                System.out.println("");
                warn = warn.getNextWarning();
            }
        }
        return rc;
    }

    // Método para obtener la conexión
    public Connection getConnection() {
        return con;
    }

    // Método para cerrar la conexión con la base de datos
    public void cerrarConexion() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("Conexión con la Base de Datos cerrada.");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión con la Base de Datos.");
            e.printStackTrace();
        }
    }
}
