package cl.speedfast.model;

import java.sql.Date;
import java.sql.Time;

/**
 *
 * Representa la clase de la entrega de los Pedidos de SpeedFast.
 */
public class Entrega {
    private int id;
    private int idPedido;
    private int idRepartidor;
    private Date fecha;
    private Time hora;

    /**
     *
     * Constructor de la Entrega.
     *
     * @param id           ID único del Pedido.
     * @param idPedido     ID del pedido asignado a la entrega.
     * @param idRepartidor ID del Repartidor asignado.
     * @param fecha        Fecha de la entrega.
     * @param hora         Hora de la entrega.
     */
    public Entrega(int id, int idPedido, int idRepartidor, Date fecha, Time hora) {
        this.id = id;
        this.idPedido = idPedido;
        this.idRepartidor = idRepartidor;
        this.fecha = fecha;
        this.hora = hora;
    }

    // Getters y Setters

    /**
     *
     * Obtiene el ID.
     *
     * @return ID.
     */
    public int getId() {
        return id;
    }

    /**
     *
     * Obtiene el ID del pedido.
     *
     * @return
     */
    public int getIdPedido() {
        return idPedido;
    }

    /**
     *
     * Obtiene el ID del Repartidor.
     *
     * @return ID del repartidor.
     */
    public int getIdRepartidor() {
        return idRepartidor;
    }

    /**
     *
     * Obtiene la Fecha del Pedido.
     *
     * @return Fecha.
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     *
     * Obtiene la Hora del Pedido.
     *
     * @return Hora.
     */
    public Time getHora() {
        return hora;
    }

    /**
     *
     * Establece el ID del Pedido.
     *
     * @param idPedido ID del Pedido.
     */
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
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
     * Establece la fecha del Pedido.
     *
     * @param fecha Fecha del Pedido.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     *
     * Establece la hora del Pedido.
     *
     * @param hora Hora del Pedido.
     */
    public void setHora(Time hora) {
        this.hora = hora;
    }
}
