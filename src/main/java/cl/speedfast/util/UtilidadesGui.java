package cl.speedfast.util;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * Representa la utilidades de apoyo para la GUI de SpeedFast.
 */
public class UtilidadesGui {

    /**
     * Configura los parámetros iniciales de la ventana (JFrame).
     *
     * @param ventana        Ventana que se va a configurar.
     * @param panelPrincipal Contenedor principal de la ventana.
     * @param titulo         Título de la ventana.
     * @param ancho          Ancho ventana.
     * @param alto           Alto ventana.
     * @param modoCierre     Cierre.
     */
    public static void configurarVentana(JFrame ventana, JPanel panelPrincipal, String titulo, int ancho, int alto, int modoCierre) {
        if (ventana == null || panelPrincipal == null) {
            return;
        }

        ventana.setContentPane(panelPrincipal);
        ventana.setTitle(titulo);
        ventana.setSize(ancho, alto);
        ventana.setDefaultCloseOperation(modoCierre);
        ventana.setLocationRelativeTo(null);
    }

    /**
     *
     * Confirma si desea cerrar el programa. Sí lo cierra. No vuelve al programa.
     *
     * @param boton   Botón que cerrará el programa.
     * @param ventana Ventana en la que se encuentra el botón.
     */
    public static void cerrarPrograma(JButton boton, JFrame ventana) {
        if (boton == null || ventana == null) {
            return;
        }

        boton.addActionListener(e -> {
            int confirmarSalir = JOptionPane.showConfirmDialog(
                    ventana,
                    "¿Está seguro que desea salir?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirmarSalir == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    /**
     *
     * Fuerza a que la posición de la ventana quede en el centro al activarse (visible).
     *
     * @param ventana Objeto Ventana a centrar.
     */
    public static void centrarVentana(JFrame ventana) {
        if (ventana == null) return;

        ventana.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowActivated(java.awt.event.WindowEvent e) {
                ventana.setLocationRelativeTo(null);
            }
        });
    }

    /**
     *
     * Cuando se presiona la X en una ventana secundaria, se cierra y es visible la ventana principal
     *
     * @param ventanaActual Ventana actual visible.
     * @param ventanaPadre  Ventana a la que se regresa.
     */
    public static void volverAlMenuAlCerrar(JFrame ventanaActual, JFrame ventanaPadre) {
        if (ventanaActual == null) return;

        ventanaActual.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (ventanaPadre != null) {
                    ventanaPadre.setLocationRelativeTo(null);
                    ventanaPadre.setVisible(true);
                }
            }

            @Override
            public void windowActivated(WindowEvent e) {
                ventanaActual.setLocationRelativeTo(null);
            }
        });
    }
}
