package cl.speedfast.gui;

import cl.speedfast.controller.ControladorPedidos;
import cl.speedfast.dao.PedidoDAO;
import cl.speedfast.model.EstadoPedido;
import cl.speedfast.model.Pedido;
import cl.speedfast.model.TipoPedido;
import cl.speedfast.util.UtilidadesGui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 *
 * Representa la Ventana Lista Pedidos (GUI) de SpeedFast. Extiende de JFrame.
 */
public class VentanaListaPedidos extends JFrame {
    private JPanel panelListaPedidos;
    private JPanel norte;
    private JPanel panelMenu;
    private JButton btnMenu;
    private JLabel txtTitulo;
    private JLabel txtVentana;
    private JTable tablaRegistros;
    private JButton btnSalir;
    private JButton btnActualizarLista;
    private JPanel panelTitulo;
    private JPanel panelTabla;
    private JPanel panelBtn;
    private JScrollPane scrollTabla;
    private JPanel panelListado;
    private JPanel panelFiltros;
    private JButton btnFiltrar;
    private JComboBox cbFiltroTipo;
    private JComboBox cbFiltroEstado;
    private JLabel txtTipo;
    private JLabel txtEstado;
    private JButton btnEditarPedido;
    private JButton btnEliminarPedido;
    private ControladorPedidos controlador;
    private JFrame ventanaPadre;
    private DefaultTableModel modeloTabla;

    /**
     *
     * Constructor de la ventana Lista Pedidos
     *
     * @param controlador Instancia del controlador de pedidos.
     */
    public VentanaListaPedidos(ControladorPedidos controlador, JFrame ventanaPadre) {
        this.controlador = controlador;
        this.ventanaPadre = ventanaPadre;

        configuracion();
        configurarTabla();
        cargarCombosFiltro();
        cargarDatosTabla();
        eventos();
    }

    /**
     *
     * Configuración inicial de la ventana.
     */
    private void configuracion() {
        UtilidadesGui.configurarVentana(
                this,
                panelListaPedidos,
                "SpeedFast S8 | Lista Pedidos",
                600,
                500,
                JFrame.DISPOSE_ON_CLOSE
        );
    }

    /**
     *
     * Define las columnas.
     */
    private void configurarTabla() {
        String[] columnas = {"ID Pedido", "Dirección", "Tipo", "Estado"};

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaRegistros = new JTable(modeloTabla);

        if (scrollTabla != null) {
            scrollTabla.setViewportView(tablaRegistros);
        }
        tablaRegistros.getColumnModel().getColumn(0).setPreferredWidth(30);
    }

    /**
     *
     * Consulta la Base de Datos y llena la tabla con todos los pedidos (sin filtros).
     */
    private void cargarDatosTabla() {
        if (controlador == null) return;
        cargarDatosTabla(controlador.getListaPedidos());
    }

    /**
     *
     * Consulta la Base de Datos y llena la tabla con o sin filtro.
     */
    private void cargarDatosTabla(List<Pedido> pedidos) {
        if (modeloTabla == null) return;

        modeloTabla.setRowCount(0);

        if (pedidos == null || pedidos.isEmpty()) {
            Object[] filaVacia = new Object[]{
                    "Sin registros",
                    "---",
                    "---",
                    "---"
            };
            modeloTabla.addRow(filaVacia);
        } else {
            for (Pedido p : pedidos) {
                Object[] fila = new Object[]{
                        String.valueOf(p.getIdPedido()),
                        p.getDireccionEntrega(),
                        p.getTipo() != null ? p.getTipo().toString() : "",
                        p.getEstado() != null ? p.getEstado().toString() : ""
                };
                modeloTabla.addRow(fila);
            }
        }

        modeloTabla.fireTableDataChanged();
    }

