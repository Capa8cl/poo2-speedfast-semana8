package cl.speedfast.gui;

import cl.speedfast.controller.ControladorPedidos;
import cl.speedfast.model.EstadoPedido;
import cl.speedfast.model.Pedido;
import cl.speedfast.model.Repartidor;
import cl.speedfast.util.UtilidadesGui;

import javax.swing.*;
import java.util.List;

/**
 *
 * Representa la Ventana principal (GUI) de SpeedFast. Extiende de JFrame.
 */
public class VentanaPrincipal extends JFrame {
    private JPanel panelPrincipal;
    private JPanel norte;
    private JPanel principal;
    private JLabel tituloPrincipal;
    private JButton btnRegistrarPedido;
    private JButton btnListarPedidos;
    private JButton btnGestionEntregas;
    private JLabel tituloVentanaActual;
    private JButton btnSalir;
    private JButton btnGestionRepartidores;
    private JButton btnIniciarEntregas;

    private ControladorPedidos controlador;

    /**
     *
     * Constructor de la ventana principal
     *
     * @param controlador Instancia del controlador de pedidos.
     */
    public VentanaPrincipal(ControladorPedidos controlador) {
        this.controlador = controlador;
        configuracion();
        eventos();
    }

    /**
     *
     * Configuración inicial de la ventana.
     */
    private void configuracion() {
        UtilidadesGui.configurarVentana(
                this,
                panelPrincipal,
                "SpeedFast S8 | Sumativa 3",
                600,
                500,
                JFrame.EXIT_ON_CLOSE
        );
    }

    /**
     *
     * Eventos de la ventana.
     */
    public void eventos() {
        UtilidadesGui.centrarVentana(this);

        btnRegistrarPedido.addActionListener(e -> {
            VentanaRegistroPedido ventanaRegistro = new VentanaRegistroPedido(controlador, this);
            this.setVisible(false);
            ventanaRegistro.setVisible(true);
        });

        btnListarPedidos.addActionListener(e -> {
            VentanaListaPedidos ventanaListaPedidos = new VentanaListaPedidos(controlador, this);
            this.setVisible(false);
            ventanaListaPedidos.setVisible(true);
        });

        btnGestionRepartidores.addActionListener(e -> {
            VentanaRepartidores ventanaRepartidores = new VentanaRepartidores(controlador, this);
            this.setVisible(false);
            ventanaRepartidores.setVisible(true);
        });

        btnGestionEntregas.addActionListener(e -> {
            VentanaEntregas ventanaEntregas = new VentanaEntregas(controlador, this);
            this.setVisible(false);
            ventanaEntregas.setVisible(true);
        });

        UtilidadesGui.cerrarPrograma(btnSalir, this);
    }
}