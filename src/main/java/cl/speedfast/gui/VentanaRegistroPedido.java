package cl.speedfast.gui;

import cl.speedfast.controller.ControladorPedidos;
import cl.speedfast.model.EstadoPedido;
import cl.speedfast.model.Pedido;
import cl.speedfast.model.TipoPedido;
import cl.speedfast.util.UtilidadesGui;

import javax.swing.*;

/**
 *
 * Representa la Ventana de Registro de Pedidos (GUI) de SpeedFast. Extiende de JFrame.
 */
public class VentanaRegistroPedido extends JFrame {
    private JPanel panelRegistroPedido;
    private JPanel norte;
    private JButton btnMenu;
    private JTextField inputDireccion;
    private JComboBox cbxTipo;
    private JLabel txtDireccion;
    private JLabel txtTipo;
    private JButton btnLimpiar;
    private JPanel panelGuardar;
    private JPanel panelDireccion;
    private JPanel panelTipo;
    private JPanel panelMenu;
    private JPanel panelTitulo;
    private JButton btnGuardar;
    private JButton btnSalir;
    private JLabel txtVentana;
    private JPanel panelContenedor;
    private JPanel panelRegistro;
    private JComboBox cbxEstado;
    private JLabel txtEstado;
    private ControladorPedidos controlador;
    private JFrame ventanaPadre;

    /**
     *
     * Constructor de la ventana Registro Pedidos.
     *
     * @param controlador Instancia del controlador de pedidos.
     */
    public VentanaRegistroPedido(ControladorPedidos controlador, JFrame ventanaPadre) {
        this.controlador = controlador;
        this.ventanaPadre = ventanaPadre;

        configuracion();
        cargarCbxTipo();
        cargarCbxEstado();
        eventos();
    }

    /**
     *
     * Configuración inicial de la ventana.
     */
    private void configuracion() {
        UtilidadesGui.configurarVentana(
                this,
                panelRegistroPedido,
                "SpeedFast S8 | Registro Pedidos",
                600,
                500,
                JFrame.DISPOSE_ON_CLOSE
        );
    }

    /**
     *
     * Llena el ComboBox con las opciones del enum TipoPedido.
     */
    private void cargarCbxTipo() {
        if (cbxTipo != null) {
            cbxTipo.setModel(new DefaultComboBoxModel<>(TipoPedido.values()));
        }
    }

    /**
     *
     * Llena el ComboBox con las opciones del enum EstadoPedido.
     */
    private void cargarCbxEstado() {
        if (cbxEstado != null) {
            cbxEstado.setModel(new DefaultComboBoxModel<>(EstadoPedido.values()));
        }
    }

    /**
     *
     * Eventos de la ventana.
     */
    public void eventos() {
        UtilidadesGui.volverAlMenuAlCerrar(this, ventanaPadre);

        btnMenu.addActionListener(e -> {
            if (ventanaPadre != null) {
                ventanaPadre.setVisible(true);
            }
            this.dispose();
        });

        btnGuardar.addActionListener(e -> {
            String direccion = inputDireccion.getText().trim();
            TipoPedido tipo = (TipoPedido) cbxTipo.getSelectedItem();
            EstadoPedido estado = (EstadoPedido) cbxEstado.getSelectedItem();

            if (direccion.isEmpty() || tipo == null || estado == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Debes llenar todos los campos.",
                        "Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            Pedido pedidoRegistrado = controlador.registrarPedido(direccion, tipo, estado);

            if (pedidoRegistrado != null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Pedido #" + pedidoRegistrado.getIdPedido() + " registrado de manera correcta.",
                        "Registro Exitoso",
                        JOptionPane.INFORMATION_MESSAGE
                );

                inputDireccion.setText("");
                cbxTipo.setSelectedIndex(0);
                cbxEstado.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo registrar el pedido. Intente nuevamente.",
                        "Error de Persistencia",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        btnLimpiar.addActionListener(e -> {
            inputDireccion.setText("");
            if (cbxTipo != null && cbxTipo.getItemCount() > 0) {
                cbxTipo.setSelectedIndex(0);
            }
            if (cbxEstado != null && cbxEstado.getItemCount() > 0) {
                cbxEstado.setSelectedIndex(0);
            }
        });

    }
}
