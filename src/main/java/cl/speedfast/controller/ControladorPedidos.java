package cl.speedfast.controller;

import cl.speedfast.dao.EntregaDAO;
import cl.speedfast.dao.PedidoDAO;
import cl.speedfast.dao.RepartidorDAO;
import cl.speedfast.model.*;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 *
 * Representa la clase que controla los Pedidos de SpeedFast.
 */
public class ControladorPedidos {
    private ZonaDeCarga zonaDeCarga;
    private final PedidoDAO pedidoDAO;
    private final RepartidorDAO repartidorDAO;
    private final EntregaDAO entregaDAO;

    /**
     *
     * Constructor del Controlador.
     *
     * @param zonaDeCarga Instancia Zona de Carga.
     */
    public ControladorPedidos(ZonaDeCarga zonaDeCarga) {
        this.zonaDeCarga = zonaDeCarga;
        this.pedidoDAO = new PedidoDAO();
        this.repartidorDAO = new RepartidorDAO();
        this.entregaDAO = new EntregaDAO();
    }

    /**
     *
     * Registra un pedido y lo añade a la zona de carga.
     *
     * @param direccion Dirección de entrega del pedido.
     * @param tipo      Tipo del pedido (Comida, Encomienda, Express).
     * @param estado    Estado del pedido (Pendiente, En Reparto, Entregado).
     * @return Devuelve true si el pedido se registra de manera correcta o false si el ID ya existe.
     */
    public Pedido registrarPedido(String direccion, TipoPedido tipo, EstadoPedido estado) {
        if (direccion == null || direccion.trim().isEmpty() || estado == null) {
            return null;
        }

        Pedido nuevoPedido = new Pedido(0, direccion, tipo, estado);
        boolean guardadoBD = this.pedidoDAO.guardar(nuevoPedido);

        if (guardadoBD) {
            this.zonaDeCarga.agregarPedido(nuevoPedido);
            return nuevoPedido;
        }

        return null;
    }

    /**
     *
     * Obtiene la lista completa de Pedidos.
     *
     * @return Lista de Pedidos.
     */
    public List<Pedido> getListaPedidos() {
        return this.pedidoDAO.listarTodos();
    }

    /**
     *
     * Inicia las entregas asociadas a un repartidor en un hilo diferente.
     */
    public void iniciarEntregas() {
        List<Pedido> pedidosBD = this.pedidoDAO.listarTodos();
        for (Pedido p : pedidosBD) {
            if (p.getEstado() == EstadoPedido.PENDIENTE) {

                boolean yaEstaEnZona = false;
                for (Pedido enZona : this.zonaDeCarga.obtenerPedidos()) {
                    if (enZona.getIdPedido() == p.getIdPedido()) {
                        yaEstaEnZona = true;
                        break;
                    }
                }

                if (!yaEstaEnZona) {
                    this.zonaDeCarga.agregarPedido(p);
                }
            }
        }

        List<Repartidor> repartidoresBD = this.repartidorDAO.listarTodos();

        if (repartidoresBD.isEmpty()) {
            System.err.println("No hay repartidores registrados en la base de datos.");
            return;
        }

        for (Repartidor r : repartidoresBD) {
            r.setZonaDeCarga(this.zonaDeCarga);

            if (!this.zonaDeCarga.estaVacia()) {
                Thread hiloRepartidor = new Thread(r);
                hiloRepartidor.start();
            }
        }
    }

    /**
     *
     * Registra la Entrega del Pedido.
     *
     * @param idPedido     ID del Pedido.
     * @param idRepartidor ID del Repartidor.
     * @return Devuelve true si se guarda la entrega y false si no se guardó.
     */
    public boolean registrarEntrega(int idPedido, int idRepartidor) {
        long ahora = System.currentTimeMillis();
        Date fechaActual = new Date(ahora);
        Time horaActual = new Time(ahora);

        Entrega entrega = new Entrega(0, idPedido, idRepartidor, fechaActual, horaActual);
        return this.entregaDAO.guardar(entrega);
    }

    // Getters

    public ZonaDeCarga getZonaDeCarga() {
        return zonaDeCarga;
    }

    public PedidoDAO getPedidoDAO() {
        return pedidoDAO;
    }

    public RepartidorDAO getRepartidorDAO() {
        return repartidorDAO;
    }

    public EntregaDAO getEntregaDAO() {
        return entregaDAO;
    }
}
