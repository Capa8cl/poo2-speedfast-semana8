package cl.speedfast.gui;

import cl.speedfast.controller.ControladorPedidos;
import cl.speedfast.model.Repartidor;
import cl.speedfast.util.UtilidadesGui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class VentanaRepartidores extends JFrame {

    private JPanel panelRepartidores;
    private JTextField txtNombreRepartidor;
    private JButton btnGuardarRepartidor;
    private JTable tablaRepartidores;
    private DefaultTableModel modeloTabla;
    private JButton btnMenu;
    private JButton btnSalir;
    private ControladorPedidos controlador;
    private JFrame ventanaPadre;
    private JPanel norte;
    private JPanel panelMenu;
    private JPanel panelTitulo;
    private JLabel txtTitulo;
    private JLabel txtVentana;
    private JPanel panelListado;
    private JPanel panelBtn;
    private JButton btnActualizarLista;
    private JPanel panelTabla;
    private JScrollPane scrollTabla;
    private JButton btnEliminarRepartidor;
    private JButton btnEditarRepartidor;

    public VentanaRepartidores(ControladorPedidos controlador, JFrame ventanaPadre) {
        this.controlador = controlador;
        this.ventanaPadre = ventanaPadre;
        configuracion();
        configurarTabla();
        cargarRepartidores();
        eventos();
    }

    private void configuracion() {
        UtilidadesGui.configurarVentana(
                this,
                panelRepartidores,
                "SpeedFast S8 | Gestión de Repartidores",
                600,
                500,
                JFrame.DISPOSE_ON_CLOSE
        );
    }

    private void configurarTabla() {
        modeloTabla = (DefaultTableModel) tablaRepartidores.getModel();
        String[] columnas = {"ID Repartidor", "Nombre"};
        modeloTabla.setColumnIdentifiers(columnas);
        tablaRepartidores.getColumnModel().getColumn(0).setPreferredWidth(100);
        tablaRepartidores.getColumnModel().getColumn(0).setMaxWidth(120);
    }

    private void cargarRepartidores() {
        if (controlador == null || modeloTabla == null) return;

        modeloTabla.setRowCount(0);

        List<Repartidor> repartidores = controlador.getRepartidorDAO().listarTodos();

        if (repartidores == null || repartidores.isEmpty()) {
            Object[] filaVacia = {"Sin registros", "---"};
            modeloTabla.addRow(filaVacia);
        } else {
            for (Repartidor r : repartidores) {
                Object[] fila = {r.getIdRepartidor(), r.getNombreRepartidor()};
                modeloTabla.addRow(fila);
            }
        }

        if (tablaRepartidores != null) {
            tablaRepartidores.revalidate();
            tablaRepartidores.repaint();
        }
    }

    public void eventos() {
        UtilidadesGui.volverAlMenuAlCerrar(this, ventanaPadre);

        btnMenu.addActionListener(e -> {
            if (ventanaPadre != null) {
                ventanaPadre.setVisible(true);
            }
            this.dispose();
        });

        btnGuardarRepartidor.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(
                    this,
                    "Ingrese el nombre del repartidor:",
                    "Registrar Repartidor",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (nombre == null) {
                return;
            }

            String nombreLimpio = nombre.trim();

            if (nombreLimpio.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Debe ingresar un nombre válido para el repartidor.",
                        "Atención",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            Repartidor nuevo = new Repartidor(0, nombreLimpio, controlador.getZonaDeCarga());
            boolean exito = controlador.getRepartidorDAO().guardar(nuevo);

            if (exito) {
                JOptionPane.showMessageDialog(
                        this,
                        "Repartidor '" + nombreLimpio + "' registrado de manera correcta.",
                        "Registrado",
                        JOptionPane.INFORMATION_MESSAGE
                );
                cargarRepartidores();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Error al guardar el repartidor.",
                        "Error BD",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        btnActualizarLista.addActionListener(e -> {
            cargarRepartidores();
            JOptionPane.showMessageDialog(this, "Listado de repartidores actualizado.", "Información", JOptionPane.INFORMATION_MESSAGE);
        });

        btnEliminarRepartidor.addActionListener(e -> {
            int filaSeleccionada = tablaRepartidores.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor seleccione un repartidor de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Object valorCelda = tablaRepartidores.getValueAt(filaSeleccionada, 0);
            if (valorCelda instanceof String) {
                JOptionPane.showMessageDialog(this, "No hay registros válidos seleccionados.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idRepartidor = (int) tablaRepartidores.getValueAt(filaSeleccionada, 0);
            String nombreRepartidor = (String) tablaRepartidores.getValueAt(filaSeleccionada, 1);

            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro de eliminar al repartidor: " + nombreRepartidor + "?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean exito = controlador.getRepartidorDAO().eliminar(idRepartidor);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Repartidor eliminado correctamente.");
                    cargarRepartidores();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "No se puede eliminar el repartidor porque tiene entregas asociadas.\nDebe eliminar la entrega primero.",
                            "Error de Restricción",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });

        btnEditarRepartidor.addActionListener(e -> {
            int filaSeleccionada = tablaRepartidores.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor seleccione un repartidor de la tabla para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Object valorCelda = tablaRepartidores.getValueAt(filaSeleccionada, 0);
            if (valorCelda instanceof String) {
                JOptionPane.showMessageDialog(this, "No hay registros válidos seleccionados para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idRepartidor = (int) tablaRepartidores.getValueAt(filaSeleccionada, 0);
            String nombreActual = (String) tablaRepartidores.getValueAt(filaSeleccionada, 1);

            String nuevoNombre = (String) JOptionPane.showInputDialog(
                    this,
                    "Editar nombre del repartidor:",
                    "Actualizar Repartidor",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    nombreActual
            );

            if (nuevoNombre != null) {
                nuevoNombre = nuevoNombre.trim();
                if (nuevoNombre.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Repartidor repartidorEditado = new Repartidor(idRepartidor, nuevoNombre, null);
                boolean exito = controlador.getRepartidorDAO().actualizar(repartidorEditado);

                if (exito) {
                    JOptionPane.showMessageDialog(this, "Repartidor actualizado con éxito.");
                    cargarRepartidores();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar el repartidor en la BD.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }
}