    /**
     *
     * Llena los JComboBox de tipo y estado con los valores de los Enums + "TODOS".
     */
    private void cargarCombosFiltro() {
        cbFiltroTipo.removeAllItems();
        cbFiltroTipo.addItem("TODOS");
        for (TipoPedido tipo : TipoPedido.values()) {
            cbFiltroTipo.addItem(tipo.name());
        }

        cbFiltroEstado.removeAllItems();
        cbFiltroEstado.addItem("TODOS");
        for (EstadoPedido estado : EstadoPedido.values()) {
            cbFiltroEstado.addItem(estado.name());
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

        btnActualizarLista.addActionListener(e -> {
            cargarDatosTabla(controlador.getListaPedidos());
            JOptionPane.showMessageDialog(
                    this,
                    "Tabla de pedidos actualizada correctamente.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        btnFiltrar.addActionListener(e -> {
            PedidoDAO pedido = new PedidoDAO();
            List<Pedido> listaFiltrada;

            if (cbFiltroTipo.getSelectedItem() == null || cbFiltroEstado.getSelectedItem() == null) {
                return;
            }

            String tipoSeleccionado = cbFiltroTipo.getSelectedItem().toString();
            String estadoSeleccionado = cbFiltroEstado.getSelectedItem().toString();

            boolean filtrarTipo = !tipoSeleccionado.equals("TODOS");
            boolean filtrarEstado = !estadoSeleccionado.equals("TODOS");

            try {
                if (filtrarTipo && filtrarEstado) {
                    TipoPedido tipo = TipoPedido.valueOf(tipoSeleccionado);
                    EstadoPedido estado = EstadoPedido.valueOf(estadoSeleccionado);

                    List<Pedido> porTipo = pedido.listarPorTipo(tipo);
                    listaFiltrada = new java.util.ArrayList<>();
                    for (Pedido p : porTipo) {
                        if (p.getEstado() == estado) {
                            listaFiltrada.add(p);
                        }
                    }
                } else if (filtrarTipo) {
                    TipoPedido tipo = TipoPedido.valueOf(tipoSeleccionado);
                    listaFiltrada = pedido.listarPorTipo(tipo);
                } else if (filtrarEstado) {
                    EstadoPedido estado = EstadoPedido.valueOf(estadoSeleccionado);
                    listaFiltrada = pedido.listarPorEstado(estado);
                } else {
                    listaFiltrada = pedido.listarTodos();
                }

                cargarDatosTabla(listaFiltrada);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error al aplicar los filtros: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        btnEliminarPedido.addActionListener(e -> {
            int filaSeleccionada = tablaRegistros.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor seleccione un pedido para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Object valorCelda = tablaRegistros.getValueAt(filaSeleccionada, 0);
            if (valorCelda.toString().equals("Sin registros")) {
                JOptionPane.showMessageDialog(this, "No hay registros válidos seleccionados para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idPedido = Integer.parseInt(valorCelda.toString());

            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar el pedido #" + idPedido + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                PedidoDAO dao = new PedidoDAO();
                if (dao.eliminar(idPedido)) {
                    JOptionPane.showMessageDialog(this, "Pedido eliminado correctamente.");
                    cargarDatosTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "No se puede eliminar el pedido. Verifique que no tenga entregas asociadas.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnEditarPedido.addActionListener(e -> {
            int filaSeleccionada = tablaRegistros.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor seleccione un pedido para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Object valorCelda = tablaRegistros.getValueAt(filaSeleccionada, 0);
            if (valorCelda.toString().equals("Sin registros")) {
                JOptionPane.showMessageDialog(this, "No hay registros válidos seleccionados para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idPedido = Integer.parseInt(valorCelda.toString());
            String direccionActual = tablaRegistros.getValueAt(filaSeleccionada, 1).toString();
            String tipoStr = tablaRegistros.getValueAt(filaSeleccionada, 2).toString();
            String estadoStr = tablaRegistros.getValueAt(filaSeleccionada, 3).toString();

            JTextField txtDireccionDlg = new JTextField(direccionActual, 20);
            JComboBox<TipoPedido> cbTipoDlg = new JComboBox<>(TipoPedido.values());
            cbTipoDlg.setSelectedItem(TipoPedido.valueOf(tipoStr));

            JComboBox<EstadoPedido> cbEstadoDlg = new JComboBox<>(EstadoPedido.values());
            cbEstadoDlg.setSelectedItem(EstadoPedido.valueOf(estadoStr));

            JPanel panelEdicion = new JPanel();
            panelEdicion.setLayout(new BoxLayout(panelEdicion, BoxLayout.Y_AXIS));
            panelEdicion.add(new JLabel("Dirección:"));
            panelEdicion.add(txtDireccionDlg);
            panelEdicion.add(Box.createVerticalStrut(10));
            panelEdicion.add(new JLabel("Tipo de Pedido:"));
            panelEdicion.add(cbTipoDlg);
            panelEdicion.add(Box.createVerticalStrut(10));
            panelEdicion.add(new JLabel("Estado:"));
            panelEdicion.add(cbEstadoDlg);

            int resultado = JOptionPane.showConfirmDialog(this, panelEdicion, "Editar Pedido #" + idPedido, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (resultado == JOptionPane.OK_OPTION) {
                String nuevaDireccion = txtDireccionDlg.getText().trim();
                if (nuevaDireccion.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "La dirección no puede estar vacía.", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Pedido pedidoActualizado = new Pedido(idPedido, nuevaDireccion, (TipoPedido) cbTipoDlg.getSelectedItem(), (EstadoPedido) cbEstadoDlg.getSelectedItem());
                PedidoDAO dao = new PedidoDAO();

                if (dao.actualizar(pedidoActualizado)) {
                    JOptionPane.showMessageDialog(this, "Pedido actualizado con éxito.");
                    cargarDatosTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar el pedido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        UtilidadesGui.cerrarPrograma(btnSalir, this);
    }
}
