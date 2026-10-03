package cl.speedfast.model;

import cl.speedfast.dao.EntregaDAO;
import cl.speedfast.dao.PedidoDAO;

import java.sql.Date;
import java.sql.Time;
import java.util.Random;

/**
 *
 * Representa la clase para el Repartidor de Pedidos de SpeedFast.
 * Implementa Runnable.
 */
public class Repartidor implements Runnable {
    private int idRepartidor;
    private String nombreRepartidor;
    private ZonaDeCarga zonaDeCarga;
    Random random = new Random();

    /**
     *
     * Constructor del Repartidor.
     *
     * @param idRepartidor     ID único del repartidor asignado en la base de datos.
     * @param nombreRepartidor Nombre del repartidor del pedido.
     * @param zonaDeCarga      Lista de los pedidos asignados al repartidor.
     */
    public Repartidor(int idRepartidor, String nombreRepartidor, ZonaDeCarga zonaDeCarga) {
        this.idRepartidor = idRepartidor;
        this.nombreRepartidor = nombreRepartidor;
        this.zonaDeCarga = zonaDeCarga;
    }

    // Getters y Setters

    /**
     *
     * Obtiene el ID del Repartidor.
     *
     * @return ID del Repartidor.
     */
    public int getIdRepartidor() {
        return idRepartidor;
    }

    /**
     *
     * Establece el ID del Repartidor.
     *
     * @param idRepartidor ID del Repartidor.
     */
    public void setIdRepartidor(int idRepartidor) {
        this.idRepartidor = idRepartidor;
    }

    /**
     *
     * Obtiene el nombre del Repartidor.
     *
     * @return Nombre del Repartidor.
     */
    public String getNombreRepartidor() {
        return nombreRepartidor;
    }

    /**
     *
     * Establece el nombre del Repartidor.
     *
     * @param nombreRepartidor Nombre del Repartidor.
     */
    public void setNombreRepartidor(String nombreRepartidor) {
        this.nombreRepartidor = nombreRepartidor;
    }

    /**
     *
     * Obtiene la zona de carga.
     *
     * @return Zona de carga.
     */
    public ZonaDeCarga getZonaDeCarga() {
        return zonaDeCarga;
    }

    /**
     *
     * Establece la zona de carga.
     *
     * @param zonaDeCarga Zona de carga.
     */
    public void setZonaDeCarga(ZonaDeCarga zonaDeCarga) {
        this.zonaDeCarga = zonaDeCarga;
    }


    /**
     *
     * Retira pedidos de la zona de carga.
     */
    @Override
    public void run() {
        PedidoDAO pedidoDAO = new PedidoDAO();
        EntregaDAO entregaDAO = new EntregaDAO();

        while (!zonaDeCarga.estaVacia()) {
            Pedido pedido = zonaDeCarga.retirarPedido();
            if (pedido == null) {
                break;
            }

            int tiempoMilisegundos = 1000 + random.nextInt(4000);

            pedido.setEstado(EstadoPedido.EN_REPARTO);
            pedidoDAO.actualizarEstado(pedido.getIdPedido(), EstadoPedido.EN_REPARTO);

            System.out.println("[Repartidor - " + nombreRepartidor + "] Retirando pedido #" + pedido.getIdPedido());

            try {
                Thread.sleep(tiempoMilisegundos);
            } catch (InterruptedException e) {
                System.err.println("Error durante la entrega: " + e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }

            pedido.setEstado(EstadoPedido.ENTREGADO);
            pedidoDAO.actualizarEstado(pedido.getIdPedido(), EstadoPedido.ENTREGADO);

            long ahora = System.currentTimeMillis();
            Date fechaActual = new Date(ahora);
            Time horaActual = new Time(ahora);

            Entrega entrega = new Entrega(0, pedido.getIdPedido(), this.idRepartidor, fechaActual, horaActual);
            entregaDAO.guardar(entrega);

            System.out.println("[Repartidor - " + nombreRepartidor + "] Pedido #" + pedido.getIdPedido() + " ENTREGADO exitosamente.");
        }
    }
}
