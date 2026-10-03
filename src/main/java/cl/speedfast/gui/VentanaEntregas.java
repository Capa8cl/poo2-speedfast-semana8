package cl.speedfast.gui;

import cl.speedfast.controller.ControladorPedidos;
import cl.speedfast.dao.EntregaDAO;
import cl.speedfast.dao.PedidoDAO;
import cl.speedfast.dao.RepartidorDAO;
import cl.speedfast.model.Entrega;
import cl.speedfast.model.Pedido;
import cl.speedfast.model.Repartidor;
import cl.speedfast.util.UtilidadesGui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class VentanaEntregas extends JFrame {
    private JPanel panelEntregas;
    private JButton btnMenu;
    private JPanel norte;
    private JPanel panelMenu;
    private JPanel panelTitulo;
    private JLabel txtTitulo;
    private JLabel txtVentana;
    private JTable tablaEntregas;
    private JButton btnSalir;
    private JButton btnActualizarLista;
    private JButton btnGuardarEntrega;
    private JButton btnEliminarEntrega;
    private JComboBox<ComboItem> cbPedidos;
    private JComboBox<ComboItem> cbRepartidores;
    private JLabel txtHora;
    private JPanel panelListado;
    private JPanel panelBtn;
    private JButton btnEditarEntrega;
    private JPanel panelTabla;
    private JTextField inputHora;
    private JLabel txtPedidos;
    private JLabel txtRepartidores;
    private JLabel txtFecha;
    private JTextField inputFecha;
    private JScrollPane scrollTabla;
    private DefaultTableModel modeloTabla;
    private JFrame ventanaPadre;
    private Integer idEntregaEnEdicion = null;

    /**
     *
     * Constructor de la ventana Entregas.
     *
     * @param controlador
     * @param ventanaPadre Ventana principal o menú anterior.
     */
    public VentanaEntregas(ControladorPedidos controlador, JFrame ventanaPadre) {
        this.ventanaPadre = ventanaPadre;

        configuracion();
        configurarTabla();
        cargarCombos();
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
                panelEntregas,
                "SpeedFast S8 | Gestión de Entregas",
                700,
                500,
                JFrame.DISPOSE_ON_CLOSE
        );
    }

    /**
     *
     * Define las columnas de la tabla de entregas.
     */
    private void configurarTabla() {
        String[] columnas = {"ID Entrega", "ID Pedido", "ID Repartidor", "Fecha", "Hora", "Estado Pedido"};

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaEntregas = new JTable(modeloTabla);

        if (scrollTabla != null) {
            scrollTabla.setViewportView(tablaEntregas);
        }
        tablaEntregas.getColumnModel().getColumn(0).setPreferredWidth(20);
        tablaEntregas.getColumnModel().getColumn(1).setPreferredWidth(20);
        tablaEntregas.getColumnModel().getColumn(2).setPreferredWidth(20);
        tablaEntregas.getColumnModel().getColumn(3).setPreferredWidth(30);
        tablaEntregas.getColumnModel().getColumn(4).setPreferredWidth(30);
    }

    /**
     *
     * Llena los JComboBox consultando la base de datos.
     */
    private void cargarCombos() {
        if (cbPedidos != null) {
            cbPedidos.removeAllItems();
            PedidoDAO pedidoDAO = new PedidoDAO();
            List<Pedido> pedidos = pedidoDAO.listarTodos();
            for (Pedido p : pedidos) {
                String desc = "Pedido #" + p.getIdPedido() + " - " + p.getDireccionEntrega();
                cbPedidos.addItem(new ComboItem(p.getIdPedido(), desc));
            }
        }

        if (cbRepartidores != null) {
            cbRepartidores.removeAllItems();
            RepartidorDAO repartidorDAO = new RepartidorDAO();
            List<Repartidor> repartidores = repartidorDAO.listarTodos();
            for (Repartidor r : repartidores) {
                String desc = "Repartidor #" + r.getIdRepartidor() + " - " + r.getNombreRepartidor();
                cbRepartidores.addItem(new ComboItem(r.getIdRepartidor(), desc));
            }
        }
    }

    /**
     *
     * Consulta la Base de Datos y llena la tabla de entregas.
     */
    public void cargarDatosTabla() {
        if (modeloTabla == null) return;

        modeloTabla.setRowCount(0);
        EntregaDAO entregaDAO = new EntregaDAO();
        PedidoDAO pedidoDAO = new PedidoDAO();
        List<Entrega> entregas = entregaDAO.listarTodos();

        if (entregas == null || entregas.isEmpty()) {
            Object[] filaVacia = new Object[]{"Sin registros", "---", "---", "---", "---", "---"};
            modeloTabla.addRow(filaVacia);
        } else {
            DateTimeFormatter formatterVisual = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            for (Entrega e : entregas) {
                String fechaFormateada = "";
                if (e.getFecha() != null) {
                    fechaFormateada = e.getFecha().toLocalDate().format(formatterVisual);
                }

                Pedido p = pedidoDAO.listarTodos().stream()
                        .filter(ped -> ped.getIdPedido() == e.getIdPedido())
                        .findFirst()
                        .orElse(null);

                String estadoPedidoStr = (p != null && p.getEstado() != null) ? p.getEstado().name() : "---";

                Object[] fila = new Object[]{
                        e.getId(),
                        e.getIdPedido(),
                        e.getIdRepartidor(),
                        fechaFormateada,
                        e.getHora(),
                        estadoPedidoStr
                };
                modeloTabla.addRow(fila);
            }
        }

        modeloTabla.fireTableDataChanged();
    }

    /**
     *
     * Clase auxiliar manejar los JComboBox guardando el ID y mostrar texto legible.
     */
    private static class ComboItem {
        private int id;
        private String texto;

        public ComboItem(int id, String texto) {
            this.id = id;
            this.texto = texto;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return texto;
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
            cargarCombos();
            cargarDatosTabla();
            idEntregaEnEdicion = null;
            JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
        });

        btnGuardarEntrega.addActionListener(e -> {
            try {
                ComboItem pedidoSeleccionado = (ComboItem) cbPedidos.getSelectedItem();
                ComboItem repartidorSeleccionado = (ComboItem) cbRepartidores.getSelectedItem();

                if (pedidoSeleccionado == null || repartidorSeleccionado == null) {
                    JOptionPane.showMessageDialog(this, "Debe seleccionar un pedido y un repartidor (o crearlo).", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int idPedido = pedidoSeleccionado.getId();
                int idRepartidor = repartidorSeleccionado.getId();

                String fechaStr = inputFecha.getText().trim();
                String horaStr = inputHora.getText().trim();

                if (fechaStr.isEmpty() || horaStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Debe ingresar fecha (DD-MM-AAAA) y hora (HH:MM:SS).", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!fechaStr.matches("^\\d{1,2}-\\d{1,2}-\\d{4}$")) {
                    JOptionPane.showMessageDialog(this, "Formato de fecha inválido.\nUse estrictamente: DD-MM-AAAA", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("d-M-yyyy");
                LocalDate localDate = LocalDate.parse(fechaStr, formatterInput);
                Date fecha = Date.valueOf(localDate);
                Time hora = Time.valueOf(horaStr.length() == 5 ? horaStr + ":00" : horaStr);

                Entrega nuevaEntrega = new Entrega(0, idPedido, idRepartidor, fecha, hora);
                EntregaDAO dao = new EntregaDAO();

                if (dao.guardar(nuevaEntrega)) {
                    JOptionPane.showMessageDialog(this, "Entrega registrada con éxito.");
                    cargarDatosTabla();
                    inputFecha.setText("");
                    inputHora.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al registrar la entrega en la BD.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (DateTimeParseException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Fecha u hora inválida (verifique que el día y mes existan).\nUse: DD-MM-AAAA y HH:MM:SS", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEliminarEntrega.addActionListener(e -> {
            int filaSeleccionada = tablaEntregas.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor seleccione una entrega de la tabla para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Object valorCelda = tablaEntregas.getValueAt(filaSeleccionada, 0);
            if (valorCelda instanceof String) {
                JOptionPane.showMessageDialog(this, "No hay registros válidos seleccionados para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idEntrega = (int) valorCelda;

            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar la entrega ID #" + idEntrega + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                EntregaDAO dao = new EntregaDAO();
                if (dao.eliminar(idEntrega)) {
                    JOptionPane.showMessageDialog(this, "Entrega eliminada correctamente.");
                    cargarDatosTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar la entrega.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnEditarEntrega.addActionListener(e -> {
            int filaSeleccionada = tablaEntregas.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor seleccione una entrega de la tabla para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Object valorCelda = tablaEntregas.getValueAt(filaSeleccionada, 0);
            if (valorCelda instanceof String) {
                JOptionPane.showMessageDialog(this, "No hay registros válidos seleccionados para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int idEntrega = (int) tablaEntregas.getValueAt(filaSeleccionada, 0);
                int idPedidoActual = (int) tablaEntregas.getValueAt(filaSeleccionada, 1);
                int idRepartidorActual = (int) tablaEntregas.getValueAt(filaSeleccionada, 2);
                String fechaActual = tablaEntregas.getValueAt(filaSeleccionada, 3).toString();
                String horaActual = tablaEntregas.getValueAt(filaSeleccionada, 4).toString();

                boolean editadoConExito = false;
                while (!editadoConExito) {
                    JComboBox<ComboItem> cbPedidosDlg = new JComboBox<>();
                    JComboBox<ComboItem> cbRepartidoresDlg = new JComboBox<>();
                    JComboBox<cl.speedfast.model.EstadoPedido> cbEstadoPedidoDlg = new JComboBox<>(cl.speedfast.model.EstadoPedido.values());
                    JTextField txtFechaDlg = new JTextField(fechaActual, 10);
                    JTextField txtHoraDlg = new JTextField(horaActual, 8);

                    PedidoDAO pedidoDAO = new PedidoDAO();
                    for (Pedido p : pedidoDAO.listarTodos()) {
                        ComboItem item = new ComboItem(p.getIdPedido(), "Pedido #" + p.getIdPedido() + " - " + p.getDireccionEntrega());
                        cbPedidosDlg.addItem(item);
                        if (p.getIdPedido() == idPedidoActual) {
                            cbPedidosDlg.setSelectedItem(item);
                            if (p.getEstado() != null) {
                                cbEstadoPedidoDlg.setSelectedItem(p.getEstado());
                            }
                        }
                    }

                    RepartidorDAO repartidorDAO = new RepartidorDAO();
                    for (Repartidor r : repartidorDAO.listarTodos()) {
                        ComboItem item = new ComboItem(r.getIdRepartidor(), "Repartidor #" + r.getIdRepartidor() + " - " + r.getNombreRepartidor());
                        cbRepartidoresDlg.addItem(item);
                        if (r.getIdRepartidor() == idRepartidorActual) cbRepartidoresDlg.setSelectedItem(item);
                    }

                    JPanel panelEdicion = new JPanel();
                    panelEdicion.setLayout(new BoxLayout(panelEdicion, BoxLayout.Y_AXIS));
                    panelEdicion.add(new JLabel("Pedido:"));
                    panelEdicion.add(cbPedidosDlg);
                    panelEdicion.add(Box.createVerticalStrut(10));
                    panelEdicion.add(new JLabel("Estado del Pedido:"));
                    panelEdicion.add(cbEstadoPedidoDlg);
                    panelEdicion.add(Box.createVerticalStrut(10));
                    panelEdicion.add(new JLabel("Repartidor:"));
                    panelEdicion.add(cbRepartidoresDlg);
                    panelEdicion.add(Box.createVerticalStrut(10));
                    panelEdicion.add(new JLabel("Fecha (DD-MM-AAAA):"));
                    panelEdicion.add(txtFechaDlg);
                    panelEdicion.add(Box.createVerticalStrut(10));
                    panelEdicion.add(new JLabel("Hora (HH:MM:SS):"));
                    panelEdicion.add(txtHoraDlg);

                    int resultado = JOptionPane.showConfirmDialog(
                            this,
                            panelEdicion,
                            "Editar Entrega ID #" + idEntrega,
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );

                    if (resultado != JOptionPane.OK_OPTION) {
                        break;
                    }

                    try {
                        ComboItem pedidoSel = (ComboItem) cbPedidosDlg.getSelectedItem();
                        ComboItem repartidorSel = (ComboItem) cbRepartidoresDlg.getSelectedItem();
                        cl.speedfast.model.EstadoPedido nuevoEstadoPedido = (cl.speedfast.model.EstadoPedido) cbEstadoPedidoDlg.getSelectedItem();

                        String fechaStr = txtFechaDlg.getText().trim();
                        String horaStr = txtHoraDlg.getText().trim();

                        if (fechaStr.isEmpty() || horaStr.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Los campos no pueden estar vacíos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                            fechaActual = fechaStr;
                            horaActual = horaStr;
                            continue;
                        }

                        if (!fechaStr.matches("^\\d{1,2}-\\d{1,2}-\\d{4}$")) {
                            JOptionPane.showMessageDialog(this, "Formato de fecha inválido.\nUse estrictamente: DD-MM-AAAA", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                            fechaActual = fechaStr;
                            horaActual = horaStr;
                            continue;
                        }

                        if (!horaStr.matches("^([01]\\d|2[0-3]):([0-5]\\d)(:([0-5]\\d))?$")) {
                            JOptionPane.showMessageDialog(this, "Formato de hora inválido.\nLas horas deben ser de 00 a 23 y los minutos/segundos de 00 a 59.\nUse: HH:MM:SS", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                            fechaActual = fechaStr;
                            horaActual = horaStr;
                            continue;
                        }

                        DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        LocalDate localDate = LocalDate.parse(fechaStr, formatterInput);
                        Date fecha = Date.valueOf(localDate);
                        Time hora = Time.valueOf(horaStr.length() == 5 ? horaStr + ":00" : horaStr);

                        pedidoDAO.actualizarEstado(pedidoSel.getId(), nuevoEstadoPedido);

                        Entrega entregaActualizada = new Entrega(idEntrega, pedidoSel.getId(), repartidorSel.getId(), fecha, hora);
                        EntregaDAO entregaDAO = new EntregaDAO();

                        if (entregaDAO.actualizar(entregaActualizada)) {
                            JOptionPane.showMessageDialog(this, "Entrega y estado de pedido actualizados con éxito.");
                            cargarDatosTabla();
                            editadoConExito = true;
                        } else {
                            JOptionPane.showMessageDialog(this, "Error al actualizar en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (DateTimeParseException ex) {
                        JOptionPane.showMessageDialog(this, "Formato de fecha inválido.\nUse estrictamente: DD-MM-AAAA", "Error", JOptionPane.ERROR_MESSAGE);
                        fechaActual = txtFechaDlg.getText();
                        horaActual = txtHoraDlg.getText();
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(this, "Formato de hora inválido.\nUse: HH:MM:SS", "Error", JOptionPane.ERROR_MESSAGE);
                        fechaActual = txtFechaDlg.getText();
                        horaActual = txtHoraDlg.getText();
                    }
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al procesar la edición: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        UtilidadesGui.cerrarPrograma(btnSalir, this);
    }
}
