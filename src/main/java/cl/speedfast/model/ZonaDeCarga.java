package cl.speedfast.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 *
 * Representa la clase de la zona de carga de SpeedFast.
 * Implementa Runnable.
 */
public class ZonaDeCarga {
    private Queue<Pedido> colaPedidos = new ArrayDeque<>();
    private List<Pedido> historialPedidos = new ArrayList<>();

    /**
     *
     * Agrega un pedido (sincronizado).
     *
     * @param pedido Objeto del pedido.
     */
    public synchronized void agregarPedido(Pedido pedido) {
        colaPedidos.offer(pedido);
        historialPedidos.add(pedido);
        System.out.println("Pedido #" + pedido.getIdPedido() + " agregado. Destino: " + pedido.getDireccionEntrega());
    }

    /**
     *
     * Retira un pedido (sincronizado).
     *
     * @return Pedido retirado.
     */
    public synchronized Pedido retirarPedido() {
        return colaPedidos.poll();
    }

    /**
     *
     * Valida si la lista está vacía.
     *
     * @return Devuelve true si está vacía y false si no está vacía.
     */
    public synchronized boolean estaVacia() {
        return colaPedidos.isEmpty();
    }

    /**
     * Devuelve una lista con todos los pedidos presentes en la cola sin eliminar.
     *
     * @return Lista de pedidos.
     */
    public synchronized List<Pedido> obtenerPedidos() {
        return new ArrayList<>(historialPedidos);
    }
}