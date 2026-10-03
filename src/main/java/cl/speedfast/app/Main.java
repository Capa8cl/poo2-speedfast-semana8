package cl.speedfast.app;

import cl.speedfast.controller.ControladorPedidos;
import cl.speedfast.gui.VentanaPrincipal;
import cl.speedfast.model.*;
import cl.speedfast.util.ConexionBD;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;


/**
 *
 * Sistema de delivery SpeedFast.
 *
 * @author Fabrizio Fernandini
 * @version 1.8
 */
public class Main {
    /**
     *
     * Clase principal del sistema SpeedFast.
     *
     * @param args Argumentos.
     */
    public static void main(String[] args) {
        try (Connection conn = ConexionBD.conectar()) {
            if (conn == null) {
                throw new SQLException("No se pudo establecer la conexión con la base de datos.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error de conexión:\nNo se pudo conectar a la base de datos (speedfast_db).\nVerifique que MySQL esté encendido y las credenciales sean correctas.\n\nDetalles: " + e.getMessage(),
                    "Error de Conexión",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(0);
            return;
        }

        SwingUtilities.invokeLater(() -> {
            ZonaDeCarga zonaDeCarga = new ZonaDeCarga();

            ControladorPedidos controlador = new ControladorPedidos(zonaDeCarga);

            VentanaPrincipal ventanaPrincipal = new VentanaPrincipal(controlador);

            ventanaPrincipal.setVisible(true);
        });
    }
}
