package cl.speedfast.model;

/**
 *
 * Representa una plantilla genérica (clase abstracta) para el Pedido de SpeedFast.
 */
public class Pedido {
    private int idPedido;
    private String direccionEntrega;
    private EstadoPedido estado;
    private TipoPedido tipo;

    /**
     *
     * Constructor del Pedido cuando no se ingresa un ID manualmente.
     *
     * @param direccionEntrega Dirección de entrega del pedido.
     * @param tipo             Tipo de Pedido.
     */
    public Pedido(String direccionEntrega, TipoPedido tipo) {
        this.direccionEntrega = direccionEntrega;
        this.tipo = tipo;
        this.estado = EstadoPedido.PENDIENTE;
    }

    /**
     *
     * Constructor del Pedido cuando se ingresa ID manualmente.
     *
     * @param idPedido         Identificador único del pedido.
     * @param direccionEntrega Dirección de entrega del pedido.
     * @param tipo             Tipo de Pedido.
     */
    public Pedido(int idPedido, String direccionEntrega, TipoPedido tipo, EstadoPedido estado) {
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        this.tipo = tipo;
        this.estado = estado;
    }

    /**
     * Actualiza el estado de un pedido.
     *
     * @param nuevoEstado Nuevo estado.
     */
    public void setEstado(String nuevoEstado) {
        try {
            this.estado = EstadoPedido.valueOf(nuevoEstado.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Estado invalido: " + nuevoEstado);
        }
    }

    /**
     *
     * Asigna un repartidor de forma genérica (sin especificar nombre).
     *
     * @return Mensaje genérico de asignación.
     */
    public String asignarRepartidor() {
        return "Asignando repartidor...";
    }

    // Getters y Setters

    /**
     *
     * Obtiene el ID del Pedido.
     *
     * @return ID del Pedido.
     */
    public int getIdPedido() {
        return idPedido;
    }

    /**
     *
     * Obtiene la dirección de entrega del Pedido.
     *
     * @return Dirección de entrega.
     */
    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    /**
     *
     * Obtiene el estado del pedido.
     *
     * @return Estado.
     */
    public EstadoPedido getEstado() {
        return estado;
    }

    /**
     *
     * Establece el nuevo estado del pedido.
     *
     * @param nuevoEstado Establece el nuevo estado.
     */
    public void setEstado(EstadoPedido nuevoEstado) {
        this.estado = nuevoEstado;
    }

    /**
     *
     * Obtiene el tipo de pedido.
     *
     * @return Tipo de pedido.
     */
    public TipoPedido getTipo() {
        return tipo;
    }

    /**
     *
     * Establece el tipo de pedido.
     *
     * @param tipo Establece el tipo de pedido.
     */
    public void setTipo(TipoPedido tipo) {
        this.tipo = tipo;
    }

    /**
     *
     * Establece el ID del Pedido.
     *
     * @param idPedido ID.
     */
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    /**
     *
     * Establece la dirección de Entrega.
     *
     * @param direccionEntrega Dirección.
     */
    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    /**
     *
     * Devuelve una representación en texto de la clase con todos sus atributos.
     *
     * @return Cadena de texto con la información detallada del objeto.
     */
    @Override
    public String toString() {
        return "ID: " + idPedido + ". Dirección entrega: " + direccionEntrega + ". Estado: " + estado + "." + "Tipo: " + tipo + ".";
    }
}
